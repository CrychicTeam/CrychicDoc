package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public FireParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.m_6569_(this.f_107223_.nextFloat() * 1.75F + 1.0F);
        this.f_172258_ = (float) ((double) this.f_172258_ - (double) this.f_107223_.nextFloat() * 0.1);
        this.f_107225_ = 10 + (int) (Math.random() * 25.0);
        this.sprites = spriteSet;
        this.m_108339_(spriteSet);
        this.f_107226_ = -0.01F;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.animateContinuously();
        if (this.f_107223_.nextFloat() <= 0.25F) {
            this.f_107208_.addParticle(ParticleHelper.EMBERS, this.f_107212_, this.f_107213_, this.f_107214_, this.f_107215_, this.f_107216_, this.f_107217_);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        return 15728880;
    }

    private void animateContinuously() {
        if (this.f_107224_ % 8 == 0) {
            this.m_108337_(this.sprites.get(this.f_107223_));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new FireParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}