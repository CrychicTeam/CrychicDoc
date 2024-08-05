package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.client.model.blocks.ModelCarpentryBench;

public class BlockCarpentryBenchRenderer implements BlockEntityRenderer<TileBlockAnvil> {

    private final ModelCarpentryBench model = new ModelCarpentryBench();

    private static final ResourceLocation TEXTURE = new ResourceLocation("customnpcs", "textures/models/carpentrybench.png");

    private static final RenderType type = RenderType.entityCutout(TEXTURE);

    public BlockCarpentryBenchRenderer(BlockEntityRendererProvider.Context dispatcher) {
    }

    public void render(TileBlockAnvil te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        int rotation = 0;
        if (te.m_58899_() != BlockPos.ZERO) {
            rotation = (Integer) te.m_58900_().m_61143_(BlockCarpentryBench.ROTATION);
        }
        matrixStack.pushPose();
        RenderSystem.disableBlend();
        matrixStack.translate(0.5F, 1.4F, 0.5F);
        matrixStack.scale(0.95F, 0.95F, 0.95F);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees((float) (90 * rotation)));
        this.model.renderToBuffer(matrixStack, buffer.getBuffer(type), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}