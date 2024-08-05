package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class AxolotlRenderer extends MobRenderer<Axolotl, AxolotlModel<Axolotl>> {

    private static final Map<Axolotl.Variant, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), p_242076_ -> {
        for (Axolotl.Variant $$1 : Axolotl.Variant.values()) {
            p_242076_.put($$1, new ResourceLocation(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", $$1.getName())));
        }
    });

    public AxolotlRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new AxolotlModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.AXOLOTL)), 0.5F);
    }

    public ResourceLocation getTextureLocation(Axolotl axolotl0) {
        return (ResourceLocation) TEXTURE_BY_TYPE.get(axolotl0.getVariant());
    }
}