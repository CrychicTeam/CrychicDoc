package io.redspace.ironsspellbooks.entity.spells.wisp;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WispRenderer extends GeoEntityRenderer<WispEntity> {

    public static final ResourceLocation textureLocation = new ResourceLocation("irons_spellbooks", "textures/entity/wisp/wisp.png");

    public WispRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WispModel());
        this.f_114477_ = 0.3F;
    }

    public ResourceLocation getTextureLocation(WispEntity animatable) {
        return textureLocation;
    }

    public RenderType getRenderType(WispEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl(texture, 0.0F, 0.0F);
    }
}