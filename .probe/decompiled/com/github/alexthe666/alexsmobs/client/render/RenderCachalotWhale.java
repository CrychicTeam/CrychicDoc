package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelCachalotWhale;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerCachalotWhaleCapturedSquid;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotPart;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCachalotWhale extends MobRenderer<EntityCachalotWhale, ModelCachalotWhale> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/cachalot/cachalot_whale.png");

    private static final ResourceLocation TEXTURE_SLEEPING = new ResourceLocation("alexsmobs:textures/entity/cachalot/cachalot_whale_sleeping.png");

    private static final ResourceLocation TEXTURE_ALBINO = new ResourceLocation("alexsmobs:textures/entity/cachalot/cachalot_whale_albino.png");

    private static final ResourceLocation TEXTURE_ALBINO_SLEEPING = new ResourceLocation("alexsmobs:textures/entity/cachalot/cachalot_whale_albino_sleeping.png");

    public RenderCachalotWhale(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelCachalotWhale(), 4.2F);
        this.m_115326_(new LayerCachalotWhaleCapturedSquid(this));
    }

    protected void scale(EntityCachalotWhale entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    public boolean shouldRender(EntityCachalotWhale livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            for (EntityCachalotPart part : livingEntityIn.whaleParts) {
                if (camera.isVisible(part.m_20191_())) {
                    return true;
                }
            }
            return false;
        }
    }

    public ResourceLocation getTextureLocation(EntityCachalotWhale entity) {
        if (entity.isAlbino()) {
            return !entity.isSleeping() && !entity.isBeached() ? TEXTURE_ALBINO : TEXTURE_ALBINO_SLEEPING;
        } else {
            return !entity.isSleeping() && !entity.isBeached() ? TEXTURE : TEXTURE_SLEEPING;
        }
    }
}