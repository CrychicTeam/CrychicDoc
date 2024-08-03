package com.github.alexthe666.citadel.client.render;

import com.github.alexthe666.citadel.server.block.CitadelLecternBlockEntity;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class CitadelLecternRenderer implements BlockEntityRenderer<CitadelLecternBlockEntity> {

    private final BookModel bookModel;

    public static final ResourceLocation BOOK_PAGE_TEXTURE = new ResourceLocation("citadel:textures/entity/lectern_book_pages.png");

    public static final ResourceLocation BOOK_BINDING_TEXTURE = new ResourceLocation("citadel:textures/entity/lectern_book_binding.png");

    private static final LecternBooks.BookData EMPTY_BOOK_DATA = new LecternBooks.BookData(12944441, 16050623);

    public CitadelLecternRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    public void render(CitadelLecternBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j) {
        BlockState blockstate = blockEntity.m_58900_();
        if ((Boolean) blockstate.m_61143_(LecternBlock.HAS_BOOK)) {
            LecternBooks.BookData bookData = (LecternBooks.BookData) LecternBooks.BOOKS.getOrDefault(ForgeRegistries.ITEMS.getKey(blockEntity.getBook().getItem()), EMPTY_BOOK_DATA);
            poseStack.pushPose();
            poseStack.translate(0.5, 1.0625, 0.5);
            float f = ((Direction) blockstate.m_61143_(LecternBlock.FACING)).getClockWise().toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(-f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(67.5F));
            poseStack.translate(0.0, -0.125, 0.0);
            this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
            int bindingR = (bookData.getBindingColor() & 0xFF0000) >> 16;
            int bindingG = (bookData.getBindingColor() & 0xFF00) >> 8;
            int bindingB = bookData.getBindingColor() & 0xFF;
            int pageR = (bookData.getPageColor() & 0xFF0000) >> 16;
            int pageG = (bookData.getPageColor() & 0xFF00) >> 8;
            int pageB = bookData.getPageColor() & 0xFF;
            VertexConsumer pages = bufferSource.getBuffer(RenderType.entityCutoutNoCull(BOOK_PAGE_TEXTURE));
            this.bookModel.render(poseStack, pages, i, j, (float) pageR / 255.0F, (float) pageG / 255.0F, (float) pageB / 255.0F, 1.0F);
            VertexConsumer binding = bufferSource.getBuffer(RenderType.entityCutoutNoCull(BOOK_BINDING_TEXTURE));
            this.bookModel.render(poseStack, binding, i, j, (float) bindingR / 255.0F, (float) bindingG / 255.0F, (float) bindingB / 255.0F, 1.0F);
            poseStack.popPose();
        }
    }
}