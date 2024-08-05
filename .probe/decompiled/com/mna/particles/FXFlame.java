package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXFlame extends MAParticleBase {

    public FXFlame(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107253_(1.0F, 0.0F, 1.0F);
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107663_ *= 0.25F;
        this.f_107225_ = 20;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_ * 3.0F);
    }

    private void setupColorTransitionNormal() {
        this.colorTransitions.add(new Vector3(1.0, 0.84705883F, 0.0));
        this.colorTransitions.add(new Vector3(1.0, 0.21960784F, 0.0));
        this.colorTransitions.add(new Vector3(0.21960784F, 0.0, 0.0));
        this.m_107253_(((Vector3) this.colorTransitions.get(0)).x, ((Vector3) this.colorTransitions.get(0)).y, ((Vector3) this.colorTransitions.get(0)).z);
    }

    private void setupColorTransitionHellfire() {
        this.colorTransitions.add(new Vector3(0.25882354F, 0.7176471F, 0.07450981F));
        this.colorTransitions.add(new Vector3(0.54509807F, 0.7176471F, 0.07450981F));
        this.colorTransitions.add(new Vector3(0.25882354F, 0.73333335F, 0.2627451F));
        this.m_107253_(((Vector3) this.colorTransitions.get(0)).x, ((Vector3) this.colorTransitions.get(0)).y, ((Vector3) this.colorTransitions.get(0)).z);
    }

    @Override
    protected boolean apply_aging() {
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        }
        float agePct = (float) this.f_107224_ / (float) this.f_107225_;
        float color_T = (double) agePct < 0.5 ? agePct / 0.5F : (agePct - 0.5F) / 0.5F;
        this.lerpAlpha(agePct);
        if (this.colorTransitions.size() >= 3) {
            Vector3 startColor = (Vector3) this.colorTransitions.get(0);
            Vector3 midColor = (Vector3) this.colorTransitions.get(1);
            Vector3 endColor = (Vector3) this.colorTransitions.get(2);
            Vector3 color = (double) agePct < 0.5 ? Vector3.lerp(startColor, midColor, color_T) : Vector3.lerp(midColor, endColor, color_T);
            this.m_107253_(color.x, color.y, color.z);
        }
        return this.f_107220_;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728640;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXFlameFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXFlameFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXFlame particle = new FXFlame(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.m_107271_(0.0F);
            particle.setupColorTransitionNormal();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXFlameLerpFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXFlameLerpFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXFlame particle = new FXFlame(worldIn, x, y, z, this.spriteSet);
            particle.setMoveLerp(x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.0F);
            particle.setupColorTransitionNormal();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXFlameOrbitFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXFlameOrbitFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double forwardSpeed, double upSpeed, double radius) {
            FXFlame particle = new FXFlame(worldIn, x, y, z, this.spriteSet);
            particle.setMoveOrbit(x, y, z, forwardSpeed, upSpeed, radius);
            particle.m_107271_(0.0F);
            particle.setupColorTransitionNormal();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXHellfireFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXHellfireFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXFlame particle = new FXFlame(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.m_107271_(0.0F);
            particle.setupColorTransitionHellfire();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}