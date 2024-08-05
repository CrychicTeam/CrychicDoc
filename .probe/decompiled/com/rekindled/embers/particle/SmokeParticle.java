package com.rekindled.embers.particle;

import com.rekindled.embers.render.EmbersRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SmokeParticle extends TextureSheetParticle {

    public float rotScale = this.f_107223_.nextFloat() * 0.1F + 0.05F;

    public SmokeParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SmokeParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_172258_ = 1.0F;
        this.f_172259_ = true;
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107227_ = pOptions.getColor().x();
        this.f_107228_ = pOptions.getColor().y();
        this.f_107229_ = pOptions.getColor().z();
        this.f_107204_ = (float) (Math.PI * 2);
        this.f_107231_ = this.f_107204_ + this.rotScale;
        this.f_107663_ = this.f_107663_ * 0.75F * pOptions.getScale();
        double i = 6.0 / (this.f_107223_.nextDouble() * 0.5 + 0.5);
        this.f_107225_ = (int) (i * (double) pOptions.getScale());
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.f_107663_ - this.f_107663_ * (((float) this.f_107224_ + pScaleFactor) / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107230_ = 0.5F * (1.0F - (float) this.f_107224_ / (float) this.f_107225_);
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = this.f_107231_ + this.rotScale;
        this.f_107216_ += 0.004;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EmbersRenderTypes.PARTICLE_SHEET_TRANSLUCENT_NODEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider.Sprite<SmokeParticleOptions> {

        public TextureSheetParticle createParticle(SmokeParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SmokeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}