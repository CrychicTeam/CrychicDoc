package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlyParticle extends TextureSheetParticle {

    private SpriteSet spriteSet;

    private final double orbitX;

    private final double orbitY;

    private final double orbitZ;

    private boolean reverseOrbit;

    private float orbitSpeed = 1.0F;

    private Vec3 orbitOffset;

    protected FlyParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107663_ = this.f_107663_ * (1.0F + world.f_46441_.nextFloat() * 0.3F);
        this.f_107219_ = true;
        this.orbitX = xSpeed;
        this.orbitY = ySpeed;
        this.orbitZ = zSpeed;
        this.spriteSet = spriteSet;
        this.f_107225_ = (int) (Math.random() * 10.0) + 40;
        this.f_172258_ = 0.8F;
        this.orbitOffset = new Vec3((double) ((0.5F - this.f_107223_.nextFloat()) * 2.0F), 0.0, (double) ((0.5F - this.f_107223_.nextFloat()) * 2.0F));
        this.reverseOrbit = this.f_107223_.nextBoolean();
        this.orbitSpeed = 3.0F + this.f_107223_.nextFloat() * 3.0F;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_ * 16.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        int sprite = this.f_107224_ % 4 >= 2 ? 1 : 0;
        this.m_108337_(this.spriteSet.get(sprite, 1));
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            Vec3 vec3 = this.getOrbitPosition((float) this.f_107224_);
            Vec3 movement = vec3.subtract(this.f_107212_, this.f_107213_, this.f_107214_).normalize().scale(0.1F);
            this.f_107215_ = movement.x + this.f_107223_.nextGaussian() * 0.015F;
            this.f_107216_ = this.f_107216_ + movement.y + this.f_107223_.nextGaussian() * 0.015F;
            if (this.f_107218_) {
                this.f_107216_ += 0.3F;
            }
            this.f_107217_ = this.f_107217_ + movement.z + this.f_107223_.nextGaussian() * 0.015F;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
    }

    private Vec3 getOrbitPosition(float angle) {
        Vec3 center = new Vec3(this.orbitX, this.orbitY, this.orbitZ);
        float rot = angle * (this.reverseOrbit ? -this.orbitSpeed : this.orbitSpeed) * (float) (Math.PI / 180.0);
        return center.add(this.orbitOffset.yRot(rot));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FlyParticle heartparticle = new FlyParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            heartparticle.m_108337_(this.spriteSet.get(0, 1));
            return heartparticle;
        }
    }
}