package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DinosaurTransformParticle extends AbstractTrailParticle {

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/trail.png");

    private int dinosaurId;

    private float lastDinosaurWidth;

    private float initialWidth;

    private float initialYRot;

    private float rotateByAge;

    private float initialOrbitHeight = -1.0F;

    public DinosaurTransformParticle(ClientLevel world, double x, double y, double z, int dinosaurId) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.dinosaurId = dinosaurId;
        this.f_107226_ = 0.0F;
        this.f_107225_ = 20 + this.f_107223_.nextInt(20);
        this.initialYRot = this.f_107223_.nextFloat() * 360.0F;
        this.rotateByAge = (10.0F + this.f_107223_.nextFloat() * 20.0F) * (this.f_107223_.nextBoolean() ? -1.0F : 1.0F);
        Vec3 vec3 = this.getOrbitPosition();
        this.f_107212_ = this.f_107209_ = vec3.x;
        this.f_107213_ = this.f_107210_ = vec3.y;
        this.f_107214_ = this.f_107211_ = vec3.z;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
    }

    public Vec3 getDinosaurPosition() {
        if (this.dinosaurId != -1 && this.f_107208_.getEntity(this.dinosaurId) instanceof DinosaurEntity dinosaur) {
            this.lastDinosaurWidth = dinosaur.m_20205_();
            if (this.initialOrbitHeight == -1.0F) {
                this.initialOrbitHeight = this.f_107223_.nextFloat() * dinosaur.m_20206_();
                this.initialWidth = this.lastDinosaurWidth + this.f_107223_.nextFloat();
            }
            return dinosaur.m_20182_();
        } else {
            return new Vec3(this.f_107212_, this.f_107213_, this.f_107214_);
        }
    }

    public Vec3 getOrbitPosition() {
        Vec3 dinoPos = this.getDinosaurPosition();
        Vec3 vec3 = new Vec3(0.0, (double) this.initialOrbitHeight, (double) this.initialWidth).yRot((float) Math.toRadians((double) (this.initialYRot + this.rotateByAge * (float) this.f_107224_)));
        return dinoPos.add(vec3);
    }

    @Override
    public void tick() {
        super.tick();
        float fade = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        this.trailA = 1.0F * fade;
        Vec3 vec3 = this.getOrbitPosition();
        this.f_107212_ = vec3.x;
        this.f_107213_ = vec3.y;
        this.f_107214_ = vec3.z;
    }

    @Override
    public int sampleCount() {
        return 4;
    }

    @Override
    public int sampleStep() {
        return 1;
    }

    @Override
    public float getTrailHeight() {
        return 0.5F;
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
    public static class AmberFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DinosaurTransformParticle particle = new DinosaurTransformParticle(worldIn, x, y, z, (int) xSpeed);
            particle.trailR = 1.0F;
            particle.trailG = 0.69F + worldIn.f_46441_.nextFloat() * 0.025F;
            particle.trailB = 0.11F + worldIn.f_46441_.nextFloat() * 0.025F;
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TectonicFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DinosaurTransformParticle particle = new DinosaurTransformParticle(worldIn, x, y, z, (int) xSpeed);
            particle.trailR = 0.9F + worldIn.f_46441_.nextFloat() * 0.05F;
            particle.trailG = 0.1F;
            particle.trailB = 0.1F;
            return particle;
        }
    }
}