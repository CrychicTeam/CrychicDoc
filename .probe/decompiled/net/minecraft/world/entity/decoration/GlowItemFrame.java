package net.minecraft.world.entity.decoration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class GlowItemFrame extends ItemFrame {

    public GlowItemFrame(EntityType<? extends ItemFrame> entityTypeExtendsItemFrame0, Level level1) {
        super(entityTypeExtendsItemFrame0, level1);
    }

    public GlowItemFrame(Level level0, BlockPos blockPos1, Direction direction2) {
        super(EntityType.GLOW_ITEM_FRAME, level0, blockPos1, direction2);
    }

    @Override
    public SoundEvent getRemoveItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM;
    }

    @Override
    public SoundEvent getBreakSound() {
        return SoundEvents.GLOW_ITEM_FRAME_BREAK;
    }

    @Override
    public SoundEvent getPlaceSound() {
        return SoundEvents.GLOW_ITEM_FRAME_PLACE;
    }

    @Override
    public SoundEvent getAddItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM;
    }

    @Override
    public SoundEvent getRotateItemSound() {
        return SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM;
    }

    @Override
    protected ItemStack getFrameItemStack() {
        return new ItemStack(Items.GLOW_ITEM_FRAME);
    }
}