package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.StraddlerAIShoot;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeMod;

public class EntityStraddler extends Monster implements IAnimatedEntity {

    public static final Animation ANIMATION_LAUNCH = Animation.create(30);

    private static final EntityDataAccessor<Integer> STRADPOLE_COUNT = SynchedEntityData.defineId(EntityStraddler.class, EntityDataSerializers.INT);

    private int animationTick;

    private Animation currentAnimation;

    protected EntityStraddler(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.STRADDLER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.STRADDLER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.STRADDLER_HURT.get();
    }

    public static boolean canStraddlerSpawn(EntityType animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_8055_(pos.below()).m_204336_(BlockTags.BASE_STONE_NETHER);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 28.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ARMOR, 5.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(STRADPOLE_COUNT, 0);
    }

    public int getStradpoleCount() {
        return this.f_19804_.get(STRADPOLE_COUNT);
    }

    public void setStradpoleCount(int index) {
        this.f_19804_.set(STRADPOLE_COUNT, index);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.straddlerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new StraddlerAIShoot(this, 0.5, 30, 16.0F));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Strider.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, true));
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
        this.m_20101_();
        if (this.m_20077_()) {
            this.f_19789_ = 0.0F;
        } else {
            super.m_7840_(p_184231_1_, p_184231_3_, p_184231_4_, p_184231_5_);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED) * (this.getAnimation() == ANIMATION_LAUNCH ? 0.5F : 1.0F) * (this.m_20077_() ? 0.2F : 1.0F));
        if (this.m_21515_() && (this.m_20069_() || this.m_20077_())) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    private void floatStrider() {
        if (this.m_20077_()) {
            CollisionContext lvt_1_1_ = CollisionContext.of(this);
            double d1 = this.getFluidTypeHeight(ForgeMod.LAVA_TYPE.get());
            if (d1 <= 0.5 && d1 > 0.0) {
                if (this.m_20184_().y < 0.0) {
                    this.m_20256_(this.m_20184_().multiply(1.0, 0.0, 1.0));
                }
                this.m_6853_(true);
            } else if (lvt_1_1_.isAbove(LiquidBlock.STABLE_SHAPE, this.m_20183_().below(), true) && !this.m_9236_().getFluidState(this.m_20183_().above()).is(FluidTags.LAVA)) {
                this.m_6853_(true);
            } else {
                this.m_20334_(0.0, Math.min(d1 - 0.5, 1.0) * 0.2F, 0.0);
            }
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    protected float nextStep() {
        return this.f_19788_ + 0.6F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        if (worldIn.m_8055_(pos).m_60819_().is(FluidTags.LAVA)) {
            return 10.0F;
        } else {
            return this.m_20077_() ? Float.NEGATIVE_INFINITY : 0.0F;
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Vec3[] avector3d = new Vec3[] { m_19903_((double) this.m_20205_(), (double) livingEntity.m_20205_(), livingEntity.m_146908_()), m_19903_((double) this.m_20205_(), (double) livingEntity.m_20205_(), livingEntity.m_146908_() - 22.5F), m_19903_((double) this.m_20205_(), (double) livingEntity.m_20205_(), livingEntity.m_146908_() + 22.5F), m_19903_((double) this.m_20205_(), (double) livingEntity.m_20205_(), livingEntity.m_146908_() - 45.0F), m_19903_((double) this.m_20205_(), (double) livingEntity.m_20205_(), livingEntity.m_146908_() + 45.0F) };
        Set<BlockPos> set = Sets.newLinkedHashSet();
        double d0 = this.m_20191_().maxY;
        double d1 = this.m_20191_().minY - 0.5;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (Vec3 vector3d : avector3d) {
            blockpos$mutable.set(this.m_20185_() + vector3d.x, d0, this.m_20189_() + vector3d.z);
            for (double d2 = d0; d2 > d1; d2--) {
                set.add(blockpos$mutable.immutable());
                blockpos$mutable.move(Direction.DOWN);
            }
        }
        for (BlockPos blockpos : set) {
            if (!this.m_9236_().getFluidState(blockpos).is(FluidTags.LAVA)) {
                double d3 = this.m_9236_().m_45573_(blockpos);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    Vec3 vector3d1 = Vec3.upFromBottomCenterOf(blockpos, d3);
                    UnmodifiableIterator var14 = livingEntity.getDismountPoses().iterator();
                    while (var14.hasNext()) {
                        Pose pose = (Pose) var14.next();
                        AABB axisalignedbb = livingEntity.getLocalBoundsForPose(pose);
                        if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity, axisalignedbb.move(vector3d1))) {
                            livingEntity.m_20124_(pose);
                            return vector3d1;
                        }
                    }
                }
            }
        }
        return new Vec3(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return p_230285_1_.is(FluidTags.LAVA);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("StradpoleCount", this.getStradpoleCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setStradpoleCount(compound.getInt("StradpoleCount"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.floatStrider();
        this.m_20101_();
        if (this.getAnimation() == ANIMATION_LAUNCH && this.m_6084_() && this.getAnimationTick() == 2) {
            this.m_5496_(SoundEvents.CROSSBOW_LOADING_MIDDLE, 2.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        }
        if (this.getAnimation() == ANIMATION_LAUNCH && this.m_6084_() && this.getAnimationTick() == 20 && this.m_5448_() != null) {
            EntityStradpole pole = AMEntityRegistry.STRADPOLE.get().create(this.m_9236_());
            pole.setParentId(this.m_20148_());
            pole.m_6034_(this.m_20185_(), this.m_20188_(), this.m_20189_());
            double d0 = this.m_5448_().m_20188_() - 1.1F;
            double d1 = this.m_5448_().m_20185_() - this.m_20185_();
            double d2 = d0 - pole.m_20186_();
            double d3 = this.m_5448_().m_20189_() - this.m_20189_();
            float f3 = Mth.sqrt((float) (d1 * d1 + d2 * d2 + d3 * d3)) * 0.2F;
            this.m_146850_(GameEvent.PROJECTILE_SHOOT);
            this.m_5496_(SoundEvents.CROSSBOW_LOADING_END, 2.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
            pole.shoot(d1, d2 + (double) f3, d3, 2.0F, 0.0F);
            pole.m_146922_(this.m_146908_() % 360.0F);
            pole.m_146926_(Mth.clamp(this.m_146908_(), -90.0F, 90.0F) % 360.0F);
            if (!this.m_9236_().isClientSide) {
                this.m_9236_().m_7967_(pole);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_LAUNCH };
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new EntityStraddler.LavaPathNavigator(this, worldIn);
    }

    public boolean shouldShoot() {
        return true;
    }

    static class LavaPathNavigator extends GroundPathNavigation {

        LavaPathNavigator(EntityStraddler p_i231565_1_, Level p_i231565_2_) {
            super(p_i231565_1_, p_i231565_2_);
        }

        @Override
        protected PathFinder createPathFinder(int p_179679_1_) {
            this.f_26508_ = new WalkNodeEvaluator();
            return new PathFinder(this.f_26508_, p_179679_1_);
        }

        @Override
        protected boolean hasValidPathType(BlockPathTypes p_230287_1_) {
            return p_230287_1_ == BlockPathTypes.LAVA || p_230287_1_ == BlockPathTypes.DAMAGE_FIRE || p_230287_1_ == BlockPathTypes.DANGER_FIRE || super.hasValidPathType(p_230287_1_);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return this.f_26495_.getBlockState(pos).m_60713_(Blocks.LAVA) || super.m_6342_(pos);
        }
    }
}