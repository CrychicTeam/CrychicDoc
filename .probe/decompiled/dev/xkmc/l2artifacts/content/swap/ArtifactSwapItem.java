package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArtifactSwapItem extends Item {

    private static final String KEY_DATA = "SwapData";

    public static ArtifactSwapData getData(ItemStack stack) {
        ArtifactSwapData data = null;
        if (stack.getOrCreateTag().contains("SwapData")) {
            data = (ArtifactSwapData) TagCodec.fromTag(stack.getOrCreateTag().getCompound("SwapData"), ArtifactSwapData.class);
        }
        return data == null ? new ArtifactSwapData() : data;
    }

    public ArtifactSwapItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    public static void setData(ItemStack stack, ArtifactSwapData data) {
        CompoundTag tag = TagCodec.toTag(new CompoundTag(), data);
        if (tag != null) {
            stack.getOrCreateTag().put("SwapData", tag);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            int slot = hand == InteractionHand.OFF_HAND ? 40 : player.getInventory().selected;
            new ArtifactSwapMenuPvd((ServerPlayer) player, slot, stack).open();
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}