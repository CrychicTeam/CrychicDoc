package yesman.epicfight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TsunamiSplashParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    TsunamiSplashParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.sprites = spriteSet7;
        this.f_107225_ = 16;
        this.m_108337_(this.sprites.get(this.f_107223_));
        this.f_107219_ = true;
        this.f_107226_ = 1.0F;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.m_5989_();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new TsunamiSplashParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}