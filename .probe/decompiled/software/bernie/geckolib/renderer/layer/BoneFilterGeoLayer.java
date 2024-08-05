package software.bernie.geckolib.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BoneFilterGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    protected final TriConsumer<GeoBone, T, Float> checkAndApply;

    public BoneFilterGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, (bone, animatable, partialTick) -> {
        });
    }

    public BoneFilterGeoLayer(GeoRenderer<T> renderer, TriConsumer<GeoBone, T, Float> checkAndApply) {
        super(renderer);
        this.checkAndApply = checkAndApply;
    }

    protected void checkAndApply(GeoBone bone, T animatable, float partialTick) {
        this.checkAndApply.accept(bone, animatable, partialTick);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        for (GeoBone bone : bakedModel.topLevelBones()) {
            this.checkChildBones(bone, animatable, partialTick);
        }
    }

    private void checkChildBones(GeoBone parentBone, T animatable, float partialTick) {
        this.checkAndApply(parentBone, animatable, partialTick);
        for (GeoBone bone : parentBone.getChildBones()) {
            this.checkChildBones(bone, animatable, partialTick);
        }
    }
}