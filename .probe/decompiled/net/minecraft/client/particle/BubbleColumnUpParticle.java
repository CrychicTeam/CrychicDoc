package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;

public class BubbleColumnUpParticle extends TextureSheetParticle {

    BubbleColumnUpParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3);
        this.f_107226_ = -0.125F;
        this.f_172258_ = 0.85F;
        this.m_107250_(0.02F, 0.02F);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.6F + 0.2F);
        this.f_107215_ = double4 * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.f_107216_ = double5 * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.f_107217_ = double6 * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.f_107225_ = (int) (40.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (!this.f_107220_ && !this.f_107208_.m_6425_(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).is(FluidTags.WATER)) {
            this.m_107274_();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            BubbleColumnUpParticle $$8 = new BubbleColumnUpParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}