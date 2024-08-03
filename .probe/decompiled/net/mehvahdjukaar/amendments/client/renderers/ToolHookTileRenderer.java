package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.common.tile.ToolHookBlockTile;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.phys.Vec3;

public class ToolHookTileRenderer implements BlockEntityRenderer<ToolHookBlockTile> {

    private final ItemRenderer itemRenderer;

    public ToolHookTileRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public boolean shouldRender(ToolHookBlockTile blockEntity, Vec3 cameraPos) {
        return blockEntity.shouldRenderFancy(cameraPos);
    }

    public void render(ToolHookBlockTile blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack item = blockEntity.getDisplayedItem();
        poseStack.translate(0.5, 0.5, 0.5);
        float scale = 0.75F;
        float x = item.getItem() instanceof DiggerItem ? 0.0625F : 0.0F;
        poseStack.mulPose(RotHlpr.rot((Direction) blockEntity.m_58900_().m_61143_(TripWireHookBlock.FACING)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(225.0F));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-x, 0.0F, 1.4F / (16.0F * scale));
        BakedModel itemModel = this.itemRenderer.getItemModelShaper().getItemModel(item);
        this.itemRenderer.render(item, ItemDisplayContext.GUI, false, poseStack, bufferSource, packedLight, packedOverlay, itemModel);
    }
}