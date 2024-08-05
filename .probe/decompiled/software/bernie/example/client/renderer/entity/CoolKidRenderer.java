package software.bernie.example.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.client.renderer.entity.layer.CoolKidGlassesLayer;
import software.bernie.example.entity.CoolKidEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CoolKidRenderer extends GeoEntityRenderer<CoolKidEntity> {

    public CoolKidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation("geckolib", "cool_kid")));
        this.f_114477_ = 0.25F;
        this.addRenderLayer(new CoolKidGlassesLayer(this));
    }
}