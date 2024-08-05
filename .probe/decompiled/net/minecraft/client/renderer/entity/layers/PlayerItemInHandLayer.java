package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PlayerItemInHandLayer<T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {

    private final ItemInHandRenderer itemInHandRenderer;

    private static final float X_ROT_MIN = (float) (-Math.PI / 6);

    private static final float X_ROT_MAX = (float) (Math.PI / 2);

    public PlayerItemInHandLayer(RenderLayerParent<T, M> renderLayerParentTM0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentTM0, itemInHandRenderer1);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    @Override
    protected void renderArmWithItem(LivingEntity livingEntity0, ItemStack itemStack1, ItemDisplayContext itemDisplayContext2, HumanoidArm humanoidArm3, PoseStack poseStack4, MultiBufferSource multiBufferSource5, int int6) {
        if (itemStack1.is(Items.SPYGLASS) && livingEntity0.getUseItem() == itemStack1 && livingEntity0.swingTime == 0) {
            this.renderArmWithSpyglass(livingEntity0, itemStack1, humanoidArm3, poseStack4, multiBufferSource5, int6);
        } else {
            super.renderArmWithItem(livingEntity0, itemStack1, itemDisplayContext2, humanoidArm3, poseStack4, multiBufferSource5, int6);
        }
    }

    private void renderArmWithSpyglass(LivingEntity livingEntity0, ItemStack itemStack1, HumanoidArm humanoidArm2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        ModelPart $$6 = ((HeadedModel) this.m_117386_()).getHead();
        float $$7 = $$6.xRot;
        $$6.xRot = Mth.clamp($$6.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
        $$6.translateAndRotate(poseStack3);
        $$6.xRot = $$7;
        CustomHeadLayer.translateToHead(poseStack3, false);
        boolean $$8 = humanoidArm2 == HumanoidArm.LEFT;
        poseStack3.translate(($$8 ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        this.itemInHandRenderer.renderItem(livingEntity0, itemStack1, ItemDisplayContext.HEAD, false, poseStack3, multiBufferSource4, int5);
        poseStack3.popPose();
    }
}