package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagneticOrbitParticle extends AbstractTrailParticle {

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/trail.png");

    protected double orbitX;

    protected double orbitY;

    protected double orbitZ;

    protected double orbitDistance;

    protected Vec3 orbitOffset;

    protected boolean reverseOrbit;

    protected int orbitAxis;

    protected float orbitSpeed = 1.0F;

    public MagneticOrbitParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.trailA = 0.8F;
        this.f_107225_ = 50 + this.f_107223_.nextInt(30);
        this.f_107226_ = 0.0F;
        this.orbitX = xd;
        this.orbitY = yd;
        this.orbitZ = zd;
        this.orbitDistance = 1.0;
        this.orbitOffset = new Vec3(this.f_107223_.nextDouble() - 0.5, this.f_107223_.nextDouble() - 0.5, this.f_107223_.nextDouble() - 0.5);
        this.reverseOrbit = this.f_107223_.nextBoolean();
        this.orbitAxis = this.f_107223_.nextInt(2);
        this.orbitSpeed = 1.0F + this.f_107223_.nextFloat() * 3.0F;
    }

    public Vec3 getOrbitPosition(float angle) {
        Vec3 center = new Vec3(this.orbitX, this.orbitY, this.orbitZ);
        Vec3 add = this.orbitOffset.scale(this.orbitDistance);
        float rot = angle * (this.reverseOrbit ? -this.orbitSpeed : this.orbitSpeed) * (float) (Math.PI / 180.0);
        switch(this.orbitAxis) {
            case 0:
                add = add.xRot(rot);
                break;
            case 1:
                add = add.yRot(rot);
                break;
            case 2:
                add = add.zRot(rot);
        }
        return center.add(add);
    }

    @Override
    public void tick() {
        Vec3 vec3 = this.getOrbitPosition((float) this.f_107224_);
        Vec3 movement = vec3.subtract(this.f_107212_, this.f_107213_, this.f_107214_).normalize().scale((double) (this.orbitSpeed * 0.01F));
        this.f_107215_ = this.f_107215_ + movement.x;
        this.f_107216_ = this.f_107216_ + movement.y;
        this.f_107217_ = this.f_107217_ + movement.z;
        float fade = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.trailA = 0.8F * fade;
        super.tick();
    }

    @Override
    public float getTrailHeight() {
        return 0.25F;
    }

    @Override
    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class AzureFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            MagneticOrbitParticle particle = new MagneticOrbitParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.trailR = 0.2F + worldIn.f_46441_.nextFloat() * 0.05F;
            particle.trailG = 0.2F + worldIn.f_46441_.nextFloat() * 0.05F;
            particle.trailB = 0.9F + worldIn.f_46441_.nextFloat() * 0.1F;
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ScarletFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            MagneticOrbitParticle particle = new MagneticOrbitParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.trailR = 0.9F + worldIn.f_46441_.nextFloat() * 0.1F;
            particle.trailG = 0.2F + worldIn.f_46441_.nextFloat() * 0.05F;
            particle.trailB = 0.2F + worldIn.f_46441_.nextFloat() * 0.05F;
            return particle;
        }
    }
}