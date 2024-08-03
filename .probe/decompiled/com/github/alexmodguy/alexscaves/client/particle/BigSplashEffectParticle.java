package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BigSplashEffectParticle extends WaterDropParticle {

    public BigSplashEffectParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        super(level, x, y, z);
        this.f_107226_ = 0.04F;
        this.f_107215_ = xMotion;
        this.f_107216_ = yMotion;
        this.f_107217_ = zMotion;
        this.f_107225_ = (int) (15.0 / (Math.random() * 0.8 + 0.2));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
            BigSplashEffectParticle splashparticle = new BigSplashEffectParticle(clientLevel, x, y, z, xMotion, yMotion, zMotion);
            splashparticle.m_108335_(this.sprite);
            return splashparticle;
        }
    }
}