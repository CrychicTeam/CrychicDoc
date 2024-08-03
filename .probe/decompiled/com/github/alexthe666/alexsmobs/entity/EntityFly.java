package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFly extends Animal implements FlyingAnimal {

    private int conversionTime = 0;

    private static final EntityDataAccessor<Boolean> NO_DESPAWN = SynchedEntityData.defineId(EntityFly.class, EntityDataSerializers.BOOLEAN);

    protected EntityFly(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new FlyingMoveControl(this, 20, true);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("NoFlyDespawn", this.isNoDespawn());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setNoDespawn(compound.getBoolean("NoFlyDespawn"));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(NO_DESPAWN, false);
    }

    public boolean isNoDespawn() {
        return this.f_19804_.get(NO_DESPAWN);
    }

    public void setNoDespawn(boolean despawn) {
        this.f_19804_.set(NO_DESPAWN, despawn);
    }

    public static boolean canFlySpawn(EntityType<EntityFly> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || pos.m_123342_() > 63 && random.nextInt(4) == 0 && worldIn.m_45524_(pos, 0) > 8 && worldIn.m_45517_(LightLayer.BLOCK, pos) == 0 && worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.FLY_SPAWNS);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return this.isNoDespawn() || this.m_8077_() || this.m_21523_() || super.m_8023_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.flySpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean isInNether() {
        return this.m_9236_().dimension() == Level.NETHER && !this.m_21525_();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.FLY_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 30;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.FLY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.FLY_HURT.get();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 2.0).add(Attributes.FLYING_SPEED, 0.8F).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_8055_(pos).m_60795_() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(Items.ROTTEN_FLESH, Items.SUGAR), false));
        this.f_21345_.addGoal(3, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Spider.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(4, new EntityFly.AnnoyZombieGoal());
        this.f_21345_.addGoal(5, new EntityFly.WanderGoal());
        this.f_21345_.addGoal(6, new FloatGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.f_26495_.getBlockState(pos.below()).m_60795_();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.m_7008_(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.m_6162_() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.f_19789_ = 0.0F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_6162_() && this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (this.m_27593_() && !this.isNoDespawn()) {
            this.setNoDespawn(true);
        }
        if (this.isInNether()) {
            this.setNoDespawn(true);
            this.conversionTime++;
            if (this.conversionTime > 300) {
                EntityCrimsonMosquito mosquito = AMEntityRegistry.CRIMSON_MOSQUITO.get().create(this.m_9236_());
                mosquito.m_20359_(this);
                if (!this.m_9236_().isClientSide) {
                    mosquito.m_6518_((ServerLevelAccessor) this.m_9236_(), this.m_9236_().getCurrentDifficultyAt(this.m_20183_()), MobSpawnType.CONVERSION, null, null);
                }
                this.m_9236_().m_7967_(mosquito);
                mosquito.onSpawnFromFly();
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack lvt_3_1_ = p_230254_1_.m_21120_(p_230254_2_);
        if (lvt_3_1_.getItem() == Items.SUGAR) {
            if (!p_230254_1_.isCreative()) {
                lvt_3_1_.shrink(1);
            }
            this.setNoDespawn(true);
            this.m_5634_(2.0F);
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    protected boolean makeFlySound() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        this.m_20256_(this.m_20184_().add(0.0, 0.01, 0.0));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.5F * this.m_20192_()), (double) (this.m_20205_() * 0.2F));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.ROTTEN_FLESH;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        EntityFly fly = AMEntityRegistry.FLY.get().create(this.m_9236_());
        fly.setNoDespawn(true);
        return fly;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    private class AnnoyZombieGoal extends Goal {

        protected final EntityFly.AnnoyZombieGoal.Sorter theNearestAttackableTargetSorter;

        protected final Predicate<? super Entity> targetEntitySelector;

        protected int executionChance = 8;

        protected boolean mustUpdate;

        private Entity targetEntity;

        private int cooldown = 0;

        AnnoyZombieGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntityFly.AnnoyZombieGoal.Sorter(EntityFly.this);
            this.targetEntitySelector = new Predicate<Entity>() {

                public boolean apply(@Nullable Entity e) {
                    return e.isAlive() && e.getType().is(AMTagRegistry.FLY_TARGETS) && (!(e instanceof LivingEntity) || (double) ((LivingEntity) e).getHealth() >= 2.0);
                }
            };
        }

        @Override
        public boolean canUse() {
            if (!EntityFly.this.m_20159_() && !EntityFly.this.m_20160_()) {
                if (!this.mustUpdate) {
                    long worldTime = EntityFly.this.m_9236_().getGameTime() % 10L;
                    if (EntityFly.this.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (EntityFly.this.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<Entity> list = EntityFly.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (Entity) list.get(0);
                    this.mustUpdate = false;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.targetEntity != null;
        }

        @Override
        public void stop() {
            this.targetEntity = null;
        }

        @Override
        public void tick() {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.targetEntity != null) {
                if (EntityFly.this.m_21573_().isDone()) {
                    int i = EntityFly.this.m_217043_().nextInt(3) - 1;
                    int k = EntityFly.this.m_217043_().nextInt(3) - 1;
                    int l = (int) ((double) (EntityFly.this.m_217043_().nextInt(3) - 1) * Math.ceil((double) this.targetEntity.getBbHeight()));
                    EntityFly.this.m_21573_().moveTo(this.targetEntity.getX() + (double) i, this.targetEntity.getY() + (double) l, this.targetEntity.getZ() + (double) k, 1.0);
                }
                if (EntityFly.this.m_20280_(this.targetEntity) < 3.0) {
                    if (!(this.targetEntity instanceof LivingEntity) || !((double) ((LivingEntity) this.targetEntity).getHealth() > 2.0)) {
                        this.stop();
                    } else if (this.cooldown == 0) {
                        this.targetEntity.hurt(EntityFly.this.m_269291_().generic(), 1.0F);
                        this.cooldown = 100;
                    }
                }
            }
        }

        protected double getTargetDistance() {
            return 16.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityFly.this.m_20185_() + 0.5, EntityFly.this.m_20186_() + 0.5, EntityFly.this.m_20189_() + 0.5);
            double renderRadius = 5.0;
            AABB aabb = new AABB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
            return aabb.move(renderCenter);
        }

        public static record Sorter(Entity theEntity) implements Comparator<Entity> {

            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                double d0 = this.theEntity.distanceToSqr(p_compare_1_);
                double d1 = this.theEntity.distanceToSqr(p_compare_2_);
                return Double.compare(d0, d1);
            }
        }
    }

    class WanderGoal extends Goal {

        WanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntityFly.this.f_21344_.isDone() && EntityFly.this.f_19796_.nextInt(3) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return EntityFly.this.f_21344_.isInProgress();
        }

        @Override
        public void start() {
            Vec3 vector3d = this.getRandomLocation();
            if (vector3d != null) {
                EntityFly.this.f_21344_.moveTo(EntityFly.this.f_21344_.createPath(AMBlockPos.fromVec3(vector3d), 1), 1.0);
            }
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vec3 = EntityFly.this.m_20252_(0.0F);
            int i = 8;
            Vec3 vec32 = HoverRandomPos.getPos(EntityFly.this, 8, 7, vec3.x, vec3.z, (float) (Math.PI / 2), 3, 1);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(EntityFly.this, 8, 4, -2, vec3.x, vec3.z, (float) (Math.PI / 2));
        }
    }
}