package software.bernie.geckolib.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.util.TriConsumer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FastBoneFilterGeoLayer<T extends GeoAnimatable> extends BoneFilterGeoLayer<T> {

    protected final Supplier<List<String>> boneSupplier;

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, List::of);
    }

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer, Supplier<List<String>> boneSupplier) {
        this(renderer, boneSupplier, (bone, animatable, partialTick) -> {
        });
    }

    public FastBoneFilterGeoLayer(GeoRenderer<T> renderer, Supplier<List<String>> boneSupplier, TriConsumer<GeoBone, T, Float> checkAndApply) {
        super(renderer, checkAndApply);
        this.boneSupplier = boneSupplier;
    }

    protected List<String> getAffectedBones() {
        return (List<String>) this.boneSupplier.get();
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        for (String boneName : this.getAffectedBones()) {
            this.renderer.getGeoModel().getBone(boneName).ifPresent(bone -> this.checkAndApply(bone, animatable, partialTick));
        }
    }
}