package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleWhaleSplash extends WaterDropParticle {

    private ParticleWhaleSplash(ClientLevel p_i232433_1_, double p_i232433_2_, double p_i232433_4_, double p_i232433_6_, double p_i232433_8_, double p_i232433_10_, double p_i232433_12_) {
        super(p_i232433_1_, p_i232433_2_, p_i232433_4_, p_i232433_6_);
        this.f_107226_ = 0.04F;
        this.f_107216_ = 1.0;
        this.f_107225_ = (int) (16.0 / (Math.random() * 0.4 + 0.1));
        this.f_107663_ = 0.2F * (this.f_107223_.nextFloat() * 0.5F + 0.5F) * 2.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_107216_ < 0.0) {
            if (Math.abs(this.f_107215_) < 0.23) {
                this.f_107215_ *= 1.2;
            }
            if (Math.abs(this.f_107217_) < 0.23) {
                this.f_107217_ *= 1.2;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet p_i50679_1_) {
            this.spriteSet = p_i50679_1_;
        }

        public Particle createParticle(SimpleParticleType p_199234_1_, ClientLevel p_199234_2_, double p_199234_3_, double p_199234_5_, double p_199234_7_, double p_199234_9_, double p_199234_11_, double p_199234_13_) {
            ParticleWhaleSplash lvt_15_1_ = new ParticleWhaleSplash(p_199234_2_, p_199234_3_, p_199234_5_, p_199234_7_, p_199234_9_, p_199234_11_, p_199234_13_);
            lvt_15_1_.m_108335_(this.spriteSet);
            return lvt_15_1_;
        }
    }
}