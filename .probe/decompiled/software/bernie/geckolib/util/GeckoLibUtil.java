package software.bernie.geckolib.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.InstancedAnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.EasingType;
import software.bernie.geckolib.loading.object.BakedModelFactory;
import software.bernie.geckolib.network.SerializableDataTicket;

public final class GeckoLibUtil {

    public static AnimatableInstanceCache createInstanceCache(GeoAnimatable animatable) {
        AnimatableInstanceCache cache = animatable.animatableCacheOverride();
        return cache != null ? cache : createInstanceCache(animatable, !(animatable instanceof Entity) && !(animatable instanceof BlockEntity));
    }

    public static AnimatableInstanceCache createInstanceCache(GeoAnimatable animatable, boolean singletonObject) {
        AnimatableInstanceCache cache = animatable.animatableCacheOverride();
        if (cache != null) {
            return cache;
        } else {
            return (AnimatableInstanceCache) (singletonObject ? new SingletonAnimatableInstanceCache(animatable) : new InstancedAnimatableInstanceCache(animatable));
        }
    }

    public static synchronized Animation.LoopType addCustomLoopType(String name, Animation.LoopType loopType) {
        return Animation.LoopType.register(name, loopType);
    }

    public static synchronized EasingType addCustomEasingType(String name, EasingType easingType) {
        return EasingType.register(name, easingType);
    }

    public static synchronized void addCustomBakedModelFactory(String namespace, BakedModelFactory factory) {
        BakedModelFactory.register(namespace, factory);
    }

    public static synchronized <D> SerializableDataTicket<D> addDataTicket(SerializableDataTicket<D> dataTicket) {
        return DataTickets.registerSerializable(dataTicket);
    }
}