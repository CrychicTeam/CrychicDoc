package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.entity.PatchedEntityRenderer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class EntityAfterImageParticle extends CustomModelParticle<AnimatedMesh> {

    private final OpenMatrix4f[] poseMatrices;

    private final Matrix4f modelMatrix;

    private float alphaO;

    public EntityAfterImageParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, AnimatedMesh particleMesh, OpenMatrix4f[] matrices, Matrix4f modelMatrix) {
        super(level, x, y, z, xd, yd, zd, particleMesh);
        this.poseMatrices = matrices;
        this.modelMatrix = modelMatrix;
        this.f_107225_ = 20;
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
        this.alphaO = 0.3F;
        this.f_107230_ = 0.3F;
    }

    @Override
    public void tick() {
        super.tick();
        this.alphaO = this.f_107230_;
        this.f_107230_ = (float) (this.f_107225_ - this.f_107224_) / (float) this.f_107225_ * 0.8F;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        poseStack.mulPoseMatrix(this.modelMatrix);
        float alpha = this.alphaO + (this.f_107230_ - this.alphaO) * partialTicks;
        this.particleMesh.drawWithPoseNoTexture(poseStack, vertexConsumer, this.m_6355_(partialTicks), this.f_107227_, this.f_107228_, this.f_107229_, alpha, OverlayTexture.NO_OVERLAY, this.poseMatrices);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Entity entity = level.getEntity((int) Double.doubleToLongBits(xSpeed));
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entitypatch != null && ClientEngine.getInstance().renderEngine.hasRendererFor(entitypatch.getOriginal())) {
                PatchedEntityRenderer renderer = ClientEngine.getInstance().renderEngine.getEntityRenderer(entitypatch.getOriginal());
                Armature armature = entitypatch.getArmature();
                PoseStack poseStack = new PoseStack();
                OpenMatrix4f[] matrices = renderer.getPoseMatrices(entitypatch, armature, 1.0F);
                renderer.mulPoseStack(poseStack, armature, entitypatch.getOriginal(), entitypatch, 1.0F);
                for (int i = 0; i < matrices.length; i++) {
                    matrices[i] = OpenMatrix4f.mul(matrices[i], armature.searchJointById(i).getToOrigin(), null);
                }
                AnimatedMesh mesh = ClientEngine.getInstance().renderEngine.getEntityRenderer(entitypatch.getOriginal()).getMesh(entitypatch);
                return new EntityAfterImageParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, mesh, matrices, poseStack.last().pose());
            } else {
                return null;
            }
        }
    }
}