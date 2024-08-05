package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class SmallExplosionParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private boolean hasFadeColor = false;

    private float fadeR;

    private float fadeG;

    private float fadeB;

    protected SmallExplosionParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, boolean shortLifespan, int color1) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
        this.m_107250_(0.5F, 0.5F);
        this.f_107663_ = (shortLifespan ? 1.0F : 0.8F) + world.f_46441_.nextFloat() * 0.3F;
        this.f_107225_ = shortLifespan ? 5 + world.f_46441_.nextInt(3) : 15 + world.f_46441_.nextInt(10);
        this.f_172258_ = 0.96F;
        float randCol = world.f_46441_.nextFloat() * 0.05F;
        this.sprites = sprites;
        this.m_107253_(Math.min((float) FastColor.ARGB32.red(color1) / 255.0F + randCol, 1.0F), (float) FastColor.ARGB32.green(color1) / 255.0F + randCol, (float) FastColor.ARGB32.blue(color1) / 255.0F + randCol);
    }

    public void setFadeColor(int i) {
        this.hasFadeColor = true;
        this.fadeR = (float) ((i & 0xFF0000) >> 16) / 255.0F;
        this.fadeG = (float) ((i & 0xFF00) >> 8) / 255.0F;
        this.fadeB = (float) ((i & 0xFF) >> 0) / 255.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.m_108339_(this.sprites);
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            if (this.hasFadeColor) {
                this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.2F;
                this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.2F;
                this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.2F;
            } else {
                this.f_107227_ *= 0.95F;
                this.f_107228_ *= 0.95F;
                this.f_107229_ *= 0.95F;
            }
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return super.m_5902_(scaleFactor);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class AmberFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public AmberFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false, 16767518);
            particle.m_108339_(this.spriteSet);
            particle.m_6569_(0.8F);
            return particle;
        }
    }

    public static class BlueRaygunFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public BlueRaygunFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 15658734);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 5 + worldIn.f_46441_.nextInt(5);
            particle.m_6569_(0.5F + worldIn.f_46441_.nextFloat() * 0.5F);
            particle.setFadeColor(4255450);
            return particle;
        }
    }

    public static class MineFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public MineFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 16757504);
            particle.m_108339_(this.spriteSet);
            return particle;
        }
    }

    public static class NukeFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public NukeFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false, 16757504);
            particle.m_108339_(this.spriteSet);
            return particle;
        }
    }

    public static class RaygunFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public RaygunFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 15658734);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 5 + worldIn.f_46441_.nextInt(3);
            particle.m_6569_(0.6F + worldIn.f_46441_.nextFloat() * 0.3F);
            particle.setFadeColor(4255296);
            return particle;
        }
    }

    public static class TotemFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public TotemFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 16711680);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 5 + worldIn.f_46441_.nextInt(3);
            particle.m_6569_(1.2F + worldIn.f_46441_.nextFloat() * 0.3F);
            particle.setFadeColor(0);
            return particle;
        }
    }

    public static class TremorzillaFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public TremorzillaFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 15658734);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 9 + worldIn.f_46441_.nextInt(3);
            particle.m_6569_(1.0F + worldIn.f_46441_.nextFloat() * 0.9F);
            particle.setFadeColor(10223421);
            return particle;
        }
    }

    public static class TremorzillaRetroFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public TremorzillaRetroFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 15658734);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 9 + worldIn.f_46441_.nextInt(3);
            particle.m_6569_(1.0F + worldIn.f_46441_.nextFloat() * 0.9F);
            particle.setFadeColor(14708479);
            return particle;
        }
    }

    public static class TremorzillaTectonicFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public TremorzillaTectonicFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true, 15658734);
            particle.m_108339_(this.spriteSet);
            particle.f_107225_ = 9 + worldIn.f_46441_.nextInt(3);
            particle.m_6569_(1.0F + worldIn.f_46441_.nextFloat() * 0.9F);
            particle.setFadeColor(16766513);
            return particle;
        }
    }

    public static class UnderzealotFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public UnderzealotFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SmallExplosionParticle particle = new SmallExplosionParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false, 0);
            particle.m_108339_(this.spriteSet);
            return particle;
        }
    }
}