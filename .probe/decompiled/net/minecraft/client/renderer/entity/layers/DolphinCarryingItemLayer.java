package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DolphinCarryingItemLayer extends RenderLayer<Dolphin, DolphinModel<Dolphin>> {

    private final ItemInHandRenderer itemInHandRenderer;

    public DolphinCarryingItemLayer(RenderLayerParent<Dolphin, DolphinModel<Dolphin>> renderLayerParentDolphinDolphinModelDolphin0, ItemInHandRenderer itemInHandRenderer1) {
        super(renderLayerParentDolphinDolphinModelDolphin0);
        this.itemInHandRenderer = itemInHandRenderer1;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Dolphin dolphin3, float float4, float float5, float float6, float float7, float float8, float float9) {
        boolean $$10 = dolphin3.m_5737_() == HumanoidArm.RIGHT;
        poseStack0.pushPose();
        float $$11 = 1.0F;
        float $$12 = -1.0F;
        float $$13 = Mth.abs(dolphin3.m_146909_()) / 60.0F;
        if (dolphin3.m_146909_() < 0.0F) {
            poseStack0.translate(0.0F, 1.0F - $$13 * 0.5F, -1.0F + $$13 * 0.5F);
        } else {
            poseStack0.translate(0.0F, 1.0F + $$13 * 0.8F, -1.0F + $$13 * 0.2F);
        }
        ItemStack $$14 = $$10 ? dolphin3.m_21205_() : dolphin3.m_21206_();
        this.itemInHandRenderer.renderItem(dolphin3, $$14, ItemDisplayContext.GROUND, false, poseStack0, multiBufferSource1, int2);
        poseStack0.popPose();
    }
}