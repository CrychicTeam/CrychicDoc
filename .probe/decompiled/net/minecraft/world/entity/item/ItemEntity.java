package net.minecraft.world.entity.item;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ItemEntity extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final int LIFETIME = 6000;

    private static final int INFINITE_PICKUP_DELAY = 32767;

    private static final int INFINITE_LIFETIME = -32768;

    private int age;

    private int pickupDelay;

    private int health = 5;

    @Nullable
    private UUID thrower;

    @Nullable
    private UUID target;

    public final float bobOffs;

    public ItemEntity(EntityType<? extends ItemEntity> entityTypeExtendsItemEntity0, Level level1) {
        super(entityTypeExtendsItemEntity0, level1);
        this.bobOffs = this.f_19796_.nextFloat() * (float) Math.PI * 2.0F;
        this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
    }

    public ItemEntity(Level level0, double double1, double double2, double double3, ItemStack itemStack4) {
        this(level0, double1, double2, double3, itemStack4, level0.random.nextDouble() * 0.2 - 0.1, 0.2, level0.random.nextDouble() * 0.2 - 0.1);
    }

    public ItemEntity(Level level0, double double1, double double2, double double3, ItemStack itemStack4, double double5, double double6, double double7) {
        this(EntityType.ITEM, level0);
        this.m_6034_(double1, double2, double3);
        this.m_20334_(double5, double6, double7);
        this.setItem(itemStack4);
    }

    private ItemEntity(ItemEntity itemEntity0) {
        super(itemEntity0.m_6095_(), itemEntity0.m_9236_());
        this.setItem(itemEntity0.getItem().copy());
        this.m_20359_(itemEntity0);
        this.age = itemEntity0.age;
        this.bobOffs = itemEntity0.bobOffs;
    }

    @Override
    public boolean dampensVibrations() {
        return this.getItem().is(ItemTags.DAMPENS_VIBRATIONS);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.thrower != null && this.m_9236_() instanceof ServerLevel $$0 ? $$0.getEntity(this.thrower) : null;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (this.getItem().isEmpty()) {
            this.m_146870_();
        } else {
            super.tick();
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                this.pickupDelay--;
            }
            this.f_19854_ = this.m_20185_();
            this.f_19855_ = this.m_20186_();
            this.f_19856_ = this.m_20189_();
            Vec3 $$0 = this.m_20184_();
            float $$1 = this.m_20192_() - 0.11111111F;
            if (this.m_20069_() && this.m_204036_(FluidTags.WATER) > (double) $$1) {
                this.setUnderwaterMovement();
            } else if (this.m_20077_() && this.m_204036_(FluidTags.LAVA) > (double) $$1) {
                this.setUnderLavaMovement();
            } else if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
            }
            if (this.m_9236_().isClientSide) {
                this.f_19794_ = false;
            } else {
                this.f_19794_ = !this.m_9236_().m_45756_(this, this.m_20191_().deflate(1.0E-7));
                if (this.f_19794_) {
                    this.m_20314_(this.m_20185_(), (this.m_20191_().minY + this.m_20191_().maxY) / 2.0, this.m_20189_());
                }
            }
            if (!this.m_20096_() || this.m_20184_().horizontalDistanceSqr() > 1.0E-5F || (this.f_19797_ + this.m_19879_()) % 4 == 0) {
                this.m_6478_(MoverType.SELF, this.m_20184_());
                float $$2 = 0.98F;
                if (this.m_20096_()) {
                    $$2 = this.m_9236_().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).m_60734_().getFriction() * 0.98F;
                }
                this.m_20256_(this.m_20184_().multiply((double) $$2, 0.98, (double) $$2));
                if (this.m_20096_()) {
                    Vec3 $$3 = this.m_20184_();
                    if ($$3.y < 0.0) {
                        this.m_20256_($$3.multiply(1.0, -0.5, 1.0));
                    }
                }
            }
            boolean $$4 = Mth.floor(this.f_19854_) != Mth.floor(this.m_20185_()) || Mth.floor(this.f_19855_) != Mth.floor(this.m_20186_()) || Mth.floor(this.f_19856_) != Mth.floor(this.m_20189_());
            int $$5 = $$4 ? 2 : 40;
            if (this.f_19797_ % $$5 == 0 && !this.m_9236_().isClientSide && this.isMergable()) {
                this.mergeWithNeighbours();
            }
            if (this.age != -32768) {
                this.age++;
            }
            this.f_19812_ = this.f_19812_ | this.m_20073_();
            if (!this.m_9236_().isClientSide) {
                double $$6 = this.m_20184_().subtract($$0).lengthSqr();
                if ($$6 > 0.01) {
                    this.f_19812_ = true;
                }
            }
            if (!this.m_9236_().isClientSide && this.age >= 6000) {
                this.m_146870_();
            }
        }
    }

    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.m_216986_(0.999999F);
    }

    private void setUnderwaterMovement() {
        Vec3 $$0 = this.m_20184_();
        this.m_20334_($$0.x * 0.99F, $$0.y + (double) ($$0.y < 0.06F ? 5.0E-4F : 0.0F), $$0.z * 0.99F);
    }

    private void setUnderLavaMovement() {
        Vec3 $$0 = this.m_20184_();
        this.m_20334_($$0.x * 0.95F, $$0.y + (double) ($$0.y < 0.06F ? 5.0E-4F : 0.0F), $$0.z * 0.95F);
    }

    private void mergeWithNeighbours() {
        if (this.isMergable()) {
            for (ItemEntity $$1 : this.m_9236_().m_6443_(ItemEntity.class, this.m_20191_().inflate(0.5, 0.0, 0.5), p_186268_ -> p_186268_ != this && p_186268_.isMergable())) {
                if ($$1.isMergable()) {
                    this.tryToMerge($$1);
                    if (this.m_213877_()) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isMergable() {
        ItemStack $$0 = this.getItem();
        return this.m_6084_() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && $$0.getCount() < $$0.getMaxStackSize();
    }

    private void tryToMerge(ItemEntity itemEntity0) {
        ItemStack $$1 = this.getItem();
        ItemStack $$2 = itemEntity0.getItem();
        if (Objects.equals(this.target, itemEntity0.target) && areMergable($$1, $$2)) {
            if ($$2.getCount() < $$1.getCount()) {
                merge(this, $$1, itemEntity0, $$2);
            } else {
                merge(itemEntity0, $$2, this, $$1);
            }
        }
    }

    public static boolean areMergable(ItemStack itemStack0, ItemStack itemStack1) {
        if (!itemStack1.is(itemStack0.getItem())) {
            return false;
        } else if (itemStack1.getCount() + itemStack0.getCount() > itemStack1.getMaxStackSize()) {
            return false;
        } else {
            return itemStack1.hasTag() ^ itemStack0.hasTag() ? false : !itemStack1.hasTag() || itemStack1.getTag().equals(itemStack0.getTag());
        }
    }

    public static ItemStack merge(ItemStack itemStack0, ItemStack itemStack1, int int2) {
        int $$3 = Math.min(Math.min(itemStack0.getMaxStackSize(), int2) - itemStack0.getCount(), itemStack1.getCount());
        ItemStack $$4 = itemStack0.copyWithCount(itemStack0.getCount() + $$3);
        itemStack1.shrink($$3);
        return $$4;
    }

    private static void merge(ItemEntity itemEntity0, ItemStack itemStack1, ItemStack itemStack2) {
        ItemStack $$3 = merge(itemStack1, itemStack2, 64);
        itemEntity0.setItem($$3);
    }

    private static void merge(ItemEntity itemEntity0, ItemStack itemStack1, ItemEntity itemEntity2, ItemStack itemStack3) {
        merge(itemEntity0, itemStack1, itemStack3);
        itemEntity0.pickupDelay = Math.max(itemEntity0.pickupDelay, itemEntity2.pickupDelay);
        itemEntity0.age = Math.min(itemEntity0.age, itemEntity2.age);
        if (itemStack3.isEmpty()) {
            itemEntity2.m_146870_();
        }
    }

    @Override
    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune();
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().is(Items.NETHER_STAR) && damageSource0.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        } else if (!this.getItem().getItem().canBeHurtBy(damageSource0)) {
            return false;
        } else if (this.m_9236_().isClientSide) {
            return true;
        } else {
            this.m_5834_();
            this.health = (int) ((float) this.health - float1);
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
            if (this.health <= 0) {
                this.getItem().onDestroyed(this);
                this.m_146870_();
            }
            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putShort("Health", (short) this.health);
        compoundTag0.putShort("Age", (short) this.age);
        compoundTag0.putShort("PickupDelay", (short) this.pickupDelay);
        if (this.thrower != null) {
            compoundTag0.putUUID("Thrower", this.thrower);
        }
        if (this.target != null) {
            compoundTag0.putUUID("Owner", this.target);
        }
        if (!this.getItem().isEmpty()) {
            compoundTag0.put("Item", this.getItem().save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.health = compoundTag0.getShort("Health");
        this.age = compoundTag0.getShort("Age");
        if (compoundTag0.contains("PickupDelay")) {
            this.pickupDelay = compoundTag0.getShort("PickupDelay");
        }
        if (compoundTag0.hasUUID("Owner")) {
            this.target = compoundTag0.getUUID("Owner");
        }
        if (compoundTag0.hasUUID("Thrower")) {
            this.thrower = compoundTag0.getUUID("Thrower");
        }
        CompoundTag $$1 = compoundTag0.getCompound("Item");
        this.setItem(ItemStack.of($$1));
        if (this.getItem().isEmpty()) {
            this.m_146870_();
        }
    }

    @Override
    public void playerTouch(Player player0) {
        if (!this.m_9236_().isClientSide) {
            ItemStack $$1 = this.getItem();
            Item $$2 = $$1.getItem();
            int $$3 = $$1.getCount();
            if (this.pickupDelay == 0 && (this.target == null || this.target.equals(player0.m_20148_())) && player0.getInventory().add($$1)) {
                player0.m_7938_(this, $$3);
                if ($$1.isEmpty()) {
                    this.m_146870_();
                    $$1.setCount($$3);
                }
                player0.awardStat(Stats.ITEM_PICKED_UP.get($$2), $$3);
                player0.m_21053_(this);
            }
        }
    }

    @Override
    public Component getName() {
        Component $$0 = this.m_7770_();
        return (Component) ($$0 != null ? $$0 : Component.translatable(this.getItem().getDescriptionId()));
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel0) {
        Entity $$1 = super.changeDimension(serverLevel0);
        if (!this.m_9236_().isClientSide && $$1 instanceof ItemEntity) {
            ((ItemEntity) $$1).mergeWithNeighbours();
        }
        return $$1;
    }

    public ItemStack getItem() {
        return this.m_20088_().get(DATA_ITEM);
    }

    public void setItem(ItemStack itemStack0) {
        this.m_20088_().set(DATA_ITEM, itemStack0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        super.onSyncedDataUpdated(entityDataAccessor0);
        if (DATA_ITEM.equals(entityDataAccessor0)) {
            this.getItem().setEntityRepresentation(this);
        }
    }

    public void setTarget(@Nullable UUID uUID0) {
        this.target = uUID0;
    }

    public void setThrower(@Nullable UUID uUID0) {
        this.thrower = uUID0;
    }

    public int getAge() {
        return this.age;
    }

    public void setDefaultPickUpDelay() {
        this.pickupDelay = 10;
    }

    public void setNoPickUpDelay() {
        this.pickupDelay = 0;
    }

    public void setNeverPickUp() {
        this.pickupDelay = 32767;
    }

    public void setPickUpDelay(int int0) {
        this.pickupDelay = int0;
    }

    public boolean hasPickUpDelay() {
        return this.pickupDelay > 0;
    }

    public void setUnlimitedLifetime() {
        this.age = -32768;
    }

    public void setExtendedLifetime() {
        this.age = -6000;
    }

    public void makeFakeItem() {
        this.setNeverPickUp();
        this.age = 5999;
    }

    public float getSpin(float float0) {
        return ((float) this.getAge() + float0) / 20.0F + this.bobOffs;
    }

    public ItemEntity copy() {
        return new ItemEntity(this);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180.0F - this.getSpin(0.5F) / (float) (Math.PI * 2) * 360.0F;
    }
}