package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.TurtleModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Turtle;

public class TurtleRenderer extends MobRenderer<Turtle, TurtleModel<Turtle>> {

    private static final ResourceLocation TURTLE_LOCATION = new ResourceLocation("textures/entity/turtle/big_sea_turtle.png");

    public TurtleRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new TurtleModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.TURTLE)), 0.7F);
    }

    public void render(Turtle turtle0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        if (turtle0.m_6162_()) {
            this.f_114477_ *= 0.5F;
        }
        super.render(turtle0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(Turtle turtle0) {
        return TURTLE_LOCATION;
    }
}