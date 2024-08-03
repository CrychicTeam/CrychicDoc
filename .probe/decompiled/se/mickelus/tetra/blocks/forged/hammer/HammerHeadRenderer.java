package se.mickelus.tetra.blocks.forged.hammer;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class HammerHeadRenderer implements BlockEntityRenderer<HammerHeadBlockEntity> {

    private static final float animationDuration = 400.0F;

    private static final float unjamDuration = 800.0F;

    private static BlockRenderDispatcher blockRenderer;

    public HammerHeadRenderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public void render(HammerHeadBlockEntity tile, float v, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BakedModel model = blockRenderer.getBlockModelShaper().getBlockModel(HammerHeadBlock.instance.m_49966_());
        double offset = Mth.clamp((1.0 * (double) System.currentTimeMillis() - (double) tile.getActivationTime()) / 400.0, 0.0, 0.875);
        offset = Math.min(offset, Mth.clamp(0.25 + (1.0 * (double) System.currentTimeMillis() - (double) tile.getUnjamTime()) / 800.0, 0.0, 1.0) - 0.125);
        if (tile.isJammed()) {
            offset = Math.min(offset, 0.25);
        }
        matrixStack.translate(0.0, offset, 0.0);
        blockRenderer.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), HammerHeadBlock.instance.m_49966_(), model, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay);
    }
}