package software.bernie.geckolib.core.keyframe.event;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;

public class CustomInstructionKeyframeEvent<T extends GeoAnimatable> extends KeyFrameEvent<T, CustomInstructionKeyframeData> {

    public CustomInstructionKeyframeEvent(T entity, double animationTick, AnimationController<T> controller, CustomInstructionKeyframeData customInstructionKeyframeData) {
        super(entity, animationTick, controller, customInstructionKeyframeData);
    }

    public CustomInstructionKeyframeData getKeyframeData() {
        return (CustomInstructionKeyframeData) super.getKeyframeData();
    }
}