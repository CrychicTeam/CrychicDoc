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
public class VaporParticle extends TextureSheetParticle {

    public float minScale = 0.5F;

    public float maxScale = 2.0F;

    public VaporParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, VaporParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_172258_ = 0.93F;
        this.f_107219_ = false;
        this.f_107226_ = 0.0F;
        float speed = this.f_107223_.nextFloat() * 0.5F + 0.7F;
        this.f_172259_ = true;
        if (!pOptions.getMotion().equals(Vec3.ZERO)) {
            this.m_172260_(pOptions.getMotion().x() * (double) speed, pOptions.getMotion().y() * (double) speed, pOptions.getMotion().z() * (double) speed);
        }
        this.f_107227_ = pOptions.getColor().x();
        this.f_107228_ = pOptions.getColor().y();
        this.f_107229_ = pOptions.getColor().z();
        this.f_107204_ = (float) (Math.PI * 2);
        this.f_107231_ = this.f_107204_ + 0.5F;
        this.maxScale = pOptions.getScale();
        double i = 8.0 / (this.f_107223_.nextDouble() * 0.5 + 0.5);
        this.f_107225_ = (int) (i * (double) pOptions.getScale());
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return (float) ((double) this.minScale + (double) (this.maxScale - this.minScale) * (-Math.cos((double) (Math.max((float) this.f_107224_ + pScaleFactor - 1.0F, 0.0F) / (float) (this.f_107225_ - 1) * 2.0F) * Math.PI) + 1.0) / 2.0) / 5.0F;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107230_ = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.f_107204_ = this.f_107231_;
        this.f_107231_ += 0.5F;
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
    public static class Provider implements ParticleProvider.Sprite<VaporParticleOptions> {

        public TextureSheetParticle createParticle(VaporParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new VaporParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}