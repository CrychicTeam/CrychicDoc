package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndermanSkullBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.EndermanSkullBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EndermanHeadItemRenderer extends ItemStackRenderer {

    private final EndermanSkullBlockTile dummyTile = new EndermanSkullBlockTile(BlockPos.ZERO, ((EndermanSkullBlock) ModRegistry.ENDERMAN_SKULL_BLOCK.get()).m_49966_());

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int combinedOverlayIn) {
        poseStack.translate(1.0F, 0.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.dummyTile, poseStack, bufferSource, packedLight, combinedOverlayIn);
    }
}