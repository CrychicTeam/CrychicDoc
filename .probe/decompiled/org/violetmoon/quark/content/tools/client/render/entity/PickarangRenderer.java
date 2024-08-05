package org.violetmoon.quark.content.tools.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tools.entity.rang.AbstractPickarang;

public class PickarangRenderer extends EntityRenderer<AbstractPickarang<?>> {

    public PickarangRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(AbstractPickarang<?> entity, float yaw, float partialTicks, @NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light) {
        if (entity.f_19797_ >= 2) {
            matrix.pushPose();
            matrix.translate(0.0, 0.2, 0.0);
            matrix.mulPose(Axis.XP.rotationDegrees(90.0F));
            Minecraft mc = Minecraft.getInstance();
            float time = (float) entity.f_19797_ + (mc.isPaused() ? 0.0F : partialTicks);
            matrix.mulPose(Axis.ZP.rotationDegrees(time * 20.0F));
            mc.getItemRenderer().renderStatic(entity.getStack(), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrix, buffer, Minecraft.getInstance().level, 0);
            matrix.popPose();
        }
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull AbstractPickarang<?> entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}