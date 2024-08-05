package dev.xkmc.l2backpack.content.remote.drawer;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.drawer.DrawerInvWrapper;
import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import dev.xkmc.l2backpack.content.render.BaseItemRenderer;
import dev.xkmc.l2backpack.events.TooltipUpdateEvents;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2backpack.init.data.LangData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class EnderDrawerItem extends BlockItem implements BaseDrawerItem {

    public static final String KEY_OWNER_ID = "owner_id";

    public static final String KEY_OWNER_NAME = "owner_name";

    private static final ResourceLocation BG = new ResourceLocation("l2backpack", "textures/block/drawer/ender_side.png");

    public static Optional<UUID> getOwner(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("owner_id") ? Optional.of(tag.getUUID("owner_id")) : Optional.empty();
    }

    public EnderDrawerItem(Block block, Item.Properties properties) {
        super(block, properties.stacksTo(1).fireResistant());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BaseItemRenderer.EXTENSIONS);
    }

    void refresh(ItemStack drawer, Player player) {
        if (!drawer.getOrCreateTag().contains("owner_id")) {
            drawer.getOrCreateTag().putUUID("owner_id", player.m_20148_());
            drawer.getOrCreateTag().putString("owner_name", player.getName().getString());
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (BaseDrawerItem.getItem(stack) == Items.AIR) {
            return InteractionResultHolder.fail(stack);
        } else if (player instanceof ServerPlayer sp) {
            this.refresh(stack, player);
            if (!player.m_6144_()) {
                ItemStack take = this.takeItem(stack, sp);
                int c = take.getCount();
                player.getInventory().placeItemBackInInventory(take);
                ContentTransfer.onExtract(player, c, stack);
            } else {
                DrawerAccess access = DrawerAccess.of(world, stack);
                int count = access.getCount();
                int max = BaseDrawerItem.getStacking(stack) * access.item().getMaxStackSize();
                int ext = BaseDrawerItem.loadFromInventory(max, count, access.item(), player);
                count += ext;
                access.setCount(count);
                ContentTransfer.onCollect(player, ext, stack);
            }
            return InteractionResultHolder.success(stack);
        } else {
            ContentTransfer.playDrawerSound(player);
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide() && context.getPlayer() != null) {
            this.refresh(context.getItemInHand(), context.getPlayer());
        }
        if (!context.getItemInHand().getOrCreateTag().contains("owner_id")) {
            return InteractionResult.FAIL;
        } else if (BaseDrawerItem.getItem(context.getItemInHand()) == Items.AIR) {
            if (!context.getLevel().isClientSide() && context.getPlayer() instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(LangData.IDS.NO_ITEM.get().withStyle(ChatFormatting.RED), true);
            }
            return InteractionResult.FAIL;
        } else if (context.getPlayer() != null && !context.getPlayer().m_6144_()) {
            return InteractionResult.PASS;
        } else {
            InteractionResult result = super.useOn(context);
            return result == InteractionResult.FAIL ? InteractionResult.PASS : result;
        }
    }

    @Override
    public void insert(ItemStack drawer, ItemStack stack, Player player) {
        this.refresh(drawer, player);
        DrawerAccess access = DrawerAccess.of(player.m_9236_(), drawer);
        int count = access.getCount();
        int take = Math.min(BaseDrawerItem.getStacking(stack) * stack.getMaxStackSize() - count, stack.getCount());
        access.setCount(access.getCount() + take);
        stack.shrink(take);
    }

    @Override
    public ItemStack takeItem(ItemStack drawer, int max, Player player, boolean simulate) {
        this.refresh(drawer, player);
        DrawerAccess access = DrawerAccess.of(player.m_9236_(), drawer);
        Item item = BaseDrawerItem.getItem(drawer);
        int take = Math.min(access.getCount(), Math.min(max, item.getMaxStackSize()));
        if (!simulate) {
            access.setCount(access.getCount() - take);
        }
        return new ItemStack(item, take);
    }

    @Override
    public boolean canSetNewItem(ItemStack drawer) {
        return BaseDrawerItem.getItem(drawer) == Items.AIR;
    }

    @Override
    public void setItem(ItemStack drawer, Item item, Player player) {
        this.refresh(drawer, player);
        BaseDrawerItem.super.setItem(drawer, item, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        Item item = BaseDrawerItem.getItem(stack);
        if (item != Items.AIR) {
            int count = TooltipUpdateEvents.getCount(stack.getOrCreateTag().getUUID("owner_id"), item);
            list.add(LangData.IDS.DRAWER_CONTENT.get(item.getDescription(), count < 0 ? "???" : count));
        }
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("owner_name")) {
            String name = tag.getString("owner_name");
            list.add(LangData.IDS.STORAGE_OWNER.get(name));
            PickupConfig.addText(stack, list);
        }
        LangData.addInfo(list, LangData.Info.ENDER_DRAWER, LangData.Info.EXTRACT_DRAWER, LangData.Info.PLACE, LangData.Info.COLLECT_DRAWER, LangData.Info.ENDER_DRAWER_USE);
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new DrawerInvWrapper(stack, trace -> trace.player == null ? null : new EnderDrawerInvAccess(stack, this, trace.player));
    }

    @Override
    public void serverTrigger(ItemStack storage, ServerPlayer player) {
        if ((Boolean) getOwner(storage).map(e -> !e.equals(player.m_20148_())).orElse(false)) {
            BackpackTriggers.SHARE.trigger(player);
        }
    }

    @Override
    public ResourceLocation backgroundLoc() {
        return BG;
    }
}