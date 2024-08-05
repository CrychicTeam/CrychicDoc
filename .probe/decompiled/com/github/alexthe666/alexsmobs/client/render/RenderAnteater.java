package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelAnteater;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerAnteaterBaby;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerAnteaterTongueItem;
import com.github.alexthe666.alexsmobs.entity.EntityAnteater;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderAnteater extends MobRenderer<EntityAnteater, ModelAnteater> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/anteater.png");

    private static final ResourceLocation TEXTURE_PETER = new ResourceLocation("alexsmobs:textures/entity/anteater_peter.png");

    public RenderAnteater(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelAnteater(), 0.45F);
        this.m_115326_(new LayerAnteaterTongueItem(this));
        this.m_115326_(new LayerAnteaterBaby(this));
    }

    public boolean shouldRender(EntityAnteater anteater, Frustum p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return anteater.m_6162_() && anteater.m_20159_() && anteater.m_20202_() instanceof EntityAnteater ? false : super.shouldRender(anteater, p_225626_2_, p_225626_3_, p_225626_5_, p_225626_7_);
    }

    protected void scale(EntityAnteater entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public ResourceLocation getTextureLocation(EntityAnteater entity) {
        return entity.isPeter() ? TEXTURE_PETER : TEXTURE;
    }
}