package software.bernie.geckolib.core.keyframe;

import com.eliotlash.mclib.math.IValue;

public record BoneAnimation(String boneName, KeyframeStack<Keyframe<IValue>> rotationKeyFrames, KeyframeStack<Keyframe<IValue>> positionKeyFrames, KeyframeStack<Keyframe<IValue>> scaleKeyFrames) {
}