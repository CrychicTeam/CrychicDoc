package net.minecraft.world.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class WitherSkull extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);

    public WitherSkull(EntityType<? extends WitherSkull> entityTypeExtendsWitherSkull0, Level level1) {
        super(entityTypeExtendsWitherSkull0, level1);
    }

    public WitherSkull(Level level0, LivingEntity livingEntity1, double double2, double double3, double double4) {
        super(EntityType.WITHER_SKULL, livingEntity1, double2, double3, double4, level0);
    }

    @Override
    protected float getInertia() {
        return this.isDangerous() ? 0.73F : super.getInertia();
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public float getBlockExplosionResistance(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4, float float5) {
        return this.isDangerous() && WitherBoss.canDestroy(blockState3) ? Math.min(0.8F, float5) : float5;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        if (!this.m_9236_().isClientSide) {
            Entity $$1 = entityHitResult0.getEntity();
            boolean $$4;
            if (this.m_19749_() instanceof LivingEntity $$3) {
                $$4 = $$1.hurt(this.m_269291_().witherSkull(this, $$3), 8.0F);
                if ($$4) {
                    if ($$1.isAlive()) {
                        this.m_19970_($$3, $$1);
                    } else {
                        $$3.heal(5.0F);
                    }
                }
            } else {
                $$4 = $$1.hurt(this.m_269291_().magic(), 5.0F);
            }
            if ($$4 && $$1 instanceof LivingEntity $$6) {
                int $$7 = 0;
                if (this.m_9236_().m_46791_() == Difficulty.NORMAL) {
                    $$7 = 10;
                } else if (this.m_9236_().m_46791_() == Difficulty.HARD) {
                    $$7 = 40;
                }
                if ($$7 > 0) {
                    $$6.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * $$7, 1), this.m_150173_());
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0F, false, Level.ExplosionInteraction.MOB);
            this.m_146870_();
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_DANGEROUS, false);
    }

    public boolean isDangerous() {
        return this.f_19804_.get(DATA_DANGEROUS);
    }

    public void setDangerous(boolean boolean0) {
        this.f_19804_.set(DATA_DANGEROUS, boolean0);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}