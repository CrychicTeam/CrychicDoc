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

public class FXCozySmoke extends MAParticleBase {

    public FXCozySmoke(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ *= 0.35F;
        this.f_107225_ = 30;
        this.f_107219_ = false;
        this.f_107230_ = 1.0F;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    @Override
    protected void lerpAlpha(float agePct) {
        if (this.f_107224_ >= this.f_107225_ - 30 && this.f_107230_ > 0.01F) {
            this.f_107230_ -= 0.03F;
        }
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_ * 3.0F);
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXCozySmokeFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXCozySmokeFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXCozySmoke particle = new FXCozySmoke(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, false);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}