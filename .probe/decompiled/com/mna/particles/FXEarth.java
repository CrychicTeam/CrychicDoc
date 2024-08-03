package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXEarth extends MAParticleBase {

    public FXEarth(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ = -Math.random() * 0.03F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.m_107271_(0.0F);
        Vector3 first = new Vector3(56.0, 182.0, 106.0);
        Vector3 second = new Vector3(178.0, 162.0, 56.0);
        Vector3 color = Vector3.lerp(first, second, (float) Math.random());
        this.colorTransitions.add(color);
        this.colorTransitions.add(color);
        this.f_107663_ *= 0.35F;
        this.f_107225_ = 30;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    private void setMoveVelocity(double x, double y, double z) {
        this.m_107253_((float) x, (float) y, (float) z);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_;
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728640;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXEarthFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXEarthFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXEarth particle = new FXEarth(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}