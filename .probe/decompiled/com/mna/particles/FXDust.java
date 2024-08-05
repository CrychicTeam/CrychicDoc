package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXDust extends MAParticleBase {

    private static final Vector3 firstColor = new Vector3(0.43529412F, 0.26666668F, 0.15686275F);

    private static final Vector3 secondColor = new Vector3(0.32156864F, 0.1882353F, 0.007843138F);

    public FXDust(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ = 0.0;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        Vector3 color = Vector3.lerp(firstColor, secondColor, (float) Math.random());
        this.m_107253_(color.x, color.y, color.z);
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107226_ = 0.003F;
        this.m_107271_(0.0F);
        this.f_107663_ *= 0.15F;
        this.f_107225_ = 30;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_ * 3.0F);
    }

    @Override
    public void tick() {
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = (float) ((double) this.f_107231_ + Math.random() * Math.PI / 180.0);
        super.tick();
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 10485760;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXDustFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXDustFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXDust particle = new FXDust(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXDustLerpFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXDustLerpFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXDust particle = new FXDust(worldIn, x, y, z, this.spriteSet);
            particle.setMoveLerp(x, y, z, xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}