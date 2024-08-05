package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MothBallBlock;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.GloomothFindLightGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GloomothFleeMothBallsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.GloomothFlightGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GloomothEntity extends PathfinderMob implements UnderzealotSacrifice {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(GloomothEntity.class, EntityDataSerializers.BOOLEAN);

    private float flyProgress;

    private float prevFlyProgress;

    private float flapAmount;

    private float prevFlapAmount;

    private float flightPitch = 0.0F;

    private float prevFlightPitch = 0.0F;

    private float flightRoll = 0.0F;

    private float prevFlightRoll = 0.0F;

    private boolean isLandNavigator;

    public BlockPos lightPos;

    private int refreshLightPosIn = 0;

    private boolean isBeingSacrificed = false;

    private int sacrificeTime = 0;

    private int flapTime = 0;

    public GloomothEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.switchNavigator(true);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new GloomothEntity.FlightMoveHelper(this);
            this.f_21344_ = this.createNavigation(this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new FlyingPathNavigation(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return this.f_26495_.getBlockState(blockPos).m_60795_();
            }
        };
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new GloomothFleeMothBallsGoal(this));
        this.f_21345_.addGoal(2, new GloomothFindLightGoal(this, 32));
        this.f_21345_.addGoal(3, new GloomothFlightGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 4.0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevFlightPitch = this.flightPitch;
        this.prevFlightRoll = this.flightRoll;
        this.prevFlapAmount = this.flapAmount;
        if (this.isFlying() && this.flyProgress < 5.0F) {
            this.flyProgress++;
        }
        if (!this.isFlying() && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        float yMov = (float) this.m_20184_().y;
        float xzMov = (float) this.m_20184_().horizontalDistance();
        if (xzMov > 0.0F) {
            if (this.flapAmount < 5.0F) {
                this.flapAmount++;
            }
        } else if (xzMov <= 0.05F && this.flapAmount > 0.0F) {
            this.flapAmount -= 0.5F;
        }
        if (this.isFlying()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.flapAmount > 0.0F && this.m_6084_() && this.flapTime-- <= 0) {
                this.flapTime = 8 + this.f_19796_.nextInt(10);
                this.m_5496_(ACSoundRegistry.GLOOMOTH_FLAP.get(), 0.7F, 1.0F);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (this.lightPos != null && this.m_6084_() && !this.m_9236_().isClientSide && this.refreshLightPosIn-- < 0) {
            this.refreshLightPosIn = 40 + this.f_19796_.nextInt(100);
            if (this.m_20238_(Vec3.atCenterOf(this.lightPos)) >= 256.0 || !this.m_9236_().getBlockState(this.lightPos).m_204336_(ACTagRegistry.GLOOMOTH_LIGHT_SOURCES) || this.m_9236_().m_7146_(this.lightPos) <= 0) {
                this.lightPos = null;
            }
        }
        this.tickRotation(yMov * 2.5F * (-180.0F / (float) Math.PI));
        if (this.isBeingSacrificed && !this.m_9236_().isClientSide) {
            this.sacrificeTime--;
            if (this.sacrificeTime < 10) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 61);
            }
            if (this.sacrificeTime < 0) {
                if (this.m_20159_() && this.m_20202_() instanceof UnderzealotEntity underzealot) {
                    underzealot.postSacrifice(this);
                    underzealot.triggerIdleDigging();
                }
                this.m_8127_();
                WatcherEntity watcherEntity = (WatcherEntity) this.m_21406_(ACEntityRegistry.WATCHER.get(), true);
                this.m_5496_(ACSoundRegistry.WATCHER_SPAWN.get(), 8.0F, 1.0F);
                if (watcherEntity != null) {
                    ForgeEventFactory.onLivingConvert(this, watcherEntity);
                    watcherEntity.m_8127_();
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 61) {
            for (int i = 0; i < 1 + this.f_19796_.nextInt(4); i++) {
                this.m_9236_().addParticle(ACParticleRegistry.UNDERZEALOT_EXPLOSION.get(), this.m_20208_(1.0), this.m_20187_(), this.m_20262_(1.0), 0.0, 0.0, 0.0);
            }
        } else {
            super.m_7822_(b);
        }
    }

    private void tickRotation(float yMov) {
        this.flightPitch = yMov;
        float threshold = 1.0F;
        boolean flag = false;
        if (this.isFlying() && this.f_19859_ - this.m_146908_() > threshold) {
            this.flightRoll += 10.0F;
            flag = true;
        }
        if (this.isFlying() && this.f_19859_ - this.m_146908_() < -threshold) {
            this.flightRoll -= 10.0F;
            flag = true;
        }
        if (!flag) {
            if (this.flightRoll > 0.0F) {
                this.flightRoll = Math.max(this.flightRoll - 5.0F, 0.0F);
            }
            if (this.flightRoll < 0.0F) {
                this.flightRoll = Math.min(this.flightRoll + 5.0F, 0.0F);
            }
        }
        this.flightRoll = Mth.clamp(this.flightRoll, -60.0F, 60.0F);
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(FLYING, flying);
    }

    public float getFlapAmount(float partialTick) {
        return (this.prevFlapAmount + (this.flapAmount - this.prevFlapAmount) * partialTick) * 0.2F;
    }

    public float getFlyProgress(float partialTick) {
        return (this.prevFlyProgress + (this.flyProgress - this.prevFlyProgress) * partialTick) * 0.2F;
    }

    public float getFlightPitch(float partialTick) {
        return this.prevFlightPitch + (this.flightPitch - this.prevFlightPitch) * partialTick;
    }

    public float getFlightRoll(float partialTick) {
        return this.prevFlightRoll + (this.flightRoll - this.prevFlightRoll) * partialTick;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(2.0, 2.0, 2.0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean idk) {
        if (!(damageSource.getEntity() instanceof ForsakenEntity) && !(damageSource.getEntity() instanceof VesperEntity)) {
            super.m_7625_(damageSource, idk);
        }
    }

    public static boolean isValidLightLevel(ServerLevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource) {
        if (levelAccessor.m_45517_(LightLayer.SKY, blockPos) > randomSource.nextInt(32)) {
            return false;
        } else {
            int lvt_3_1_ = levelAccessor.getLevel().m_46470_() ? levelAccessor.m_46849_(blockPos, 10) : levelAccessor.m_46803_(blockPos);
            return lvt_3_1_ <= randomSource.nextInt(8);
        }
    }

    public static boolean canMonsterSpawnInLight(EntityType<GloomothEntity> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return isValidLightLevel(levelAccessor, blockPos, randomSource) && m_217057_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
    }

    public static <T extends Mob> boolean checkGloomothSpawnRules(EntityType<GloomothEntity> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random)) {
            BlockPos.MutableBlockPos above = new BlockPos.MutableBlockPos();
            above.set(pos);
            int k = 0;
            while (iServerWorld.m_46859_(above) && above.m_123342_() < iServerWorld.m_151558_()) {
                above.move(0, 1, 0);
                if (++k > 4) {
                    return true;
                }
            }
        }
        return false;
    }

    public BlockPos getNearestMothBall(ServerLevel world, BlockPos to, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return (BlockPos) pointofinterestmanager.findClosest(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.MOTH_BALL.getKey()), blockPos -> this.isWithinMothBallRange(blockPos, range), to, range, PoiManager.Occupancy.ANY).orElse(null);
    }

    private boolean isWithinMothBallRange(BlockPos blockPos, int range) {
        BlockState state = this.m_9236_().getBlockState(blockPos);
        if (state.m_60713_(ACBlockRegistry.MOTH_BALL.get())) {
            float balls = (float) ((Integer) state.m_61143_(MothBallBlock.BALLS)).intValue() / 5.0F;
            int distance = (int) Math.sqrt(blockPos.m_123331_(this.m_20183_()));
            return (float) distance < balls * (float) range;
        } else {
            return false;
        }
    }

    @Override
    public void triggerSacrificeIn(int time) {
        this.isBeingSacrificed = true;
        this.sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return distanceFromGround < 4;
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos above = this.m_20183_();
        int upBy = 3 + this.f_19796_.nextInt(5);
        for (int k = 0; world.m_46859_(above) && above.m_123342_() < this.m_9236_().m_151558_() && k < upBy; k++) {
            above = above.above();
        }
        this.setFlying(true);
        this.m_6034_((double) ((float) above.m_123341_() + 0.5F), (double) above.m_123342_(), (double) ((float) above.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.GLOOMOTH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.GLOOMOTH_DEATH.get();
    }

    class FlightMoveHelper extends MoveControl {

        public FlightMoveHelper(GloomothEntity gloomoth) {
            super(gloomoth);
        }

        @Override
        public void tick() {
            int maxRotChange = 10;
            boolean flag = false;
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.f_24974_.getNavigation().isDone()) {
                double d0 = this.f_24975_ - this.f_24974_.m_20185_();
                double d1 = this.f_24976_ - this.f_24974_.m_20186_();
                double d2 = this.f_24977_ - this.f_24974_.m_20189_();
                double d3 = (double) Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
                double d4 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
                d1 /= d3;
                this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
                float f1 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED) * 3.0);
                float rotBy = (float) maxRotChange;
                this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(0.0, (double) f1 * d1 * 0.025, 0.0));
                if (d4 < (double) (this.f_24974_.m_20205_() + 1.4F)) {
                    f1 *= 0.7F;
                    if (d4 < 0.3F) {
                        rotBy = 0.0F;
                    } else {
                        rotBy = (float) Math.max(40, maxRotChange);
                    }
                } else {
                    flag = true;
                }
                float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), f, rotBy));
                if (d3 > 0.3) {
                    this.f_24974_.setSpeed(f1);
                    flag = true;
                } else {
                    this.f_24974_.setSpeed(0.0F);
                }
            } else {
                this.f_24974_.setSpeed(0.0F);
            }
            this.f_24974_.m_20242_(flag);
        }
    }
}