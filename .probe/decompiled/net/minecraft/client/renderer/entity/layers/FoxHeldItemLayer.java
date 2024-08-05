package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class FoxHeldItemLayer extends RenderLayer<Fox, FoxModel<Fox>> {

    private final ItemInHandRenderer itemInHandRenderer;

    public FoxHeldItemLayer(RenderLayerParent<Fox, FoxModel<Fox>> renderLayerParentFoxFoxModelFox0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentFoxFoxModelFox0);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Fox fox3, float float4, float float5, float float6, float float7, float float8, float float9) {
        boolean $$10 = fox3.isSleeping();
        boolean $$11 = fox3.m_6162_();
        poseStack0.pushPose();
        if ($$11) {
            float $$12 = 0.75F;
            poseStack0.scale(0.75F, 0.75F, 0.75F);
            poseStack0.translate(0.0F, 0.5F, 0.209375F);
        }
        poseStack0.translate(((FoxModel) this.m_117386_()).head.x / 16.0F, ((FoxModel) this.m_117386_()).head.y / 16.0F, ((FoxModel) this.m_117386_()).head.z / 16.0F);
        float $$13 = fox3.getHeadRollAngle(float6);
        poseStack0.mulPose(Axis.ZP.rotation($$13));
        poseStack0.mulPose(Axis.YP.rotationDegrees(float8));
        poseStack0.mulPose(Axis.XP.rotationDegrees(float9));
        if (fox3.m_6162_()) {
            if ($$10) {
                poseStack0.translate(0.4F, 0.26F, 0.15F);
            } else {
                poseStack0.translate(0.06F, 0.26F, -0.5F);
            }
        } else if ($$10) {
            poseStack0.translate(0.46F, 0.26F, 0.22F);
        } else {
            poseStack0.translate(0.06F, 0.27F, -0.5F);
        }
        poseStack0.mulPose(Axis.XP.rotationDegrees(90.0F));
        if ($$10) {
            poseStack0.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
        ItemStack $$14 = fox3.m_6844_(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(fox3, $$14, ItemDisplayContext.GROUND, false, poseStack0, multiBufferSource1, int2);
        poseStack0.popPose();
    }
}