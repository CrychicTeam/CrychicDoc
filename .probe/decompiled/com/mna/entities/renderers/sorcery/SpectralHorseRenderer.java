package com.mna.entities.renderers.sorcery;

import com.mna.api.tools.RLoc;
import com.mna.entities.summon.SummonedSpectralHorse;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpectralHorseRenderer extends AbstractHorseRenderer<SummonedSpectralHorse, HorseModel<SummonedSpectralHorse>> {

    private static final ResourceLocation SPECTRAL_HORSE_TEXTURE = RLoc.create("textures/entity/spectral_horse.png");

    public SpectralHorseRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel<>(context.bakeLayer(ModelLayers.HORSE)), 1.1F);
    }

    public ResourceLocation getTextureLocation(SummonedSpectralHorse entity) {
        return SPECTRAL_HORSE_TEXTURE;
    }

    protected RenderType getRenderType(SummonedSpectralHorse entity, boolean visible, boolean notInvisibleToPlayer, boolean glowing) {
        return visible && !notInvisibleToPlayer && !glowing ? RenderType.entityTranslucentCull(SPECTRAL_HORSE_TEXTURE) : super.m_7225_(entity, visible, notInvisibleToPlayer, glowing);
    }
}