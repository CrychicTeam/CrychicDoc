package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

public class EntityGorgon extends Monster implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IHasCustomizableAttributes {

    public static Animation ANIMATION_SCARE;

    public static Animation ANIMATION_HIT;

    private int animationTick;

    private Animation currentAnimation;

    private GorgonAIStareAttack aiStare;

    private MeleeAttackGoal aiMelee;

    private int playerStatueCooldown;

    public EntityGorgon(EntityType<EntityGorgon> type, Level worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        degree *= 1.0 + (double) looker.m_20270_(seen) * 0.1;
        Vec3 Vector3d = looker.m_20252_(1.0F).normalize();
        Vec3 Vector3d1 = new Vec3(seen.m_20185_() - looker.m_20185_(), seen.m_20191_().minY + (double) seen.m_20192_() - (looker.m_20186_() + (double) looker.m_20192_()), seen.m_20189_() - looker.m_20189_());
        double d0 = Vector3d1.length();
        Vector3d1 = Vector3d1.normalize();
        double d1 = Vector3d.dot(Vector3d1);
        return d1 > 1.0 - degree / d0 && looker.hasLineOfSight(seen) && !isStoneMob(seen);
    }

    public static boolean isStoneMob(LivingEntity mob) {
        return mob instanceof EntityStoneStatue;
    }

    public static boolean isBlindfolded(LivingEntity attackTarget) {
        return attackTarget != null && (attackTarget.getItemBySlot(EquipmentSlot.HEAD).getItem() == IafItemRegistry.BLINDFOLD.get() || attackTarget.hasEffect(MobEffects.BLINDNESS) || ServerEvents.isBlindMob(attackTarget));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.gorgonMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 1.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.gorgonMaxHealth);
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        HitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getType() != HitResult.Type.MISS;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new RestrictSunGoal(this));
        this.f_21345_.addGoal(3, new FleeSunGoal(this, 1.0));
        this.f_21345_.addGoal(3, this.aiStare = new GorgonAIStareAttack(this, 1.0, 0, 15.0F));
        this.f_21345_.addGoal(3, this.aiMelee = new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0) {

            @Override
            public boolean canUse() {
                this.f_25730_ = 20;
                return super.m_8036_();
            }
        });
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F) {

            @Override
            public boolean canContinueToUse() {
                return this.f_25513_ != null && this.f_25513_ instanceof Player && ((Player) this.f_25513_).isCreative() ? false : super.canContinueToUse();
            }
        });
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, false, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity.isAlive();
            }
        }));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) || entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone();
            }
        }));
        this.f_21345_.removeGoal(this.aiMelee);
    }

    public void attackEntityWithRangedAttack(LivingEntity entity) {
        if (!(entity instanceof Mob) && entity instanceof LivingEntity) {
            this.forcePreyToLook(entity);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || this.m_5448_() != null && this.m_5448_().hasEffect(MobEffects.BLINDNESS) || this.m_5448_() != null && this.m_5448_() instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) this.m_5448_()).canBeTurnedToStone();
        if (blindness && this.f_20919_ == 0) {
            if (this.getAnimation() != ANIMATION_HIT) {
                this.setAnimation(ANIMATION_HIT);
            }
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2, false, true));
            }
        }
        return super.m_7327_(entityIn);
    }

    @Override
    public void setTarget(@Nullable LivingEntity LivingEntityIn) {
        super.m_6710_(LivingEntityIn);
        if (LivingEntityIn != null && !this.m_9236_().isClientSide) {
            boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || LivingEntityIn.hasEffect(MobEffects.BLINDNESS) || LivingEntityIn instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) LivingEntityIn).canBeTurnedToStone() || isBlindfolded(LivingEntityIn);
            if (blindness && this.f_20919_ == 0) {
                this.f_21345_.addGoal(3, this.aiMelee);
                this.f_21345_.removeGoal(this.aiStare);
            } else {
                this.f_21345_.addGoal(3, this.aiStare);
                this.f_21345_.removeGoal(this.aiMelee);
            }
        }
    }

    @Override
    public int getExperienceReward() {
        return 30;
    }

    @Override
    protected void tickDeath() {
        this.f_20919_++;
        this.f_21363_ = 20;
        if (this.m_9236_().isClientSide) {
            for (int k = 0; k < 5; k++) {
                double d2 = 0.4;
                double d0 = 0.1;
                double d1 = 0.1;
                IceAndFire.PROXY.spawnParticle(EnumParticles.Blood, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_(), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d2, d0, d1);
            }
        }
        if (this.f_20919_ >= 200) {
            if (!this.m_9236_().isClientSide && (this.m_6124_() || this.f_20889_ > 0 && this.m_6149_() && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))) {
                int i = this.getExperienceReward();
                i = ForgeEventFactory.getExperienceDrop(this, this.f_20888_, i);
                while (i > 0) {
                    int j = ExperienceOrb.getExperienceValue(i);
                    i -= j;
                    this.m_9236_().m_7967_(new ExperienceOrb(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), j));
                }
            }
            this.m_142687_(Entity.RemovalReason.KILLED);
            for (int k = 0; k < 20; k++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d2, d0, d1);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.playerStatueCooldown > 0) {
            this.playerStatueCooldown--;
        }
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null) {
            boolean blindness = this.m_21023_(MobEffects.BLINDNESS) || attackTarget.hasEffect(MobEffects.BLINDNESS);
            if (!blindness && this.f_20919_ == 0 && attackTarget instanceof Mob && !(attackTarget instanceof Player)) {
                this.forcePreyToLook(attackTarget);
            }
            if (isEntityLookingAt(attackTarget, this, 0.4)) {
                this.m_21563_().setLookAt(attackTarget.m_20185_(), attackTarget.m_20186_() + (double) attackTarget.m_20192_(), attackTarget.m_20189_(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
            }
        }
        if (attackTarget != null && isEntityLookingAt(this, attackTarget, 0.4) && isEntityLookingAt(attackTarget, this, 0.4) && !isBlindfolded(attackTarget)) {
            boolean blindnessx = this.m_21023_(MobEffects.BLINDNESS) || attackTarget.hasEffect(MobEffects.BLINDNESS) || attackTarget instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) attackTarget).canBeTurnedToStone();
            if (!blindnessx && this.f_20919_ == 0) {
                if (this.getAnimation() != ANIMATION_SCARE) {
                    this.m_5496_(IafSoundRegistry.GORGON_ATTACK, 1.0F, 1.0F);
                    this.setAnimation(ANIMATION_SCARE);
                }
                if (this.getAnimation() == ANIMATION_SCARE && this.getAnimationTick() > 10 && !this.m_9236_().isClientSide && this.playerStatueCooldown == 0) {
                    EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(attackTarget);
                    statue.m_19890_(attackTarget.m_20185_(), attackTarget.m_20186_(), attackTarget.m_20189_(), attackTarget.m_146908_(), attackTarget.m_146909_());
                    if (!this.m_9236_().isClientSide) {
                        this.m_9236_().m_7967_(statue);
                    }
                    statue.m_146922_(attackTarget.m_146908_());
                    statue.m_146922_(attackTarget.m_146908_());
                    statue.f_20885_ = attackTarget.m_146908_();
                    statue.f_20883_ = attackTarget.m_146908_();
                    statue.f_20884_ = attackTarget.m_146908_();
                    this.playerStatueCooldown = 40;
                    if (attackTarget instanceof Player) {
                        attackTarget.hurt(IafDamageRegistry.causeGorgonDamage(this), 2.1474836E9F);
                    } else {
                        attackTarget.remove(Entity.RemovalReason.KILLED);
                    }
                    this.setTarget(null);
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public int getMaxHeadXRot() {
        return 10;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public void forcePreyToLook(LivingEntity mob) {
        if (mob instanceof Mob mobEntity) {
            mobEntity.getLookControl().setLookAt(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), (float) mobEntity.getMaxHeadYRot(), (float) mobEntity.getMaxHeadXRot());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.m_7378_(pCompound);
        this.setConfigurableAttributes();
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SCARE, ANIMATION_HIT };
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GORGON_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.GORGON_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GORGON_DIE;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}