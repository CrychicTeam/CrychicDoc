package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BookPileBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BookPileHorizontalBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BookPileBlockTile;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BookPileBlockTileRenderer implements BlockEntityRenderer<BookPileBlockTile> {

    private static ModelBlockRenderer renderer;

    public BookPileBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
    }

    public boolean shouldRender(BookPileBlockTile blockEntity, Vec3 cameraPos) {
        return (Boolean) ClientConfigs.Tweaks.BOOK_GLINT.get();
    }

    public void render(BookPileBlockTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int light, int overlay) {
        BlockState state = tile.m_58900_();
        renderBookPile(tile.horizontal, tile.booksVisuals, matrixStack, v -> v.getBuilder(bufferIn), light, overlay, state);
    }

    public static void renderBookPile(boolean horizontal, BookPileBlockTile.BooksList books, PoseStack matrixStack, Function<BookPileBlockTile.VisualBook, VertexConsumer> bufferIn, int light, int overlay, BlockState state) {
        if (horizontal) {
            renderHorizontal(books, state, matrixStack, bufferIn, light, overlay);
        } else {
            renderVertical(books, state, matrixStack, bufferIn, light, overlay);
        }
    }

    private static void renderHorizontal(BookPileBlockTile.BooksList visualBooks, BlockState state, PoseStack poseStack, Function<BookPileBlockTile.VisualBook, VertexConsumer> buffer, int light, int overlay) {
        int books = Math.min((Integer) state.m_61143_(BookPileBlock.BOOKS), visualBooks.size());
        Direction dir = (Direction) state.m_61143_(BookPileHorizontalBlock.FACING);
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(RotHlpr.rot(dir));
        poseStack.translate(-0.5, -0.6875, -0.5);
        float angle = (float) (Math.PI / 16);
        switch(books) {
            case 1:
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(0));
                break;
            case 2:
                poseStack.translate(-0.1875F, 0.0F, 0.0F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(0));
                poseStack.translate(0.3125F, 0.0F, 0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(1), 0.0F, angle);
                break;
            case 3:
                poseStack.translate(-0.3125F, 0.0F, 0.0F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(0));
                poseStack.translate(0.25F, 0.0F, -0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(1));
                poseStack.translate(0.3125F, 0.0F, 0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(2), 0.0F, angle);
                break;
            case 4:
                poseStack.translate(-0.375F, 0.0F, 0.0F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(0));
                poseStack.translate(0.25F, 0.0F, -0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(1));
                poseStack.translate(0.25F, 0.0F, 0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(2));
                poseStack.translate(0.25F, 0.0F, -0.0625F);
                renderBook(poseStack, buffer, light, overlay, visualBooks.get(3));
        }
    }

    private static void renderVertical(BookPileBlockTile.BooksList booksList, BlockState state, PoseStack matrixStack, Function<BookPileBlockTile.VisualBook, VertexConsumer> builder, int light, int overlay) {
        int maxBooks = Math.min((Integer) state.m_61143_(BookPileBlock.BOOKS), booksList.size());
        matrixStack.translate(0.0F, -0.375F, 0.0F);
        float zRot = (float) (-Math.PI / 2);
        for (int i = 0; i < maxBooks; i++) {
            BookPileBlockTile.VisualBook b = booksList.get(i);
            renderBook(matrixStack, builder, light, overlay, b, b.getAngle(), zRot);
            matrixStack.translate(0.0F, 0.25F, 0.0F);
        }
    }

    private static void renderBook(PoseStack poseStack, Function<BookPileBlockTile.VisualBook, VertexConsumer> vertexBuilder, int light, int overlay, BookPileBlockTile.VisualBook b) {
        renderBook(poseStack, vertexBuilder, light, overlay, b, 0.0F, 0.0F);
    }

    private static void renderBook(PoseStack poseStack, Function<BookPileBlockTile.VisualBook, VertexConsumer> vertexBuilder, int light, int overlay, BookPileBlockTile.VisualBook b, float xRot, float zRot) {
        VertexConsumer builder = (VertexConsumer) vertexBuilder.apply(b);
        if (builder != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            if (zRot != 0.0F) {
                poseStack.mulPose(Axis.ZP.rotation(zRot));
            }
            if (xRot != 0.0F) {
                poseStack.mulPose(Axis.XP.rotation(xRot));
            }
            poseStack.translate(-0.5, -0.3125, -0.5);
            BakedModel model = ClientHelper.getModel(Minecraft.getInstance().getModelManager(), b.getType().modelPath());
            if (model != null) {
                renderer.renderModel(poseStack.last(), builder, null, model, 1.0F, 1.0F, 1.0F, light, overlay);
            } else {
                Supplementaries.error();
            }
            poseStack.popPose();
        }
    }
}