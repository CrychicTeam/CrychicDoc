package com.mna.items.renderers;

import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ManaweaveBottleRenderer extends BlockEntityWithoutLevelRenderer {

    private BakedModel bottleModel;

    private final ResourceLocation bottle_texture_location = new ResourceLocation("mna", "item/manaweave_bottle_texture");

    public ManaweaveBottleRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (this.bottleModel == null) {
            this.bottleModel = Minecraft.getInstance().getModelManager().getModel(this.bottle_texture_location);
        }
        matrixStack.popPose();
        matrixStack.pushPose();
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            Lighting.setupForFlatItems();
        }
        Minecraft.getInstance().getItemRenderer().render(stack, itemDisplayContext, itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrixStack, buffer, itemDisplayContext == ItemDisplayContext.GUI ? 15728880 : combinedLight, itemDisplayContext == ItemDisplayContext.GUI ? OverlayTexture.NO_OVERLAY : combinedOverlay, this.bottleModel);
        if (ItemManaweaveBottle.hasPattern(stack)) {
            ManaweavingPattern p = ItemManaweaveBottle.getPattern(stack);
            if (p != null) {
                matrixStack.pushPose();
                switch(itemDisplayContext) {
                    case FIRST_PERSON_LEFT_HAND:
                    case FIRST_PERSON_RIGHT_HAND:
                        matrixStack.scale(0.4F, 0.4F, 0.4F);
                        matrixStack.mulPose(Axis.XN.rotationDegrees(15.0F));
                        matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        matrixStack.translate(-0.3, -1.65F, 0.0);
                        break;
                    case FIXED:
                    case GUI:
                    case HEAD:
                        matrixStack.scale(0.75F, 0.75F, 1.0F);
                        matrixStack.translate(0.0F, -2.05F, 0.0F);
                        break;
                    case THIRD_PERSON_LEFT_HAND:
                    case THIRD_PERSON_RIGHT_HAND:
                        matrixStack.scale(0.4F, 0.4F, 0.4F);
                        matrixStack.translate(0.0F, -1.75F, 0.1F);
                        break;
                    case GROUND:
                    case NONE:
                    default:
                        matrixStack.scale(0.4F, 0.4F, 0.4F);
                        matrixStack.translate(0.0F, -1.75F, 0.0F);
                }
                if (itemDisplayContext == ItemDisplayContext.GROUND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                    WorldRenderUtils.renderManaweavePattern(p, Axis.YP.rotationDegrees(0.0F), matrixStack, buffer, true);
                }
                if (itemDisplayContext != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                    WorldRenderUtils.renderManaweavePattern(p, Axis.YP.rotationDegrees(180.0F), matrixStack, buffer, true);
                }
                matrixStack.popPose();
            }
        }
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        bufferSource.endBatch();
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            Lighting.setupFor3DItems();
        }
    }
}