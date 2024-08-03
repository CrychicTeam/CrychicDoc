package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TremorzillaProtonParticle extends ProtonParticle {

    private static final ResourceLocation CENTER_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/tremorzilla_proton.png");

    private final int tremorzillaId;

    private final float initialXRot;

    private final float initialYRot;

    protected TremorzillaProtonParticle(ClientLevel world, double x, double y, double z, int entityId) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107225_ = 20;
        this.orbitOffset = new Vec3(0.0, 0.0, (double) (1.0F + this.f_107223_.nextFloat() * 0.2F));
        this.orbitSpeed = 10.0F;
        this.tremorzillaId = entityId;
        this.setInMouthPos(1.0F);
        this.m_107264_(this.orbitX, this.orbitY, this.orbitZ);
        this.initialXRot = this.f_107223_.nextFloat() * 180.0F;
        this.initialYRot = this.f_107223_.nextFloat() * 180.0F;
    }

    @Override
    public ResourceLocation getTexture() {
        return CENTER_TEXTURE;
    }

    @Override
    public float getTrailHeight() {
        return 0.5F;
    }

    @Override
    public void tick() {
        this.setInMouthPos(1.0F);
        super.tick();
        float fadeIn = 0.8F * Mth.clamp((float) this.f_107224_ / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
        float fadeOut = Mth.clamp(1.0F - (float) this.f_107224_ / (float) this.f_107225_ * 0.5F, 0.0F, 1.0F);
        this.trailA = fadeIn * fadeOut;
    }

    @Override
    public float getAlpha() {
        return this.f_107224_ < 2 ? 0.0F : 1.0F;
    }

    @Override
    public Vec3 getOrbitPosition(float angle) {
        Vec3 center = new Vec3(this.orbitX, this.orbitY, this.orbitZ);
        Vec3 add = this.orbitOffset.scale(this.orbitDistance).yRot((float) Math.toRadians((double) this.initialYRot)).xRot((float) Math.toRadians((double) this.initialXRot));
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

    public void setInMouthPos(float partialTick) {
        if (this.tremorzillaId != -1 && this.f_107208_.getEntity(this.tremorzillaId) instanceof TremorzillaEntity entity) {
            Vec3 mouthPos = TremorzillaRenderer.getMouthPositionFor(this.tremorzillaId);
            if (mouthPos != null) {
                Vec3 translate = mouthPos.yRot((float) (Math.PI - (double) (entity.f_20883_ * (float) (Math.PI / 180.0))));
                Vec3 newOrbit = new Vec3(entity.m_20185_() + translate.x, entity.m_20186_() + translate.y, entity.m_20189_() + translate.z);
                this.orbitX = newOrbit.x;
                this.orbitY = newOrbit.y;
                this.orbitZ = newOrbit.z;
            }
            if (entity.getBeamProgress(1.0F) <= 0.0F) {
                this.m_107274_();
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TremorzillaProtonParticle particle = new TremorzillaProtonParticle(worldIn, x, y, z, (int) xSpeed);
            particle.m_107253_(0.0F, 1.0F, 0.0F);
            particle.trailR = 0.0F;
            particle.trailG = 1.0F;
            particle.trailB = 0.0F;
            return particle;
        }
    }

    public static class RetroFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TremorzillaProtonParticle particle = new TremorzillaProtonParticle(worldIn, x, y, z, (int) xSpeed);
            particle.m_107253_(0.5F, 0.2F, 1.0F);
            particle.trailR = 0.5F;
            particle.trailG = 0.2F;
            particle.trailB = 1.0F;
            return particle;
        }
    }

    public static class TectonicFactory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TremorzillaProtonParticle particle = new TremorzillaProtonParticle(worldIn, x, y, z, (int) xSpeed);
            particle.m_107253_(1.0F, 0.85F, 0.15F);
            particle.trailR = 1.0F;
            particle.trailG = 0.85F;
            particle.trailB = 0.15F;
            return particle;
        }
    }
}