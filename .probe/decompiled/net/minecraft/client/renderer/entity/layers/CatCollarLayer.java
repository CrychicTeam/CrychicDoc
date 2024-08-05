package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

public class CatCollarLayer extends RenderLayer<Cat, CatModel<Cat>> {

    private static final ResourceLocation CAT_COLLAR_LOCATION = new ResourceLocation("textures/entity/cat/cat_collar.png");

    private final CatModel<Cat> catModel;

    public CatCollarLayer(RenderLayerParent<Cat, CatModel<Cat>> renderLayerParentCatCatModelCat0, EntityModelSet entityModelSet1) {
        super(renderLayerParentCatCatModelCat0);
        this.catModel = new CatModel<>(entityModelSet1.bakeLayer(ModelLayers.CAT_COLLAR));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Cat cat3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (cat3.m_21824_()) {
            float[] $$10 = cat3.getCollarColor().getTextureDiffuseColors();
            m_117359_(this.m_117386_(), this.catModel, CAT_COLLAR_LOCATION, poseStack0, multiBufferSource1, int2, cat3, float4, float5, float7, float8, float9, float6, $$10[0], $$10[1], $$10[2]);
        }
    }
}