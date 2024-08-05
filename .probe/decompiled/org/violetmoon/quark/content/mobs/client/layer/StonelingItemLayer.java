package org.violetmoon.quark.content.mobs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.client.model.StonelingModel;
import org.violetmoon.quark.content.mobs.entity.Stoneling;

public class StonelingItemLayer extends RenderLayer<Stoneling, StonelingModel> {

    public StonelingItemLayer(RenderLayerParent<Stoneling, StonelingModel> renderer) {
        super(renderer);
    }

    public void render(@NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light, Stoneling stoneling, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        ItemStack stack = stoneling.getCarryingItem();
        if (!stack.isEmpty()) {
            boolean isBlock = stack.getItem() instanceof BlockItem;
            matrix.pushPose();
            matrix.translate(0.0F, 0.515F, 0.0F);
            if (!isBlock) {
                matrix.mulPose(Axis.YP.rotationDegrees(stoneling.getItemAngle() + 180.0F));
                matrix.mulPose(Axis.XP.rotationDegrees(90.0F));
            } else {
                matrix.mulPose(Axis.XP.rotationDegrees(180.0F));
            }
            float scale = 0.8F;
            matrix.scale(scale, scale, scale);
            Minecraft mc = Minecraft.getInstance();
            mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrix, buffer, stoneling.m_9236_(), 0);
            matrix.popPose();
        }
    }
}