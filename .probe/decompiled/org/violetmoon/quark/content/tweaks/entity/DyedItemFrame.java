package org.violetmoon.quark.content.tweaks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.module.DyeableItemFramesModule;

public class DyedItemFrame extends ItemFrame {

    private static final String TAG_COLOR = "q_color";

    private static final String TAG_GLOW = "q_glow";

    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(DyedItemFrame.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_GLOW = SynchedEntityData.defineId(DyedItemFrame.class, EntityDataSerializers.BOOLEAN);

    public DyedItemFrame(EntityType<? extends DyedItemFrame> entityTypeExtendsDyedItemFrame0, Level level1) {
        super(entityTypeExtendsDyedItemFrame0, level1);
    }

    public DyedItemFrame(Level level, BlockPos pos, Direction direction, int color, boolean glow) {
        super(DyeableItemFramesModule.entityType, level, pos, direction);
        this.m_20088_().set(DATA_COLOR, color);
        this.m_20088_().set(DATA_GLOW, glow);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_COLOR, 0);
        this.m_20088_().define(DATA_GLOW, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        cmp.putInt("q_color", this.getColor());
        cmp.putBoolean("q_glow", this.isGlow());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        this.m_20088_().set(DATA_COLOR, cmp.getInt("q_color"));
        this.m_20088_().set(DATA_GLOW, cmp.getBoolean("q_glow"));
    }

    public int getColor() {
        return this.m_20088_().get(DATA_COLOR);
    }

    public boolean isGlow() {
        return this.m_20088_().get(DATA_GLOW);
    }

    @Override
    public SoundEvent getRemoveItemSound() {
        return this.isGlow() ? SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM : super.getRemoveItemSound();
    }

    @Override
    public SoundEvent getBreakSound() {
        return this.isGlow() ? SoundEvents.GLOW_ITEM_FRAME_BREAK : super.getRemoveItemSound();
    }

    @Override
    public SoundEvent getPlaceSound() {
        return this.isGlow() ? SoundEvents.GLOW_ITEM_FRAME_PLACE : super.getRemoveItemSound();
    }

    @Override
    public SoundEvent getAddItemSound() {
        return this.isGlow() ? SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM : super.getRemoveItemSound();
    }

    @Override
    public SoundEvent getRotateItemSound() {
        return this.isGlow() ? SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM : super.getRemoveItemSound();
    }

    @Override
    protected ItemStack getFrameItemStack() {
        ItemStack stack = new ItemStack(this.isGlow() ? Items.GLOW_ITEM_FRAME : Items.ITEM_FRAME);
        Quark.ZETA.dyeables.applyDye(stack, this.getColor());
        return stack;
    }
}