package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RenderLectern<T extends TileEntityLectern> implements BlockEntityRenderer<T> {

    private static final RenderType ENCHANTMENT_TABLE_BOOK_TEXTURE = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/lectern_book.png"));

    private final BookModel bookModel;

    public RenderLectern(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    public void render(@NotNull T entity, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.1F, 0.5F);
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(this.getRotation(entity)));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(112.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        float f4 = entity.pageFlipPrev + (entity.pageFlip - entity.pageFlipPrev) * partialTicks + 0.25F;
        float f5 = entity.pageFlipPrev + (entity.pageFlip - entity.pageFlipPrev) * partialTicks + 0.75F;
        f4 = (f4 - (float) Mth.floor(f4)) * 1.6F - 0.3F;
        f5 = (f5 - (float) Mth.floor(f5)) * 1.6F - 0.3F;
        if (f4 < 0.0F) {
            f4 = 0.0F;
        }
        if (f5 < 0.0F) {
            f5 = 0.0F;
        }
        if (f4 > 1.0F) {
            f4 = 1.0F;
        }
        if (f5 > 1.0F) {
            f5 = 1.0F;
        }
        float f6 = 1.29F;
        this.bookModel.setupAnim(partialTicks, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        this.bookModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(ENCHANTMENT_TABLE_BOOK_TEXTURE), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }

    private float getRotation(TileEntityLectern lectern) {
        switch((Direction) lectern.m_58900_().m_61143_(BlockLectern.FACING)) {
            case EAST:
                return 90.0F;
            case WEST:
                return -90.0F;
            case SOUTH:
                return 0.0F;
            default:
                return 180.0F;
        }
    }
}