package software.bernie.geckolib.core.animatable.instance;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SingletonAnimatableInstanceCache extends AnimatableInstanceCache {

    protected final Long2ObjectMap<AnimatableManager<?>> managers = new Long2ObjectOpenHashMap();

    public SingletonAnimatableInstanceCache(GeoAnimatable animatable) {
        super(animatable);
    }

    @Override
    public AnimatableManager<?> getManagerForId(long uniqueId) {
        if (!this.managers.containsKey(uniqueId)) {
            this.managers.put(uniqueId, new AnimatableManager(this.animatable));
        }
        return (AnimatableManager<?>) this.managers.get(uniqueId);
    }
}