package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXGlow extends MAParticleBase {

    private static final Vector3 firstColor = new Vector3(0.12941177F, 0.22745098F, 0.8117647F);

    private static final Vector3 secondColor = new Vector3(0.2901961F, 0.38039216F, 0.9372549F);

    public FXGlow(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        Vector3 color = Vector3.lerp(firstColor, secondColor, (float) Math.random());
        this.m_107253_(color.x, color.y, color.z);
        this.f_107231_ = 0.0F;
        this.m_107271_(0.0F);
        this.f_107663_ *= 0.35F;
        this.f_107225_ = 30;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXGlowFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXGlowFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXGlow particle = new FXGlow(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, false);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}