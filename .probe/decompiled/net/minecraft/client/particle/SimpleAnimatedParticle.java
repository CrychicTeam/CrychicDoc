package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class SimpleAnimatedParticle extends TextureSheetParticle {

    protected final SpriteSet sprites;

    private float fadeR;

    private float fadeG;

    private float fadeB;

    private boolean hasFade;

    protected SimpleAnimatedParticle(ClientLevel clientLevel0, double double1, double double2, double double3, SpriteSet spriteSet4, float float5) {
        super(clientLevel0, double1, double2, double3);
        this.f_172258_ = 0.91F;
        this.f_107226_ = float5;
        this.sprites = spriteSet4;
    }

    public void setColor(int int0) {
        float $$1 = (float) ((int0 & 0xFF0000) >> 16) / 255.0F;
        float $$2 = (float) ((int0 & 0xFF00) >> 8) / 255.0F;
        float $$3 = (float) ((int0 & 0xFF) >> 0) / 255.0F;
        float $$4 = 1.0F;
        this.m_107253_($$1 * 1.0F, $$2 * 1.0F, $$3 * 1.0F);
    }

    public void setFadeColor(int int0) {
        this.fadeR = (float) ((int0 & 0xFF0000) >> 16) / 255.0F;
        this.fadeG = (float) ((int0 & 0xFF00) >> 8) / 255.0F;
        this.fadeB = (float) ((int0 & 0xFF) >> 0) / 255.0F;
        this.hasFade = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_);
            if (this.hasFade) {
                this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.2F;
                this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.2F;
                this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.2F;
            }
        }
    }

    @Override
    public int getLightColor(float float0) {
        return 15728880;
    }
}