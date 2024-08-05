package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TridentItem;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    public CuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(CuttingBoardBlockEntity cuttingBoardEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Direction direction = ((Direction) cuttingBoardEntity.m_58900_().m_61143_(CuttingBoardBlock.FACING)).getOpposite();
        ItemStack boardStack = cuttingBoardEntity.getStoredItem();
        int posLong = (int) cuttingBoardEntity.m_58899_().asLong();
        if (!boardStack.isEmpty()) {
            poseStack.pushPose();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            poseStack.pushPose();
            boolean isBlockItem = itemRenderer.getModel(boardStack, cuttingBoardEntity.m_58904_(), null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
            poseStack.popPose();
            if (cuttingBoardEntity.isItemCarvingBoard()) {
                this.renderItemCarved(poseStack, direction, boardStack);
            } else if (isBlockItem && !boardStack.is(ModTags.FLAT_ON_CUTTING_BOARD)) {
                this.renderBlock(poseStack, direction);
            } else {
                this.renderItemLayingDown(poseStack, direction);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(boardStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, cuttingBoardEntity.m_58904_(), posLong);
            poseStack.popPose();
        }
    }

    public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction) {
        matrixStackIn.translate(0.5, 0.08, 0.5);
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }

    public void renderBlock(PoseStack matrixStackIn, Direction direction) {
        matrixStackIn.translate(0.5, 0.27, 0.5);
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
    }

    public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
        matrixStackIn.translate(0.5, 0.23, 0.5);
        float f = -direction.toYRot() + 180.0F;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        Item toolItem = itemStack.getItem();
        float poseAngle;
        if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem) {
            poseAngle = 225.0F;
        } else if (toolItem instanceof TridentItem) {
            poseAngle = 135.0F;
        } else {
            poseAngle = 180.0F;
        }
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }
}