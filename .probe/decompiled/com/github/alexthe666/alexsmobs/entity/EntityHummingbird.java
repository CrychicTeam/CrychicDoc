package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.BlockHummingbirdFeeder;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.HummingbirdAIPollinate;
import com.github.alexthe666.alexsmobs.entity.ai.HummingbirdAIWander;
import com.github.alexthe666.alexsmobs.misc.AMPointOfInterestRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.google.common.base.Predicates;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHummingbird extends Animal {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityHummingbird.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHummingbird.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CROPS_POLLINATED = SynchedEntityData.defineId(EntityHummingbird.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<BlockPos>> FEEDER_POS = SynchedEntityData.defineId(EntityHummingbird.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public float flyProgress;

    public float prevFlyProgress;

    public float movingProgress;

    public float prevMovingProgress;

    public int hummingStill = 0;

    public int pollinateCooldown = 0;

    public int sipCooldown = 0;

    private int loopSoundTick = 0;

    private boolean sippy;

    public float sipProgress;

    public float prevSipProgress;

    protected EntityHummingbird(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new FlightMoveController(this, 1.5F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, -1.0F);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.hummingbirdSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.HUMMINGBIRD_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 60;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.HUMMINGBIRD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.HUMMINGBIRD_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.FLYING_SPEED, 7.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.MOVEMENT_SPEED, 0.45F);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FLOWERS);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 7;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.0, Ingredient.of(ItemTags.FLOWERS), false));
        this.f_21345_.addGoal(3, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(4, new EntityHummingbird.AIUseFeeder(this));
        this.f_21345_.addGoal(4, new HummingbirdAIPollinate(this));
        this.f_21345_.addGoal(5, new HummingbirdAIWander(this, 16, 6, 15, 1.0F));
        this.f_21345_.addGoal(6, new FloatGoal(this));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.f_26495_.getBlockState(pos.below(2)).m_60795_();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.m_7008_(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    protected boolean makeFlySound() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.m_6162_() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_8055_(pos).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("CropsPollinated", this.getCropsPollinated());
        compound.putInt("PollinateCooldown", this.pollinateCooldown);
        BlockPos blockpos = this.getFeederPos();
        if (blockpos != null) {
            compound.putInt("HLPX", blockpos.m_123341_());
            compound.putInt("HLPY", blockpos.m_123342_());
            compound.putInt("HLPZ", blockpos.m_123343_());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setCropsPollinated(compound.getInt("CropsPollinated"));
        this.pollinateCooldown = compound.getInt("PollinateCooldown");
        if (compound.contains("HLPX")) {
            int i = compound.getInt("HLPX");
            int j = compound.getInt("HLPY");
            int k = compound.getInt("HLPZ");
            this.f_19804_.set(FEEDER_POS, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.f_19804_.set(FEEDER_POS, Optional.empty());
        }
    }

    public BlockPos getFeederPos() {
        return (BlockPos) this.f_19804_.get(FEEDER_POS).orElse(null);
    }

    public void setFeederPos(BlockPos pos) {
        this.f_19804_.set(FEEDER_POS, Optional.ofNullable(pos));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(CROPS_POLLINATED, 0);
        this.f_19804_.define(FEEDER_POS, Optional.empty());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.m_217043_().nextInt(3));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private List<BlockPos> getNearbyFeeders(BlockPos blockpos, ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        Stream<BlockPos> stream = pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(AMPointOfInterestRegistry.HUMMINGBIRD_FEEDER.getKey()), Predicates.alwaysTrue(), blockpos, range, PoiManager.Occupancy.ANY);
        return (List<BlockPos>) stream.collect(Collectors.toList());
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getCropsPollinated() {
        return this.f_19804_.get(CROPS_POLLINATED);
    }

    public void setCropsPollinated(int crops) {
        this.f_19804_.set(CROPS_POLLINATED, crops);
    }

    @Override
    public void tick() {
        super.m_8119_();
        Vec3 vector3d = this.m_20184_();
        boolean flag = this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z >= 0.001;
        if (!this.m_20096_() && vector3d.y < 0.0) {
            this.m_20256_(vector3d.multiply(1.0, 0.4, 1.0));
        }
        this.setFlying(true);
        this.m_20242_(true);
        if (this.isFlying() && this.flyProgress < 5.0F) {
            this.flyProgress++;
        }
        if (!this.isFlying() && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.sippy && this.sipProgress < 5.0F) {
            this.sipProgress++;
        }
        if (!this.sippy && this.sipProgress > 0.0F) {
            this.sipProgress--;
        }
        if (this.sippy && this.sipProgress == 5.0F) {
            this.sippy = false;
        }
        if (flag && this.movingProgress < 5.0F) {
            this.movingProgress++;
        }
        if (!flag && this.movingProgress > 0.0F) {
            this.movingProgress--;
        }
        if (this.m_20184_().lengthSqr() < 1.0E-7) {
            this.hummingStill++;
        } else {
            this.hummingStill = 0;
        }
        if (this.pollinateCooldown > 0) {
            this.pollinateCooldown--;
        }
        if (this.sipCooldown > 0) {
            this.sipCooldown--;
        }
        if (this.loopSoundTick == 0) {
            this.m_5496_(AMSoundRegistry.HUMMINGBIRD_LOOP.get(), this.m_6121_() * 0.33F, this.m_6100_());
        }
        this.loopSoundTick++;
        if (this.loopSoundTick > 27) {
            this.loopSoundTick = 0;
        }
        this.prevFlyProgress = this.flyProgress;
        this.prevMovingProgress = this.movingProgress;
        this.prevSipProgress = this.sipProgress;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 68) {
            if (this.getFeederPos() != null) {
                if (this.f_19796_.nextFloat() < 0.2F) {
                    double d2 = this.f_19796_.nextGaussian() * 0.02;
                    double d0 = this.f_19796_.nextGaussian() * 0.02;
                    double d1 = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(ParticleTypes.FALLING_NECTAR, (double) ((float) this.getFeederPos().m_123341_() + 0.2F) + (double) (this.f_19796_.nextFloat() * 0.6F), (double) ((float) this.getFeederPos().m_123342_() + 0.1F), (double) ((float) this.getFeederPos().m_123343_() + 0.2F) + (double) (this.f_19796_.nextFloat() * 0.6F), d0, d1, d2);
                }
                this.sippy = true;
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.HUMMINGBIRD.get().create(serverWorld);
    }

    public static <T extends Mob> boolean canHummingbirdSpawn(EntityType<EntityHummingbird> hummingbird, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return (blockstate.m_204336_(BlockTags.LEAVES) || blockstate.m_60713_(Blocks.GRASS_BLOCK) || blockstate.m_204336_(BlockTags.LOGS) || blockstate.m_60713_(Blocks.AIR)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    public boolean canBlockBeSeen(BlockPos pos) {
        double x = (double) ((float) pos.m_123341_() + 0.5F);
        double y = (double) ((float) pos.m_123342_() + 0.5F);
        double z = (double) ((float) pos.m_123343_() + 0.5F);
        HitResult result = this.m_9236_().m_45547_(new ClipContext(new Vec3(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }

    private class AIUseFeeder extends Goal {

        int runCooldown = 0;

        private int idleAtFlowerTime = 0;

        private BlockPos localFeeder;

        public AIUseFeeder(EntityHummingbird entityHummingbird) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public void stop() {
            this.localFeeder = null;
            this.idleAtFlowerTime = 0;
        }

        @Override
        public boolean canUse() {
            if (EntityHummingbird.this.sipCooldown > 0) {
                return false;
            } else {
                if (this.runCooldown > 0) {
                    this.runCooldown--;
                } else {
                    BlockPos feedPos = EntityHummingbird.this.getFeederPos();
                    if (feedPos != null && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(feedPos))) {
                        this.localFeeder = feedPos;
                        return true;
                    }
                    List<BlockPos> beacons = EntityHummingbird.this.getNearbyFeeders(EntityHummingbird.this.m_20183_(), (ServerLevel) EntityHummingbird.this.m_9236_(), 64);
                    BlockPos closest = null;
                    for (BlockPos pos : beacons) {
                        if ((closest == null || EntityHummingbird.this.m_20275_((double) closest.m_123341_(), (double) closest.m_123342_(), (double) closest.m_123343_()) > EntityHummingbird.this.m_20275_((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_())) && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(pos))) {
                            closest = pos;
                        }
                    }
                    if (closest != null && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(closest))) {
                        this.localFeeder = closest;
                        return true;
                    }
                }
                this.runCooldown = 400 + EntityHummingbird.this.f_19796_.nextInt(600);
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.localFeeder != null && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(this.localFeeder)) && EntityHummingbird.this.sipCooldown == 0;
        }

        @Override
        public void tick() {
            if (this.localFeeder != null && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(this.localFeeder))) {
                if (EntityHummingbird.this.m_20186_() > (double) this.localFeeder.m_123342_() && !EntityHummingbird.this.m_20096_()) {
                    EntityHummingbird.this.m_21566_().setWantedPosition((double) ((float) this.localFeeder.m_123341_() + 0.5F), (double) ((float) this.localFeeder.m_123342_() + 0.1F), (double) ((float) this.localFeeder.m_123343_() + 0.5F), 1.0);
                } else {
                    EntityHummingbird.this.m_21566_().setWantedPosition((double) (this.localFeeder.m_123341_() + EntityHummingbird.this.f_19796_.nextInt(4) - 2), EntityHummingbird.this.m_20186_() + 1.0, (double) (this.localFeeder.m_123343_() + EntityHummingbird.this.f_19796_.nextInt(4) - 2), 1.0);
                }
                Vec3 vec = Vec3.upFromBottomCenterOf(this.localFeeder, 0.1F);
                double dist = (double) Mth.sqrt((float) EntityHummingbird.this.m_20238_(vec));
                if (dist < 2.5 && EntityHummingbird.this.m_20186_() > (double) this.localFeeder.m_123342_()) {
                    EntityHummingbird.this.m_7618_(EntityAnchorArgument.Anchor.EYES, vec);
                    this.idleAtFlowerTime++;
                    EntityHummingbird.this.setFeederPos(this.localFeeder);
                    EntityHummingbird.this.m_9236_().broadcastEntityEvent(EntityHummingbird.this, (byte) 68);
                    if (this.idleAtFlowerTime > 55) {
                        if (EntityHummingbird.this.getCropsPollinated() > 2 && EntityHummingbird.this.f_19796_.nextInt(25) == 0 && this.isValidFeeder(EntityHummingbird.this.m_9236_().getBlockState(this.localFeeder))) {
                            EntityHummingbird.this.m_9236_().setBlockAndUpdate(this.localFeeder, (BlockState) EntityHummingbird.this.m_9236_().getBlockState(this.localFeeder).m_61124_(BlockHummingbirdFeeder.CONTENTS, 0));
                        }
                        EntityHummingbird.this.setCropsPollinated(EntityHummingbird.this.getCropsPollinated() + 1);
                        EntityHummingbird.this.sipCooldown = 120 + EntityHummingbird.this.f_19796_.nextInt(1200);
                        EntityHummingbird.this.pollinateCooldown = Math.max(0, EntityHummingbird.this.pollinateCooldown / 3);
                        this.runCooldown = 400 + EntityHummingbird.this.f_19796_.nextInt(600);
                        this.stop();
                    }
                }
            }
        }

        public boolean isValidFeeder(BlockState state) {
            return state.m_60734_() instanceof BlockHummingbirdFeeder && (Integer) state.m_61143_(BlockHummingbirdFeeder.CONTENTS) == 3;
        }
    }
}