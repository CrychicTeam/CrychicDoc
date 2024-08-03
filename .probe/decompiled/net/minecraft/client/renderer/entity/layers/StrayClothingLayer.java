package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class StrayClothingLayer<T extends Mob & RangedAttackMob, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation STRAY_CLOTHES_LOCATION = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");

    private final SkeletonModel<T> layerModel;

    public StrayClothingLayer(RenderLayerParent<T, M> renderLayerParentTM0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTM0);
        this.layerModel = new SkeletonModel<>(entityModelSet1.bakeLayer(ModelLayers.STRAY_OUTER_LAYER));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        m_117359_(this.m_117386_(), this.layerModel, STRAY_CLOTHES_LOCATION, poseStack0, multiBufferSource1, int2, t3, float4, float5, float7, float8, float9, float6, 1.0F, 1.0F, 1.0F);
    }
}