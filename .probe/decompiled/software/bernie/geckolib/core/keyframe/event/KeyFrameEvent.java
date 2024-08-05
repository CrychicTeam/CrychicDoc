package software.bernie.geckolib.core.keyframe.event;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.data.KeyFrameData;

public abstract class KeyFrameEvent<T extends GeoAnimatable, E extends KeyFrameData> {

    private final T animatable;

    private final double animationTick;

    private final AnimationController<T> controller;

    private final E eventKeyFrame;

    public KeyFrameEvent(T animatable, double animationTick, AnimationController<T> controller, E eventKeyFrame) {
        this.animatable = animatable;
        this.animationTick = animationTick;
        this.controller = controller;
        this.eventKeyFrame = eventKeyFrame;
    }

    public double getAnimationTick() {
        return this.animationTick;
    }

    public T getAnimatable() {
        return this.animatable;
    }

    public AnimationController<T> getController() {
        return this.controller;
    }

    public E getKeyframeData() {
        return this.eventKeyFrame;
    }
}