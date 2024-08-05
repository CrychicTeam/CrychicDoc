package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.client.model.ModelCachalotWhale;
import com.github.alexthe666.alexsmobs.client.render.RenderCachalotWhale;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
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

public class LayerCachalotWhaleCapturedSquid extends RenderLayer<EntityCachalotWhale, ModelCachalotWhale> {

    public LayerCachalotWhaleCapturedSquid(RenderCachalotWhale render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityCachalotWhale whale, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (whale.hasCaughtSquid() && whale.m_6084_()) {
            Entity squid = whale.getCaughtSquid();
            if (squid != null && squid.isAlive()) {
                boolean rightSquid = !whale.isHoldingSquidLeft();
                float riderRot = squid.yRotO + (squid.getYRot() - squid.yRotO) * partialTicks;
                EntityRenderer render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(squid);
                EntityModel modelBase = null;
                if (render instanceof LivingEntityRenderer) {
                    modelBase = ((LivingEntityRenderer) render).getModel();
                }
                if (modelBase != null) {
                    ClientProxy.currentUnrenderedEntities.remove(squid.getUUID());
                    matrixStackIn.pushPose();
                    this.translateToPouch(matrixStackIn);
                    matrixStackIn.translate(rightSquid ? -1.2F : 1.2F, 0.0F, -3.4F);
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(riderRot + (rightSquid ? -90.0F : 90.0F)));
                    this.renderEntity(squid, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                    matrixStackIn.popPose();
                    ClientProxy.currentUnrenderedEntities.add(squid.getUUID());
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
        ((ModelCachalotWhale) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelCachalotWhale) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelCachalotWhale) this.m_117386_()).head.translateAndRotate(matrixStack);
        ((ModelCachalotWhale) this.m_117386_()).jaw.translateAndRotate(matrixStack);
    }
}