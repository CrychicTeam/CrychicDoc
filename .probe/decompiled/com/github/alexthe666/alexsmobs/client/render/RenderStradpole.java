package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelStradpole;
import com.github.alexthe666.alexsmobs.entity.EntityStradpole;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderStradpole extends MobRenderer<EntityStradpole, ModelStradpole> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/stradpole.png");

    public RenderStradpole(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelStradpole(), 0.25F);
    }

    protected void scale(EntityStradpole entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(EntityStradpole entity) {
        return TEXTURE;
    }
}