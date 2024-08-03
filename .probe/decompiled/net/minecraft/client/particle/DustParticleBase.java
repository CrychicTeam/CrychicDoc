package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.util.Mth;

public class DustParticleBase<T extends DustParticleOptionsBase> extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected DustParticleBase(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, T t7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.sprites = spriteSet8;
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        float $$9 = this.f_107223_.nextFloat() * 0.4F + 0.6F;
        this.f_107227_ = this.randomizeColor(t7.getColor().x(), $$9);
        this.f_107228_ = this.randomizeColor(t7.getColor().y(), $$9);
        this.f_107229_ = this.randomizeColor(t7.getColor().z(), $$9);
        this.f_107663_ = this.f_107663_ * 0.75F * t7.getScale();
        int $$10 = (int) (8.0 / (this.f_107223_.nextDouble() * 0.8 + 0.2));
        this.f_107225_ = (int) Math.max((float) $$10 * t7.getScale(), 1.0F);
        this.m_108339_(spriteSet8);
    }

    protected float randomizeColor(float float0, float float1) {
        return (this.f_107223_.nextFloat() * 0.2F + 0.8F) * float0 * float1;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
    }
}