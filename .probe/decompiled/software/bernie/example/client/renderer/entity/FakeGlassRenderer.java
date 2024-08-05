package software.bernie.example.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.model.entity.FakeGlassModel;
import software.bernie.example.entity.FakeGlassEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;

public class FakeGlassRenderer extends DynamicGeoEntityRenderer<FakeGlassEntity> {

    private static final ResourceLocation WHITE_STAINED_GLASS_TEXTURE = new ResourceLocation("textures/block/white_stained_glass.png");

    public FakeGlassRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FakeGlassModel());
    }

    @Nullable
    protected ResourceLocation getTextureOverrideForBone(GeoBone bone, FakeGlassEntity animatable, float partialTick) {
        return bone.getName().equals("outer_glass") ? WHITE_STAINED_GLASS_TEXTURE : null;
    }
}