package com.rekindled.embers.particle;

import com.rekindled.embers.render.EmbersRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SparkParticle extends TextureSheetParticle {

    public float rBase = 1.0F;

    public float gBase = 1.0F;

    public float bBase = 1.0F;

    public float rotScale = this.f_107223_.nextFloat() * 0.1F + 0.05F;

    public SparkParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SparkParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.f_107226_ = 0.04F;
        this.f_107216_ = 0.12 * (double) this.f_107223_.nextFloat();
        this.f_107227_ = pOptions.getColor().x();
        this.f_107228_ = pOptions.getColor().y();
        this.f_107229_ = pOptions.getColor().z();
        this.rBase = pOptions.getColor().x();
        this.gBase = pOptions.getColor().y();
        this.bBase = pOptions.getColor().z();
        this.f_107204_ = this.f_107223_.nextFloat();
        this.f_107231_ = this.rotScale;
        this.f_107663_ = this.f_107663_ * 0.75F * pOptions.getScale();
        double i = 20.0 / (this.f_107223_.nextDouble() * 0.5 + 0.5);
        this.f_107225_ = (int) (i * (double) pOptions.getScale());
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.f_107663_ - this.f_107663_ * (((float) this.f_107224_ + pScaleFactor) / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107230_ = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        float brightness = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.f_107227_ = this.rBase * brightness;
        this.f_107228_ = this.gBase * brightness;
        this.f_107229_ = this.bBase * brightness;
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = this.f_107231_ + this.rotScale;
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EmbersRenderTypes.PARTICLE_SHEET_ADDITIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider.Sprite<SparkParticleOptions> {

        public TextureSheetParticle createParticle(SparkParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SparkParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}