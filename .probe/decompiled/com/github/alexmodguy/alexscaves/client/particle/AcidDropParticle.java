package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class AcidDropParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private int onGroundTime;

    protected AcidDropParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = 0.0F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ = this.f_107663_ * (1.2F + this.f_107223_.nextFloat());
        this.f_107225_ = 100 + this.f_107223_.nextInt(40);
        this.f_107225_ = Math.max(this.f_107225_, 1);
        this.m_108339_(sprites);
        this.f_107219_ = true;
    }

    @Override
    public float getQuadSize(float f) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + f) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        if ((float) this.f_107224_ < (float) this.f_107225_ * 0.25F) {
            this.f_107226_ = 0.0F;
        } else {
            this.f_107226_ = 1.0F;
            if (this.f_107218_) {
                this.onGroundTime++;
            }
        }
        int sprite = this.f_107218_ ? 1 : 0;
        this.m_108337_(this.sprites.get(sprite, 1));
        if (this.onGroundTime > 5) {
            this.m_107274_();
            this.f_107208_.addParticle(ParticleTypes.SMOKE.getType(), this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
        }
        super.m_5989_();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AcidDropParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}