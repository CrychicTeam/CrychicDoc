package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntitySpectre extends Animal implements FlyingAnimal {

    private static final EntityDataAccessor<Integer> CARDINAL_ORDINAL = SynchedEntityData.defineId(EntitySpectre.class, EntityDataSerializers.INT);

    public float birdPitch = 0.0F;

    public float prevBirdPitch = 0.0F;

    public Vec3 lurePos = null;

    protected EntitySpectre(EntityType type, Level world) {
        super(type, world);
        this.f_21342_ = new EntitySpectre.MoveHelperController(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.spectreSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canSpectreSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(pos.below());
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SPECTRE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SPECTRE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SPECTRE_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 1.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CARDINAL_ORDINAL, Direction.NORTH.get3DDataValue());
    }

    public int getCardinalInt() {
        return this.f_19804_.get(CARDINAL_ORDINAL);
    }

    public void setCardinalInt(int command) {
        this.f_19804_.set(CARDINAL_ORDINAL, command);
    }

    public Direction getCardinalDirection() {
        return Direction.from3DDataValue(this.getCardinalInt());
    }

    public void setCardinalDirection(Direction dir) {
        this.setCardinalInt(dir.get3DDataValue());
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new EntitySpectre.TemptHeartGoal(this, 1.0, Ingredient.of(AMItemRegistry.SOUL_HEART.get()), false));
        this.f_21345_.addGoal(2, new EntitySpectre.FlyGoal(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(DamageTypes.MAGIC) && !source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.isCreativePlayer() || super.m_6673_(source);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_146926_(0.0F);
        this.randomizeDirection();
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void tick() {
        super.m_8119_();
        Vec3 vector3d1 = this.m_20184_();
        this.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
        this.f_20883_ = this.m_146908_();
        this.prevBirdPitch = this.birdPitch;
        this.f_19794_ = true;
        this.birdPitch = (float) (-((double) ((float) this.m_20184_().y * 0.5F) * 180.0F / (float) Math.PI));
        if (this.m_21524_() != null && !(this.m_21524_() instanceof LeashFenceKnotEntity)) {
            Entity entity = this.m_21524_();
            float f = this.m_20270_(entity);
            if (f > 10.0F) {
                double d0 = (this.m_20185_() - entity.getX()) / (double) f;
                double d1 = (this.m_20186_() - entity.getY()) / (double) f;
                double d2 = (this.m_20189_() - entity.getZ()) / (double) f;
                entity.setDeltaMovement(entity.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4, d0), Math.copySign(d1 * d1 * 0.4, d1), Math.copySign(d2 * d2 * 0.4, d2)));
            }
            entity.fallDistance = 0.0F;
            if (entity.getDeltaMovement().y < 0.0) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.7F, 1.0));
            }
            if (entity.isShiftKeyDown()) {
                this.m_21455_(true, true);
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    @Override
    protected void tickLeash() {
        if (this.m_21524_() != null) {
            if (this.m_21524_().isPassenger() || this.m_21524_() instanceof LeashFenceKnotEntity) {
                super.m_6119_();
                return;
            }
            float f = this.m_20270_(this.m_21524_());
            if (f > 30.0F) {
                double lvt_3_1_ = (this.m_21524_().getX() - this.m_20185_()) / (double) f;
                double lvt_5_1_ = (this.m_21524_().getY() - this.m_20186_()) / (double) f;
                double lvt_7_1_ = (this.m_21524_().getZ() - this.m_20189_()) / (double) f;
                this.m_20256_(this.m_20184_().add(Math.copySign(lvt_3_1_ * lvt_3_1_ * 0.4, lvt_3_1_), Math.copySign(lvt_5_1_ * lvt_5_1_ * 0.4, lvt_5_1_), Math.copySign(lvt_7_1_ * lvt_7_1_ * 0.4, lvt_7_1_)));
            }
        }
        if (this.f_21359_ != null) {
            this.m_21528_();
        }
        if (this.m_21524_() != null && (!this.m_6084_() || !this.m_21524_().isAlive())) {
            this.m_21455_(true, true);
        }
    }

    private void randomizeDirection() {
        this.setCardinalInt(2 + this.f_19796_.nextInt(3));
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    private class FlyGoal extends Goal {

        private final EntitySpectre parentEntity;

        boolean island = false;

        float circlingTime = 0.0F;

        float circleDistance = 14.0F;

        float maxCirclingTime = 80.0F;

        boolean clockwise = false;

        private BlockPos target = null;

        private int islandCheckTime = 20;

        public FlyGoal(EntitySpectre sunbird) {
            this.parentEntity = sunbird;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.parentEntity.lurePos != null) {
                return false;
            } else {
                MoveControl movementcontroller = this.parentEntity.m_21566_();
                this.clockwise = EntitySpectre.this.f_19796_.nextBoolean();
                this.circleDistance = (float) (5 + EntitySpectre.this.f_19796_.nextInt(10));
                if (movementcontroller.hasWanted() && this.target != null) {
                    return false;
                } else {
                    this.target = this.island ? this.getIslandPos(this.parentEntity.m_20183_()) : this.getBlockFromDirection();
                    if (this.target != null) {
                        this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                    }
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.lurePos == null;
        }

        @Override
        public void stop() {
            this.island = false;
            this.islandCheckTime = 0;
            this.circleDistance = (float) (5 + EntitySpectre.this.f_19796_.nextInt(10));
            this.circlingTime = 0.0F;
            this.clockwise = EntitySpectre.this.f_19796_.nextBoolean();
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.islandCheckTime-- <= 0) {
                this.islandCheckTime = 20;
                if (this.circlingTime == 0.0F) {
                    this.island = this.parentEntity.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.parentEntity.m_20183_()).m_123342_() > 2;
                    if (this.island) {
                        this.parentEntity.randomizeDirection();
                    }
                }
            }
            if (this.island) {
                this.circlingTime++;
                if (this.circlingTime > 100.0F) {
                    this.island = false;
                    this.islandCheckTime = 1200;
                }
            } else if (this.circlingTime > 0.0F) {
                this.circlingTime--;
            }
            if (this.target == null) {
                this.target = this.island ? this.getIslandPos(this.parentEntity.m_20183_()) : this.getBlockFromDirection();
            }
            if (!this.island) {
                this.parentEntity.m_146922_(this.parentEntity.getCardinalDirection().toYRot());
            }
            if (this.target != null) {
                this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) < 5.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockFromDirection() {
            float radius = 15.0F;
            BlockPos forwards = this.parentEntity.m_20183_().relative(this.parentEntity.getCardinalDirection(), (int) Math.ceil((double) radius));
            int height = 0;
            if (EntitySpectre.this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, forwards).m_123342_() < 15) {
                height = 70 + EntitySpectre.this.f_19796_.nextInt(2);
            } else {
                height = EntitySpectre.this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, forwards).m_123342_() + 10 + EntitySpectre.this.f_19796_.nextInt(10);
            }
            return new BlockPos(forwards.m_123341_(), height, forwards.m_123343_());
        }

        public BlockPos getIslandPos(BlockPos orbit) {
            float angle = 0.05235988F * (this.clockwise ? -this.circlingTime : this.circlingTime);
            double extraX = (double) (this.circleDistance * Mth.sin(angle));
            double extraZ = (double) (this.circleDistance * Mth.cos(angle));
            int height = EntitySpectre.this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, orbit).m_123342_();
            if (height < 3) {
                this.island = false;
                return this.getBlockFromDirection();
            } else {
                return new BlockPos((int) ((double) orbit.m_123341_() + extraX), Math.min(height + 10, orbit.m_123342_() + EntitySpectre.this.f_19796_.nextInt(3) - EntitySpectre.this.f_19796_.nextInt(1)), (int) ((double) orbit.m_123343_() + extraZ));
            }
        }
    }

    static class MoveHelperController extends MoveControl {

        private final EntitySpectre parentEntity;

        public MoveHelperController(EntitySpectre sunbird) {
            super(sunbird);
            this.parentEntity = sunbird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < 0.3) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    double d0 = this.f_24975_ - this.parentEntity.m_20185_();
                    double d1 = this.f_24976_ - this.parentEntity.m_20186_();
                    double d2 = this.f_24977_ - this.parentEntity.m_20189_();
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.05 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }

        private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class TemptHeartGoal extends Goal {

        protected final EntitySpectre creature;

        private final TargetingConditions ENTITY_PREDICATE = TargetingConditions.forNonCombat().range(64.0).ignoreInvisibilityTesting().ignoreLineOfSight();

        private final double speed;

        private final Ingredient temptItem;

        protected Player closestPlayer;

        private int delayTemptCounter;

        public TemptHeartGoal(EntitySpectre p_i47822_1_, double p_i47822_2_, Ingredient p_i47822_4_, boolean p_i47822_5_) {
            this(p_i47822_1_, p_i47822_2_, p_i47822_5_, p_i47822_4_);
        }

        public TemptHeartGoal(EntitySpectre p_i47823_1_, double p_i47823_2_, boolean p_i47823_4_, Ingredient p_i47823_5_) {
            this.creature = p_i47823_1_;
            this.speed = p_i47823_2_;
            this.temptItem = p_i47823_5_;
        }

        @Override
        public boolean canUse() {
            if (this.delayTemptCounter > 0) {
                this.delayTemptCounter--;
                return false;
            } else {
                this.closestPlayer = this.creature.m_9236_().m_45946_(this.ENTITY_PREDICATE, this.creature);
                return this.closestPlayer != null && this.creature.m_21524_() != this.closestPlayer ? this.isTempting(this.closestPlayer.m_21205_()) || this.isTempting(this.closestPlayer.m_21206_()) : false;
            }
        }

        protected boolean isTempting(ItemStack p_188508_1_) {
            return this.temptItem.test(p_188508_1_);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.creature.lurePos = this.closestPlayer.m_20182_();
        }

        @Override
        public void stop() {
            this.closestPlayer = null;
            this.delayTemptCounter = 100;
            this.creature.lurePos = null;
        }

        @Override
        public void tick() {
            this.creature.m_21563_().setLookAt(this.closestPlayer, (float) (this.creature.m_8085_() + 20), (float) this.creature.m_8132_());
            if (this.creature.m_20280_(this.closestPlayer) < 6.25) {
                this.creature.m_21573_().stop();
            } else {
                this.creature.m_21566_().setWantedPosition(this.closestPlayer.m_20185_(), this.closestPlayer.m_20186_() + (double) this.closestPlayer.m_20192_(), this.closestPlayer.m_20189_(), this.speed);
            }
        }
    }
}