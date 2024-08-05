package net.minecraft.world.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ThrowableItemProjectile extends ThrowableProjectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrowableItemProjectile.class, EntityDataSerializers.ITEM_STACK);

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> entityTypeExtendsThrowableItemProjectile0, Level level1) {
        super(entityTypeExtendsThrowableItemProjectile0, level1);
    }

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> entityTypeExtendsThrowableItemProjectile0, double double1, double double2, double double3, Level level4) {
        super(entityTypeExtendsThrowableItemProjectile0, double1, double2, double3, level4);
    }

    public ThrowableItemProjectile(EntityType<? extends ThrowableItemProjectile> entityTypeExtendsThrowableItemProjectile0, LivingEntity livingEntity1, Level level2) {
        super(entityTypeExtendsThrowableItemProjectile0, livingEntity1, level2);
    }

    public void setItem(ItemStack itemStack0) {
        if (!itemStack0.is(this.getDefaultItem()) || itemStack0.hasTag()) {
            this.m_20088_().set(DATA_ITEM_STACK, itemStack0.copyWithCount(1));
        }
    }

    protected abstract Item getDefaultItem();

    protected ItemStack getItemRaw() {
        return this.m_20088_().get(DATA_ITEM_STACK);
    }

    @Override
    public ItemStack getItem() {
        ItemStack $$0 = this.getItemRaw();
        return $$0.isEmpty() ? new ItemStack(this.getDefaultItem()) : $$0;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            compoundTag0.put("Item", $$1.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        ItemStack $$1 = ItemStack.of(compoundTag0.getCompound("Item"));
        this.setItem($$1);
    }
}