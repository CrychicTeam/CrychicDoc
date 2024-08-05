package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

public class TremorzillaRiderLayer extends RenderLayer<TremorzillaEntity, TremorzillaModel> {

    public TremorzillaRiderLayer(TremorzillaRenderer render) {
        super(render);
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, TremorzillaEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float bodyYaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks;
        if (entity.m_20160_()) {
            float swimProgress = entity.m_20998_(partialTicks);
            float burnProgress = entity.getBeamProgress(partialTicks);
            for (Entity passenger : entity.m_20197_()) {
                if (passenger != Minecraft.getInstance().player || !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    poseStack.pushPose();
                    ((TremorzillaModel) this.m_117386_()).translateToNeck(poseStack);
                    poseStack.translate(0.0F, 0.5F - burnProgress * 0.5F - swimProgress * 0.5F, 0.35F - burnProgress * 0.5F - swimProgress * 0.5F);
                    poseStack.mulPose(Axis.XN.rotationDegrees(190.0F - burnProgress * 40.0F));
                    poseStack.mulPose(Axis.YN.rotationDegrees(360.0F - bodyYaw));
                    AlexsCaves.PROXY.releaseRenderingEntity(passenger.getUUID());
                    renderPassenger(passenger, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, bufferIn, packedLightIn);
                    AlexsCaves.PROXY.blockRenderingEntity(passenger.getUUID());
                    poseStack.popPose();
                }
            }
        }
    }

    public static <E extends Entity> void renderPassenger(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable var18) {
                    throw new ReportedException(CrashReport.forThrowable(var18, "Rendering entity in world"));
                }
            }
        } catch (Throwable var19) {
            CrashReport crashreport = CrashReport.forThrowable(var19, "Rendering entity in world");
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