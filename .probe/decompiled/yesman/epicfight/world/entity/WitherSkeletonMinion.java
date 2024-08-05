package yesman.epicfight.world.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;

public class WitherSkeletonMinion extends WitherSkeleton {

    private WitherBoss summoner;

    public WitherSkeletonMinion(EntityType<? extends WitherSkeletonMinion> entityTypeExtendsWitherSkeletonMinion0, Level level1) {
        super(entityTypeExtendsWitherSkeletonMinion0, level1);
    }

    public WitherSkeletonMinion(Level level, WitherBoss summoner, double x, double y, double z) {
        super(EpicFightEntities.WITHER_SKELETON_MINION.get(), level);
        this.m_20343_(x, y, z);
        this.summoner = summoner;
        if (this.summoner != null && this.summoner.m_6084_()) {
            this.m_6710_((LivingEntity) this.summoner.m_9236_().getEntity(this.summoner.getAlternativeTarget(0)));
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance p_70687_1_) {
        return p_70687_1_.getEffect() != MobEffects.WITHER && super.canBeAffected(p_70687_1_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, livingentity -> livingentity.getMobType() != MobType.UNDEAD && livingentity.attackable()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.summoner != null && source.getEntity() == this.summoner ? false : super.m_6469_(source, amount);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_9236_().isClientSide()) {
            this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + this.f_19796_.nextGaussian() * 0.3F, this.m_20188_() + this.f_19796_.nextGaussian() * 0.3F, this.m_20189_() + this.f_19796_.nextGaussian() * 0.3F, 0.0, 0.0, 0.0);
        } else {
            if (this.f_19797_ > 200 && this.f_19797_ % 30 == 0) {
                this.hurt(this.m_9236_().damageSources().wither(), 1.0F);
            }
            if (this.summoner != null && !this.summoner.m_6084_()) {
                this.m_21153_(0.0F);
            }
        }
    }
}