package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntitySeaBear extends WaterAnimal implements IAnimatedEntity {

    public static final Animation ANIMATION_ATTACK = Animation.create(17);

    public static final Animation ANIMATION_POINT = Animation.create(25);

    public float prevOnLandProgress;

    public float onLandProgress;

    public int circleCooldown = 0;

    private int animationTick;

    private Animation currentAnimation;

    private BlockPos lastCircle = null;

    public static final Predicate<LivingEntity> SOMBRERO = player -> player.getItemBySlot(EquipmentSlot.HEAD).is(AMItemRegistry.SOMBRERO.get());

    protected EntitySeaBear(EntityType entityType, Level level) {
        super(entityType, level);
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 10.0F);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 200.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.MOVEMENT_SPEED, 0.325F);
    }

    public static boolean isMobSafe(Entity entity) {
        if (entity instanceof Player && ((Player) entity).isCreative()) {
            return true;
        } else {
            BlockState state = entity.level().getBlockState(entity.blockPosition().below());
            return state.m_60713_(AMBlockRegistry.SAND_CIRCLE.get()) || state.m_60713_(AMBlockRegistry.RED_SAND_CIRCLE.get());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.GRIZZLY_BEAR_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GRIZZLY_BEAR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GRIZZLY_BEAR_DIE.get();
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntitySeaBear.AttackAI());
        this.f_21345_.addGoal(3, new EntitySeaBear.AvoidCircleAI());
        this.f_21345_.addGoal(4, new AnimalAISwimBottom(this, 1.0, 7) {

            @Override
            public boolean canUse() {
                return super.m_8036_() && EntitySeaBear.this.getAnimation() == IAnimatedEntity.NO_ANIMATION;
            }

            @Override
            public boolean canContinueToUse() {
                return super.m_8045_() && EntitySeaBear.this.getAnimation() == IAnimatedEntity.NO_ANIMATION;
            }
        });
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, false, SOMBRERO));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        if (this.m_20069_()) {
            if (this.onLandProgress > 0.0F) {
                this.onLandProgress--;
            }
        } else if (this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (this.m_20096_() && !this.m_20069_()) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_6853_(false);
            this.f_19812_ = true;
        }
        if (this.circleCooldown > 0) {
            this.circleCooldown--;
            this.setTarget(null);
            this.m_6703_(null);
        }
        if (this.getAnimation() == ANIMATION_POINT) {
            this.f_20883_ = this.m_6080_();
            this.f_20896_ = this.m_6080_();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity e) {
        return !isMobSafe(e);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getAnimation() == ANIMATION_POINT) {
            super.m_7023_(Vec3.ZERO);
        } else if (this.m_21515_() && this.m_20069_()) {
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_POINT, ANIMATION_ATTACK };
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        if (entity == null || !isMobSafe(entity)) {
            super.m_6710_(entity);
        }
    }

    @Override
    public void push(Entity entity) {
        if (!isMobSafe(entity)) {
            super.m_7334_(entity);
        }
    }

    private class AttackAI extends Goal {

        public AttackAI() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntitySeaBear.this.m_5448_() != null && EntitySeaBear.this.m_5448_().m_20072_() && EntitySeaBear.this.m_5448_().isAlive() && (EntitySeaBear.this.circleCooldown == 0 || EntitySeaBear.this.getAnimation() == EntitySeaBear.ANIMATION_POINT);
        }

        @Override
        public void tick() {
            LivingEntity enemy = EntitySeaBear.this.m_5448_();
            if (EntitySeaBear.this.getAnimation() == EntitySeaBear.ANIMATION_POINT) {
                EntitySeaBear.this.m_21573_().stop();
                EntitySeaBear.this.m_20256_(EntitySeaBear.this.m_20184_().multiply(0.0, 1.0, 0.0));
                EntitySeaBear.this.m_21391_(enemy, 360.0F, 50.0F);
            } else if (EntitySeaBear.isMobSafe(enemy) && EntitySeaBear.this.m_20270_(enemy) < 6.0F) {
                EntitySeaBear.this.circleCooldown = 100 + EntitySeaBear.this.f_19796_.nextInt(100);
                EntitySeaBear.this.setAnimation(EntitySeaBear.ANIMATION_POINT);
                EntitySeaBear.this.m_21391_(enemy, 360.0F, 50.0F);
                EntitySeaBear.this.lastCircle = enemy.m_20183_();
            } else {
                EntitySeaBear.this.m_21573_().moveTo(enemy.m_20185_(), enemy.m_20227_(0.5), enemy.m_20189_(), 1.6);
                if (EntitySeaBear.this.m_142582_(enemy) && EntitySeaBear.this.m_20270_(enemy) < 3.5F) {
                    EntitySeaBear.this.setAnimation(EntitySeaBear.ANIMATION_ATTACK);
                    if (EntitySeaBear.this.getAnimationTick() % 5 == 0) {
                        enemy.hurt(EntitySeaBear.this.m_269291_().mobAttack(EntitySeaBear.this), 6.0F);
                    }
                }
            }
        }
    }

    private class AvoidCircleAI extends Goal {

        private Vec3 target = null;

        public AvoidCircleAI() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntitySeaBear.this.circleCooldown > 0 && EntitySeaBear.this.lastCircle != null && EntitySeaBear.this.getAnimation() != EntitySeaBear.ANIMATION_POINT;
        }

        @Override
        public void tick() {
            BlockPos pos = EntitySeaBear.this.lastCircle;
            if (this.target == null || EntitySeaBear.this.m_20238_(this.target) < 2.0 || !EntitySeaBear.this.m_9236_().getFluidState(AMBlockPos.fromVec3(this.target).above()).is(FluidTags.WATER)) {
                this.target = DefaultRandomPos.getPosAway(EntitySeaBear.this, 20, 7, Vec3.atCenterOf(pos));
            }
            if (this.target != null && EntitySeaBear.this.m_9236_().getFluidState(AMBlockPos.fromVec3(this.target).above()).is(FluidTags.WATER)) {
                EntitySeaBear.this.m_21573_().moveTo(this.target.x, this.target.y, this.target.z, 1.0);
            }
        }
    }
}