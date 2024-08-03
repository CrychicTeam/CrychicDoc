package com.simibubi.create.content.kinetics.steamEngine;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SteamJetParticle extends SimpleAnimatedParticle {

    private float yaw;

    private float pitch;

    protected SteamJetParticle(ClientLevel world, SteamJetParticleData data, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprite) {
        super(world, x, y, z, sprite, world.f_46441_.nextFloat() * 0.5F);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107226_ = 0.0F;
        this.f_107663_ = 0.375F;
        this.m_107257_(21);
        this.m_107264_(x, y, z);
        this.f_107231_ = this.f_107204_ = world.f_46441_.nextFloat() * (float) Math.PI;
        this.yaw = (float) Mth.atan2(dx, dz) - (float) Math.PI;
        this.pitch = (float) Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) - (float) (Math.PI / 2);
        this.m_108339_(sprite);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float) (this.f_107212_ - vec3.x);
        float f1 = (float) (this.f_107213_ - vec3.y);
        float f2 = (float) (this.f_107214_ - vec3.z);
        float f3 = Mth.lerp(pPartialTicks, this.f_107204_, this.f_107231_);
        float f7 = this.m_5970_();
        float f8 = this.m_5952_();
        float f5 = this.m_5951_();
        float f6 = this.m_5950_();
        float f4 = this.m_5902_(pPartialTicks);
        for (int i = 0; i < 4; i++) {
            Quaternionf quaternion = Axis.YP.rotation(this.yaw);
            quaternion.mul(Axis.XP.rotation(this.pitch));
            quaternion.mul(Axis.YP.rotation(f3 + (float) (Math.PI / 2) * (float) i + this.f_107231_));
            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.rotate(quaternion);
            Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
            for (int j = 0; j < 4; j++) {
                Vector3f vector3f = avector3f[j];
                vector3f.add(0.0F, 1.0F, 0.0F);
                vector3f.rotate(quaternion);
                vector3f.mul(f4);
                vector3f.add(f, f1, f2);
            }
            int j = this.getLightColor(pPartialTicks);
            pBuffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            pBuffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            pBuffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            pBuffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        BlockPos blockpos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        return this.f_107208_.m_46749_(blockpos) ? LevelRenderer.getLightColor(this.f_107208_, blockpos) : 0;
    }

    public static class Factory implements ParticleProvider<SteamJetParticleData> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet animatedSprite) {
            this.spriteSet = animatedSprite;
        }

        public Particle createParticle(SteamJetParticleData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SteamJetParticle(worldIn, data, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}