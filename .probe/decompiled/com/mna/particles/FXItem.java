package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FXItem extends MAParticleBase {

    private FXItem(ClientLevel world, double x, double y, double z, double velX, double velY, double velZ) {
        super(world, x, y, z, velX, velY, velZ);
        this.f_107230_ = 0.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory extends MAParticleBase.FXParticleFactoryBase {

        public Factory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType type, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ) {
            FXItem particle = new FXItem(world, x, y, z, velX, velY, velZ);
            this.configureParticle(particle, type);
            return particle;
        }
    }
}