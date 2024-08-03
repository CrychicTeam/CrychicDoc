package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PandaHoldsItemLayer extends RenderLayer<Panda, PandaModel<Panda>> {

    private final ItemInHandRenderer itemInHandRenderer;

    public PandaHoldsItemLayer(RenderLayerParent<Panda, PandaModel<Panda>> renderLayerParentPandaPandaModelPanda0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentPandaPandaModelPanda0);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Panda panda3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ItemStack $$10 = panda3.m_6844_(EquipmentSlot.MAINHAND);
        if (panda3.isSitting() && !panda3.isScared()) {
            float $$11 = -0.6F;
            float $$12 = 1.4F;
            if (panda3.isEating()) {
                $$11 -= 0.2F * Mth.sin(float7 * 0.6F) + 0.2F;
                $$12 -= 0.09F * Mth.sin(float7 * 0.6F);
            }
            poseStack0.pushPose();
            poseStack0.translate(0.1F, $$12, $$11);
            this.itemInHandRenderer.renderItem(panda3, $$10, ItemDisplayContext.GROUND, false, poseStack0, multiBufferSource1, int2);
            poseStack0.popPose();
        }
    }
}