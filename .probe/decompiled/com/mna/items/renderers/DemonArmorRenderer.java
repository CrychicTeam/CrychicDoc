package com.mna.items.renderers;

import com.mna.items.armor.DemonArmorItem;
import com.mna.items.armor.models.DemonArmorModel;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DemonArmorRenderer extends GeoArmorRenderer<DemonArmorItem> {

    private static final RenderType EMISSIVE = RenderType.eyes(new ResourceLocation("mna", "textures/item/armor/demon_armor.png"));

    public DemonArmorRenderer() {
        super(new DemonArmorModel());
    }

    public RenderType getRenderType(DemonArmorItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}