package software.bernie.geckolib.core.animatable.model;

import java.util.Optional;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.animation.AnimationState;

public interface CoreGeoModel<E extends GeoAnimatable> {

    CoreBakedGeoModel getBakedGeoModel(String var1);

    default Optional<? extends CoreGeoBone> getBone(String name) {
        return Optional.ofNullable(this.getAnimationProcessor().getBone(name));
    }

    AnimationProcessor<E> getAnimationProcessor();

    Animation getAnimation(E var1, String var2);

    void handleAnimations(E var1, long var2, AnimationState<E> var4);

    default void setCustomAnimations(E animatable, long instanceId, AnimationState<E> animationState) {
    }

    default void applyMolangQueries(E animatable, double animTime) {
    }
}