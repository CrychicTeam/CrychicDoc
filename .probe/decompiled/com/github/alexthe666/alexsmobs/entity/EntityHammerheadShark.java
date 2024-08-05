package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityHammerheadShark extends WaterAnimal {

    private static final Predicate<LivingEntity> INJURED_PREDICATE = mob -> (double) mob.getHealth() <= (double) mob.getMaxHealth() / 2.0;

    protected EntityHammerheadShark(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new AquaticMoveController(this, 1.0F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.hammerheadSharkSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
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

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(1, new EntityHammerheadShark.CirclePreyGoal(this, 1.0F));
        this.f_21345_.addGoal(4, new RandomSwimmingGoal(this, 0.6F, 7));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(8, new FollowBoatGoal(this));
        this.f_21345_.addGoal(9, new AvoidEntityGoal(this, Guardian.class, 8.0F, 1.0, 1.0));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, LivingEntity.class, 50, false, true, INJURED_PREDICATE));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Squid.class, 50, false, true, null));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, EntityMimicOctopus.class, 80, false, true, null));
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, AbstractSchoolingFish.class, 70, false, true, null));
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.BLOCK;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    public static <T extends Mob> boolean canHammerheadSharkSpawn(EntityType<EntityHammerheadShark> p_223364_0_, LevelAccessor p_223364_1_, MobSpawnType reason, BlockPos p_223364_3_, RandomSource p_223364_4_) {
        return p_223364_3_.m_123342_() > 45 && p_223364_3_.m_123342_() < p_223364_1_.m_5736_() ? p_223364_1_.m_6425_(p_223364_3_).is(FluidTags.WATER) : false;
    }

    private static class CirclePreyGoal extends Goal {

        EntityHammerheadShark shark;

        float speed;

        float circlingTime = 0.0F;

        float circleDistance = 5.0F;

        float maxCirclingTime = 80.0F;

        boolean clockwise = false;

        public CirclePreyGoal(EntityHammerheadShark shark, float speed) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.shark = shark;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            return this.shark.m_5448_() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.shark.m_5448_() != null;
        }

        @Override
        public void start() {
            this.circlingTime = 0.0F;
            this.maxCirclingTime = (float) (360 + this.shark.f_19796_.nextInt(80));
            this.circleDistance = 5.0F + this.shark.f_19796_.nextFloat() * 5.0F;
            this.clockwise = this.shark.f_19796_.nextBoolean();
        }

        @Override
        public void stop() {
            this.circlingTime = 0.0F;
            this.maxCirclingTime = (float) (360 + this.shark.f_19796_.nextInt(80));
            this.circleDistance = 5.0F + this.shark.f_19796_.nextFloat() * 5.0F;
            this.clockwise = this.shark.f_19796_.nextBoolean();
        }

        @Override
        public void tick() {
            LivingEntity prey = this.shark.m_5448_();
            if (prey != null) {
                double dist = (double) this.shark.m_20270_(prey);
                if (this.circlingTime >= this.maxCirclingTime) {
                    this.shark.m_21391_(prey, 30.0F, 30.0F);
                    this.shark.m_21573_().moveTo(prey, 1.5);
                    if (dist < 2.0) {
                        this.shark.m_7327_(prey);
                        if (this.shark.f_19796_.nextFloat() < 0.3F) {
                            this.shark.m_19983_(new ItemStack(AMItemRegistry.SHARK_TOOTH.get()));
                        }
                        this.stop();
                    }
                } else if (dist <= 25.0) {
                    this.circlingTime++;
                    BlockPos circlePos = this.getSharkCirclePos(prey);
                    if (circlePos != null) {
                        this.shark.m_21573_().moveTo((double) circlePos.m_123341_() + 0.5, (double) circlePos.m_123342_() + 0.5, (double) circlePos.m_123343_() + 0.5, 0.6);
                    }
                } else {
                    this.shark.m_21391_(prey, 30.0F, 30.0F);
                    this.shark.m_21573_().moveTo(prey, 0.8);
                }
            }
        }

        public BlockPos getSharkCirclePos(LivingEntity target) {
            float angle = (float) (Math.PI / 180.0) * (this.clockwise ? -this.circlingTime : this.circlingTime);
            double extraX = (double) (this.circleDistance * Mth.sin(angle));
            double extraZ = (double) (this.circleDistance * Mth.cos(angle));
            BlockPos ground = AMBlockPos.fromCoords(target.m_20185_() + 0.5 + extraX, this.shark.m_20186_(), target.m_20189_() + 0.5 + extraZ);
            return this.shark.m_9236_().getFluidState(ground).is(FluidTags.WATER) ? ground : null;
        }
    }
}