package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractDragonPhaseInstance implements DragonPhaseInstance {

    protected final EnderDragon dragon;

    public AbstractDragonPhaseInstance(EnderDragon enderDragon0) {
        this.dragon = enderDragon0;
    }

    @Override
    public boolean isSitting() {
        return false;
    }

    @Override
    public void doClientTick() {
    }

    @Override
    public void doServerTick() {
    }

    @Override
    public void onCrystalDestroyed(EndCrystal endCrystal0, BlockPos blockPos1, DamageSource damageSource2, @Nullable Player player3) {
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public float getFlySpeed() {
        return 0.6F;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return null;
    }

    @Override
    public float onHurt(DamageSource damageSource0, float float1) {
        return float1;
    }

    @Override
    public float getTurnSpeed() {
        float $$0 = (float) this.dragon.m_20184_().horizontalDistance() + 1.0F;
        float $$1 = Math.min($$0, 40.0F);
        return 0.7F / $$1 / $$0;
    }
}