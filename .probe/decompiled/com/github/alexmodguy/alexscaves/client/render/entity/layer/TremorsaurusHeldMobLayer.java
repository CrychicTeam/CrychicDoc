package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.model.TremorsaurusModel;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorsaurusRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
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

public class TremorsaurusHeldMobLayer extends RenderLayer<TremorsaurusEntity, TremorsaurusModel> {

    public TremorsaurusHeldMobLayer(TremorsaurusRenderer render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TremorsaurusEntity tremorsaurus, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Entity heldMob = tremorsaurus.getHeldMob();
        if (heldMob != null) {
            float riderRot = heldMob.yRotO + (heldMob.getYRot() - heldMob.yRotO) * partialTicks;
            boolean holdSideways = heldMob.getBbHeight() > heldMob.getBbWidth() + 0.2F;
            AlexsCaves.PROXY.releaseRenderingEntity(heldMob.getUUID());
            matrixStackIn.pushPose();
            ((TremorsaurusModel) this.m_117386_()).translateToMouth(matrixStackIn);
            matrixStackIn.translate(0.0F, heldMob.getBbWidth() * 0.35F + 0.2F, -1.0F);
            if (heldMob instanceof SubterranodonEntity subterranodon) {
                matrixStackIn.translate(0.0F, subterranodon.getFlyProgress(partialTicks) * -0.5F, 0.0F);
            }
            if (holdSideways) {
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(riderRot + 180.0F));
            if (!holdSideways) {
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            matrixStackIn.translate(0.0F, -heldMob.getBbHeight() * 0.5F, 0.0F);
            if (!AlexsCaves.PROXY.isFirstPersonPlayer(heldMob)) {
                this.renderEntity(heldMob, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            }
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