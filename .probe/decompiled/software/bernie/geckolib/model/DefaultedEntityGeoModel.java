package software.bernie.geckolib.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class DefaultedEntityGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    private final boolean turnsHead;

    public DefaultedEntityGeoModel(ResourceLocation assetSubpath) {
        this(assetSubpath, false);
    }

    public DefaultedEntityGeoModel(ResourceLocation assetSubpath, boolean turnsHead) {
        super(assetSubpath);
        this.turnsHead = turnsHead;
    }

    @Override
    protected String subtype() {
        return "entity";
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (this.turnsHead) {
            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                head.setRotX(entityData.headPitch() * (float) (Math.PI / 180.0));
                head.setRotY(entityData.netHeadYaw() * (float) (Math.PI / 180.0));
            }
        }
    }

    public DefaultedEntityGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltModel(altPath);
    }

    public DefaultedEntityGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltAnimations(altPath);
    }

    public DefaultedEntityGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedEntityGeoModel<T>) super.withAltTexture(altPath);
    }
}