package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MagneticCavesAmbientParticle extends Particle {

    protected MagneticCavesAmbientParticle(ClientLevel clientLevel, double x, double y, double z) {
        super(clientLevel, x, y, z);
        this.f_107225_ = 1;
        this.f_107226_ = 0.0F;
    }

    @Override
    public void tick() {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (!(this.f_107223_.nextFloat() > 0.85F) || entity != null && !(entity.distanceToSqr(this.f_107212_, this.f_107213_, this.f_107214_) > 25.0)) {
            this.f_107208_.addParticle(ACParticleRegistry.GALENA_DEBRIS.get(), this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
        } else {
            Vec3 offset = new Vec3((double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F)).scale(30.0);
            Vec3 startPos = getStartPosition(this.f_107208_, this.f_107223_.nextBoolean(), this.f_107212_ + offset.x, this.f_107213_ + offset.y, this.f_107214_ + offset.z);
            this.f_107208_.addParticle(ACParticleRegistry.MAGNET_LIGHTNING.get(), startPos.x, startPos.y, startPos.z, 0.0, 0.0, 0.0);
        }
        super.tick();
        this.m_107274_();
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    public static Vec3 getStartPosition(ClientLevel level, boolean goUp, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        if (goUp) {
            while (pos.m_123342_() < level.m_151558_() && level.m_8055_(pos).m_60795_()) {
                pos = pos.above();
            }
        } else {
            while (pos.m_123342_() > level.m_141937_() && level.m_8055_(pos).m_60795_()) {
                pos = pos.below();
            }
        }
        return Vec3.atBottomCenterOf(pos).add(0.0, goUp ? 0.0 : 1.0, 0.0);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MagneticCavesAmbientParticle(worldIn, x, y, z);
        }
    }
}