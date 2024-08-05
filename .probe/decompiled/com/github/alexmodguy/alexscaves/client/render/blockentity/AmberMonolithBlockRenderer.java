package com.github.alexmodguy.alexscaves.client.render.blockentity;

import com.github.alexmodguy.alexscaves.client.model.SauropodBaseModel;
import com.github.alexmodguy.alexscaves.server.block.blockentity.AmberMonolithBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.ForgeRenderTypes;

public class AmberMonolithBlockRenderer<T extends AmberMonolithBlockEntity> implements BlockEntityRenderer<T> {

    protected final RandomSource random = RandomSource.create();

    public AmberMonolithBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public void render(T amber, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Entity currentEntity = amber.getDisplayEntity(Minecraft.getInstance().level);
        float age = (float) amber.tickCount + partialTicks;
        float spin = amber.getRotation(partialTicks);
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.65F, 0.5F);
        if (currentEntity != null) {
            float f = 0.45F;
            float f1 = Math.max(currentEntity.getBbWidth(), currentEntity.getBbHeight());
            if ((double) f1 > 1.0) {
                f /= f1 * 1.5F;
            }
            poseStack.translate(0.0F, f * 1.5F - 1.25F + (float) (Math.cos((double) age * 0.05) * 0.05F), 0.0F);
            poseStack.scale(f, f, f);
            poseStack.mulPose(Axis.YP.rotationDegrees(spin));
            renderEntityInAmber(currentEntity, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, bufferIn, 1.0F);
        }
        poseStack.popPose();
    }

    public static <E extends Entity> void renderEntityInAmber(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, float transparency) {
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
                if (render instanceof LivingEntityRenderer<?, ?> renderer && renderer.getModel() != null) {
                    EntityModel model = renderer.getModel();
                    VertexConsumer ivertexbuilder = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(render.getTextureLocation(entityIn)));
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
                    if (model instanceof SauropodBaseModel sauropodBaseModel) {
                        sauropodBaseModel.straighten = true;
                    }
                    model.setupAnim(living, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
                    if (model instanceof SauropodBaseModel sauropodBaseModel) {
                        sauropodBaseModel.straighten = false;
                    }
                    matrixStack.scale(living.getScale(), -living.getScale(), living.getScale());
                    model.m_7695_(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 0.3F, 0.16F, 0.2F, transparency);
                    matrixStack.popPose();
                    if (model instanceof HumanoidModel<?> humanoidModel) {
                        humanoidModel.crouching = prevCrouching;
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
        } catch (Throwable var32) {
            CrashReport crashreport = CrashReport.forThrowable(var32, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }
}