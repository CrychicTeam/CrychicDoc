package net.minecraft.world.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SmallFireball extends Fireball {

    public SmallFireball(EntityType<? extends SmallFireball> entityTypeExtendsSmallFireball0, Level level1) {
        super(entityTypeExtendsSmallFireball0, level1);
    }

    public SmallFireball(Level level0, LivingEntity livingEntity1, double double2, double double3, double double4) {
        super(EntityType.SMALL_FIREBALL, livingEntity1, double2, double3, double4, level0);
    }

    public SmallFireball(Level level0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(EntityType.SMALL_FIREBALL, double1, double2, double3, double4, double5, double6, level0);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        if (!this.m_9236_().isClientSide) {
            Entity $$1 = entityHitResult0.getEntity();
            Entity $$2 = this.m_19749_();
            int $$3 = $$1.getRemainingFireTicks();
            $$1.setSecondsOnFire(5);
            if (!$$1.hurt(this.m_269291_().fireball(this, $$2), 5.0F)) {
                $$1.setRemainingFireTicks($$3);
            } else if ($$2 instanceof LivingEntity) {
                this.m_19970_((LivingEntity) $$2, $$1);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult0) {
        super.m_8060_(blockHitResult0);
        if (!this.m_9236_().isClientSide) {
            Entity $$1 = this.m_19749_();
            if (!($$1 instanceof Mob) || this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockPos $$2 = blockHitResult0.getBlockPos().relative(blockHitResult0.getDirection());
                if (this.m_9236_().m_46859_($$2)) {
                    this.m_9236_().setBlockAndUpdate($$2, BaseFireBlock.getState(this.m_9236_(), $$2));
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
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
}