package software.bernie.geckolib.core.keyframe;

public record AnimationPoint(Keyframe<?> keyFrame, double currentTick, double transitionLength, double animationStartValue, double animationEndValue) {

    public String toString() {
        return "Tick: " + this.currentTick + " | Transition Length: " + this.transitionLength + " | Start Value: " + this.animationStartValue + " | End Value: " + this.animationEndValue;
    }
}