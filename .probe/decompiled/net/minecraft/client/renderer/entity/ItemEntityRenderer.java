package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemEntityRenderer extends EntityRenderer<ItemEntity> {

    private static final float ITEM_BUNDLE_OFFSET_SCALE = 0.15F;

    private static final int ITEM_COUNT_FOR_5_BUNDLE = 48;

    private static final int ITEM_COUNT_FOR_4_BUNDLE = 32;

    private static final int ITEM_COUNT_FOR_3_BUNDLE = 16;

    private static final int ITEM_COUNT_FOR_2_BUNDLE = 1;

    private static final float FLAT_ITEM_BUNDLE_OFFSET_X = 0.0F;

    private static final float FLAT_ITEM_BUNDLE_OFFSET_Y = 0.0F;

    private static final float FLAT_ITEM_BUNDLE_OFFSET_Z = 0.09375F;

    private final ItemRenderer itemRenderer;

    private final RandomSource random = RandomSource.create();

    public ItemEntityRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.itemRenderer = entityRendererProviderContext0.getItemRenderer();
        this.f_114477_ = 0.15F;
        this.f_114478_ = 0.75F;
    }

    private int getRenderAmount(ItemStack itemStack0) {
        int $$1 = 1;
        if (itemStack0.getCount() > 48) {
            $$1 = 5;
        } else if (itemStack0.getCount() > 32) {
            $$1 = 4;
        } else if (itemStack0.getCount() > 16) {
            $$1 = 3;
        } else if (itemStack0.getCount() > 1) {
            $$1 = 2;
        }
        return $$1;
    }

    public void render(ItemEntity itemEntity0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        ItemStack $$6 = itemEntity0.getItem();
        int $$7 = $$6.isEmpty() ? 187 : Item.getId($$6.getItem()) + $$6.getDamageValue();
        this.random.setSeed((long) $$7);
        BakedModel $$8 = this.itemRenderer.getModel($$6, itemEntity0.m_9236_(), null, itemEntity0.m_19879_());
        boolean $$9 = $$8.isGui3d();
        int $$10 = this.getRenderAmount($$6);
        float $$11 = 0.25F;
        float $$12 = Mth.sin(((float) itemEntity0.getAge() + float2) / 10.0F + itemEntity0.bobOffs) * 0.1F + 0.1F;
        float $$13 = $$8.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        poseStack3.translate(0.0F, $$12 + 0.25F * $$13, 0.0F);
        float $$14 = itemEntity0.getSpin(float2);
        poseStack3.mulPose(Axis.YP.rotation($$14));
        float $$15 = $$8.getTransforms().ground.scale.x();
        float $$16 = $$8.getTransforms().ground.scale.y();
        float $$17 = $$8.getTransforms().ground.scale.z();
        if (!$$9) {
            float $$18 = -0.0F * (float) ($$10 - 1) * 0.5F * $$15;
            float $$19 = -0.0F * (float) ($$10 - 1) * 0.5F * $$16;
            float $$20 = -0.09375F * (float) ($$10 - 1) * 0.5F * $$17;
            poseStack3.translate($$18, $$19, $$20);
        }
        for (int $$21 = 0; $$21 < $$10; $$21++) {
            poseStack3.pushPose();
            if ($$21 > 0) {
                if ($$9) {
                    float $$22 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float $$23 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float $$24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    poseStack3.translate($$22, $$23, $$24);
                } else {
                    float $$25 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float $$26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    poseStack3.translate($$25, $$26, 0.0F);
                }
            }
            this.itemRenderer.render($$6, ItemDisplayContext.GROUND, false, poseStack3, multiBufferSource4, int5, OverlayTexture.NO_OVERLAY, $$8);
            poseStack3.popPose();
            if (!$$9) {
                poseStack3.translate(0.0F * $$15, 0.0F * $$16, 0.09375F * $$17);
            }
        }
        poseStack3.popPose();
        super.render(itemEntity0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(ItemEntity itemEntity0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}