package com.rekindled.embers.particle;

import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.render.EmbersRenderTypes;
import com.rekindled.embers.util.Misc;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TyrfingParticle extends TextureSheetParticle {

    public int phase = Misc.random.nextInt(360);

    public TyrfingParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, TyrfingParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_172258_ = 1.0F;
        this.f_172259_ = true;
        this.f_107219_ = true;
        if (!pOptions.getMotion().equals(Vec3.ZERO)) {
            this.m_172260_(pOptions.getMotion().x(), pOptions.getMotion().y(), pOptions.getMotion().z());
        } else if (pXSpeed == 0.0 && pYSpeed == 0.0 && pZSpeed == 0.0) {
            this.f_107215_ = ((double) Misc.random.nextFloat() - 0.5) * 0.75;
            this.f_107216_ = ((double) Misc.random.nextFloat() - 0.5) * 0.75;
            this.f_107217_ = ((double) Misc.random.nextFloat() - 0.5) * 0.75;
        } else {
            this.f_107215_ = pXSpeed;
            this.f_107216_ = pYSpeed;
            this.f_107217_ = pZSpeed;
        }
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107204_ = this.f_107223_.nextFloat();
        this.f_107231_ = this.f_107204_ + 1.0F;
        this.f_107663_ = this.f_107663_ * 0.75F * pOptions.getScale();
        float timerSine = ((float) Math.sin(8.0 * Math.toRadians((double) (EmbersClientEvents.ticks % 360 + this.phase))) + 1.0F) / 2.0F;
        this.f_107227_ = 0.25F * timerSine;
        this.f_107228_ = 0.0625F;
        this.f_107229_ = 0.125F + 0.125F * timerSine;
        if (pOptions.getLifetime() > 0) {
            this.f_107225_ = pOptions.getLifetime();
        } else {
            this.f_107225_ = (int) Math.max((float) (Misc.random.nextInt(8) * 2) * pOptions.getScale(), 1.0F);
        }
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.f_107663_ - this.f_107663_ * (((float) this.f_107224_ + pScaleFactor) / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107230_ = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.f_107204_ = this.f_107231_++;
        float timerSine = ((float) Math.sin(8.0 * Math.toRadians((double) (EmbersClientEvents.ticks % 360 + this.phase))) + 1.0F) / 2.0F;
        this.f_107227_ = 0.25F * timerSine;
        this.f_107228_ = 0.0625F;
        this.f_107229_ = 0.125F + 0.125F * timerSine;
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EmbersRenderTypes.PARTICLE_SHEET_TRANSLUCENT_NODEPTH;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider.Sprite<TyrfingParticleOptions> {

        public TextureSheetParticle createParticle(TyrfingParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new TyrfingParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}