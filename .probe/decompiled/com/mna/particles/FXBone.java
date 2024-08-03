package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXBone extends MAParticleBase {

    public FXBone(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ = 0.0;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107226_ = 0.003F;
        this.m_107271_(0.0F);
        this.f_107663_ *= 0.25F;
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

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXBoneFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXBoneFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXBone particle = new FXBone(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.f_107226_ = 0.01F;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXBoneOrbitFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXBoneOrbitFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXBone particle = new FXBone(worldIn, x, y, z, this.spriteSet);
            particle.setMoveOrbit(x, y, z, xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}