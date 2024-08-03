package se.mickelus.tetra.blocks.forged.extractor;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class CoreExtractorPistonRenderer implements BlockEntityRenderer<CoreExtractorPistonBlockEntity> {

    private static BlockRenderDispatcher blockRenderer;

    public CoreExtractorPistonRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public void render(CoreExtractorPistonBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = CoreExtractorPistonBlock.instance.get().m_49966_();
        double offset = (double) tile.getProgress(partialTicks);
        if (offset > 0.98) {
            offset = -49.0 * offset + 49.0;
        }
        BlockState shaftState = (BlockState) state.m_61124_(CoreExtractorPistonBlock.hackProp, true);
        BakedModel shaftModel = blockRenderer.getBlockModelShaper().getBlockModel(shaftState);
        blockRenderer.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), shaftState, shaftModel, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay);
        matrixStack.translate(0.0, offset, 0.0);
        BlockState coverState = (BlockState) state.m_61124_(CoreExtractorPistonBlock.hackProp, false);
        BakedModel coverModel = blockRenderer.getBlockModelShaper().getBlockModel(coverState);
        blockRenderer.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), coverState, coverModel, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay);
    }
}