package yesman.epicfight.client.renderer.patched.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.mesh.DragonMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.LightningRenderHelper;
import yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon.DragonCrystalLinkPhase;
import yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon.EnderDragonPatch;
import yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon.PatchedPhases;

@OnlyIn(Dist.CLIENT)
public class PEnderDragonRenderer extends PatchedEntityRenderer<EnderDragon, EnderDragonPatch, EnderDragonRenderer, DragonMesh> {

    private static final ResourceLocation DRAGON_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon.png");

    private static final ResourceLocation DRAGON_EXPLODING_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");

    public void render(EnderDragon entityIn, EnderDragonPatch entitypatch, EnderDragonRenderer renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        DragonMesh mesh = this.getMesh(entitypatch);
        Armature armature = entitypatch.getArmature();
        poseStack.pushPose();
        this.mulPoseStack(poseStack, armature, entityIn, entitypatch, partialTicks);
        OpenMatrix4f[] poses = this.getPoseMatrices(entitypatch, armature, partialTicks);
        poses[0] = OpenMatrix4f.rotate(-90.0F, Vec3f.X_AXIS, poses[0], null);
        if (entityIn.dragonDeathTime > 0) {
            poseStack.translate(entityIn.m_217043_().nextGaussian() * 0.08, 0.0, entityIn.m_217043_().nextGaussian() * 0.08);
            float deathTimeProgression = ((float) entityIn.dragonDeathTime + partialTicks) / 200.0F;
            VertexConsumer builder = buffer.getBuffer(EpicFightRenderTypes.triangles(RenderType.dragonExplosionAlpha(DRAGON_EXPLODING_LOCATION)));
            mesh.drawModelWithPose(poseStack, builder, packedLight, 1.0F, 1.0F, 1.0F, deathTimeProgression, OverlayTexture.NO_OVERLAY, armature, poses);
            VertexConsumer builder2 = buffer.getBuffer(EpicFightRenderTypes.triangles(RenderType.entityDecal(DRAGON_LOCATION)));
            mesh.drawModelWithPose(poseStack, builder2, packedLight, 1.0F, 1.0F, 1.0F, 1.0F, this.getOverlayCoord(entityIn, entitypatch, partialTicks), armature, poses);
        } else {
            VertexConsumer builder = buffer.getBuffer(EpicFightRenderTypes.triangles(RenderType.entityCutoutNoCull(DRAGON_LOCATION)));
            mesh.drawModelWithPose(poseStack, builder, packedLight, 1.0F, 1.0F, 1.0F, 1.0F, this.getOverlayCoord(entityIn, entitypatch, partialTicks), armature, poses);
        }
        if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            for (Layer.Priority priority : Layer.Priority.HIGHEST.lowers()) {
                AnimationPlayer animPlayer = entitypatch.getClientAnimator().getCompositeLayer(priority).animationPlayer;
                float playTime = animPlayer.getPrevElapsedTime() + (animPlayer.getElapsedTime() - animPlayer.getPrevElapsedTime()) * partialTicks;
                animPlayer.getAnimation().renderDebugging(poseStack, buffer, entitypatch, playTime, partialTicks);
            }
        }
        poseStack.popPose();
        if (entityIn.nearestCrystal != null) {
            float x = (float) (entityIn.nearestCrystal.m_20185_() - Mth.lerp((double) partialTicks, entityIn.f_19854_, entityIn.m_20185_()));
            float y = (float) (entityIn.nearestCrystal.m_20186_() - Mth.lerp((double) partialTicks, entityIn.f_19855_, entityIn.m_20186_()));
            float z = (float) (entityIn.nearestCrystal.m_20189_() - Mth.lerp((double) partialTicks, entityIn.f_19856_, entityIn.m_20189_()));
            poseStack.pushPose();
            EnderDragonRenderer.renderCrystalBeams(x, y + EndCrystalRenderer.getY(entityIn.nearestCrystal, partialTicks), z, partialTicks, entityIn.f_19797_, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
        if (entityIn.dragonDeathTime > 0) {
            float deathTimeProgression = ((float) entityIn.dragonDeathTime + partialTicks) / 200.0F;
            VertexConsumer lightningBuffer = buffer.getBuffer(RenderType.lightning());
            int density = (int) ((deathTimeProgression + deathTimeProgression * deathTimeProgression) / 2.0F * 60.0F);
            float f7 = Math.min(deathTimeProgression > 0.8F ? (deathTimeProgression - 0.8F) / 0.2F : 0.0F, 1.0F);
            poseStack.pushPose();
            LightningRenderHelper.renderCyclingLight(lightningBuffer, poseStack, 255, 0, 255, density, 1.0F, deathTimeProgression, f7);
            poseStack.popPose();
        }
    }

    public void mulPoseStack(PoseStack matStack, Armature armature, EnderDragon entityIn, EnderDragonPatch entitypatch, float partialTicks) {
        OpenMatrix4f modelMatrix;
        if (entitypatch.isGroundPhase() && entitypatch.getOriginal().dragonDeathTime <= 0) {
            modelMatrix = entitypatch.getModelMatrix(partialTicks).scale(-1.0F, 1.0F, -1.0F);
        } else {
            float f = (float) entityIn.getLatencyPos(7, partialTicks)[0];
            float f1 = (float) (entityIn.getLatencyPos(5, partialTicks)[1] - entityIn.getLatencyPos(10, partialTicks)[1]);
            float f2 = entitypatch.getOriginal().dragonDeathTime > 0 ? 0.0F : MathUtils.rotWrap(entityIn.getLatencyPos(5, partialTicks)[0] - entityIn.getLatencyPos(10, partialTicks)[0]);
            modelMatrix = MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f1, f1, f, f, partialTicks, 1.0F, 1.0F, 1.0F).rotateDeg(-f2 * 1.5F, Vec3f.Z_AXIS);
        }
        OpenMatrix4f transpose = new OpenMatrix4f(modelMatrix).transpose();
        MathUtils.translateStack(matStack, modelMatrix);
        MathUtils.rotateStack(matStack, transpose);
        MathUtils.scaleStack(matStack, transpose);
    }

    protected int getOverlayCoord(EnderDragon entity, EnderDragonPatch entitypatch, float partialTicks) {
        DragonPhaseInstance currentPhase = entity.getPhaseManager().getCurrentPhase();
        float chargingTick = 158.0F;
        float progression = currentPhase.getPhase() == PatchedPhases.CRYSTAL_LINK ? (chargingTick - (float) ((DragonCrystalLinkPhase) currentPhase).getChargingCount()) / chargingTick : 0.0F;
        return OverlayTexture.pack(OverlayTexture.u(progression), OverlayTexture.v(entity.f_20916_ > 5 || entity.f_20919_ > 0));
    }

    public DragonMesh getMesh(EnderDragonPatch entitypatch) {
        return Meshes.DRAGON;
    }
}