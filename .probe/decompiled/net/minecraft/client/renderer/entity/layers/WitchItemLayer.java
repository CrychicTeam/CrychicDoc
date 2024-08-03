package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WitchItemLayer<T extends LivingEntity> extends CrossedArmsItemLayer<T, WitchModel<T>> {

    public WitchItemLayer(RenderLayerParent<T, WitchModel<T>> renderLayerParentTWitchModelT0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentTWitchModelT0, itemInHandRenderer1);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ItemStack $$10 = t3.getMainHandItem();
        poseStack0.pushPose();
        if ($$10.is(Items.POTION)) {
            ((WitchModel) this.m_117386_()).m_5585_().translateAndRotate(poseStack0);
            ((WitchModel) this.m_117386_()).getNose().translateAndRotate(poseStack0);
            poseStack0.translate(0.0625F, 0.25F, 0.0F);
            poseStack0.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack0.mulPose(Axis.XP.rotationDegrees(140.0F));
            poseStack0.mulPose(Axis.ZP.rotationDegrees(10.0F));
            poseStack0.translate(0.0F, -0.4F, 0.4F);
        }
        super.render(poseStack0, multiBufferSource1, int2, t3, float4, float5, float6, float7, float8, float9);
        poseStack0.popPose();
    }
}