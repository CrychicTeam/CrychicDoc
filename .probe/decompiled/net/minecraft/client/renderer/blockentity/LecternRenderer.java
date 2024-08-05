package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LecternRenderer implements BlockEntityRenderer<LecternBlockEntity> {

    private final BookModel bookModel;

    public LecternRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.bookModel = new BookModel(blockEntityRendererProviderContext0.bakeLayer(ModelLayers.BOOK));
    }

    public void render(LecternBlockEntity lecternBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        BlockState $$6 = lecternBlockEntity0.m_58900_();
        if ((Boolean) $$6.m_61143_(LecternBlock.HAS_BOOK)) {
            poseStack2.pushPose();
            poseStack2.translate(0.5F, 1.0625F, 0.5F);
            float $$7 = ((Direction) $$6.m_61143_(LecternBlock.FACING)).getClockWise().toYRot();
            poseStack2.mulPose(Axis.YP.rotationDegrees(-$$7));
            poseStack2.mulPose(Axis.ZP.rotationDegrees(67.5F));
            poseStack2.translate(0.0F, -0.125F, 0.0F);
            this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
            VertexConsumer $$8 = EnchantTableRenderer.BOOK_LOCATION.buffer(multiBufferSource3, RenderType::m_110446_);
            this.bookModel.render(poseStack2, $$8, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack2.popPose();
        }
    }
}