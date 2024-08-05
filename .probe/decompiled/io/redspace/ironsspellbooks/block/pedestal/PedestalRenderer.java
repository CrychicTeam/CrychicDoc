package io.redspace.ironsspellbooks.block.pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;

public class PedestalRenderer implements BlockEntityRenderer<PedestalTile> {

    ItemRenderer itemRenderer;

    private static final Vec3 ITEM_POS = new Vec3(0.5, 1.5, 0.5);

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack heldItem = pedestalTile.getHeldItem();
        if (!heldItem.isEmpty()) {
            Player player = Minecraft.getInstance().player;
            float bob = 0.0F;
            float rotation = (float) (player.f_19797_ * 2) + partialTick;
            this.renderItem(heldItem, ITEM_POS.add(0.0, (double) bob, 0.0), rotation, pedestalTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int) pedestalTile.m_58899_().asLong();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof DiggerItem) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        }
        poseStack.scale(0.65F, 0.65F, 0.65F);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(pedestalTile.m_58904_(), pedestalTile.m_58899_()), packedOverlay, poseStack, bufferSource, pedestalTile.m_58904_(), renderId);
        poseStack.popPose();
    }
}