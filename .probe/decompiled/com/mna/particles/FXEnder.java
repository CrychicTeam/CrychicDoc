package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.particles.types.render.ParticleRenderTypes;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import javax.annotation.Nonnull;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXEnder extends MAParticleBase {

    public FXEnder(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107663_ *= 0.35F;
        this.f_107225_ = 30;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    private void setupColorTransitions() {
        this.m_107271_(0.0F);
        this.colorTransitions.add(new Vector3(0.54901963F, 0.2627451F, 0.5764706F));
        this.colorTransitions.add(new Vector3(0.0, 0.0, 0.0));
        this.m_107253_(((Vector3) this.colorTransitions.get(0)).x, ((Vector3) this.colorTransitions.get(0)).y, ((Vector3) this.colorTransitions.get(0)).z);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_ * 4.0F);
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728640;
    }

    @Override
    protected boolean apply_aging() {
        super.apply_aging();
        if (this.colorTransitions.size() >= 2) {
            float agePct = MathUtils.clamp01((float) this.f_107224_ / (float) this.f_107225_);
            Vector3 startColor = (Vector3) this.colorTransitions.get(0);
            Vector3 endColor = (Vector3) this.colorTransitions.get(1);
            Vector3 color = Vector3.lerp(startColor, endColor, agePct);
            this.m_107253_(color.x, color.y, color.z);
        }
        return this.f_107220_;
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.INVERTED;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXEnderBezier extends MAParticleBase.FXParticleFactoryBase {

        public FXEnderBezier(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXEnder particle = new FXEnder(worldIn, x, y, z, this.spriteSet);
            particle.setMoveBezier(x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setupColorTransitions();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXEnderVelocity extends MAParticleBase.FXParticleFactoryBase {

        public FXEnderVelocity(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXEnder particle = new FXEnder(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.setupColorTransitions();
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}