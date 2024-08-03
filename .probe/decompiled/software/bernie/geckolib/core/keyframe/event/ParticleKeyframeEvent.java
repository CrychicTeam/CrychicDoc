package software.bernie.geckolib.core.keyframe.event;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;

public class ParticleKeyframeEvent<T extends GeoAnimatable> extends KeyFrameEvent<T, ParticleKeyframeData> {

    public ParticleKeyframeEvent(T animatable, double animationTick, AnimationController<T> controller, ParticleKeyframeData particleKeyFrameData) {
        super(animatable, animationTick, controller, particleKeyFrameData);
    }

    public ParticleKeyframeData getKeyframeData() {
        return (ParticleKeyframeData) super.getKeyframeData();
    }
}