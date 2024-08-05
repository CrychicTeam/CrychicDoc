package software.bernie.geckolib.core.keyframe.event;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;

public class SoundKeyframeEvent<T extends GeoAnimatable> extends KeyFrameEvent<T, SoundKeyframeData> {

    public SoundKeyframeEvent(T entity, double animationTick, AnimationController<T> controller, SoundKeyframeData keyFrameData) {
        super(entity, animationTick, controller, keyFrameData);
    }

    public SoundKeyframeData getKeyframeData() {
        return (SoundKeyframeData) super.getKeyframeData();
    }
}