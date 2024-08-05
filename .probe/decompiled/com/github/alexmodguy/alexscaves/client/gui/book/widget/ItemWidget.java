package com.github.alexmodguy.alexscaves.client.gui.book.widget;

import com.github.alexmodguy.alexscaves.client.gui.book.CaveBookScreen;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.item.ACItemstackRenderer;
import com.google.gson.annotations.Expose;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemWidget extends BookWidget {

    @Expose
    private String item;

    @Expose
    private String nbt;

    @Expose
    private boolean sepia;

    @Expose(serialize = false, deserialize = false)
    private ItemStack actualItem = ItemStack.EMPTY;

    private static final RenderType SEPIA_ITEM_RENDER_TYPE = ACRenderTypes.getBookWidget(TextureAtlas.LOCATION_BLOCKS, true);

    public ItemWidget(int displayPage, String item, String nbt, boolean sepia, int x, int y, float scale) {
        super(displayPage, BookWidget.Type.ITEM, x, y, scale);
        this.item = item;
        this.nbt = nbt;
        this.sepia = sepia;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        if (this.actualItem == null && this.item != null) {
            this.actualItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.item)));
            if (this.nbt != null && !this.nbt.isEmpty()) {
                CompoundTag tag = null;
                try {
                    tag = TagParser.parseTag(this.nbt);
                } catch (CommandSyntaxException var7) {
                    var7.printStackTrace();
                }
                this.actualItem.setTag(tag);
            }
        }
        float scale = 16.0F * this.getScale();
        poseStack.pushPose();
        poseStack.translate((float) this.getX(), (float) this.getY(), 0.0F);
        poseStack.translate(0.0F, 0.0F, 50.0F);
        renderItem(this.actualItem, poseStack, bufferSource, this.sepia, scale);
        poseStack.popPose();
    }

    public static void renderItem(ItemStack itemStack, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, boolean sepia, float scale) {
        if (itemStack != null) {
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemStack, Minecraft.getInstance().level, null, 0);
            poseStack.pushPose();
            try {
                poseStack.scale(scale, scale, scale);
                CaveBookScreen.fixLighting();
                if (!sepia) {
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                } else {
                    poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
                    poseStack.scale(-1.0F, 1.0F, 1.0F);
                    ACItemstackRenderer.sepiaFlag = true;
                }
                if (sepia && !bakedmodel.isCustomRenderer()) {
                    renderSepiaItem(poseStack, bakedmodel, itemStack, bufferSource);
                } else {
                    Minecraft.getInstance().getItemRenderer().render(itemStack, ItemDisplayContext.GUI, false, poseStack, bufferSource, 240, OverlayTexture.NO_OVERLAY, bakedmodel);
                }
                if (sepia) {
                    ACItemstackRenderer.sepiaFlag = false;
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
            poseStack.popPose();
        }
    }

    public static void renderSepiaItem(PoseStack poseStack, BakedModel bakedmodel, ItemStack itemStack, MultiBufferSource.BufferSource bufferSource) {
        poseStack.pushPose();
        bakedmodel = ForgeHooksClient.handleCameraTransforms(poseStack, bakedmodel, ItemDisplayContext.GUI, false);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        for (RenderType rt : bakedmodel.getRenderTypes(itemStack, false)) {
            renderModel(poseStack.last(), bufferSource.getBuffer(SEPIA_ITEM_RENDER_TYPE), 1.0F, null, bakedmodel, 1.0F, 1.0F, 1.0F, 240, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt);
        }
        poseStack.popPose();
    }

    private static void renderModel(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float alpha, @Nullable BlockState blockState2, BakedModel bakedModel3, float float4, float float5, float float6, int int7, int int8, ModelData modelData, RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;
        for (Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, alpha, bakedModel3.getQuads(blockState2, direction, randomsource, modelData, renderType), int7, int8);
        }
        randomsource.setSeed(42L);
        renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, alpha, bakedModel3.getQuads(blockState2, (Direction) null, randomsource, modelData, renderType), int7, int8);
    }

    private static void renderQuadList(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float alpha, List<BakedQuad> listBakedQuad5, int int6, int int7) {
        for (BakedQuad bakedquad : listBakedQuad5) {
            float f = Mth.clamp(float2, 0.0F, 1.0F);
            float f1 = Mth.clamp(float3, 0.0F, 1.0F);
            float f2 = Mth.clamp(float4, 0.0F, 1.0F);
            vertexConsumer1.putBulkData(poseStackPose0, bakedquad, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, f, f1, f2, alpha, new int[] { int6, int6, int6, int6 }, int7, false);
        }
    }
}