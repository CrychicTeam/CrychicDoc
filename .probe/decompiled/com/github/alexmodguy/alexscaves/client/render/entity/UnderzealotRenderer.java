package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.UnderzealotModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class UnderzealotRenderer extends MobRenderer<UnderzealotEntity, UnderzealotModel> implements CustomBookEntityRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/underzealot.png");

    private boolean sepia = false;

    public UnderzealotRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new UnderzealotModel(), 0.25F);
    }

    public void render(UnderzealotEntity entity, float f1, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light) {
        ((UnderzealotModel) this.f_115290_).noBurrowing = this.sepia;
        if (entity.getBuriedProgress(partialTicks) != 1.0F || this.sepia) {
            super.render(entity, f1, partialTicks, poseStack, source, light);
        }
    }

    @Nullable
    protected RenderType getRenderType(UnderzealotEntity underzealot, boolean normal, boolean translucent, boolean outline) {
        return this.sepia ? ACRenderTypes.getBookWidget(TEXTURE, true) : super.m_7225_(underzealot, normal, translucent, outline);
    }

    @Override
    public void setSepiaFlag(boolean sepiaFlag) {
        this.sepia = sepiaFlag;
    }

    public ResourceLocation getTextureLocation(UnderzealotEntity entity) {
        return TEXTURE;
    }
}