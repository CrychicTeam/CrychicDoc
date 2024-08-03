package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.HullbreakerModel;
import com.github.alexmodguy.alexscaves.client.model.NotorModel;
import com.github.alexmodguy.alexscaves.client.model.SauropodBaseModel;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.model.UnderzealotModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.FerrouslimeEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class NotorRenderer extends MobRenderer<NotorEntity, NotorModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/notor.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/notor_glow.png");

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("alexscaves:textures/entity/notor_eyes.png");

    private static final List<NotorEntity> allOnScreen = new ArrayList();

    public NotorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new NotorModel(), 0.25F);
        this.m_115326_(new NotorRenderer.LayerGlow());
    }

    public boolean shouldRender(NotorEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else if (entity.getBeamProgress(1.0F) > 0.0F) {
            Vec3 vec3 = entity.m_20182_();
            Vec3 vec31 = entity.getBeamEndPosition(1.0F);
            return camera.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
        } else {
            return false;
        }
    }

    protected void scale(NotorEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public void render(NotorEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, source, packedLight);
        Vec3 renderAt = entity.m_20318_(partialTicks);
        if (entity.m_6084_()) {
            Entity hologramEntity = entity.getHologramEntity();
            boolean scanning = !entity.showingHologram();
            Vec3 hologramScanPos = entity.getBeamEndPosition(partialTicks);
            float beamProgress = entity.getBeamProgress(partialTicks);
            if (hologramEntity != null && entity.showingHologram()) {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
                poseStack.pushPose();
                poseStack.translate(hologramScanPos.x - renderAt.x, hologramScanPos.y - renderAt.y, hologramScanPos.z - renderAt.z);
                poseStack.scale(1.0F, entity.getHologramProgress(partialTicks), 1.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(((float) entity.f_19797_ + partialTicks) * 3.0F));
                renderEntityInHologram(hologramEntity, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, source, 240);
                poseStack.popPose();
            }
            if (hologramScanPos != null) {
                Vec3 eyeOffset = entity.m_20252_(1.0F).scale(0.1F);
                Vec3 modelOffset = ((NotorModel) this.f_115290_).getChainPosition(Vec3.ZERO).add(eyeOffset);
                Vec3 toTranslate = hologramScanPos.subtract(entity.m_20318_(partialTicks).add(modelOffset));
                float yRot = (float) Mth.atan2(toTranslate.x, toTranslate.z) * 180.0F / (float) Math.PI;
                float xRot = -((float) (Mth.atan2(toTranslate.y, toTranslate.horizontalDistance()) * 180.0F / (float) Math.PI));
                float length = ((float) toTranslate.length() - (float) (scanning ? 0 : 0)) * beamProgress;
                float width = hologramEntity == null ? 1.3F : hologramEntity.getBbHeight() / 2.0F;
                poseStack.pushPose();
                poseStack.translate(modelOffset.x, modelOffset.y, modelOffset.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(xRot));
                poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
                if (scanning) {
                    poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
                }
                PoseStack.Pose posestack$pose = poseStack.last();
                Matrix4f matrix4f1 = posestack$pose.pose();
                Matrix3f matrix3f1 = posestack$pose.normal();
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
                VertexConsumer lightConsumer = source.getBuffer(ACRenderTypes.getHologramLights());
                shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
                shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
                shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
                shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
                poseStack.popPose();
            }
        }
    }

    public static <E extends Entity> void renderEntityInHologram(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            float animSpeed = 0.0F;
            float animSpeedOld = 0.0F;
            float animPos = 0.0F;
            float xRot = entityIn.getXRot();
            float xRotOld = entityIn.xRotO;
            float yRot = entityIn.getYRot();
            float yRotOld = entityIn.yRotO;
            float yBodyRot = 0.0F;
            float yBodyRotOld = 0.0F;
            float headRot = 0.0F;
            float headRotOld = 0.0F;
            if (entityIn instanceof LivingEntity living) {
                label79: {
                    headRot = living.yHeadRot;
                    headRotOld = living.yHeadRotO;
                    yBodyRot = living.yBodyRot;
                    yBodyRotOld = living.yBodyRotO;
                    living.yHeadRot = 0.0F;
                    living.yHeadRotO = 0.0F;
                    living.yBodyRot = 0.0F;
                    living.yBodyRotO = 0.0F;
                    entityIn.setXRot(0.0F);
                    entityIn.xRotO = 0.0F;
                    entityIn.setYRot(0.0F);
                    entityIn.yRotO = 0.0F;
                    if (render instanceof LivingEntityRenderer renderer && renderer.getModel() != null) {
                        EntityModel model = renderer.getModel();
                        VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getHologram(entityIn instanceof DeepOneMageEntity ? DeepOneMageRenderer.TEXTURE : render.getTextureLocation(entityIn)));
                        matrixStack.pushPose();
                        boolean shouldSit = entityIn.isPassenger() && entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit();
                        model.young = living.isBaby();
                        model.riding = shouldSit;
                        model.attackTime = living.getAttackAnim(partialTicks);
                        boolean prevCrouching = false;
                        if (model instanceof HumanoidModel<?> humanoidModel) {
                            prevCrouching = humanoidModel.crouching;
                            humanoidModel.crouching = false;
                        }
                        if (model instanceof UnderzealotModel underzealotModel) {
                            underzealotModel.noBurrowing = true;
                        }
                        if (model instanceof HullbreakerModel hullbreakerModel) {
                            hullbreakerModel.straighten = true;
                        }
                        if (model instanceof SauropodBaseModel sauropodBaseModel) {
                            sauropodBaseModel.straighten = true;
                        }
                        if (model instanceof TremorzillaModel tremorzillaModel) {
                            tremorzillaModel.straighten = true;
                        }
                        model.setupAnim(living, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
                        if (model instanceof UnderzealotModel underzealotModel) {
                            underzealotModel.noBurrowing = false;
                        }
                        if (model instanceof HullbreakerModel hullbreakerModel) {
                            hullbreakerModel.straighten = false;
                        }
                        if (model instanceof SauropodBaseModel sauropodBaseModel) {
                            sauropodBaseModel.straighten = false;
                        }
                        if (model instanceof TremorzillaModel tremorzillaModel) {
                            tremorzillaModel.straighten = false;
                        }
                        matrixStack.scale(-living.getScale(), -living.getScale(), living.getScale());
                        ((LivingEntityRendererAccessor) renderer).scaleForHologram(living, matrixStack, partialTicks);
                        model.m_7695_(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                        matrixStack.popPose();
                        if (model instanceof HumanoidModel<?> humanoidModel) {
                            humanoidModel.crouching = prevCrouching;
                        }
                        break label79;
                    }
                    if (render instanceof FerrouslimeRenderer && living instanceof FerrouslimeEntity ferrouslime) {
                        matrixStack.pushPose();
                        matrixStack.translate(0.0F, -1.0F, 0.0F);
                        matrixStack.scale(-living.getScale(), -living.getScale(), living.getScale());
                        VertexConsumer ivertexbuilderx = bufferIn.getBuffer(ACRenderTypes.getHologram(render.getTextureLocation(entityIn)));
                        FerrouslimeRenderer.FERROUSLIME_MODEL.setupAnim(ferrouslime, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
                        FerrouslimeRenderer.FERROUSLIME_MODEL.m_7695_(matrixStack, ivertexbuilderx, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                        matrixStack.popPose();
                    }
                }
                entityIn.setXRot(xRot);
                entityIn.xRotO = xRotOld;
                entityIn.setYRot(yRot);
                entityIn.yRotO = yRotOld;
                living.yHeadRot = headRot;
                living.yHeadRotO = headRotOld;
                living.yBodyRot = yBodyRot;
                living.yBodyRotO = yBodyRotOld;
            }
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        } catch (Throwable var33) {
            CrashReport crashreport = CrashReport.forThrowable(var33, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 230).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 0, 255, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 0, 255, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(NotorEntity entity) {
        return TEXTURE;
    }

    class LayerGlow extends RenderLayer<NotorEntity, NotorModel> {

        public LayerGlow() {
            super(NotorRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NotorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getGhostly(NotorRenderer.TEXTURE_GLOW));
            float alpha = 1.0F;
            ((NotorModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
            VertexConsumer ivertexbuilder2;
            if (entitylivingbaseIn.getBeamProgress(partialTicks) > 0.0F) {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
                ivertexbuilder2 = bufferIn.getBuffer(ACRenderTypes.getHologram(NotorRenderer.TEXTURE_EYES));
            } else {
                ivertexbuilder2 = bufferIn.getBuffer(RenderType.eyes(NotorRenderer.TEXTURE_EYES));
            }
            ((NotorModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}