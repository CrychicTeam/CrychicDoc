package software.bernie.geckolib.core.animatable.instance;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class InstancedAnimatableInstanceCache extends AnimatableInstanceCache {

    protected AnimatableManager<?> manager;

    public InstancedAnimatableInstanceCache(GeoAnimatable animatable) {
        super(animatable);
    }

    @Override
    public AnimatableManager<?> getManagerForId(long uniqueId) {
        if (this.manager == null) {
            this.manager = new AnimatableManager(this.animatable);
        }
        return this.manager;
    }
}