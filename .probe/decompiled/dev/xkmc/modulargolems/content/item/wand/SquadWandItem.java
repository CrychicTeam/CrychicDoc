package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.modulargolems.content.capability.GolemConfigEditor;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.SquadEditor;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SquadWandItem extends BaseWandItem implements GolemInteractItem, IGlowingTarget {

    private static final int RANGE = 64;

    public SquadWandItem(Item.Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties, MGLangData.WAND_SQUAD, null, base);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() && selected && entity instanceof Player player) {
            RayTraceUtil.clientUpdateTarget(player, 64.0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            LivingEntity target = RayTraceUtil.serverGetTarget(player);
            if (target != null) {
                this.interactLivingEntity(stack, player, target, hand);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getDistance(ItemStack itemStack) {
        return 64;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (target instanceof AbstractGolemEntity<?, ?> golem) {
            return choose(target.m_9236_(), user, golem) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static boolean choose(Level level, Player user, AbstractGolemEntity<?, ?> golem) {
        if (!ConfigCard.getFilter(user).test(golem)) {
            return false;
        } else if (!golem.canModify(user)) {
            return false;
        } else if (level.isClientSide()) {
            return true;
        } else {
            if (!(golem instanceof DogGolemEntity)) {
                GolemConfigEntry entry = golem.getConfigEntry(null);
                if (entry == null) {
                    return false;
                }
                SquadEditor editor = new GolemConfigEditor.Writable(level, entry).getSquad();
                UUID capId = editor.getCaptainId();
                UUID golemId = golem.m_20148_();
                if (capId != null && capId.equals(golemId)) {
                    editor.setCaptainId(null);
                } else {
                    editor.setCaptainId(golemId);
                }
            }
            return false;
        }
    }
}