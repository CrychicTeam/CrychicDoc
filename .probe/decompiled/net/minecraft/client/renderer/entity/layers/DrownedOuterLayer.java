package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;

public class DrownedOuterLayer<T extends Drowned> extends RenderLayer<T, DrownedModel<T>> {

    private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/drowned_outer_layer.png");

    private final DrownedModel<T> model;

    public DrownedOuterLayer(RenderLayerParent<T, DrownedModel<T>> renderLayerParentTDrownedModelT0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTDrownedModelT0);
        this.model = new DrownedModel<>(entityModelSet1.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        m_117359_(this.m_117386_(), this.model, DROWNED_OUTER_LAYER_LOCATION, poseStack0, multiBufferSource1, int2, t3, float4, float5, float7, float8, float9, float6, 1.0F, 1.0F, 1.0F);
    }
}