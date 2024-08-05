package com.github.alexthe666.alexsmobs.client.particle;

import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleStaticSpark extends Particle {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/particle/generic_0.png"), new ResourceLocation("textures/particle/generic_1.png"), new ResourceLocation("textures/particle/generic_2.png"), new ResourceLocation("textures/particle/generic_3.png"), new ResourceLocation("textures/particle/generic_4.png"), new ResourceLocation("textures/particle/generic_5.png"), new ResourceLocation("textures/particle/generic_6.png"), new ResourceLocation("textures/particle/generic_7.png") };

    private int decrement = 1;

    private int textureIndex = 0;

    private float size;

    private ParticleStaticSpark(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.m_107250_(1.0F, 1.0F);
        this.f_107226_ = 0.0F;
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
        this.f_107225_ = 10 + this.f_107223_.nextInt(15);
        this.textureIndex = Mth.clamp(this.f_107223_.nextInt(8), 0, 7);
        this.decrement = this.textureIndex > 0 ? this.f_107225_ / this.textureIndex : this.f_107225_;
        this.size = this.f_107223_.nextFloat() * 0.2F + 0.2F;
    }

    @Override
    public void tick() {
        super.tick();
        this.f_107215_ *= 0.97;
        this.f_107216_ *= 0.97;
        this.f_107217_ *= 0.97;
        if (this.textureIndex > 0 && this.f_107224_ % this.decrement == 0) {
            this.textureIndex--;
        }
        if (this.size > 0.2F) {
            this.size -= 0.015F;
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTick, this.f_107204_, this.f_107231_);
            quaternion.mul(Axis.ZP.rotation(f3));
        }
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer portalStatic = AMRenderTypes.createMergedVertexConsumer(multibuffersource$buffersource.getBuffer(AMRenderTypes.STATIC_PARTICLE), multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(TEXTURES[this.textureIndex])));
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f4 = this.size;
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0.0F;
        float f8 = 1.0F;
        float f5 = 0.0F;
        float f6 = 1.0F;
        int j = 240;
        portalStatic.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.endBatch();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleStaticSpark(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}