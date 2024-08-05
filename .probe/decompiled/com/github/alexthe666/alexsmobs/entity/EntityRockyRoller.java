package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.MovementControllerCustomCollisions;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityRockyRoller extends Monster implements ICustomCollisions {

    private static final EntityDataAccessor<Boolean> ROLLING = SynchedEntityData.defineId(EntityRockyRoller.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityRockyRoller.class, EntityDataSerializers.BOOLEAN);

    public float rollProgress;

    public float prevRollProgress;

    public int rollCounter = 0;

    public float clientRoll = 0.0F;

    private int maxRollTime = 50;

    private Vec3 rollDelta;

    private float rollYRot;

    private int rollCooldown = 0;

    private int earthquakeCooldown = 0;

    protected EntityRockyRoller(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);
        this.f_21364_ = 8;
        this.f_21342_ = new MovementControllerCustomCollisions(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rockyRollerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean checkRockyRollerSpawnRules(EntityType<? extends Monster> animal, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && m_219009_(worldIn, pos, random) && (worldIn.m_8055_(pos.below()).m_60713_(Blocks.POINTED_DRIPSTONE) || worldIn.m_8055_(pos.below()).m_280296_() || worldIn.m_8055_(pos.below()).m_60713_(Blocks.DRIPSTONE_BLOCK));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ARMOR, 20.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.7F).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new EntityRockyRoller.Navigator(this, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityRockyRoller.AIMelee());
        this.f_21345_.addGoal(2, new EntityRockyRoller.AIRollIdle(this));
        this.f_21345_.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, AbstractVillager.class, false, true));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, true));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ANGRY, false);
        this.f_19804_.define(ROLLING, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.ROCKY_ROLLER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ROCKY_ROLLER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ROCKY_ROLLER_HURT.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevRollProgress = this.rollProgress;
        if (this.isRolling()) {
            if (this.rollProgress < 5.0F) {
                this.rollProgress++;
            }
        } else if (this.rollProgress > 0.0F) {
            this.rollProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            this.setAngry(this.m_5448_() != null && this.m_5448_().isAlive() && this.m_20280_(this.m_5448_()) < 400.0);
        }
        if (this.isRolling() && this.rollCooldown <= 0) {
            this.handleRoll();
            if (this.isAngry() && this.m_6084_()) {
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(0.3F))) {
                    if (!this.m_7307_(entity) && entity != this) {
                        entity.hurt(this.m_269291_().mobAttack(this), (this.isTarget(entity) ? 5.0F : 2.0F) + this.f_19796_.nextFloat() * 2.0F);
                        this.launch(entity, this.isTarget(entity));
                        if (this.isTarget(entity)) {
                            this.maxRollTime = this.rollCounter + 10;
                        }
                    }
                }
            }
            if (this.rollCounter > 2 && !this.isMoving() || !this.m_6084_()) {
                this.setRolling(false);
            }
            this.m_274367_(1.0F);
        } else {
            this.m_274367_(0.66F);
            this.rollCounter = 0;
        }
        if (this.rollCooldown > 0) {
            this.rollCooldown--;
        }
        if (this.earthquakeCooldown > 0) {
            this.earthquakeCooldown--;
        }
    }

    private boolean isMoving() {
        return this.m_20184_().lengthSqr() > 0.02;
    }

    private void earthquake() {
        boolean flag = false;
        for (LivingEntity e : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(6.0, 8.0, 6.0))) {
            if (!(e instanceof EntityRockyRoller) && e.isAlive()) {
                e.addEffect(new MobEffectInstance(AMEffectRegistry.EARTHQUAKE.get(), 20, 0, false, false, true));
                flag = true;
            }
        }
        if (!this.m_9236_().m_45527_(this.m_20183_()) && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            BlockPos ceil = this.m_20183_().offset(0, 2, 0);
            while ((!this.m_9236_().getBlockState(ceil).m_280296_() || this.m_9236_().getBlockState(ceil).m_60734_() == Blocks.POINTED_DRIPSTONE) && ceil.m_123342_() < this.m_9236_().m_151558_()) {
                ceil = ceil.above();
            }
            int i = 2 + this.f_19796_.nextInt(2);
            int j = 2 + this.f_19796_.nextInt(2);
            int k = 2 + this.f_19796_.nextInt(2);
            float f = (float) (i + j + k) * 0.333F + 0.5F;
            double fTimesF = (double) (f * f);
            for (BlockPos blockpos1 : BlockPos.betweenClosed(ceil.offset(-i, -j, -k), ceil.offset(i, j, k))) {
                if (blockpos1.m_123331_(ceil) <= fTimesF && this.m_9236_().getBlockState(blockpos1).m_60734_() instanceof Fallable) {
                    if (!this.isHangingDripstone(blockpos1)) {
                        this.m_9236_().m_186460_(blockpos1, this.m_9236_().getBlockState(blockpos1).m_60734_(), 2);
                    } else {
                        while (this.isHangingDripstone(blockpos1.above()) && blockpos1.m_123342_() < this.m_9236_().m_151558_()) {
                            blockpos1 = blockpos1.above();
                        }
                        if (this.isHangingDripstone(blockpos1)) {
                            Vec3 vec3 = Vec3.atBottomCenterOf(blockpos1);
                            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(this.m_9236_(), new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z), this.m_9236_().getBlockState(blockpos1));
                            this.m_9236_().m_46961_(blockpos1, false);
                            this.m_9236_().m_7967_(fallingblockentity);
                        }
                    }
                    flag = true;
                }
            }
        }
        if (flag) {
            this.m_146850_(GameEvent.ENTITY_ROAR);
            this.m_5496_(AMSoundRegistry.ROCKY_ROLLER_EARTHQUAKE.get(), this.m_6121_(), this.m_6100_());
        }
    }

    private boolean isHangingDripstone(BlockPos pos) {
        return this.m_9236_().getBlockState(pos).m_60734_() instanceof PointedDripstoneBlock && this.m_9236_().getBlockState(pos).m_61143_(PointedDripstoneBlock.TIP_DIRECTION) == Direction.DOWN;
    }

    private boolean isTarget(Entity entity) {
        return this.m_5448_() != null && this.m_5448_().m_7306_(entity);
    }

    public boolean isRolling() {
        return this.f_19804_.get(ROLLING);
    }

    public void setRolling(boolean rolling) {
        this.f_19804_.set(ROLLING, rolling);
    }

    public boolean isAngry() {
        return this.f_19804_.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.f_19804_.set(ANGRY, angry);
    }

    private void handleRoll() {
        this.rollCounter++;
        if (!this.m_9236_().isClientSide) {
            if (this.f_19862_ && this.earthquakeCooldown == 0 & this.isAngry()) {
                this.earthquakeCooldown = this.maxRollTime;
                this.earthquake();
            }
            if (this.rollCounter > this.maxRollTime) {
                this.setRolling(false);
                this.rollCooldown = 10 + this.f_19796_.nextInt(10);
                this.rollCounter = 0;
                this.m_20256_(Vec3.ZERO);
            } else {
                Vec3 vec3 = this.m_20184_();
                if (this.rollCounter == 1) {
                    float f = this.m_146908_() * (float) (Math.PI / 180.0);
                    float f1 = this.m_6162_() ? 0.2F : 0.35F;
                    this.rollYRot = this.m_146908_();
                    this.rollDelta = new Vec3(vec3.x + (double) (-Mth.sin(f) * f1), 0.0, vec3.z + (double) (Mth.cos(f) * f1));
                    this.m_20256_(this.rollDelta.add(0.0, 0.27, 0.0));
                } else {
                    this.m_146922_(this.rollYRot);
                    this.m_5616_(this.rollYRot);
                    this.m_5618_(this.rollYRot);
                    this.m_20334_(this.rollDelta.x, vec3.y, this.rollDelta.z);
                }
            }
        }
    }

    private void rollFor(int time) {
        if (this.rollCooldown == 0) {
            this.maxRollTime = time;
            this.earthquakeCooldown = 0;
            this.setRolling(true);
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.onGround()) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 1.0F : 0.35F;
            e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.equals("fallingStalactite") || super.m_6673_(source);
    }

    @Override
    public int getMaxFallDistance() {
        return super.m_6056_() * 2;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.m_6084_();
    }

    @Override
    public void push(Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().add(this.m_20184_()));
    }

    @Override
    public boolean canPassThrough(BlockPos blockPos, BlockState blockstate, VoxelShape voxelShape) {
        return blockstate.m_60734_() instanceof PointedDripstoneBlock;
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState blockstate) {
        return !(blockstate.m_60734_() instanceof PointedDripstoneBlock) && super.m_20039_(pos, blockstate);
    }

    @Override
    public Vec3 collide(Vec3 vec3) {
        return ICustomCollisions.getAllowedMovementForEntity(this, vec3);
    }

    @Override
    public boolean hurt(DamageSource dmg, float amount) {
        if (!this.isMoving() && !dmg.is(DamageTypes.MAGIC) && dmg.getDirectEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) dmg.getDirectEntity();
            if (!dmg.is(DamageTypes.EXPLOSION) && !livingentity.f_19864_) {
                livingentity.hurt(this.m_269291_().thorns(this), 2.0F);
            }
        }
        return super.m_6469_(dmg, amount);
    }

    private class AIMelee extends Goal {

        private BlockPos rollFromPos = null;

        private int rollTimeout = 0;

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityRockyRoller.this.m_5448_() != null && EntityRockyRoller.this.m_5448_().isAlive() && !EntityRockyRoller.this.isRolling();
        }

        @Override
        public void tick() {
            LivingEntity enemy = EntityRockyRoller.this.m_5448_();
            double d0 = this.validRollDistance(enemy);
            double distToEnemySqr = (double) EntityRockyRoller.this.m_20270_(enemy);
            if (this.rollFromPos == null || enemy.m_20275_((double) ((float) this.rollFromPos.m_123341_() + 0.5F), (double) ((float) this.rollFromPos.m_123342_() + 0.5F), (double) this.rollFromPos.m_123343_() + 0.5) > 60.0 || !this.canEntitySeePosition(enemy, this.rollFromPos)) {
                this.rollFromPos = this.getRollAtPosition(enemy);
            }
            EntityRockyRoller.this.m_21391_(enemy, 100.0F, 5.0F);
            if (this.rollTimeout < 40 && this.rollFromPos != null && distToEnemySqr <= d0 && EntityRockyRoller.this.m_20275_((double) ((float) this.rollFromPos.m_123341_() + 0.5F), (double) ((float) this.rollFromPos.m_123342_() + 0.5F), (double) this.rollFromPos.m_123343_() + 0.5) > 2.25) {
                EntityRockyRoller.this.m_21573_().moveTo((double) ((float) this.rollFromPos.m_123341_() + 0.5F), (double) ((float) this.rollFromPos.m_123342_() + 0.5F), (double) ((float) this.rollFromPos.m_123343_() + 0.5F), 1.6);
                this.rollTimeout++;
            } else {
                double d1 = enemy.m_20185_() - EntityRockyRoller.this.m_20185_();
                double d2 = enemy.m_20189_() - EntityRockyRoller.this.m_20189_();
                float f = (float) (Mth.atan2(d2, d1) * 180.0F / (float) Math.PI) - 90.0F;
                EntityRockyRoller.this.m_146922_(f);
                EntityRockyRoller.this.f_20883_ = f;
                EntityRockyRoller.this.rollFor(30 + EntityRockyRoller.this.f_19796_.nextInt(40));
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.rollTimeout = 0;
        }

        protected double validRollDistance(LivingEntity attackTarget) {
            return (double) (3.0F + attackTarget.m_20205_());
        }

        private boolean canEntitySeePosition(LivingEntity entity, BlockPos destinationBlock) {
            Vec3 Vector3d = new Vec3(entity.m_20185_(), entity.m_20186_() + 0.5, entity.m_20189_());
            Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
            BlockHitResult result = entity.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
            return result != null && (result.getBlockPos().equals(destinationBlock) || entity.m_9236_().getBlockState(result.getBlockPos()).m_60734_() == Blocks.POINTED_DRIPSTONE);
        }

        public BlockPos getRollAtPosition(Entity target) {
            float radius = (float) (EntityRockyRoller.this.m_217043_().nextInt(2) + 6) + target.getBbWidth();
            int orbit = EntityRockyRoller.this.m_217043_().nextInt(360);
            float angle = (float) (Math.PI / 180.0) * (float) orbit;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos circlePos = new BlockPos((int) (target.getX() + extraX), (int) target.getEyeY(), (int) (target.getZ() + extraZ));
            while (!EntityRockyRoller.this.m_9236_().getBlockState(circlePos).m_60795_() && circlePos.m_123342_() < EntityRockyRoller.this.m_9236_().m_151558_()) {
                circlePos = circlePos.above();
            }
            while (!EntityRockyRoller.this.m_9236_().getBlockState(circlePos.below()).m_60634_(EntityRockyRoller.this.m_9236_(), circlePos.below(), EntityRockyRoller.this) && circlePos.m_123342_() > 1) {
                circlePos = circlePos.below();
            }
            return EntityRockyRoller.this.m_21692_(circlePos) > -1.0F ? circlePos : null;
        }
    }

    class AIRollIdle extends Goal {

        EntityRockyRoller rockyRoller;

        public AIRollIdle(EntityRockyRoller entityRockyRoller0) {
            this.rockyRoller = entityRockyRoller0;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (!this.rockyRoller.m_20096_()) {
                return false;
            } else if (!this.rockyRoller.isRolling() && this.rockyRoller.rollCooldown <= 0 && (this.rockyRoller.m_5448_() == null || !this.rockyRoller.m_5448_().isAlive())) {
                float f = this.rockyRoller.m_146908_() * (float) (Math.PI / 180.0);
                int i = 0;
                int j = 0;
                float f1 = -Mth.sin(f);
                float f2 = Mth.cos(f);
                if ((double) Math.abs(f1) > 0.5) {
                    i = (int) ((float) i + f1 / Math.abs(f1));
                }
                if ((double) Math.abs(f2) > 0.5) {
                    j = (int) ((float) j + f2 / Math.abs(f2));
                }
                return this.rockyRoller.m_9236_().getBlockState(this.rockyRoller.m_20183_().offset(i, -1, j)).m_60795_();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.rockyRoller.rollFor(30 + EntityRockyRoller.this.f_19796_.nextInt(30));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }
    }

    static class Navigator extends GroundPathNavigatorWide {

        public Navigator(Mob mob, Level world) {
            super(mob, world, 0.75F);
        }

        @Override
        protected PathFinder createPathFinder(int i) {
            this.f_26508_ = new EntityRockyRoller.RockyRollerNodeEvaluator();
            return new PathFinder(this.f_26508_, i);
        }
    }

    static class RockyRollerNodeEvaluator extends WalkNodeEvaluator {

        @Override
        protected BlockPathTypes evaluateBlockPathType(BlockGetter level, BlockPos pos, BlockPathTypes typeIn) {
            return level.getBlockState(pos).m_60734_() instanceof PointedDripstoneBlock ? BlockPathTypes.OPEN : super.evaluateBlockPathType(level, pos, typeIn);
        }
    }
}