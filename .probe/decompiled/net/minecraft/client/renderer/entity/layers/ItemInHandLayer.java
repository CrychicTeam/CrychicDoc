package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {

    private final ItemInHandRenderer itemInHandRenderer;

    public ItemInHandLayer(RenderLayerParent<T, M> renderLayerParentTM0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentTM0);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        boolean $$10 = t3.getMainArm() == HumanoidArm.RIGHT;
        ItemStack $$11 = $$10 ? t3.getOffhandItem() : t3.getMainHandItem();
        ItemStack $$12 = $$10 ? t3.getMainHandItem() : t3.getOffhandItem();
        if (!$$11.isEmpty() || !$$12.isEmpty()) {
            poseStack0.pushPose();
            if (this.m_117386_().young) {
                float $$13 = 0.5F;
                poseStack0.translate(0.0F, 0.75F, 0.0F);
                poseStack0.scale(0.5F, 0.5F, 0.5F);
            }
            this.renderArmWithItem(t3, $$12, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack0, multiBufferSource1, int2);
            this.renderArmWithItem(t3, $$11, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack0, multiBufferSource1, int2);
            poseStack0.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity livingEntity0, ItemStack itemStack1, ItemDisplayContext itemDisplayContext2, HumanoidArm humanoidArm3, PoseStack poseStack4, MultiBufferSource multiBufferSource5, int int6) {
        if (!itemStack1.isEmpty()) {
            poseStack4.pushPose();
            ((ArmedModel) this.m_117386_()).translateToHand(humanoidArm3, poseStack4);
            poseStack4.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack4.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean $$7 = humanoidArm3 == HumanoidArm.LEFT;
            poseStack4.translate((float) ($$7 ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            this.itemInHandRenderer.renderItem(livingEntity0, itemStack1, itemDisplayContext2, $$7, poseStack4, multiBufferSource5, int6);
            poseStack4.popPose();
        }
    }
}