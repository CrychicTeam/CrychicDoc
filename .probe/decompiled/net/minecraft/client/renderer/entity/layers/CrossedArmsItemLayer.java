package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrossedArmsItemLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final ItemInHandRenderer itemInHandRenderer;

    public CrossedArmsItemLayer(RenderLayerParent<T, M> renderLayerParentTM0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentTM0);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        poseStack0.pushPose();
        poseStack0.translate(0.0F, 0.4F, -0.4F);
        poseStack0.mulPose(Axis.XP.rotationDegrees(180.0F));
        ItemStack $$10 = t3.getItemBySlot(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(t3, $$10, ItemDisplayContext.GROUND, false, poseStack0, multiBufferSource1, int2);
        poseStack0.popPose();
    }
}