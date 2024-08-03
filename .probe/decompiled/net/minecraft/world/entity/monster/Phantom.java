package net.minecraft.world.entity.monster;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class Phantom extends FlyingMob implements Enemy {

    public static final float FLAP_DEGREES_PER_TICK = 7.448451F;

    public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);

    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.INT);

    Vec3 moveTargetPoint = Vec3.ZERO;

    BlockPos anchorPoint = BlockPos.ZERO;

    Phantom.AttackPhase attackPhase = Phantom.AttackPhase.CIRCLE;

    public Phantom(EntityType<? extends Phantom> entityTypeExtendsPhantom0, Level level1) {
        super(entityTypeExtendsPhantom0, level1);
        this.f_21364_ = 5;
        this.f_21342_ = new Phantom.PhantomMoveControl(this);
        this.f_21365_ = new Phantom.PhantomLookControl(this);
    }

    @Override
    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.f_19797_) % TICKS_PER_FLAP == 0;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new Phantom.PhantomBodyRotationControl(this);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new Phantom.PhantomAttackStrategyGoal());
        this.f_21345_.addGoal(2, new Phantom.PhantomSweepAttackGoal());
        this.f_21345_.addGoal(3, new Phantom.PhantomCircleAroundAnchorGoal());
        this.f_21346_.addGoal(1, new Phantom.PhantomAttackPlayerTargetGoal());
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ID_SIZE, 0);
    }

    public void setPhantomSize(int int0) {
        this.f_19804_.set(ID_SIZE, Mth.clamp(int0, 0, 64));
    }

    private void updatePhantomSizeInfo() {
        this.m_6210_();
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) (6 + this.getPhantomSize()));
    }

    public int getPhantomSize() {
        return this.f_19804_.get(ID_SIZE);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.35F;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (ID_SIZE.equals(entityDataAccessor0)) {
            this.updatePhantomSizeInfo();
        }
        super.m_7350_(entityDataAccessor0);
    }

    public int getUniqueFlapTickOffset() {
        return this.m_19879_() * 3;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_9236_().isClientSide) {
            float $$0 = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.f_19797_) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
            float $$1 = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.f_19797_ + 1) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
            if ($$0 > 0.0F && $$1 <= 0.0F) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.f_19796_.nextFloat() * 0.05F, 0.95F + this.f_19796_.nextFloat() * 0.05F, false);
            }
            int $$2 = this.getPhantomSize();
            float $$3 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * (float) $$2);
            float $$4 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * (float) $$2);
            float $$5 = (0.3F + $$0 * 0.45F) * ((float) $$2 * 0.2F + 1.0F);
            this.m_9236_().addParticle(ParticleTypes.MYCELIUM, this.m_20185_() + (double) $$3, this.m_20186_() + (double) $$5, this.m_20189_() + (double) $$4, 0.0, 0.0, 0.0);
            this.m_9236_().addParticle(ParticleTypes.MYCELIUM, this.m_20185_() - (double) $$3, this.m_20186_() + (double) $$5, this.m_20189_() - (double) $$4, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void aiStep() {
        if (this.m_6084_() && this.m_21527_()) {
            this.m_20254_(8);
        }
        super.m_8107_();
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.anchorPoint = this.m_20183_().above(5);
        this.setPhantomSize(0);
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("AX")) {
            this.anchorPoint = new BlockPos(compoundTag0.getInt("AX"), compoundTag0.getInt("AY"), compoundTag0.getInt("AZ"));
        }
        this.setPhantomSize(compoundTag0.getInt("Size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("AX", this.anchorPoint.m_123341_());
        compoundTag0.putInt("AY", this.anchorPoint.m_123342_());
        compoundTag0.putInt("AZ", this.anchorPoint.m_123343_());
        compoundTag0.putInt("Size", this.getPhantomSize());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        return true;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public boolean canAttackType(EntityType<?> entityType0) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        int $$1 = this.getPhantomSize();
        EntityDimensions $$2 = super.m_6972_(pose0);
        float $$3 = ($$2.width + 0.2F * (float) $$1) / $$2.width;
        return $$2.scale($$3);
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20192_();
    }

    static enum AttackPhase {

        CIRCLE, SWOOP
    }

    class PhantomAttackPlayerTargetGoal extends Goal {

        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);

        private int nextScanTick = m_186073_(20);

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                this.nextScanTick--;
                return false;
            } else {
                this.nextScanTick = m_186073_(60);
                List<Player> $$0 = Phantom.this.m_9236_().m_45955_(this.attackTargeting, Phantom.this, Phantom.this.m_20191_().inflate(16.0, 64.0, 16.0));
                if (!$$0.isEmpty()) {
                    $$0.sort(Comparator.comparing(Entity::m_20186_).reversed());
                    for (Player $$1 : $$0) {
                        if (Phantom.this.m_21040_($$1, TargetingConditions.DEFAULT)) {
                            Phantom.this.m_6710_($$1);
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity $$0 = Phantom.this.m_5448_();
            return $$0 != null ? Phantom.this.m_21040_($$0, TargetingConditions.DEFAULT) : false;
        }
    }

    class PhantomAttackStrategyGoal extends Goal {

        private int nextSweepTick;

        @Override
        public boolean canUse() {
            LivingEntity $$0 = Phantom.this.m_5448_();
            return $$0 != null ? Phantom.this.m_21040_($$0, TargetingConditions.DEFAULT) : false;
        }

        @Override
        public void start() {
            this.nextSweepTick = this.m_183277_(10);
            Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        @Override
        public void stop() {
            Phantom.this.anchorPoint = Phantom.this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, Phantom.this.anchorPoint).above(10 + Phantom.this.f_19796_.nextInt(20));
        }

        @Override
        public void tick() {
            if (Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE) {
                this.nextSweepTick--;
                if (this.nextSweepTick <= 0) {
                    Phantom.this.attackPhase = Phantom.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.m_183277_((8 + Phantom.this.f_19796_.nextInt(4)) * 20);
                    Phantom.this.m_5496_(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.this.f_19796_.nextFloat() * 0.1F);
                }
            }
        }

        private void setAnchorAboveTarget() {
            Phantom.this.anchorPoint = Phantom.this.m_5448_().m_20183_().above(20 + Phantom.this.f_19796_.nextInt(20));
            if (Phantom.this.anchorPoint.m_123342_() < Phantom.this.m_9236_().getSeaLevel()) {
                Phantom.this.anchorPoint = new BlockPos(Phantom.this.anchorPoint.m_123341_(), Phantom.this.m_9236_().getSeaLevel() + 1, Phantom.this.anchorPoint.m_123343_());
            }
        }
    }

    class PhantomBodyRotationControl extends BodyRotationControl {

        public PhantomBodyRotationControl(Mob mob0) {
            super(mob0);
        }

        @Override
        public void clientTick() {
            Phantom.this.f_20885_ = Phantom.this.f_20883_;
            Phantom.this.f_20883_ = Phantom.this.m_146908_();
        }
    }

    class PhantomCircleAroundAnchorGoal extends Phantom.PhantomMoveTargetGoal {

        private float angle;

        private float distance;

        private float height;

        private float clockwise;

        @Override
        public boolean canUse() {
            return Phantom.this.m_5448_() == null || Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE;
        }

        @Override
        public void start() {
            this.distance = 5.0F + Phantom.this.f_19796_.nextFloat() * 10.0F;
            this.height = -4.0F + Phantom.this.f_19796_.nextFloat() * 9.0F;
            this.clockwise = Phantom.this.f_19796_.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        @Override
        public void tick() {
            if (Phantom.this.f_19796_.nextInt(this.m_183277_(350)) == 0) {
                this.height = -4.0F + Phantom.this.f_19796_.nextFloat() * 9.0F;
            }
            if (Phantom.this.f_19796_.nextInt(this.m_183277_(250)) == 0) {
                this.distance++;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }
            if (Phantom.this.f_19796_.nextInt(this.m_183277_(450)) == 0) {
                this.angle = Phantom.this.f_19796_.nextFloat() * 2.0F * (float) Math.PI;
                this.selectNext();
            }
            if (this.m_33246_()) {
                this.selectNext();
            }
            if (Phantom.this.moveTargetPoint.y < Phantom.this.m_20186_() && !Phantom.this.m_9236_().m_46859_(Phantom.this.m_20183_().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }
            if (Phantom.this.moveTargetPoint.y > Phantom.this.m_20186_() && !Phantom.this.m_9236_().m_46859_(Phantom.this.m_20183_().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }
        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(Phantom.this.anchorPoint)) {
                Phantom.this.anchorPoint = Phantom.this.m_20183_();
            }
            this.angle = this.angle + this.clockwise * 15.0F * (float) (Math.PI / 180.0);
            Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf(Phantom.this.anchorPoint).add((double) (this.distance * Mth.cos(this.angle)), (double) (-4.0F + this.height), (double) (this.distance * Mth.sin(this.angle)));
        }
    }

    class PhantomLookControl extends LookControl {

        public PhantomLookControl(Mob mob0) {
            super(mob0);
        }

        @Override
        public void tick() {
        }
    }

    class PhantomMoveControl extends MoveControl {

        private float speed = 0.1F;

        public PhantomMoveControl(Mob mob0) {
            super(mob0);
        }

        @Override
        public void tick() {
            if (Phantom.this.f_19862_) {
                Phantom.this.m_146922_(Phantom.this.m_146908_() + 180.0F);
                this.speed = 0.1F;
            }
            double $$0 = Phantom.this.moveTargetPoint.x - Phantom.this.m_20185_();
            double $$1 = Phantom.this.moveTargetPoint.y - Phantom.this.m_20186_();
            double $$2 = Phantom.this.moveTargetPoint.z - Phantom.this.m_20189_();
            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$3) > 1.0E-5F) {
                double $$4 = 1.0 - Math.abs($$1 * 0.7F) / $$3;
                $$0 *= $$4;
                $$2 *= $$4;
                $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                double $$5 = Math.sqrt($$0 * $$0 + $$2 * $$2 + $$1 * $$1);
                float $$6 = Phantom.this.m_146908_();
                float $$7 = (float) Mth.atan2($$2, $$0);
                float $$8 = Mth.wrapDegrees(Phantom.this.m_146908_() + 90.0F);
                float $$9 = Mth.wrapDegrees($$7 * (180.0F / (float) Math.PI));
                Phantom.this.m_146922_(Mth.approachDegrees($$8, $$9, 4.0F) - 90.0F);
                Phantom.this.f_20883_ = Phantom.this.m_146908_();
                if (Mth.degreesDifferenceAbs($$6, Phantom.this.m_146908_()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }
                float $$10 = (float) (-(Mth.atan2(-$$1, $$3) * 180.0F / (float) Math.PI));
                Phantom.this.m_146926_($$10);
                float $$11 = Phantom.this.m_146908_() + 90.0F;
                double $$12 = (double) (this.speed * Mth.cos($$11 * (float) (Math.PI / 180.0))) * Math.abs($$0 / $$5);
                double $$13 = (double) (this.speed * Mth.sin($$11 * (float) (Math.PI / 180.0))) * Math.abs($$2 / $$5);
                double $$14 = (double) (this.speed * Mth.sin($$10 * (float) (Math.PI / 180.0))) * Math.abs($$1 / $$5);
                Vec3 $$15 = Phantom.this.m_20184_();
                Phantom.this.m_20256_($$15.add(new Vec3($$12, $$14, $$13).subtract($$15).scale(0.2)));
            }
        }
    }

    abstract class PhantomMoveTargetGoal extends Goal {

        public PhantomMoveTargetGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return Phantom.this.moveTargetPoint.distanceToSqr(Phantom.this.m_20185_(), Phantom.this.m_20186_(), Phantom.this.m_20189_()) < 4.0;
        }
    }

    class PhantomSweepAttackGoal extends Phantom.PhantomMoveTargetGoal {

        private static final int CAT_SEARCH_TICK_DELAY = 20;

        private boolean isScaredOfCat;

        private int catSearchTick;

        @Override
        public boolean canUse() {
            return Phantom.this.m_5448_() != null && Phantom.this.attackPhase == Phantom.AttackPhase.SWOOP;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity $$0 = Phantom.this.m_5448_();
            if ($$0 == null) {
                return false;
            } else if (!$$0.isAlive()) {
                return false;
            } else {
                if ($$0 instanceof Player $$1 && ($$0.m_5833_() || $$1.isCreative())) {
                    return false;
                }
                if (!this.canUse()) {
                    return false;
                } else {
                    if (Phantom.this.f_19797_ > this.catSearchTick) {
                        this.catSearchTick = Phantom.this.f_19797_ + 20;
                        List<Cat> $$2 = Phantom.this.m_9236_().m_6443_(Cat.class, Phantom.this.m_20191_().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
                        for (Cat $$3 : $$2) {
                            $$3.hiss();
                        }
                        this.isScaredOfCat = !$$2.isEmpty();
                    }
                    return !this.isScaredOfCat;
                }
            }
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            Phantom.this.m_6710_(null);
            Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = Phantom.this.m_5448_();
            if ($$0 != null) {
                Phantom.this.moveTargetPoint = new Vec3($$0.m_20185_(), $$0.m_20227_(0.5), $$0.m_20189_());
                if (Phantom.this.m_20191_().inflate(0.2F).intersects($$0.m_20191_())) {
                    Phantom.this.m_7327_($$0);
                    Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
                    if (!Phantom.this.m_20067_()) {
                        Phantom.this.m_9236_().m_46796_(1039, Phantom.this.m_20183_(), 0);
                    }
                } else if (Phantom.this.f_19862_ || Phantom.this.f_20916_ > 0) {
                    Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
                }
            }
        }
    }
}