package io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FrozenHumanoid extends LivingEntity {

    protected static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> DATA_IS_SITTING = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Float> DATA_FROZEN_SPEED = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING_AMOUNT = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> DATA_ATTACK_TIME = SynchedEntityData.defineId(FrozenHumanoid.class, EntityDataSerializers.FLOAT);

    private float shatterProjectileDamage;

    private int deathTimer = -1;

    private UUID summonerUUID;

    private LivingEntity cachedSummoner;

    private boolean isAutoSpinAttack;

    private HumanoidArm mainArm = HumanoidArm.RIGHT;

    public FrozenHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_IS_BABY, false);
        this.f_19804_.define(DATA_IS_SITTING, false);
        this.f_19804_.define(DATA_FROZEN_SPEED, 0.0F);
        this.f_19804_.define(DATA_LIMB_SWING, 0.0F);
        this.f_19804_.define(DATA_LIMB_SWING_AMOUNT, 0.0F);
        this.f_19804_.define(DATA_ATTACK_TIME, 0.0F);
    }

    public FrozenHumanoid(Level level, LivingEntity entityToCopy) {
        this(EntityRegistry.FROZEN_HUMANOID.get(), level);
        this.m_7678_(entityToCopy.m_20185_(), entityToCopy.m_20186_(), entityToCopy.m_20189_(), entityToCopy.m_146908_(), entityToCopy.m_146909_());
        if (entityToCopy.isBaby()) {
            this.f_19804_.set(DATA_IS_BABY, true);
        }
        if (entityToCopy.m_20159_() && entityToCopy.m_20202_() != null && entityToCopy.m_20202_().shouldRiderSit()) {
            this.f_19804_.set(DATA_IS_SITTING, true);
        }
        this.m_5618_(entityToCopy.yBodyRot);
        this.f_20884_ = this.f_20883_;
        this.m_5616_(entityToCopy.getYHeadRot());
        this.f_20886_ = this.f_20885_;
        float limbSwing = entityToCopy.walkAnimation.speed();
        float limbSwingAmount = entityToCopy.walkAnimation.position();
        this.f_19804_.set(DATA_LIMB_SWING, limbSwing);
        this.f_19804_.set(DATA_LIMB_SWING_AMOUNT, limbSwingAmount);
        this.f_19804_.set(DATA_ATTACK_TIME, entityToCopy.attackAnim);
        this.m_20124_(entityToCopy.m_20089_());
        this.isAutoSpinAttack = entityToCopy.isAutoSpinAttack();
        this.mainArm = entityToCopy.getMainArm();
        if (entityToCopy instanceof Player player) {
            this.m_6593_(player.getDisplayName());
            this.m_20340_(true);
        } else {
            this.m_20340_(false);
        }
        this.setSummoner(entityToCopy);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.m_20148_();
            this.cachedSummoner = owner;
        }
    }

    public LivingEntity getSummoner() {
        if (this.cachedSummoner != null && this.cachedSummoner.isAlive()) {
            return this.cachedSummoner;
        } else if (this.summonerUUID != null && this.m_9236_() instanceof ServerLevel) {
            if (((ServerLevel) this.m_9236_()).getEntity(this.summonerUUID) instanceof LivingEntity livingEntity) {
                this.cachedSummoner = livingEntity;
            }
            return this.cachedSummoner;
        } else {
            return null;
        }
    }

    public boolean isSitting() {
        return this.f_19804_.get(DATA_IS_SITTING);
    }

    @Override
    public boolean isBaby() {
        return this.f_19804_.get(DATA_IS_BABY);
    }

    public float getLimbSwing() {
        return this.f_19804_.get(DATA_LIMB_SWING);
    }

    public float getLimbSwingAmount() {
        return this.f_19804_.get(DATA_LIMB_SWING_AMOUNT);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.deathTimer > 0) {
            this.deathTimer--;
        }
        if (this.deathTimer == 0) {
            this.hurt(this.m_9236_().damageSources().generic(), 100.0F);
        }
    }

    public void setDeathTimer(int timeInTicks) {
        this.deathTimer = timeInTicks;
    }

    public float getAttacktime() {
        return this.f_19804_.get(DATA_ATTACK_TIME);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GLASS_BREAK;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.m_9236_().isClientSide && !this.m_6673_(pSource)) {
            this.spawnIcicleShards(this.m_146892_(), this.shatterProjectileDamage);
            this.m_6677_(pSource);
            this.m_146870_();
            return true;
        } else {
            return false;
        }
    }

    private void spawnIcicleShards(Vec3 origin, float damage) {
        int count = 8;
        int offset = 360 / count;
        for (int i = 0; i < count; i++) {
            Vec3 motion = new Vec3(0.0, 0.0, 0.55);
            motion = motion.xRot((float) (Math.PI / 6));
            motion = motion.yRot((float) (offset * i) * (float) (Math.PI / 180.0));
            IcicleProjectile shard = new IcicleProjectile(this.m_9236_(), this.getSummoner());
            shard.setDamage(damage);
            shard.m_20256_(motion);
            Vec3 spawn = origin.add(motion.multiply(1.0, 0.0, 1.0).normalize().scale(0.5));
            Vec2 angle = Utils.rotationFromDirection(motion);
            shard.m_7678_(spawn.x, spawn.y - shard.m_20191_().getYsize() / 2.0, spawn.z, angle.y, angle.x);
            this.m_9236_().m_7967_(shard);
        }
    }

    public void setShatterDamage(float damage) {
        this.shatterProjectileDamage = damage;
    }

    @Override
    public boolean isAutoSpinAttack() {
        return this.isAutoSpinAttack;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("Summoner")) {
            this.summonerUUID = compoundTag.getUUID("Summoner");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.summonerUUID != null) {
            compoundTag.putUUID("Summoner", this.summonerUUID);
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return this.mainArm;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.MAX_HEALTH, 1.0).add(Attributes.FOLLOW_RANGE, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 100.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }
}