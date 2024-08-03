package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;

public class EnchantTableRenderer implements BlockEntityRenderer<EnchantmentTableBlockEntity> {

    public static final Material BOOK_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/enchanting_table_book"));

    private final BookModel bookModel;

    public EnchantTableRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.bookModel = new BookModel(blockEntityRendererProviderContext0.bakeLayer(ModelLayers.BOOK));
    }

    public void render(EnchantmentTableBlockEntity enchantmentTableBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        poseStack2.pushPose();
        poseStack2.translate(0.5F, 0.75F, 0.5F);
        float $$6 = (float) enchantmentTableBlockEntity0.time + float1;
        poseStack2.translate(0.0F, 0.1F + Mth.sin($$6 * 0.1F) * 0.01F, 0.0F);
        float $$7 = enchantmentTableBlockEntity0.rot - enchantmentTableBlockEntity0.oRot;
        while ($$7 >= (float) Math.PI) {
            $$7 -= (float) (Math.PI * 2);
        }
        while ($$7 < (float) -Math.PI) {
            $$7 += (float) (Math.PI * 2);
        }
        float $$8 = enchantmentTableBlockEntity0.oRot + $$7 * float1;
        poseStack2.mulPose(Axis.YP.rotation(-$$8));
        poseStack2.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float $$9 = Mth.lerp(float1, enchantmentTableBlockEntity0.oFlip, enchantmentTableBlockEntity0.flip);
        float $$10 = Mth.frac($$9 + 0.25F) * 1.6F - 0.3F;
        float $$11 = Mth.frac($$9 + 0.75F) * 1.6F - 0.3F;
        float $$12 = Mth.lerp(float1, enchantmentTableBlockEntity0.oOpen, enchantmentTableBlockEntity0.open);
        this.bookModel.setupAnim($$6, Mth.clamp($$10, 0.0F, 1.0F), Mth.clamp($$11, 0.0F, 1.0F), $$12);
        VertexConsumer $$13 = BOOK_LOCATION.buffer(multiBufferSource3, RenderType::m_110446_);
        this.bookModel.render(poseStack2, $$13, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack2.popPose();
    }
}