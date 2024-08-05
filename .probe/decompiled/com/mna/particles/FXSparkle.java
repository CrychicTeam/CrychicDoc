package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXSparkle extends MAParticleBase {

    public FXSparkle(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107253_(1.0F, 0.0F, 1.0F);
        this.f_107663_ *= 0.15F;
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.m_108339_(sprite);
    }

    private void gravity(double x, double y, double z) {
        this.movementType = FXMovementType.VELOCITY;
        this.f_107215_ = x;
        this.f_107216_ = y;
        this.f_107217_ = z;
        this.f_107226_ = 0.06F;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (float) (this.f_107225_ - this.f_107224_ + 1) / (float) this.f_107225_;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728640;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleBezierPointFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleBezierPointFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.setMoveBezier(x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleGravityFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleGravityFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.gravity(xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleLerpPointFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleLerpPointFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.setMoveLerp(x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleRandomFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleRandomFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.setMoveRandomly(xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleStationaryFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleStationaryFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSparkleVelocityFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSparkleVelocityFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSparkle particle = new FXSparkle(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, false);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}