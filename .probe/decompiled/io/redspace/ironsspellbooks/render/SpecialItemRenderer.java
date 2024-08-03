package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SpecialItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ItemRenderer renderer;

    public final BakedModel guiModel;

    public final BakedModel normalModel;

    public SpecialItemRenderer(ItemRenderer renderDispatcher, EntityModelSet modelSet, String name) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = renderDispatcher;
        this.guiModel = this.renderer.getItemModelShaper().getModelManager().getModel(new ResourceLocation("irons_spellbooks", "item/" + name + "_gui"));
        this.normalModel = this.renderer.getItemModelShaper().getModelManager().getModel(new ResourceLocation("irons_spellbooks", "item/" + name + "_normal"));
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        if (transformType == ItemDisplayContext.GUI) {
            Lighting.setupForFlatItems();
            BakedModel model = this.guiModel;
            this.renderer.render(itemStack, transformType, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            Lighting.setupFor3DItems();
        } else {
            BakedModel model = this.normalModel;
            boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            this.renderer.render(itemStack, transformType, leftHand, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, model);
        }
        poseStack.popPose();
    }
}