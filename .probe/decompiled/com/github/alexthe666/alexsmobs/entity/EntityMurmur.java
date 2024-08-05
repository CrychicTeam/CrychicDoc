package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityMurmur extends Monster implements ISemiAquatic {

    private static final EntityDataAccessor<Optional<UUID>> HEAD_UUID = SynchedEntityData.defineId(EntityMurmur.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> HEAD_ID = SynchedEntityData.defineId(EntityMurmur.class, EntityDataSerializers.INT);

    private boolean renderFakeHead = true;

    protected EntityMurmur(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.f_21364_ = 10;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.3F).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(2, new AnimalAIWanderRanged(this, 55, 1.0, 14, 7));
        this.f_21346_.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MURMUR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MURMUR_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    public static <T extends Mob> boolean checkMurmurSpawnRules(EntityType<EntityMurmur> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || !iServerWorld.m_45527_(pos) && (pos.m_123342_() <= AMConfig.murmurSpawnHeight || iServerWorld.m_204166_(pos).is(AMTagRegistry.SPAWNS_MURMURS_IGNORE_HEIGHT)) && m_219013_(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.murmurSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return this.getHeadUUID() != null && entity.getUUID().equals(this.getHeadUUID()) || super.m_7307_(entity);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 1.2F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HEAD_UUID, Optional.empty());
        this.f_19804_.define(HEAD_ID, -1);
    }

    @Nullable
    public UUID getHeadUUID() {
        return (UUID) this.f_19804_.get(HEAD_UUID).orElse(null);
    }

    public void setHeadUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(HEAD_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getHead() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getHeadUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(HEAD_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    public boolean shouldRenderFakeHead() {
        return this.renderFakeHead;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.renderFakeHead) {
            this.renderFakeHead = false;
        }
        this.f_20883_ = this.m_146908_();
        this.f_20885_ = Mth.clamp(this.f_20885_, this.f_20883_ - 70.0F, this.f_20883_ + 70.0F);
        if (!this.m_9236_().isClientSide) {
            Entity head = this.getHead();
            if (head == null) {
                LivingEntity created = this.createHead();
                this.setHeadUUID(created.m_20148_());
                this.f_19804_.set(HEAD_ID, created.m_19879_());
            }
        }
    }

    public Vec3 getNeckBottom(float partialTick) {
        double d0 = Mth.lerp((double) partialTick, this.f_19854_, this.m_20185_());
        double d1 = Mth.lerp((double) partialTick, this.f_19855_, this.m_20186_());
        double d2 = Mth.lerp((double) partialTick, this.f_19856_, this.m_20189_());
        double height = (double) (this.m_20206_() - 0.4F) + this.calculateWalkBounce(partialTick);
        Vec3 rotatedOnDeath = new Vec3(0.0, height, 0.0);
        if (this.f_20919_ > 0) {
            float f = ((float) this.f_20919_ + partialTick - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            rotatedOnDeath = rotatedOnDeath.add((double) (f * 0.1F), (double) (f * 0.4F), 0.0).zRot((float) ((double) f * Math.PI / 2.0)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
        }
        return new Vec3(d0, d1, d2).add(rotatedOnDeath);
    }

    public double calculateWalkBounce(float partialTick) {
        float limbSwingAmount = this.f_267362_.speed(partialTick);
        float limbSwing = this.f_267362_.position() - this.f_267362_.speed() * (1.0F - partialTick);
        return Math.abs(Math.sin((double) (limbSwing * 0.9F)) * (double) limbSwingAmount * 0.25);
    }

    @Override
    public boolean shouldEnterWater() {
        return false;
    }

    @Override
    public boolean shouldLeaveWater() {
        return true;
    }

    @Override
    public boolean shouldStopMoving() {
        return false;
    }

    @Override
    public int getWaterSearchRange() {
        return 5;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("HeadUUID")) {
            this.setHeadUUID(compound.getUUID("HeadUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getHeadUUID() != null) {
            compound.putUUID("HeadUUID", this.getHeadUUID());
        }
    }

    private LivingEntity createHead() {
        EntityMurmurHead head = new EntityMurmurHead(this);
        this.m_9236_().m_7967_(head);
        return head;
    }

    public boolean isAngry() {
        Entity entity = this.getHead();
        return entity instanceof EntityMurmurHead ? ((EntityMurmurHead) entity).isAngry() : false;
    }
}