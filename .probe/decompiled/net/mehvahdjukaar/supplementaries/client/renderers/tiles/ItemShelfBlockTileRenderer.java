package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.supplementaries.common.block.tiles.ItemShelfBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemShelfBlockTileRenderer implements BlockEntityRenderer<ItemShelfBlockTile> {

    protected final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public ItemShelfBlockTileRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected boolean canRenderName(ItemShelfBlockTile tile) {
        if (Minecraft.renderNames() && tile.m_8020_(0).hasCustomHoverName()) {
            double d0 = Minecraft.getInstance().getEntityRenderDispatcher().distanceToSqr((double) tile.m_58899_().m_123341_() + 0.5, (double) tile.m_58899_().m_123342_() + 0.5, (double) tile.m_58899_().m_123343_() + 0.5);
            return d0 < 16.0;
        } else {
            return false;
        }
    }

    public void render(ItemShelfBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tile.m_7983_()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.5, 0.5);
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            float yaw = tile.getYaw();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
            matrixStackIn.translate(0.0, 0.0, 0.8125);
            if (this.canRenderName(tile)) {
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-yaw));
                Component name = tile.m_8020_(0).getHoverName();
                PedestalBlockTileRenderer.renderName(name, 0.625F, matrixStackIn, bufferIn, combinedLightIn);
                matrixStackIn.popPose();
            }
            ItemStack stack = tile.getDisplayedItem();
            if (MiscUtils.FESTIVITY.isAprilsFool()) {
                stack = new ItemStack(Items.SALMON);
            }
            BakedModel model = this.itemRenderer.getModel(stack, tile.m_58904_(), null, 0);
            if (model.usesBlockLight() && (Boolean) ClientConfigs.Blocks.SHELF_TRANSLATE.get()) {
                matrixStackIn.translate(0.0, -0.25, 0.0);
            }
            this.itemRenderer.render(stack, ItemDisplayContext.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);
            matrixStackIn.popPose();
        }
    }
}