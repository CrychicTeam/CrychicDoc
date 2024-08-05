package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishPatternLayer extends RenderLayer<TropicalFish, ColorableHierarchicalModel<TropicalFish>> {

    private static final ResourceLocation KOB_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_1.png");

    private static final ResourceLocation SUNSTREAK_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_2.png");

    private static final ResourceLocation SNOOPER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_3.png");

    private static final ResourceLocation DASHER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_4.png");

    private static final ResourceLocation BRINELY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_5.png");

    private static final ResourceLocation SPOTTY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_6.png");

    private static final ResourceLocation FLOPPER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_1.png");

    private static final ResourceLocation STRIPEY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_2.png");

    private static final ResourceLocation GLITTER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_3.png");

    private static final ResourceLocation BLOCKFISH_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_4.png");

    private static final ResourceLocation BETTY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_5.png");

    private static final ResourceLocation CLAYFISH_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_6.png");

    private final TropicalFishModelA<TropicalFish> modelA;

    private final TropicalFishModelB<TropicalFish> modelB;

    public TropicalFishPatternLayer(RenderLayerParent<TropicalFish, ColorableHierarchicalModel<TropicalFish>> renderLayerParentTropicalFishColorableHierarchicalModelTropicalFish0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTropicalFishColorableHierarchicalModelTropicalFish0);
        this.modelA = new TropicalFishModelA<>(entityModelSet1.bakeLayer(ModelLayers.TROPICAL_FISH_SMALL_PATTERN));
        this.modelB = new TropicalFishModelB<>(entityModelSet1.bakeLayer(ModelLayers.TROPICAL_FISH_LARGE_PATTERN));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, TropicalFish tropicalFish3, float float4, float float5, float float6, float float7, float float8, float float9) {
        TropicalFish.Pattern $$10 = tropicalFish3.getVariant();
        EntityModel<TropicalFish> $$11 = (EntityModel<TropicalFish>) (switch($$10.base()) {
            case SMALL ->
                this.modelA;
            case LARGE ->
                this.modelB;
        });
        ResourceLocation $$12 = switch($$10) {
            case KOB ->
                KOB_TEXTURE;
            case SUNSTREAK ->
                SUNSTREAK_TEXTURE;
            case SNOOPER ->
                SNOOPER_TEXTURE;
            case DASHER ->
                DASHER_TEXTURE;
            case BRINELY ->
                BRINELY_TEXTURE;
            case SPOTTY ->
                SPOTTY_TEXTURE;
            case FLOPPER ->
                FLOPPER_TEXTURE;
            case STRIPEY ->
                STRIPEY_TEXTURE;
            case GLITTER ->
                GLITTER_TEXTURE;
            case BLOCKFISH ->
                BLOCKFISH_TEXTURE;
            case BETTY ->
                BETTY_TEXTURE;
            case CLAYFISH ->
                CLAYFISH_TEXTURE;
        };
        float[] $$13 = tropicalFish3.getPatternColor().getTextureDiffuseColors();
        m_117359_(this.m_117386_(), $$11, $$12, poseStack0, multiBufferSource1, int2, tropicalFish3, float4, float5, float7, float8, float9, float6, $$13[0], $$13[1], $$13[2]);
    }
}