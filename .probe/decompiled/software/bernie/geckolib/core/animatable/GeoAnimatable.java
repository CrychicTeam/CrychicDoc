package software.bernie.geckolib.core.animatable;

import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public interface GeoAnimatable {

    void registerControllers(AnimatableManager.ControllerRegistrar var1);

    AnimatableInstanceCache getAnimatableInstanceCache();

    default double getBoneResetTime() {
        return 1.0;
    }

    default boolean shouldPlayAnimsWhileGamePaused() {
        return false;
    }

    double getTick(Object var1);

    default AnimatableInstanceCache animatableCacheOverride() {
        return null;
    }
}