package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.TextBlockClient;

public class BlockScriptedRenderer extends BlockRendererInterface<TileScripted> {

    private static RandomSource random = RandomSource.create();

    public BlockScriptedRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public void render(TileScripted tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        matrixStack.pushPose();
        if (this.overrideModel()) {
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.scale(2.0F, 2.0F, 2.0F);
            this.renderItem(new ItemStack(CustomBlocks.scripted), matrixStack, buffer, light, overlay);
        } else {
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) tile.rotationY));
            matrixStack.mulPose(Axis.XP.rotationDegrees((float) tile.rotationX));
            matrixStack.mulPose(Axis.ZP.rotationDegrees((float) tile.rotationZ));
            matrixStack.scale(tile.scaleX, tile.scaleY, tile.scaleZ);
            Block b = tile.blockModel;
            if (b != null && b != Blocks.AIR && b != CustomBlocks.scripted) {
                BlockState state = b.defaultBlockState();
                this.renderBlock(tile, b, state, matrixStack, buffer, light, overlay);
                if (state.m_155947_() && !tile.renderTileErrored) {
                    try {
                        if (tile.renderTile == null) {
                            BlockEntity entity = ((EntityBlock) b).newBlockEntity(tile.m_58899_(), state);
                            entity.setLevel(tile.m_58904_());
                            tile.renderTile = entity;
                            tile.renderState = state;
                            tile.renderTileUpdate = ((EntityBlock) b).getTicker(tile.m_58904_(), state, entity.getType());
                        }
                        BlockEntityRenderer renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(tile.renderTile);
                        if (renderer != null) {
                            renderer.render(tile.renderTile, partialTicks, matrixStack, buffer, light, overlay);
                        } else {
                            tile.renderTileErrored = true;
                        }
                    } catch (Exception var10) {
                        tile.renderTileErrored = true;
                    }
                }
            } else {
                matrixStack.translate(0.5F, 0.5F, 0.5F);
                matrixStack.scale(2.0F, 2.0F, 2.0F);
                this.renderItem(tile.itemModel, matrixStack, buffer, light, overlay);
            }
        }
        matrixStack.popPose();
        if (!tile.text1.text.isEmpty()) {
            this.drawText(matrixStack, tile.text1, buffer, light, overlay);
        }
        if (!tile.text2.text.isEmpty()) {
            this.drawText(matrixStack, tile.text2, buffer, light, overlay);
        }
        if (!tile.text3.text.isEmpty()) {
            this.drawText(matrixStack, tile.text3, buffer, light, overlay);
        }
        if (!tile.text4.text.isEmpty()) {
            this.drawText(matrixStack, tile.text4, buffer, light, overlay);
        }
        if (!tile.text5.text.isEmpty()) {
            this.drawText(matrixStack, tile.text5, buffer, light, overlay);
        }
        if (!tile.text6.text.isEmpty()) {
            this.drawText(matrixStack, tile.text6, buffer, light, overlay);
        }
    }

    private void drawText(PoseStack matrixStack, TileScripted.TextPlane text1, MultiBufferSource buffer, int light, int overlay) {
        if (text1.textBlock == null || text1.textHasChanged) {
            text1.textBlock = new TextBlockClient(text1.text, 336, true, Minecraft.getInstance().player);
            text1.textHasChanged = false;
        }
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Axis.YP.rotationDegrees((float) text1.rotationY));
        matrixStack.mulPose(Axis.XP.rotationDegrees((float) text1.rotationX));
        matrixStack.mulPose(Axis.ZP.rotationDegrees((float) text1.rotationZ));
        matrixStack.scale(text1.scale, text1.scale, 1.0F);
        matrixStack.translate(text1.offsetX, text1.offsetY, text1.offsetZ);
        float f1 = 0.6666667F;
        float f3 = 0.0133F * f1;
        matrixStack.translate(0.0F, 0.5F, 0.01F);
        matrixStack.scale(f3, -f3, f3);
        Font fontrenderer = Minecraft.getInstance().font;
        float lineOffset = 0.0F;
        if (text1.textBlock.lines.size() < 14) {
            lineOffset = (14.0F - (float) text1.textBlock.lines.size()) / 2.0F;
        }
        for (int i = 0; i < text1.textBlock.lines.size(); i++) {
            Component text = (Component) text1.textBlock.lines.get(i);
            fontrenderer.drawInBatch(text, (float) (-fontrenderer.width(text) / 2), (float) ((int) ((double) (lineOffset + (float) i) * (9.0 - 0.3))), 0, false, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, light, overlay);
        }
        matrixStack.popPose();
    }

    private void renderItem(ItemStack item, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer, null, 0);
    }

    private void renderBlock(TileScripted tile, Block b, BlockState state, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        matrixStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
        if (random.nextInt(12) == 1) {
            state.m_60734_().animateTick(state, tile.m_58904_(), tile.m_58899_(), random);
        }
        matrixStack.popPose();
    }

    private boolean overrideModel() {
        ItemStack held = Minecraft.getInstance().player.m_21205_();
        return held == null ? false : held.getItem() == CustomItems.wand || held.getItem() == CustomItems.scripter;
    }
}