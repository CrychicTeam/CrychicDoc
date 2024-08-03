package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.client.ClientEventHandler;
import noppes.npcs.schematics.Schematic;

public class BlockBuilderRenderer extends BlockRendererInterface<TileBuilder> {

    private static final ItemStack item = new ItemStack(CustomBlocks.builder);

    public static Schematic schematic = null;

    public static BlockPos pos = null;

    public BlockBuilderRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public void render(TileBuilder tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        matrixStack.pushPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        if (tile.m_58899_().equals(TileBuilder.DrawPos)) {
            ClientEventHandler.onRenderTick(matrixStack, tile.m_58899_(), tile);
        }
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStack.popPose();
    }

    public void drawSelectionBox(PoseStack matrixStack, MultiBufferSource buffer, BlockPos pos) {
        AABB bb = new AABB(BlockPos.ZERO, pos);
        matrixStack.translate(0.001F, 0.001F, 0.001F);
        LevelRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), bb, 1.0F, 0.0F, 0.0F, 1.0F);
    }
}