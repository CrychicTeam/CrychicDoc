package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.RelicheirusModel;
import com.github.alexmodguy.alexscaves.client.render.entity.RelicheirusRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TrilocarisEntity;
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

public class RelicheirusHeldTrilocarisLayer extends RenderLayer<RelicheirusEntity, RelicheirusModel> {

    public RelicheirusHeldTrilocarisLayer(RelicheirusRenderer render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, RelicheirusEntity relicheirus, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Entity heldMob = relicheirus.getHeldMob();
        if (heldMob instanceof TrilocarisEntity && relicheirus.getAnimation() == RelicheirusEntity.ANIMATION_EAT_TRILOCARIS && relicheirus.getAnimationTick() > 15) {
            float riderRot = heldMob.yRotO + (heldMob.getYRot() - heldMob.yRotO) * partialTicks;
            AlexsCaves.PROXY.releaseRenderingEntity(heldMob.getUUID());
            matrixStackIn.pushPose();
            ((RelicheirusModel) this.m_117386_()).translateToMouth(matrixStackIn);
            matrixStackIn.translate(0.0F, -1.34F, -1.0F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(riderRot + 180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            matrixStackIn.translate(0.0F, -heldMob.getBbHeight() * 0.5F, 0.0F);
            this.renderEntity(heldMob, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            AlexsCaves.PROXY.blockRenderingEntity(heldMob.getUUID());
        }
    }

    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable var19) {
                    throw new ReportedException(CrashReport.forThrowable(var19, "Rendering entity in world"));
                }
            }
        } catch (Throwable var20) {
            CrashReport crashreport = CrashReport.forThrowable(var20, "Rendering entity in world");
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