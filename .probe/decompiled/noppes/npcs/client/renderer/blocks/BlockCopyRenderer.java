package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.schematics.Schematic;

public class BlockCopyRenderer extends BlockRendererInterface<TileCopy> {

    private static final ItemStack item = new ItemStack(CustomBlocks.copy);

    public static Schematic schematic = null;

    public static BlockPos pos = null;

    public BlockCopyRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public void render(TileCopy tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        matrixStack.pushPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        this.drawSelectionBox(matrixStack, buffer, new BlockPos(tile.width, tile.height, tile.length));
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
        matrixStack.popPose();
    }

    public void drawSelectionBox(PoseStack matrixStack, MultiBufferSource buffer, BlockPos pos) {
        AABB bb = new AABB(BlockPos.ZERO, pos);
        matrixStack.translate(0.001F, 0.001F, 0.001F);
        LevelRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), bb, 1.0F, 0.0F, 0.0F, 1.0F);
    }
}