package org.violetmoon.quark.addons.oddities.client.render.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;

public class MatrixEnchantingTableRenderer implements BlockEntityRenderer<MatrixEnchantingTableBlockEntity> {

    public static final Material TEXTURE_BOOK = EnchantTableRenderer.BOOK_LOCATION;

    private final BookModel modelBook;

    public MatrixEnchantingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.modelBook = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    public void render(MatrixEnchantingTableBlockEntity te, float partialTicks, @NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light, int overlay) {
        float time = (float) te.tickCount + partialTicks;
        float f1 = te.bookRotation - te.bookRotationPrev;
        while ((double) f1 >= Math.PI) {
            f1 = (float) ((double) f1 - (Math.PI * 2));
        }
        while ((double) f1 < -Math.PI) {
            f1 = (float) ((double) f1 + (Math.PI * 2));
        }
        float rot = te.bookRotationPrev + f1 * partialTicks;
        float bookOpen = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
        this.renderBook(te, time, rot, partialTicks, matrix, buffer, light, overlay);
        ItemStack item = te.m_8020_(0);
        if (!item.isEmpty()) {
            this.renderItem(item, time, bookOpen, rot, matrix, buffer, light, overlay, te.m_58904_());
        }
    }

    private void renderItem(ItemStack item, float time, float bookOpen, float rot, PoseStack matrix, MultiBufferSource buffer, int light, int overlay, Level level) {
        matrix.pushPose();
        matrix.translate(0.5F, 0.8F, 0.5F);
        matrix.scale(0.6F, 0.6F, 0.6F);
        rot *= -180.0F / (float) Math.PI;
        rot -= 90.0F;
        rot *= bookOpen;
        matrix.mulPose(Axis.YP.rotationDegrees(rot));
        matrix.translate(0.0, (double) (bookOpen * 1.4F), Math.sin((double) bookOpen * Math.PI));
        matrix.mulPose(Axis.XP.rotationDegrees(-90.0F * (bookOpen - 1.0F)));
        float trans = (float) Math.sin((double) time * 0.06) * bookOpen * 0.2F;
        matrix.translate(0.0F, trans, 0.0F);
        ItemRenderer render = Minecraft.getInstance().getItemRenderer();
        render.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, matrix, buffer, level, 0);
        matrix.popPose();
    }

    private void renderBook(MatrixEnchantingTableBlockEntity tileEntityIn, float time, float bookRot, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.75, 0.5);
        float f = (float) tileEntityIn.tickCount + partialTicks;
        matrixStackIn.translate(0.0, (double) (0.1F + Mth.sin(f * 0.1F) * 0.01F), 0.0);
        float f1 = tileEntityIn.bookRotation - tileEntityIn.bookRotationPrev;
        while (f1 >= (float) Math.PI) {
            f1 -= (float) (Math.PI * 2);
        }
        while (f1 < (float) -Math.PI) {
            f1 += (float) (Math.PI * 2);
        }
        float f2 = tileEntityIn.bookRotationPrev + f1 * partialTicks;
        matrixStackIn.mulPose(Axis.YP.rotation(-f2));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float f3 = Mth.lerp(partialTicks, tileEntityIn.pageFlipPrev, tileEntityIn.pageFlip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(partialTicks, tileEntityIn.bookSpreadPrev, tileEntityIn.bookSpread);
        this.modelBook.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
        VertexConsumer ivertexbuilder = TEXTURE_BOOK.buffer(bufferIn, RenderType::m_110446_);
        this.modelBook.renderToBuffer(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }
}