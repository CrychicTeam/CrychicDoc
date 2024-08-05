package net.minecraft.world.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class LargeFireball extends Fireball {

    private int explosionPower = 1;

    public LargeFireball(EntityType<? extends LargeFireball> entityTypeExtendsLargeFireball0, Level level1) {
        super(entityTypeExtendsLargeFireball0, level1);
    }

    public LargeFireball(Level level0, LivingEntity livingEntity1, double double2, double double3, double double4, int int5) {
        super(EntityType.FIREBALL, livingEntity1, double2, double3, double4, level0);
        this.explosionPower = int5;
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
            boolean $$1 = this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), (float) this.explosionPower, $$1, Level.ExplosionInteraction.MOB);
            this.m_146870_();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        if (!this.m_9236_().isClientSide) {
            Entity $$1 = entityHitResult0.getEntity();
            Entity $$2 = this.m_19749_();
            $$1.hurt(this.m_269291_().fireball(this, $$2), 6.0F);
            if ($$2 instanceof LivingEntity) {
                this.m_19970_((LivingEntity) $$2, $$1);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("ExplosionPower", 99)) {
            this.explosionPower = compoundTag0.getByte("ExplosionPower");
        }
    }
}