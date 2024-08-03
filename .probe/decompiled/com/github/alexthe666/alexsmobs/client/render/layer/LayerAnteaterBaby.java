package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.client.model.ModelAnteater;
import com.github.alexthe666.alexsmobs.client.render.RenderAnteater;
import com.github.alexthe666.alexsmobs.entity.EntityAnteater;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

public class LayerAnteaterBaby extends RenderLayer<EntityAnteater, ModelAnteater> {

    public LayerAnteaterBaby(RenderAnteater render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityAnteater roo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (roo.m_20160_() && !roo.m_6162_()) {
            for (Entity passenger : roo.m_20197_()) {
                float riderRot = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks;
                EntityRenderer render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
                EntityModel modelBase = null;
                if (render instanceof LivingEntityRenderer) {
                    modelBase = ((LivingEntityRenderer) render).getModel();
                }
                if (modelBase != null) {
                    ClientProxy.currentUnrenderedEntities.remove(passenger.getUUID());
                    matrixStackIn.pushPose();
                    this.translateToPouch(matrixStackIn);
                    matrixStackIn.translate(0.0F, -0.12F, 0.1F);
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(riderRot + 180.0F));
                    this.renderEntity(passenger, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                    matrixStackIn.popPose();
                    ClientProxy.currentUnrenderedEntities.add(passenger.getUUID());
                }
            }
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

    protected void translateToPouch(PoseStack matrixStack) {
        ((ModelAnteater) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelAnteater) this.m_117386_()).body.translateAndRotate(matrixStack);
    }
}