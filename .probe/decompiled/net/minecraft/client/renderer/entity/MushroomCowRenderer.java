package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowRenderer extends MobRenderer<MushroomCow, CowModel<MushroomCow>> {

    private static final Map<MushroomCow.MushroomType, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), p_115516_ -> {
        p_115516_.put(MushroomCow.MushroomType.BROWN, new ResourceLocation("textures/entity/cow/brown_mooshroom.png"));
        p_115516_.put(MushroomCow.MushroomType.RED, new ResourceLocation("textures/entity/cow/red_mooshroom.png"));
    });

    public MushroomCowRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new CowModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.MOOSHROOM)), 0.7F);
        this.m_115326_(new MushroomCowMushroomLayer<>(this, entityRendererProviderContext0.getBlockRenderDispatcher()));
    }

    public ResourceLocation getTextureLocation(MushroomCow mushroomCow0) {
        return (ResourceLocation) TEXTURES.get(mushroomCow0.getVariant());
    }
}