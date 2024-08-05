package net.minecraft.world.entity.projectile;

import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireworkRocketEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);

    private int life;

    private int lifetime;

    @Nullable
    private LivingEntity attachedToEntity;

    public FireworkRocketEntity(EntityType<? extends FireworkRocketEntity> entityTypeExtendsFireworkRocketEntity0, Level level1) {
        super(entityTypeExtendsFireworkRocketEntity0, level1);
    }

    public FireworkRocketEntity(Level level0, double double1, double double2, double double3, ItemStack itemStack4) {
        super(EntityType.FIREWORK_ROCKET, level0);
        this.life = 0;
        this.m_6034_(double1, double2, double3);
        int $$5 = 1;
        if (!itemStack4.isEmpty() && itemStack4.hasTag()) {
            this.f_19804_.set(DATA_ID_FIREWORKS_ITEM, itemStack4.copy());
            $$5 += itemStack4.getOrCreateTagElement("Fireworks").getByte("Flight");
        }
        this.m_20334_(this.f_19796_.triangle(0.0, 0.002297), 0.05, this.f_19796_.triangle(0.0, 0.002297));
        this.lifetime = 10 * $$5 + this.f_19796_.nextInt(6) + this.f_19796_.nextInt(7);
    }

    public FireworkRocketEntity(Level level0, @Nullable Entity entity1, double double2, double double3, double double4, ItemStack itemStack5) {
        this(level0, double2, double3, double4, itemStack5);
        this.m_5602_(entity1);
    }

    public FireworkRocketEntity(Level level0, ItemStack itemStack1, LivingEntity livingEntity2) {
        this(level0, livingEntity2, livingEntity2.m_20185_(), livingEntity2.m_20186_(), livingEntity2.m_20189_(), itemStack1);
        this.f_19804_.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(livingEntity2.m_19879_()));
        this.attachedToEntity = livingEntity2;
    }

    public FireworkRocketEntity(Level level0, ItemStack itemStack1, double double2, double double3, double double4, boolean boolean5) {
        this(level0, double2, double3, double4, itemStack1);
        this.f_19804_.set(DATA_SHOT_AT_ANGLE, boolean5);
    }

    public FireworkRocketEntity(Level level0, ItemStack itemStack1, Entity entity2, double double3, double double4, double double5, boolean boolean6) {
        this(level0, itemStack1, double3, double4, double5, boolean6);
        this.m_5602_(entity2);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_ID_FIREWORKS_ITEM, ItemStack.EMPTY);
        this.f_19804_.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
        this.f_19804_.define(DATA_SHOT_AT_ANGLE, false);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        return double0 < 4096.0 && !this.isAttachedToEntity();
    }

    @Override
    public boolean shouldRender(double double0, double double1, double double2) {
        return super.m_6000_(double0, double1, double2) && !this.isAttachedToEntity();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAttachedToEntity()) {
            if (this.attachedToEntity == null) {
                this.f_19804_.get(DATA_ATTACHED_TO_TARGET).ifPresent(p_289492_ -> {
                    Entity $$1x = this.m_9236_().getEntity(p_289492_);
                    if ($$1x instanceof LivingEntity) {
                        this.attachedToEntity = (LivingEntity) $$1x;
                    }
                });
            }
            if (this.attachedToEntity != null) {
                Vec3 $$4;
                if (this.attachedToEntity.isFallFlying()) {
                    Vec3 $$0 = this.attachedToEntity.m_20154_();
                    double $$1 = 1.5;
                    double $$2 = 0.1;
                    Vec3 $$3 = this.attachedToEntity.m_20184_();
                    this.attachedToEntity.m_20256_($$3.add($$0.x * 0.1 + ($$0.x * 1.5 - $$3.x) * 0.5, $$0.y * 0.1 + ($$0.y * 1.5 - $$3.y) * 0.5, $$0.z * 0.1 + ($$0.z * 1.5 - $$3.z) * 0.5));
                    $$4 = this.attachedToEntity.m_204034_(Items.FIREWORK_ROCKET);
                } else {
                    $$4 = Vec3.ZERO;
                }
                this.m_6034_(this.attachedToEntity.m_20185_() + $$4.x, this.attachedToEntity.m_20186_() + $$4.y, this.attachedToEntity.m_20189_() + $$4.z);
                this.m_20256_(this.attachedToEntity.m_20184_());
            }
        } else {
            if (!this.isShotAtAngle()) {
                double $$6 = this.f_19862_ ? 1.0 : 1.15;
                this.m_20256_(this.m_20184_().multiply($$6, 1.0, $$6).add(0.0, 0.04, 0.0));
            }
            Vec3 $$7 = this.m_20184_();
            this.m_6478_(MoverType.SELF, $$7);
            this.m_20256_($$7);
        }
        HitResult $$8 = ProjectileUtil.getHitResultOnMoveVector(this, this::m_5603_);
        if (!this.f_19794_) {
            this.m_6532_($$8);
            this.f_19812_ = true;
        }
        this.m_37283_();
        if (this.life == 0 && !this.m_20067_()) {
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
        }
        this.life++;
        if (this.m_9236_().isClientSide && this.life % 2 < 2) {
            this.m_9236_().addParticle(ParticleTypes.FIREWORK, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.05, -this.m_20184_().y * 0.5, this.f_19796_.nextGaussian() * 0.05);
        }
        if (!this.m_9236_().isClientSide && this.life > this.lifetime) {
            this.explode();
        }
    }

    private void explode() {
        this.m_9236_().broadcastEntityEvent(this, (byte) 17);
        this.m_146852_(GameEvent.EXPLODE, this.m_19749_());
        this.dealExplosionDamage();
        this.m_146870_();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.onHitEntity(entityHitResult0);
        if (!this.m_9236_().isClientSide) {
            this.explode();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult0) {
        BlockPos $$1 = new BlockPos(blockHitResult0.getBlockPos());
        this.m_9236_().getBlockState($$1).m_60682_(this.m_9236_(), $$1, this);
        if (!this.m_9236_().isClientSide() && this.hasExplosion()) {
            this.explode();
        }
        super.onHitBlock(blockHitResult0);
    }

    private boolean hasExplosion() {
        ItemStack $$0 = this.f_19804_.get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag $$1 = $$0.isEmpty() ? null : $$0.getTagElement("Fireworks");
        ListTag $$2 = $$1 != null ? $$1.getList("Explosions", 10) : null;
        return $$2 != null && !$$2.isEmpty();
    }

    private void dealExplosionDamage() {
        float $$0 = 0.0F;
        ItemStack $$1 = this.f_19804_.get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag $$2 = $$1.isEmpty() ? null : $$1.getTagElement("Fireworks");
        ListTag $$3 = $$2 != null ? $$2.getList("Explosions", 10) : null;
        if ($$3 != null && !$$3.isEmpty()) {
            $$0 = 5.0F + (float) ($$3.size() * 2);
        }
        if ($$0 > 0.0F) {
            if (this.attachedToEntity != null) {
                this.attachedToEntity.hurt(this.m_269291_().fireworks(this, this.m_19749_()), 5.0F + (float) ($$3.size() * 2));
            }
            double $$4 = 5.0;
            Vec3 $$5 = this.m_20182_();
            for (LivingEntity $$7 : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(5.0))) {
                if ($$7 != this.attachedToEntity && !(this.m_20280_($$7) > 25.0)) {
                    boolean $$8 = false;
                    for (int $$9 = 0; $$9 < 2; $$9++) {
                        Vec3 $$10 = new Vec3($$7.m_20185_(), $$7.m_20227_(0.5 * (double) $$9), $$7.m_20189_());
                        HitResult $$11 = this.m_9236_().m_45547_(new ClipContext($$5, $$10, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        if ($$11.getType() == HitResult.Type.MISS) {
                            $$8 = true;
                            break;
                        }
                    }
                    if ($$8) {
                        float $$12 = $$0 * (float) Math.sqrt((5.0 - (double) this.m_20270_($$7)) / 5.0);
                        $$7.hurt(this.m_269291_().fireworks(this, this.m_19749_()), $$12);
                    }
                }
            }
        }
    }

    private boolean isAttachedToEntity() {
        return this.f_19804_.get(DATA_ATTACHED_TO_TARGET).isPresent();
    }

    public boolean isShotAtAngle() {
        return this.f_19804_.get(DATA_SHOT_AT_ANGLE);
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 17 && this.m_9236_().isClientSide) {
            if (!this.hasExplosion()) {
                for (int $$1 = 0; $$1 < this.f_19796_.nextInt(3) + 2; $$1++) {
                    this.m_9236_().addParticle(ParticleTypes.POOF, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.05, 0.005, this.f_19796_.nextGaussian() * 0.05);
                }
            } else {
                ItemStack $$2 = this.f_19804_.get(DATA_ID_FIREWORKS_ITEM);
                CompoundTag $$3 = $$2.isEmpty() ? null : $$2.getTagElement("Fireworks");
                Vec3 $$4 = this.m_20184_();
                this.m_9236_().createFireworks(this.m_20185_(), this.m_20186_(), this.m_20189_(), $$4.x, $$4.y, $$4.z, $$3);
            }
        }
        super.m_7822_(byte0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("Life", this.life);
        compoundTag0.putInt("LifeTime", this.lifetime);
        ItemStack $$1 = this.f_19804_.get(DATA_ID_FIREWORKS_ITEM);
        if (!$$1.isEmpty()) {
            compoundTag0.put("FireworksItem", $$1.save(new CompoundTag()));
        }
        compoundTag0.putBoolean("ShotAtAngle", this.f_19804_.get(DATA_SHOT_AT_ANGLE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.life = compoundTag0.getInt("Life");
        this.lifetime = compoundTag0.getInt("LifeTime");
        ItemStack $$1 = ItemStack.of(compoundTag0.getCompound("FireworksItem"));
        if (!$$1.isEmpty()) {
            this.f_19804_.set(DATA_ID_FIREWORKS_ITEM, $$1);
        }
        if (compoundTag0.contains("ShotAtAngle")) {
            this.f_19804_.set(DATA_SHOT_AT_ANGLE, compoundTag0.getBoolean("ShotAtAngle"));
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack $$0 = this.f_19804_.get(DATA_ID_FIREWORKS_ITEM);
        return $$0.isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : $$0;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}