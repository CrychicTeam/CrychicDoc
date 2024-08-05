package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StunStarParticle extends AbstractTrailParticle {

    private static final ResourceLocation CENTER_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/stun_star.png");

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/teletor_trail.png");

    private final int entityId;

    private final float offset;

    private boolean reverseOrbit;

    protected StunStarParticle(ClientLevel world, double x, double y, double z, int entityId, float offset) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.offset = offset;
        this.f_107217_ = 0.0;
        this.f_107230_ = 1.0F;
        this.entityId = entityId;
        this.f_107219_ = false;
        this.reverseOrbit = this.f_107223_.nextBoolean();
        this.f_107225_ = 30 + this.f_107223_.nextInt(5);
        Vec3 vec3 = this.getOrbitPos();
        this.m_107264_(vec3.x, vec3.y, vec3.z);
        this.f_107209_ = x;
        this.f_107210_ = y;
        this.f_107211_ = z;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        if (this.f_107224_ >= 2) {
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
            VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(ACRenderTypes.m_110467_(CENTER_TEXTURE));
            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.rotate(quaternion);
            Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
            float f4 = 0.3F;
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
            float alpha = 1.0F;
            int j = 240;
            PoseStack posestack = new PoseStack();
            PoseStack.Pose posestack$pose = posestack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertexconsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, alpha).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, alpha).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, alpha).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, alpha).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            multibuffersource$buffersource.endBatch();
            super.render(vertexConsumer, camera, partialTick);
        }
    }

    @Override
    public void tick() {
        this.f_107215_ *= 0.9;
        this.f_107216_ *= 0.9;
        this.f_107217_ *= 0.9;
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = (float) ((float) Math.PI * Math.sin((double) ((float) this.f_107224_ * 0.6F)) * 0.3F);
        super.tick();
        this.trailA = 0.2F * Mth.clamp((float) this.f_107224_ / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
        if (this.entityId != -1) {
            Vec3 orbit = this.getOrbitPos();
            this.m_107264_(orbit.x, orbit.y, orbit.z);
            if (this.f_107208_.getEntity(this.entityId) instanceof LivingEntity living && !living.hasEffect(ACEffectRegistry.STUNNED.get())) {
                this.m_107274_();
            }
        }
    }

    public Vec3 getOrbitPos() {
        Entity entity = this.f_107208_.getEntity(this.entityId);
        if (entity != null) {
            float angle = (float) (this.f_107224_ * 10) + this.offset;
            Vec3 eyes;
            Vec3 orbitOffset;
            if (entity instanceof SauropodBaseEntity sauropod) {
                eyes = sauropod.headPart.m_20182_().add(0.0, 1.0, 0.0);
                orbitOffset = new Vec3(0.0, 0.0, 1.9F).yRot(angle * (float) (this.reverseOrbit ? -1 : 1) * (float) (Math.PI / 180.0));
            } else {
                eyes = entity.getEyePosition().add(entity.getViewVector(0.0F).scale((double) (entity.getBbWidth() * 0.85F))).add(0.0, 0.5 + 0.12 * (double) entity.getBbHeight(), 0.0);
                orbitOffset = new Vec3(0.0, 0.0, (double) (entity.getBbWidth() * 0.5F + 0.2F)).yRot(angle * (float) (this.reverseOrbit ? -1 : 1) * (float) (Math.PI / 180.0));
            }
            return eyes.add(orbitOffset);
        } else {
            return Vec3.ZERO;
        }
    }

    @Override
    public float getTrailHeight() {
        return 0.4F;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    @Override
    public int sampleCount() {
        return Math.min(10, this.f_107225_ - this.f_107224_);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            StunStarParticle particle = new StunStarParticle(worldIn, x, y, z, (int) xSpeed, (float) ySpeed);
            particle.trailR = 1.0F;
            particle.trailG = 1.0F;
            particle.trailB = 1.0F;
            return particle;
        }
    }
}