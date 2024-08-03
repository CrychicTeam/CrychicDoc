package net.minecraft.world.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public abstract class Fireball extends AbstractHurtingProjectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.ITEM_STACK);

    public Fireball(EntityType<? extends Fireball> entityTypeExtendsFireball0, Level level1) {
        super(entityTypeExtendsFireball0, level1);
    }

    public Fireball(EntityType<? extends Fireball> entityTypeExtendsFireball0, double double1, double double2, double double3, double double4, double double5, double double6, Level level7) {
        super(entityTypeExtendsFireball0, double1, double2, double3, double4, double5, double6, level7);
    }

    public Fireball(EntityType<? extends Fireball> entityTypeExtendsFireball0, LivingEntity livingEntity1, double double2, double double3, double double4, Level level5) {
        super(entityTypeExtendsFireball0, livingEntity1, double2, double3, double4, level5);
    }

    public void setItem(ItemStack itemStack0) {
        if (!itemStack0.is(Items.FIRE_CHARGE) || itemStack0.hasTag()) {
            this.m_20088_().set(DATA_ITEM_STACK, itemStack0.copyWithCount(1));
        }
    }

    protected ItemStack getItemRaw() {
        return this.m_20088_().get(DATA_ITEM_STACK);
    }

    @Override
    public ItemStack getItem() {
        ItemStack $$0 = this.getItemRaw();
        return $$0.isEmpty() ? new ItemStack(Items.FIRE_CHARGE) : $$0;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            compoundTag0.put("Item", $$1.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        ItemStack $$1 = ItemStack.of(compoundTag0.getCompound("Item"));
        this.setItem($$1);
    }
}