package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class BaseAshSmokeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected BaseAshSmokeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, float float4, float float5, float float6, double double7, double double8, double double9, float float10, SpriteSet spriteSet11, float float12, int int13, float float14, boolean boolean15) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = float14;
        this.f_172259_ = true;
        this.sprites = spriteSet11;
        this.f_107215_ *= (double) float4;
        this.f_107216_ *= (double) float5;
        this.f_107217_ *= (double) float6;
        this.f_107215_ += double7;
        this.f_107216_ += double8;
        this.f_107217_ += double9;
        float $$16 = clientLevel0.f_46441_.nextFloat() * float12;
        this.f_107227_ = $$16;
        this.f_107228_ = $$16;
        this.f_107229_ = $$16;
        this.f_107663_ *= 0.75F * float10;
        this.f_107225_ = (int) ((double) int13 / ((double) clientLevel0.f_46441_.nextFloat() * 0.8 + 0.2) * (double) float10);
        this.f_107225_ = Math.max(this.f_107225_, 1);
        this.m_108339_(spriteSet11);
        this.f_107219_ = boolean15;
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