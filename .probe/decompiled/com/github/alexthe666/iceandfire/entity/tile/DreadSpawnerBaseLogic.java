package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class DreadSpawnerBaseLogic extends BaseSpawner {

    private short spawnDelay = 20;

    private double spin;

    private double oSpin;

    @Override
    public void clientTick(@NotNull Level level0, @NotNull BlockPos blockPos1) {
        if (!this.isNearPlayer(level0, blockPos1)) {
            this.oSpin = this.spin;
        } else {
            double d0 = (double) blockPos1.m_123341_() + level0.random.nextDouble();
            double d1 = (double) blockPos1.m_123342_() + level0.random.nextDouble();
            double d2 = (double) blockPos1.m_123343_() + level0.random.nextDouble();
            level0.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0, d1, d2, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            }
            this.oSpin = this.spin;
            this.spin = (this.spin + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0;
        }
    }

    private boolean isNearPlayer(Level level0, BlockPos blockPos1) {
        return level0.m_45914_((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, 20.0);
    }

    @Override
    public double getSpin() {
        return this.spin;
    }

    @Override
    public double getoSpin() {
        return this.oSpin;
    }
}