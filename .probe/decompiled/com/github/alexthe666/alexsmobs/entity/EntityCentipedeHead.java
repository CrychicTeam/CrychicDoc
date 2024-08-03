package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFleeLight;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.Arrays;
import java.util.List;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityCentipedeHead extends Monster {

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityCentipedeHead.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> CHILD_ID = SynchedEntityData.defineId(EntityCentipedeHead.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SEGMENT_COUNT = SynchedEntityData.defineId(EntityCentipedeHead.class, EntityDataSerializers.INT);

    public final float[] ringBuffer = new float[64];

    public int ringBufferIndex = -1;

    private EntityCentipedeBody[] parts;

    protected EntityCentipedeHead(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21364_ = 13;
        this.m_274367_(3.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 35.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 6.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.22F);
    }

    public static <T extends Mob> boolean canCentipedeSpawn(EntityType<EntityCentipedeHead> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || !iServerWorld.m_45527_(pos) && pos.m_123342_() <= AMConfig.caveCentipedeSpawnHeight && m_219013_(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.caveCentipedeSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.4, false));
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 13, false));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new AnimalAIFleeLight(this, 1.0, 75, 5));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 20, true, true, null));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, AbstractVillager.class, 20, true, true, null));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, EntityCockroach.class, 45, true, true, null));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.CENTIPEDE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.CENTIPEDE_HURT.get();
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.m_5496_(AMSoundRegistry.CENTIPEDE_WALK.get(), 1.0F, 1.0F);
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    public int getHeadRotSpeed() {
        return 1;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(CHILD_ID, -1);
        this.f_19804_.define(SEGMENT_COUNT, 5);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (super.m_7327_(entityIn)) {
            if (entityIn instanceof LivingEntity) {
                Difficulty difficulty = this.m_9236_().m_46791_();
                int i;
                if (difficulty == Difficulty.NORMAL) {
                    i = 10;
                } else if (difficulty == Difficulty.HARD) {
                    i = 20;
                } else {
                    i = 3;
                }
                ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 1));
            }
            this.m_5496_(AMSoundRegistry.CENTIPEDE_ATTACK.get(), this.m_6121_(), this.m_6100_());
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            return true;
        } else {
            return false;
        }
    }

    public int getSegmentCount() {
        return Math.max(this.f_19804_.get(SEGMENT_COUNT), 1);
    }

    public void setSegmentCount(int segments) {
        this.f_19804_.set(SEGMENT_COUNT, segments);
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        entities.stream().filter(entity -> !(entity instanceof EntityCentipedeBody) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setSegmentCount(this.f_19796_.nextInt(4) + 5);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
        compound.putInt("SegCount", this.getSegmentCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
        this.setSegmentCount(compound.getInt("SegCount"));
    }

    private boolean shouldReplaceParts() {
        if (this.parts != null && this.parts[0] != null && this.parts.length == this.getSegmentCount()) {
            for (int i = 0; i < this.getSegmentCount(); i++) {
                if (this.parts[i] == null) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.f_19817_ = false;
        this.f_20883_ = Mth.clamp(this.m_146908_(), this.f_20883_ - 2.0F, this.f_20883_ + 2.0F);
        this.f_20885_ = this.f_20883_;
        if (this.ringBufferIndex < 0) {
            Arrays.fill(this.ringBuffer, this.f_20883_);
        }
        if (this.updateRingBuffer() || this.ringBufferIndex < 0) {
            this.ringBufferIndex++;
        }
        if (this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }
        this.ringBuffer[this.ringBufferIndex] = this.m_146908_();
        if (!this.m_9236_().isClientSide) {
            Entity child = this.getChild();
            if (child == null) {
                LivingEntity partParent = this;
                this.parts = new EntityCentipedeBody[this.getSegmentCount()];
                Vec3 prevPos = this.m_20182_();
                float backOffset = 0.45F;
                for (int i = 0; i < this.getSegmentCount(); i++) {
                    EntityCentipedeBody part = this.createBody(partParent, i == this.getSegmentCount() - 1);
                    part.setParent(partParent);
                    part.setBodyIndex(i);
                    if (partParent == this) {
                        this.setChildId(part.m_20148_());
                        this.f_19804_.set(CHILD_ID, part.m_19879_());
                    }
                    if (partParent instanceof EntityCentipedeBody body) {
                        body.setChildId(part.m_20148_());
                    }
                    part.m_146884_(part.tickMultipartPosition(this.m_19879_(), backOffset, prevPos, this.m_146909_(), this.getYawForPart(i), false));
                    this.m_9236_().m_7967_(part);
                    this.parts[i] = part;
                    partParent = part;
                    backOffset = part.getBackOffset();
                    prevPos = part.m_20182_();
                }
            }
            if (this.f_19797_ > 1) {
                if (this.shouldReplaceParts() && this.getChild() instanceof EntityCentipedeBody) {
                    this.parts = new EntityCentipedeBody[this.getSegmentCount()];
                    this.parts[0] = (EntityCentipedeBody) this.getChild();
                    this.f_19804_.set(CHILD_ID, this.parts[0].m_19879_());
                    for (int i = 1; i < this.parts.length && this.parts[i - 1].getChild() instanceof EntityCentipedeBody; i++) {
                        this.parts[i] = (EntityCentipedeBody) this.parts[i - 1].getChild();
                    }
                }
                Vec3 prev = this.m_20182_();
                float xRot = this.m_146909_();
                float backOffset = 0.45F;
                for (int i = 0; i < this.getSegmentCount(); i++) {
                    if (this.parts[i] != null) {
                        float reqRot = this.getYawForPart(i);
                        prev = this.parts[i].tickMultipartPosition(this.m_19879_(), backOffset, prev, xRot, reqRot, true);
                        xRot = this.parts[i].getXRot();
                        backOffset = this.parts[i].getBackOffset();
                    }
                }
            }
        }
    }

    private boolean updateRingBuffer() {
        return this.m_20184_().lengthSqr() >= 0.005;
    }

    public EntityCentipedeBody createBody(LivingEntity parent, boolean tail) {
        return tail ? new EntityCentipedeBody(AMEntityRegistry.CENTIPEDE_TAIL.get(), parent, 0.84F, 180.0F, 0.0F) : new EntityCentipedeBody(AMEntityRegistry.CENTIPEDE_BODY.get(), parent, 0.84F, 180.0F, 0.0F);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    private float getYawForPart(int i) {
        return this.getRingBuffer(4 + i * 4, 1.0F);
    }

    public float getRingBuffer(int bufferOffset, float partialTicks) {
        if (this.m_21224_()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - bufferOffset & 63;
        int j = this.ringBufferIndex - bufferOffset - 1 & 63;
        float d0 = this.ringBuffer[i];
        float d1 = this.ringBuffer[j] - d0;
        return Mth.wrapDegrees(d0 + d1 * partialTicks);
    }
}