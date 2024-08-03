package software.bernie.geckolib.core.animatable.instance;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.object.DataTicket;

public abstract class AnimatableInstanceCache {

    protected final GeoAnimatable animatable;

    public AnimatableInstanceCache(GeoAnimatable animatable) {
        this.animatable = animatable;
    }

    public abstract <T extends GeoAnimatable> AnimatableManager<T> getManagerForId(long var1);

    public <D> void addDataPoint(long uniqueId, DataTicket<D> dataTicket, D data) {
        this.getManagerForId(uniqueId).setData(dataTicket, data);
    }

    public <D> D getDataPoint(long uniqueId, DataTicket<D> dataTicket) {
        return this.getManagerForId(uniqueId).getData(dataTicket);
    }
}