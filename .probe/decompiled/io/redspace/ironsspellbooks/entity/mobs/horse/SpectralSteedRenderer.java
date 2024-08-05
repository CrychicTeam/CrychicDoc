package io.redspace.ironsspellbooks.entity.mobs.horse;

import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpectralSteedRenderer extends AbstractHorseRenderer<SummonedHorse, HorseModel<SummonedHorse>> {

    public SpectralSteedRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new HorseModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.HORSE)), 1.1F);
    }

    public ResourceLocation getTextureLocation(SummonedHorse pEntity) {
        return new ResourceLocation("irons_spellbooks", "textures/entity/horse/spectral_steed.png");
    }
}