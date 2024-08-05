package com.mna.items.renderers.books;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class ItemBookRenderer extends BlockEntityWithoutLevelRenderer {

    private ArrayList<BakedModel> bookClosed;

    private ArrayList<BakedModel> bookOpen;

    private final ArrayList<ResourceLocation> book_open = new ArrayList();

    private final ArrayList<ResourceLocation> book_closed;

    public ItemBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems, ResourceLocation openModel, ResourceLocation closedModel) {
        super(berd, ems);
        this.book_open.add(openModel);
        this.book_closed = new ArrayList();
        this.book_closed.add(closedModel);
    }

    public ItemBookRenderer addLayer(ResourceLocation openModel, ResourceLocation closedModel) {
        this.book_open.add(openModel);
        this.book_closed.add(closedModel);
        return this;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        ArrayList<BakedModel> model_layers;
        if (itemDisplayContext != ItemDisplayContext.FIXED && itemDisplayContext != ItemDisplayContext.GROUND && itemDisplayContext != ItemDisplayContext.GUI && itemDisplayContext != ItemDisplayContext.NONE) {
            if (this.bookOpen == null) {
                this.bookOpen = new ArrayList();
                this.book_open.forEach(m -> this.bookOpen.add(Minecraft.getInstance().getModelManager().getModel(m)));
            }
            model_layers = this.bookOpen;
            matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            if (itemDisplayContext != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && itemDisplayContext != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                matrixStack.translate(0.45, 0.4, -0.75);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
            } else {
                matrixStack.translate(0.35, 0.35, -0.75);
                matrixStack.scale(0.35F, 0.35F, 0.35F);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(30.0F));
            }
        } else {
            if (this.bookClosed == null) {
                this.bookClosed = new ArrayList();
                this.book_closed.forEach(m -> this.bookClosed.add(Minecraft.getInstance().getModelManager().getModel(m)));
            }
            model_layers = this.bookClosed;
            if (itemDisplayContext == ItemDisplayContext.FIXED) {
                matrixStack.translate(1.0, 0.0, 0.85);
                matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            } else if (itemDisplayContext == ItemDisplayContext.GUI) {
                matrixStack.translate(-0.125, 0.125, 0.0);
                matrixStack.mulPose(Axis.XP.rotationDegrees(15.0F));
                matrixStack.mulPose(Axis.YP.rotationDegrees(30.0F));
                matrixStack.scale(0.9F, 0.9F, 0.9F);
            } else {
                matrixStack.translate(0.25, 0.3, 0.25);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
            }
        }
        if (model_layers != null) {
            RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
            for (int i = 0; i < model_layers.size(); i++) {
                BakedModel model = (BakedModel) model_layers.get(i);
                this.renderModelLists(model, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder, i != 0);
            }
            matrixStack.popPose();
        }
    }

    public void renderModelLists(BakedModel p_229114_1_, ItemStack p_229114_2_, int p_229114_3_, int p_229114_4_, PoseStack p_229114_5_, VertexConsumer p_229114_6_, boolean tint) {
        RandomSource random = RandomSource.create();
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            this.renderQuadList(p_229114_5_, p_229114_6_, p_229114_1_.getQuads((BlockState) null, direction, random, ModelData.EMPTY, null), p_229114_2_, p_229114_3_, p_229114_4_, tint);
        }
        random.setSeed(42L);
        this.renderQuadList(p_229114_5_, p_229114_6_, p_229114_1_.getQuads((BlockState) null, (Direction) null, random, ModelData.EMPTY, null), p_229114_2_, p_229114_3_, p_229114_4_, tint);
    }

    public void renderQuadList(PoseStack p_229112_1_, VertexConsumer p_229112_2_, List<BakedQuad> p_229112_3_, ItemStack p_229112_4_, int p_229112_5_, int p_229112_6_, boolean tint) {
        PoseStack.Pose matrixstack$entry = p_229112_1_.last();
        for (BakedQuad bakedquad : p_229112_3_) {
            int i = -1;
            if (tint) {
                i = Minecraft.getInstance().getItemColors().getColor(p_229112_4_, bakedquad.getTintIndex());
            }
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
            float f2 = (float) (i & 0xFF) / 255.0F;
            p_229112_2_.putBulkData(matrixstack$entry, bakedquad, f, f1, f2, p_229112_5_, p_229112_6_);
        }
    }
}