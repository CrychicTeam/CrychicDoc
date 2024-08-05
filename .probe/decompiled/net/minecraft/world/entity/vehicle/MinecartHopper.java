package net.minecraft.world.entity.vehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinecartHopper extends AbstractMinecartContainer implements Hopper {

    private boolean enabled = true;

    public MinecartHopper(EntityType<? extends MinecartHopper> entityTypeExtendsMinecartHopper0, Level level1) {
        super(entityTypeExtendsMinecartHopper0, level1);
    }

    public MinecartHopper(Level level0, double double1, double double2, double double3) {
        super(EntityType.HOPPER_MINECART, double1, double2, double3, level0);
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.HOPPER;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.HOPPER.defaultBlockState();
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 1;
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    public void activateMinecart(int int0, int int1, int int2, boolean boolean3) {
        boolean $$4 = !boolean3;
        if ($$4 != this.isEnabled()) {
            this.setEnabled($$4);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean boolean0) {
        this.enabled = boolean0;
    }

    @Override
    public double getLevelX() {
        return this.m_20185_();
    }

    @Override
    public double getLevelY() {
        return this.m_20186_() + 0.5;
    }

    @Override
    public double getLevelZ() {
        return this.m_20189_();
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide && this.m_6084_() && this.isEnabled() && this.suckInItems()) {
            this.m_6596_();
        }
    }

    public boolean suckInItems() {
        if (HopperBlockEntity.suckInItems(this.m_9236_(), this)) {
            return true;
        } else {
            for (ItemEntity $$1 : this.m_9236_().m_6443_(ItemEntity.class, this.m_20191_().inflate(0.25, 0.0, 0.25), EntitySelector.ENTITY_STILL_ALIVE)) {
                if (HopperBlockEntity.addItem(this, $$1)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected Item getDropItem() {
        return Items.HOPPER_MINECART;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("Enabled", this.enabled);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.enabled = compoundTag0.contains("Enabled") ? compoundTag0.getBoolean("Enabled") : true;
    }

    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new HopperMenu(int0, inventory1, this);
    }
}