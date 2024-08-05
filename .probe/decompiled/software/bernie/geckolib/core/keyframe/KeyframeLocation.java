package software.bernie.geckolib.core.keyframe;

public record KeyframeLocation<T extends Keyframe<?>>(T keyframe, double startTick) {
}