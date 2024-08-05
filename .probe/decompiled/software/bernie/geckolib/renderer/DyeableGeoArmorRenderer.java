package software.bernie.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Collection;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;

public abstract class DyeableGeoArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    protected final Set<GeoBone> dyeableBones = new ObjectArraySet();

    protected BakedGeoModel lastModel = null;

    public DyeableGeoArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (!isReRender) {
            this.checkBoneDyeCache(animatable, model, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.dyeableBones.contains(bone)) {
            Color color = this.getColorForBone(bone);
            red *= color.getRedFloat();
            green *= color.getGreenFloat();
            blue *= color.getBlueFloat();
            alpha *= color.getAlphaFloat();
        }
        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected abstract boolean isBoneDyeable(GeoBone var1);

    @NotNull
    protected abstract Color getColorForBone(GeoBone var1);

    protected void checkBoneDyeCache(T animatable, BakedGeoModel model, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (model != this.lastModel) {
            this.dyeableBones.clear();
            this.lastModel = model;
            this.collectDyeableBones(model.topLevelBones());
        }
    }

    protected void collectDyeableBones(Collection<GeoBone> bones) {
        for (GeoBone bone : bones) {
            if (this.isBoneDyeable(bone)) {
                this.dyeableBones.add(bone);
            }
            this.collectDyeableBones(bone.getChildBones());
        }
    }
}