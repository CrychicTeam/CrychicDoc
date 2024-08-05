package com.mna.items.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.items.relic.ItemAstroBlade;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AstroBladeRenderer extends BlockEntityWithoutLevelRenderer {

    public static final ResourceLocation blade_model = RLoc.create("item/special/astro_blade_blade");

    public static final ResourceLocation handle_model = RLoc.create("item/special/astro_blade_handle");

    public static final ResourceLocation blade_model_warden = RLoc.create("item/special/warden_blade_blade");

    public static final ResourceLocation handle_model_warden = RLoc.create("item/special/warden_blade_handle");

    public AstroBladeRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ItemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof ItemAstroBlade) {
            matrixStack.pushPose();
            if (stack.hasTag() && stack.getTag().contains("as_warden")) {
                ModelUtils.renderEntityModel(buffer.getBuffer(RenderType.solid()), ManaAndArtifice.instance.proxy.getClientWorld(), handle_model_warden, matrixStack, combinedLight, combinedOverlay);
                ModelUtils.renderEntityModel(buffer.getBuffer(RenderType.solid()), ManaAndArtifice.instance.proxy.getClientWorld(), blade_model_warden, matrixStack, 15728880, combinedOverlay);
            } else {
                ModelUtils.renderEntityModel(buffer.getBuffer(RenderType.solid()), ManaAndArtifice.instance.proxy.getClientWorld(), handle_model, matrixStack, combinedLight, combinedOverlay);
                ModelUtils.renderEntityModel(buffer.getBuffer(RenderType.endPortal()), ManaAndArtifice.instance.proxy.getClientWorld(), blade_model, matrixStack, combinedLight, combinedOverlay);
            }
            matrixStack.popPose();
        }
    }
}