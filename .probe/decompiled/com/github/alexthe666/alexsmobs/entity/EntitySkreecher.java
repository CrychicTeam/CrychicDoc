package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class EntitySkreecher extends Monster {

    public static final float MAX_DIST_TO_CEILING = 4.0F;

    private static final EntityDataAccessor<Boolean> CLINGING = SynchedEntityData.defineId(EntitySkreecher.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> JUMPING_UP = SynchedEntityData.defineId(EntitySkreecher.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CLAPPING = SynchedEntityData.defineId(EntitySkreecher.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> DIST_TO_CEILING = SynchedEntityData.defineId(EntitySkreecher.class, EntityDataSerializers.FLOAT);

    protected static final EntityDimensions GROUND_SIZE = EntityDimensions.scalable(0.99F, 1.35F);

    public float prevClingProgress;

    public float clingProgress;

    public float prevClapProgress;

    public float clapProgress;

    public float prevDistanceToCeiling;

    private int clapTick = 0;

    private int clingCooldown = 0;

    private boolean isUpsideDownNavigator;

    private boolean hasAttemptedWardenSpawning;

    private boolean hasGroundSize = false;

    protected EntitySkreecher(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.switchNavigator(false);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Warden.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(2, new EntitySkreecher.FollowTargetGoal());
        this.f_21345_.addGoal(3, new EntitySkreecher.WanderUpsideDownGoal());
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 30.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, true) {

            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                AABB bb = this.f_26135_.m_20191_().inflate(16.0, 1.0, 16.0);
                return new AABB(bb.minX, -64.0, bb.minZ, bb.maxX, 320.0, bb.maxZ);
            }
        });
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.skreecherSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean checkSkreecherSpawnRules(EntityType<? extends Monster> animal, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean isOnSculk = worldIn.m_8055_(pos.below()).m_60713_(Blocks.SCULK);
        return worldIn.m_46791_() != Difficulty.PEACEFUL && m_219009_(worldIn, pos, random) && isOnSculk;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    private void switchNavigator(boolean clinging) {
        if (clinging) {
            this.f_21342_ = new EntitySkreecher.MoveController();
            this.f_21344_ = this.createScreecherNavigation(this.m_9236_());
            this.isUpsideDownNavigator = true;
        } else {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isUpsideDownNavigator = false;
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 2.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DIST_TO_CEILING, 0.0F);
        this.f_19804_.define(CLINGING, false);
        this.f_19804_.define(JUMPING_UP, false);
        this.f_19804_.define(CLAPPING, false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SKREECHER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SKREECHER_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float value) {
        this.setClinging(false);
        this.setClapping(false);
        this.clingCooldown = 200 + this.f_19796_.nextInt(200);
        return super.m_6469_(source, value);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevClapProgress = this.clapProgress;
        this.prevClingProgress = this.clingProgress;
        this.prevDistanceToCeiling = this.getDistanceToCeiling();
        boolean clingVisually = this.isClinging() || this.isJumpingUp() || this.f_20899_;
        if (clingVisually && this.clingProgress < 5.0F) {
            this.clingProgress++;
        }
        if (!clingVisually && this.clingProgress > 0.0F && this.getDistanceToCeiling() == 0.0F) {
            this.clingProgress--;
        }
        boolean clapping = this.isClapping();
        if (clapping) {
            if (this.clapProgress < 5.0F) {
                this.clapProgress++;
            }
        } else if (this.clapProgress > 0.0F) {
            this.clapProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            float technicalDistToCeiling = this.calculateDistanceToCeiling();
            float gap = Math.max(technicalDistToCeiling - this.getDistanceToCeiling(), 0.0F);
            if (this.isClinging()) {
                this.m_20242_(true);
                if (technicalDistToCeiling > 4.0F || !this.m_6084_() || this.clingCooldown > 0 || this.isInFluidType()) {
                    this.setClinging(false);
                }
                float goal = Math.min(technicalDistToCeiling, 4.0F);
                if (this.getDistanceToCeiling() < goal) {
                    this.setDistanceToCeiling(Math.min(goal, this.prevDistanceToCeiling + 0.15F));
                }
                if (this.getDistanceToCeiling() > goal) {
                    this.setDistanceToCeiling(Math.max(goal, this.prevDistanceToCeiling - 0.15F));
                }
                if (this.getDistanceToCeiling() < 1.0F) {
                    gap = -0.03F;
                }
                this.m_20256_(this.m_20184_().add(0.0, (double) (gap * 0.5F), 0.0));
            } else {
                this.m_20242_(false);
                if (technicalDistToCeiling < 4.0F && this.clingCooldown <= 0) {
                    this.setClinging(true);
                }
                this.setDistanceToCeiling(Math.max(0.0F, this.prevDistanceToCeiling - 0.5F));
                if (this.m_20096_() && this.clingCooldown <= 0 && !this.isJumpingUp() && this.m_6084_() && this.f_19796_.nextFloat() < 0.0085F && technicalDistToCeiling > 4.0F && !this.m_9236_().m_45527_(this.m_20183_())) {
                    this.setJumpingUp(true);
                }
            }
        }
        if (this.isJumpingUp()) {
            if (this.m_6084_() && !this.m_9236_().m_45527_(this.m_20183_()) && (!this.f_19863_ || this.m_20096_())) {
                this.setDistanceToCeiling(1.5F);
                this.m_20256_(this.m_20184_().add(0.0, 0.2F, 0.0));
                for (int i = 0; i < 3; i++) {
                    this.m_9236_().addParticle(ParticleTypes.SCULK_CHARGE_POP, this.m_20208_(0.5), this.m_20186_() - 0.2F, this.m_20262_(0.5), 0.0, -0.2F, 0.0);
                }
            } else {
                this.setJumpingUp(false);
            }
        }
        if (this.clingCooldown > 0) {
            this.clingCooldown--;
        }
        if (!this.m_6084_() || this.clingCooldown > 0 && this.isClinging()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.25, 0.0));
        }
        if (this.isClinging() && !this.isUpsideDownNavigator) {
            this.switchNavigator(true);
        }
        if (!this.isClinging() && this.isUpsideDownNavigator) {
            this.switchNavigator(false);
        }
        if (this.isClapping() && this.m_6084_() && this.clingCooldown <= 0) {
            float dir = this.isClinging() ? -0.5F : 0.1F;
            if (this.clapTick % 8 == 0) {
                this.m_5496_(AMSoundRegistry.SKREECHER_CLAP.get(), this.m_6121_() * 3.0F, this.m_6100_());
                this.m_146850_(GameEvent.ENTITY_ROAR);
                this.angerAllNearbyWardens();
                this.m_9236_().addParticle(AMParticleRegistry.SKULK_BOOM.get(), this.m_20185_(), this.m_20188_(), this.m_20189_(), 0.0, (double) dir, 0.0);
            } else if (this.clapTick % 15 == 0) {
                this.m_5496_(AMSoundRegistry.SKREECHER_CALL.get(), this.m_6121_() * 4.0F, this.m_6100_());
            }
            if (this.clapTick >= 100 && !this.hasAttemptedWardenSpawning && AMConfig.skreechersSummonWarden) {
                this.hasAttemptedWardenSpawning = true;
                BlockPos spawnAt = this.m_20183_().below();
                while (spawnAt.m_123342_() > -64 && !this.m_9236_().getBlockState(spawnAt).m_60783_(this.m_9236_(), spawnAt, Direction.UP)) {
                    spawnAt = spawnAt.below();
                }
                Holder<Biome> holder = this.m_9236_().m_204166_(spawnAt);
                if (!this.m_9236_().isClientSide && this.getNearbyWardens().isEmpty() && holder.is(AMTagRegistry.SKREECHERS_CAN_SPAWN_WARDENS)) {
                    Warden warden = EntityType.WARDEN.create(this.m_9236_());
                    warden.m_7678_(this.m_20185_(), (double) (spawnAt.m_123342_() + 1), this.m_20189_(), this.m_146908_(), 0.0F);
                    warden.finalizeSpawn((ServerLevel) this.m_9236_(), this.m_9236_().getCurrentDifficultyAt(this.m_20183_()), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                    warden.setAttackTarget(this);
                    warden.increaseAngerAt(this, 79, false);
                    this.m_9236_().m_7967_(warden);
                }
            }
            this.clapTick++;
            if (!this.m_9236_().isClientSide) {
                if (this.m_5448_() != null && this.m_5448_().isAlive() && this.m_142582_(this.m_5448_()) && !this.m_5448_().hasEffect(MobEffects.INVISIBILITY) && !this.m_21023_(MobEffects.BLINDNESS)) {
                    double horizDist = this.m_5448_().m_20182_().subtract(this.m_20182_()).horizontalDistance();
                    if (horizDist > 20.0) {
                        this.setClapping(false);
                    }
                } else {
                    this.setClapping(false);
                }
            }
        }
        if (!this.isClinging() && !this.hasGroundSize) {
            this.m_6210_();
            this.hasGroundSize = true;
        }
        if (this.isClinging() && this.hasGroundSize) {
            this.m_6210_();
            this.hasGroundSize = false;
        }
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    public void angerAllNearbyWardens() {
        for (Warden warden : this.getNearbyWardens()) {
            if (warden.m_142582_(this)) {
                warden.increaseAngerAt(this, 100, false);
            }
        }
    }

    private List<Warden> getNearbyWardens() {
        AABB angerBox = new AABB(this.m_20185_() - 35.0, this.m_20186_() + (double) (this.isClinging() ? 5.0F : 25.0F), this.m_20189_() - 35.0, this.m_20185_() + 35.0, -64.0, this.m_20189_() + 35.0);
        return this.m_9236_().m_45976_(Warden.class, angerBox);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Clinging", this.isClinging());
        compound.putDouble("CeilDist", (double) this.getDistanceToCeiling());
        compound.putBoolean("SummonedWarden", this.hasAttemptedWardenSpawning);
        compound.putInt("ClingCooldown", this.clingCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setClinging(compound.getBoolean("Clinging"));
        this.setDistanceToCeiling((float) compound.getDouble("CeilDist"));
        this.hasAttemptedWardenSpawning = compound.getBoolean("SummonedWarden");
        this.clingCooldown = compound.getInt("ClingCooldown");
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isClinging() ? super.m_6972_(poseIn) : GROUND_SIZE.scale(this.m_6134_());
    }

    public boolean isClinging() {
        return this.f_19804_.get(CLINGING);
    }

    public void setClinging(boolean upsideDown) {
        this.f_19804_.set(CLINGING, upsideDown);
    }

    public boolean isClapping() {
        return this.f_19804_.get(CLAPPING);
    }

    public void setClapping(boolean clapping) {
        this.f_19804_.set(CLAPPING, clapping);
        if (!clapping) {
            this.clapTick = 0;
        }
    }

    public boolean isJumpingUp() {
        return this.f_19804_.get(JUMPING_UP);
    }

    public void setJumpingUp(boolean jumping) {
        this.f_19804_.set(JUMPING_UP, jumping);
    }

    protected BlockPos getPositionAbove(float height) {
        return AMBlockPos.fromCoords(this.m_20182_().x, this.m_20191_().maxY + (double) height + 0.5000001, this.m_20182_().z);
    }

    protected PathNavigation createScreecherNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                int airAbove;
                for (airAbove = 0; EntitySkreecher.this.m_9236_().getBlockState(pos).m_60795_() && (float) airAbove < 6.0F; airAbove++) {
                    pos = pos.above();
                }
                return (float) airAbove < Math.min(4.0F, (float) EntitySkreecher.this.f_19796_.nextInt(4));
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(false);
        return flyingpathnavigation;
    }

    private float calculateDistanceToCeiling() {
        BlockPos ceiling = this.getCeilingOf(this.m_20183_());
        return (float) ((double) ceiling.m_123342_() - this.m_20191_().maxY);
    }

    private boolean isOpaqueBlockAt(double x, double y, double z) {
        if (this.f_19794_) {
            return false;
        } else {
            double d = 0.3F;
            Vec3 vec3 = new Vec3(x, y, z);
            AABB axisAlignedBB = AABB.ofSize(vec3, 0.3F, 1.0E-6, 0.3F);
            return this.m_9236_().m_45556_(axisAlignedBB).filter(Predicate.not(BlockBehaviour.BlockStateBase::m_60795_)).anyMatch(p_185969_ -> {
                BlockPos blockpos = AMBlockPos.fromVec3(vec3);
                return p_185969_.m_60828_(this.m_9236_(), blockpos) && Shapes.joinIsNotEmpty(p_185969_.m_60812_(this.m_9236_(), blockpos).move(vec3.x, vec3.y, vec3.z), Shapes.create(axisAlignedBB), BooleanOp.AND);
            });
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public float getDistanceToCeiling() {
        return this.f_19804_.get(DIST_TO_CEILING);
    }

    public void setDistanceToCeiling(float dist) {
        this.f_19804_.set(DIST_TO_CEILING, dist);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.isClinging() && !this.isInFluidType()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.75));
        } else {
            super.m_7023_(travelVector);
        }
    }

    public BlockPos getCeilingOf(BlockPos usPos) {
        while (!this.m_9236_().getBlockState(usPos).m_60783_(this.m_9236_(), usPos, Direction.DOWN) && usPos.m_123342_() < this.m_9236_().m_151558_()) {
            usPos = usPos.above();
        }
        return usPos;
    }

    private class FollowTargetGoal extends Goal {

        public FollowTargetGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntitySkreecher.this.m_5448_() != null && EntitySkreecher.this.m_5448_().isAlive() && EntitySkreecher.this.clingCooldown <= 0;
        }

        @Override
        public void start() {
            EntitySkreecher.this.m_5496_(AMSoundRegistry.SKREECHER_DETECT.get(), EntitySkreecher.this.m_6121_() * 6.0F, EntitySkreecher.this.m_6100_());
        }

        @Override
        public void tick() {
            LivingEntity target = EntitySkreecher.this.m_5448_();
            if (target != null) {
                if (EntitySkreecher.this.isClinging()) {
                    BlockPos ceilAbove = EntitySkreecher.this.getCeilingOf(target.m_20183_().above());
                    EntitySkreecher.this.m_21573_().moveTo(target.m_20185_(), (double) ((float) ceilAbove.m_123342_() - EntitySkreecher.this.f_19796_.nextFloat() * 4.0F), target.m_20189_(), 1.2F);
                } else {
                    EntitySkreecher.this.m_21573_().moveTo(target.m_20185_(), target.m_20186_(), target.m_20189_(), 1.0);
                }
                Vec3 vec = target.m_20182_().subtract(EntitySkreecher.this.m_20182_());
                EntitySkreecher.this.m_21563_().setLookAt(target, 360.0F, 180.0F);
                if (vec.horizontalDistance() < 2.5 && EntitySkreecher.this.clingCooldown == 0) {
                    EntitySkreecher.this.setClapping(true);
                }
            }
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = EntitySkreecher.this;

        public MoveController() {
            super(EntitySkreecher.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.035 / d0);
                float verticalSpeed = 0.15F;
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1.multiply(1.0, (double) verticalSpeed, 1.0)));
                if (this.parentEntity.getTarget() != null) {
                    double d1 = this.parentEntity.getTarget().m_20189_() - this.parentEntity.m_20189_();
                    double d3 = this.parentEntity.getTarget().m_20186_() - this.parentEntity.m_20186_();
                    double d2 = this.parentEntity.getTarget().m_20185_() - this.parentEntity.m_20185_();
                    float f = Mth.sqrt((float) (d2 * d2 + d1 * d1));
                    this.parentEntity.m_146922_(-((float) Mth.atan2(d2, d1)) * (180.0F / (float) Math.PI));
                    this.parentEntity.m_146926_((float) (Mth.atan2(d3, (double) f) * 180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                } else if (d0 >= width) {
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                }
            }
        }
    }

    class WanderUpsideDownGoal extends RandomStrollGoal {

        private int stillTicks = 0;

        public WanderUpsideDownGoal() {
            super(EntitySkreecher.this, 1.0, 25);
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            if (EntitySkreecher.this.isClinging()) {
                int distance = 16;
                int i = 0;
                if (i < 15) {
                    Random rand = new Random();
                    BlockPos randPos = EntitySkreecher.this.m_20183_().offset(rand.nextInt(distance * 2) - distance, -4, rand.nextInt(distance * 2) - distance);
                    BlockPos lowestPos = EntitySkreecher.this.getCeilingOf(randPos).below(rand.nextInt(4));
                    return Vec3.atCenterOf(lowestPos);
                } else {
                    return null;
                }
            } else {
                return super.getPosition();
            }
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        public void stop() {
            super.stop();
            this.f_25726_ = 0.0;
            this.f_25727_ = 0.0;
            this.f_25728_ = 0.0;
        }

        @Override
        public void start() {
            this.stillTicks = 0;
            this.f_25725_.m_21573_().moveTo(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_);
        }
    }
}