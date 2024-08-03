package software.bernie.example.client.model.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.block.entity.FertilizerBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class FertilizerModel extends DefaultedBlockGeoModel<FertilizerBlockEntity> {

    private final ResourceLocation BOTARIUM_MODEL = this.buildFormattedModelPath(new ResourceLocation("geckolib", "botarium"));

    private final ResourceLocation BOTARIUM_TEXTURE = this.buildFormattedTexturePath(new ResourceLocation("geckolib", "botarium"));

    private final ResourceLocation BOTARIUM_ANIMATIONS = this.buildFormattedAnimationPath(new ResourceLocation("geckolib", "botarium"));

    public FertilizerModel() {
        super(new ResourceLocation("geckolib", "fertilizer"));
    }

    public ResourceLocation getAnimationResource(FertilizerBlockEntity animatable) {
        return animatable.m_58904_().isRaining() ? super.getAnimationResource(animatable) : this.BOTARIUM_ANIMATIONS;
    }

    public ResourceLocation getModelResource(FertilizerBlockEntity animatable) {
        return animatable.m_58904_().isRaining() ? super.getModelResource(animatable) : this.BOTARIUM_MODEL;
    }

    public ResourceLocation getTextureResource(FertilizerBlockEntity animatable) {
        return animatable.m_58904_().isRaining() ? super.getTextureResource(animatable) : this.BOTARIUM_TEXTURE;
    }

    public RenderType getRenderType(FertilizerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}