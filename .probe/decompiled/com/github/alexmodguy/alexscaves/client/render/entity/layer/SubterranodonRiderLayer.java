package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.SubterranodonModel;
import com.github.alexmodguy.alexscaves.client.render.entity.SubterranodonRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
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
import net.minecraft.world.phys.Vec3;

public class SubterranodonRiderLayer extends RenderLayer<SubterranodonEntity, SubterranodonModel> {

    public SubterranodonRiderLayer(SubterranodonRenderer render) {
        super(render);
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, SubterranodonEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float bodyYaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks;
        if (entity.m_20160_()) {
            float flight = entity.getFlyProgress(partialTicks) - entity.getHoverProgress(partialTicks);
            float flightRoll = flight * entity.getFlightRoll(partialTicks);
            Vec3 offset = new Vec3(0.0, 0.25, 0.5);
            Vec3 centerLegPos = ((SubterranodonModel) this.m_117386_()).getLegPosition(true, offset).add(((SubterranodonModel) this.m_117386_()).getLegPosition(false, offset)).scale(0.5);
            for (Entity passenger : entity.m_20197_()) {
                if (passenger != Minecraft.getInstance().player || !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    AlexsCaves.PROXY.releaseRenderingEntity(passenger.getUUID());
                    poseStack.pushPose();
                    poseStack.translate(centerLegPos.x, centerLegPos.y + (double) passenger.getBbHeight() - (double) (1.25F * flight), centerLegPos.z + (double) (2.0F * flight));
                    poseStack.mulPose(Axis.XP.rotationDegrees(70.0F * flight));
                    poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.YN.rotationDegrees(360.0F - bodyYaw + flightRoll));
                    renderPassenger(passenger, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, bufferIn, packedLightIn);
                    poseStack.popPose();
                    AlexsCaves.PROXY.blockRenderingEntity(passenger.getUUID());
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