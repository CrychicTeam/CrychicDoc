package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.client.BlackboardManager;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlackboardItemRenderer extends ItemStackRenderer {

    private static final BlockState STATE = ((Block) ModRegistry.BLACKBOARD.get()).defaultBlockState();

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.0, -0.34375);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = ClientHelper.getModel(blockRenderer.getBlockModelShaper().getModelManager(), ClientRegistry.BLACKBOARD_FRAME);
        blockRenderer.getModelRenderer().renderModel(matrixStackIn.last(), bufferIn.getBuffer(ItemBlockRenderTypes.getRenderType(STATE, false)), STATE, model, 1.0F, 1.0F, 1.0F, combinedLightIn, combinedOverlayIn);
        CompoundTag com = stack.getTagElement("BlockEntityTag");
        long[] packed = new long[16];
        if (com != null && com.contains("Pixels")) {
            packed = com.getLongArray("Pixels");
        }
        BlackboardManager.Blackboard blackboard = BlackboardManager.getInstance(BlackboardManager.Key.of(packed));
        VertexConsumer builder = bufferIn.getBuffer(blackboard.getRenderType());
        int lu = combinedLightIn & 65535;
        int lv = combinedLightIn >> 16 & 65535;
        matrixStackIn.translate(0.0, 0.0, 0.6875);
        VertexUtil.addQuad(builder, matrixStackIn, 0.0F, 0.0F, 1.0F, 1.0F, lu, lv);
        matrixStackIn.popPose();
    }
}