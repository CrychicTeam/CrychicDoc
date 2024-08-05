package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.wand.GolemInteractItem;
import dev.xkmc.modulargolems.content.menu.config.ConfigMenuProvider;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ConfigCard extends Item implements GolemInteractItem {

    private static final String KEY_OWNER = "config_owner";

    private final DyeColor color;

    @Nullable
    public static UUID getUUID(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains("config_owner") ? stack.getTag().getUUID("config_owner") : null;
    }

    public static Predicate<AbstractGolemEntity<?, ?>> getFilter(Player player) {
        ItemStack stack = player.m_21206_();
        if (stack.getItem() instanceof ConfigCard card) {
            UUID uuid = getUUID(stack);
            if (uuid != null) {
                return e -> (Boolean) Optional.ofNullable(e.getConfigEntry(null)).map(x -> x.getID().equals(uuid) && x.getColor() == card.color.getId()).orElse(true);
            }
        }
        return e -> true;
    }

    private static boolean mayClientEdit(UUID id) {
        LocalPlayer player = Proxy.getClientPlayer();
        return player != null && player.m_20148_().equals(id);
    }

    public ConfigCard(Item.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof AbstractGolemEntity<?, ?> golem) {
            if (!golem.canModify(player)) {
                return InteractionResult.FAIL;
            } else if (player.m_9236_().isClientSide()) {
                return InteractionResult.SUCCESS;
            } else {
                UUID uuid = getUUID(stack);
                if (uuid == null) {
                    stack.getOrCreateTag().putUUID("config_owner", uuid = player.m_20148_());
                }
                GolemConfigEntry old = golem.getConfigEntry(null);
                if (old != null && old.getID().equals(uuid) && old.getColor() == this.color.getId()) {
                    golem.setConfigCard(null, 0);
                } else {
                    golem.setConfigCard(uuid, this.color.getId());
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        UUID uuid = getUUID(stack);
        if (uuid == null) {
            stack.getOrCreateTag().putUUID("config_owner", uuid = player.m_20148_());
        }
        if (level instanceof ServerLevel sl) {
            GolemConfigEditor editor;
            if (player.m_20148_().equals(uuid)) {
                GolemConfigEntry entry = GolemConfigStorage.get(level).getOrCreateStorage(uuid, this.color.getId(), stack.getHoverName());
                entry.setName(stack.getHoverName(), sl);
                entry.heartBeat(sl, (ServerPlayer) player);
                editor = new GolemConfigEditor.Writable(level, entry);
            } else {
                GolemConfigEntry entry = GolemConfigStorage.get(level).getStorage(uuid, this.color.getId());
                if (entry != null) {
                    editor = new GolemConfigEditor.Writable(level, entry);
                } else {
                    editor = null;
                }
            }
            if (editor != null && player instanceof ServerPlayer sp) {
                ConfigMenuProvider pvd = new ConfigMenuProvider(uuid, this.color.getId(), editor);
                NetworkHooks.openScreen(sp, pvd, pvd::writeBuffer);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag pIsAdvanced) {
        UUID id = getUUID(stack);
        if (id == null) {
            list.add(MGLangData.CONFIG_INIT.get());
        } else {
            if (level != null) {
                GolemConfigEntry entry = GolemConfigStorage.get(level).getOrCreateStorage(id, this.color.getId(), MGLangData.LOADING.get());
                entry.clientTick(level, false);
                list.add(entry.getDisplayName());
            }
            if (!mayClientEdit(id)) {
                list.add(MGLangData.CONFIG_OTHER.get());
            }
            list.add(MGLangData.CONFIG_CARD.get());
        }
    }
}