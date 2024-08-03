package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class FerrouslimeEntity extends Monster {

    private Map<Integer, Vec3> headOffsets = new HashMap();

    public float prevHeadCount = 1.0F;

    private float prevMergeProgress;

    private float mergeProgress;

    private float prevAttackProgress;

    private float attackProgress;

    private static final EntityDataAccessor<Integer> HEADS = SynchedEntityData.defineId(FerrouslimeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(FerrouslimeEntity.class, EntityDataSerializers.INT);

    private int mergeCooldown = 0;

    private int noMoveTime = 0;

    public FerrouslimeEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.f_21364_ = 1;
        this.f_21342_ = new FerrouslimeEntity.MoveController();
        this.prevMergeProgress = 5.0F;
        this.mergeProgress = 5.0F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new FerrouslimeEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new FerrouslimeEntity.FormGoal());
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 20));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new MobTarget3DGoal(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(HEADS, 1);
        this.f_19804_.define(ATTACK_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void dropFromLootTable(DamageSource source, boolean b) {
        if (this.getHeadCount() <= 1) {
            super.m_7625_(source, b);
        }
    }

    public boolean isFakeEntity() {
        return this.f_19803_;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.m_5616_(this.m_146908_());
        this.prevMergeProgress = this.mergeProgress;
        this.prevAttackProgress = this.attackProgress;
        if (this.prevHeadCount != (float) this.getHeadCount()) {
            this.refreshDimensions();
            if (this.mergeProgress < 5.0F) {
                this.mergeProgress++;
            } else {
                this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) Mth.clamp(this.getHeadCount() * 10, 10, 100));
                this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) Mth.clamp(this.getHeadCount() * 2, 2, 10));
                double d = this.m_21051_(Attributes.MAX_HEALTH).getValue();
                if ((double) this.m_21223_() < d && this.getHeadCount() > 0) {
                    this.m_5634_((float) Math.ceil(d - (double) this.m_21223_()));
                }
                this.prevHeadCount = (float) this.getHeadCount();
            }
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else {
            LivingEntity target = this.m_5448_();
            if (this.attackProgress >= 3.0F && target != null && this.m_20270_(target) < this.getSlimeSize(1.0F)) {
                target.hurt(this.m_269291_().mobAttack(this), 4.0F + (float) (this.getHeadCount() * 2));
            }
            if (this.attackProgress > 0.0F) {
                this.attackProgress--;
            }
        }
        if (this.m_9236_().isClientSide && this.m_6084_()) {
            float slimeSize = this.getSlimeSize(1.0F);
            for (int i = 0; (double) i < Math.ceil((double) slimeSize); i++) {
                double particleX = this.m_20185_() + (this.f_19796_.nextDouble() - 0.5) * (double) (slimeSize + 1.5F);
                double particleY = this.m_20186_() + (this.f_19796_.nextDouble() - 0.5) * (double) (slimeSize + 1.5F);
                double particleZ = this.m_20189_() + (this.f_19796_.nextDouble() - 0.5) * (double) (slimeSize + 1.5F);
                this.m_9236_().addParticle(ACParticleRegistry.FERROUSLIME.get(), particleX, particleY, particleZ, (double) this.m_19879_(), 0.0, 0.0);
            }
            AlexsCaves.PROXY.playWorldSound(this, (byte) 13);
        } else {
            LivingEntity living = this.m_5448_();
            if (living != null && living.isAlive()) {
                if (this.m_20184_().length() < 0.1) {
                    this.noMoveTime++;
                } else {
                    this.noMoveTime = 0;
                }
                if (this.noMoveTime > 40 && this.mergeCooldown <= 0) {
                    this.split(1200);
                }
            }
        }
        if (this.mergeCooldown > 0) {
            this.mergeCooldown--;
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        if (this.getHeadCount() >= 2 && this.m_21224_()) {
            int ours = this.getHeadCount() / 2;
            int theirs = this.getHeadCount() - ours;
            this.mergeCooldown = 1200;
            this.m_9236_().m_7967_(this.makeSlime(ours, 1200));
            this.m_9236_().m_7967_(this.makeSlime(theirs, 1200));
        }
        super.m_142687_(removalReason);
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_(d0, d1, d2);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions dimensions) {
        return 0.625F * dimensions.height;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.m_7350_(entityDataAccessor);
        if (HEADS.equals(entityDataAccessor)) {
            this.refreshDimensions();
            this.m_146922_(this.f_20885_);
            this.f_20883_ = this.f_20885_;
            this.mergeProgress = 0.0F;
        }
    }

    public int getHeadCount() {
        return this.f_19804_.get(HEADS);
    }

    public void setHeadCount(int headCount) {
        this.f_19804_.set(HEADS, headCount);
    }

    public float getMergeProgress(float partialTick) {
        return (this.prevMergeProgress + (this.mergeProgress - this.prevMergeProgress) * partialTick) * 0.2F;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level, AdvancedPathNavigate.MovementType.FLYING);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return this.m_9236_().getBlockState(pos).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    public Vec3 getHeadOffsetPos(int i) {
        if (i <= 1) {
            return Vec3.ZERO;
        } else if (this.headOffsets.containsKey(i)) {
            return (Vec3) this.headOffsets.get(i);
        } else {
            Vec3 vec = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)).scale((double) (this.getSlimeSize(1.0F) * 0.5F));
            this.headOffsets.put(i, vec);
            return vec;
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return super.m_6972_(pose0).scale(this.getSlimeSize(1.0F));
    }

    public float getSlimeSize(float partialTicks) {
        float smoothHeadCount = (float) (this.getHeadCount() - 1) + this.getMergeProgress(partialTicks);
        return Math.min((float) (Math.log((double) smoothHeadCount) + 1.0) * 1.2F, 3.2F);
    }

    public boolean split(int cooldown) {
        if (this.getHeadCount() >= 2) {
            int ours = this.getHeadCount() / 2;
            int theirs = this.getHeadCount() - ours;
            this.mergeCooldown = 1200;
            this.m_9236_().m_7967_(this.makeSlime(ours, 1200));
            this.m_9236_().m_7967_(this.makeSlime(theirs, 1200));
            this.remove(Entity.RemovalReason.DISCARDED);
            return true;
        } else {
            return false;
        }
    }

    private FerrouslimeEntity makeSlime(int heads, int cooldown) {
        Component component = this.m_7770_();
        FerrouslimeEntity ferrouslime = ACEntityRegistry.FERROUSLIME.get().create(this.m_9236_());
        ferrouslime.m_146884_(this.m_20182_());
        ferrouslime.setHeadCount(heads);
        ferrouslime.m_6593_(component);
        ferrouslime.m_21557_(this.m_21525_());
        ferrouslime.m_20331_(this.m_20147_());
        ferrouslime.mergeCooldown = cooldown;
        ferrouslime.m_146922_(this.f_20885_);
        ferrouslime.f_20883_ = this.f_20885_;
        return ferrouslime;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setHeadCount(1 + this.f_19796_.nextInt(2));
        return super.m_6518_(level, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean canCutCorner(BlockPathTypes types) {
        return true;
    }

    public float getAttackProgress(float partialTicks) {
        return (this.prevAttackProgress + (this.attackProgress - this.prevAttackProgress) * partialTicks) * 0.2F;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.f_19804_.set(ATTACK_TICK, 10);
        return super.m_7327_(entityIn);
    }

    private boolean canForm() {
        return this.m_6084_() && this.mergeCooldown <= 0;
    }

    @Override
    public int getExperienceReward() {
        return 2;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.FERROUSLIME_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.FERROUSLIME_DEATH.get();
    }

    private class FormGoal extends Goal {

        int executionCooldown = 0;

        FerrouslimeEntity otherslime;

        public FormGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!FerrouslimeEntity.this.canForm()) {
                return false;
            } else if (this.executionCooldown-- >= 0) {
                return false;
            } else {
                this.executionCooldown = FerrouslimeEntity.this.m_5448_() == null ? 100 : 20;
                FerrouslimeEntity closest = null;
                for (FerrouslimeEntity slime : FerrouslimeEntity.this.m_9236_().m_45976_(FerrouslimeEntity.class, FerrouslimeEntity.this.m_20191_().inflate(30.0, 30.0, 30.0))) {
                    if (slime != FerrouslimeEntity.this && slime.canForm() && (closest == null || slime.m_20270_(FerrouslimeEntity.this) < closest.m_20270_(FerrouslimeEntity.this))) {
                        closest = slime;
                    }
                }
                this.otherslime = closest;
                return this.otherslime != null;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.otherslime != null && FerrouslimeEntity.this.canForm() && this.otherslime.canForm() && FerrouslimeEntity.this.m_20270_(this.otherslime) < 32.0F;
        }

        @Override
        public void tick() {
            FerrouslimeEntity.this.m_21573_().moveTo(this.otherslime, 1.0);
            if ((double) FerrouslimeEntity.this.m_20270_(this.otherslime) <= 0.5 + (double) (FerrouslimeEntity.this.m_20205_() + this.otherslime.m_20205_()) / 2.0 && this.otherslime.canForm()) {
                FerrouslimeEntity.this.setHeadCount(FerrouslimeEntity.this.getHeadCount() + this.otherslime.getHeadCount());
                this.otherslime.remove(Entity.RemovalReason.DISCARDED);
                FerrouslimeEntity.this.m_216990_(ACSoundRegistry.FERROUSLIME_COMBINE.get());
                this.otherslime = null;
                FerrouslimeEntity.this.mergeCooldown = 600;
            }
        }
    }

    private class MeleeGoal extends Goal {

        private int cooldown = 0;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return FerrouslimeEntity.this.m_5448_() != null && FerrouslimeEntity.this.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = FerrouslimeEntity.this.m_5448_();
            if (target != null && target.isAlive()) {
                FerrouslimeEntity.this.m_21573_().moveTo(target, 1.0);
                FerrouslimeEntity.this.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                if (FerrouslimeEntity.this.m_20270_(target) < 1.0F + FerrouslimeEntity.this.getSlimeSize(1.0F) && FerrouslimeEntity.this.m_142582_(target) && this.cooldown == 0) {
                    FerrouslimeEntity.this.doHurtTarget(target);
                    this.cooldown = 10;
                }
            }
            if (this.cooldown > 0) {
                this.cooldown--;
            }
        }

        @Override
        public void stop() {
            this.cooldown = 0;
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity;

        private Direction lastSlideDirection;

        private int slideStop = 0;

        private int slideFor = 0;

        public MoveController() {
            super(FerrouslimeEntity.this);
            this.parentEntity = FerrouslimeEntity.this;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                BlockPos target = BlockPos.containing(this.f_24975_, this.f_24976_, this.f_24977_);
                BlockPos centerPos = BlockPos.containing(this.parentEntity.m_20185_(), this.parentEntity.m_20227_(0.5), this.parentEntity.m_20189_());
                BlockPos closest = centerPos;
                if (this.slideStop > 0) {
                    this.slideStop--;
                } else {
                    this.lastSlideDirection = null;
                    for (Direction direction : Direction.values()) {
                        BlockPos check = centerPos.relative(direction);
                        if (check.m_123331_(target) < closest.m_123331_(target) && FerrouslimeEntity.this.m_9236_().getBlockState(check).m_60795_()) {
                            this.lastSlideDirection = direction;
                        }
                    }
                    this.slideStop = 6;
                }
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                if (this.lastSlideDirection != null && !this.parentEntity.f_19862_) {
                    vector3d = vector3d.multiply((double) Math.abs(this.lastSlideDirection.getStepX()), (double) Math.abs(this.lastSlideDirection.getStepY()), (double) Math.abs(this.lastSlideDirection.getStepZ()));
                }
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                LivingEntity attackTarget = this.parentEntity.getTarget();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.25 / d0);
                if (d0 < 0.5) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else {
                    this.parentEntity.m_20256_(vector3d1);
                    if (d0 >= width && attackTarget == null) {
                        this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                        if (FerrouslimeEntity.this.m_5448_() != null) {
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        }
                    }
                }
            }
        }
    }
}