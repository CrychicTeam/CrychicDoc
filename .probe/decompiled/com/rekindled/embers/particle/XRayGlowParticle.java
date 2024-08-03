package com.rekindled.embers.particle;

import com.rekindled.embers.render.EmbersRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class XRayGlowParticle extends TextureSheetParticle {

    public float rBase = 1.0F;

    public float gBase = 1.0F;

    public float bBase = 1.0F;

    public XRayGlowParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, XRayGlowParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.f_107219_ = false;
        if (!pOptions.getMotion().equals(Vec3.ZERO)) {
            this.m_172260_(pOptions.getMotion().x(), pOptions.getMotion().y(), pOptions.getMotion().z());
        } else if (pXSpeed == 0.0 && pYSpeed == 0.0 && pZSpeed == 0.0) {
            this.f_107215_ = (Math.random() * 2.0 - 1.0) * 0.4F;
            this.f_107216_ = (Math.random() * 2.0 - 1.0) * 0.4F;
            this.f_107217_ = (Math.random() * 2.0 - 1.0) * 0.4F;
        } else {
            this.f_107215_ = pXSpeed;
            this.f_107216_ = pYSpeed;
            this.f_107217_ = pZSpeed;
        }
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107227_ = pOptions.getColor().x();
        this.f_107228_ = pOptions.getColor().y();
        this.f_107229_ = pOptions.getColor().z();
        this.rBase = pOptions.getColor().x();
        this.gBase = pOptions.getColor().y();
        this.bBase = pOptions.getColor().z();
        this.f_107204_ = this.f_107223_.nextFloat();
        this.f_107231_ = this.f_107204_ + 1.0F;
        this.f_107663_ = this.f_107663_ * 0.75F * pOptions.getScale();
        double i = 4.5 / (this.f_107223_.nextDouble() * 0.3 + 0.7);
        if (pOptions.getLifetime() > 0) {
            this.f_107225_ = pOptions.getLifetime();
        } else {
            this.f_107225_ = (int) Math.max((float) i * pOptions.getScale(), 1.0F);
        }
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.f_107663_ - this.f_107663_ * (((float) this.f_107224_ + pScaleFactor) / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        super.m_5989_();
        float brightness = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.f_107227_ = this.rBase * brightness;
        this.f_107228_ = this.gBase * brightness;
        this.f_107229_ = this.bBase * brightness;
        this.f_107204_ = this.f_107231_++;
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EmbersRenderTypes.PARTICLE_SHEET_ADDITIVE_XRAY;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider.Sprite<XRayGlowParticleOptions> {

        public TextureSheetParticle createParticle(XRayGlowParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new XRayGlowParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}