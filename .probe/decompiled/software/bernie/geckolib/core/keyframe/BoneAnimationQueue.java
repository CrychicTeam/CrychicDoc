package software.bernie.geckolib.core.keyframe;

import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.state.BoneSnapshot;

public record BoneAnimationQueue(CoreGeoBone bone, AnimationPointQueue rotationXQueue, AnimationPointQueue rotationYQueue, AnimationPointQueue rotationZQueue, AnimationPointQueue positionXQueue, AnimationPointQueue positionYQueue, AnimationPointQueue positionZQueue, AnimationPointQueue scaleXQueue, AnimationPointQueue scaleYQueue, AnimationPointQueue scaleZQueue) {

    public BoneAnimationQueue(CoreGeoBone bone) {
        this(bone, new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue(), new AnimationPointQueue());
    }

    public void addPosXPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.positionXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addPosYPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.positionYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addPosZPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.positionZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addNextPosition(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
        this.addPosXPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getOffsetX(), nextXPoint.animationStartValue());
        this.addPosYPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getOffsetY(), nextYPoint.animationStartValue());
        this.addPosZPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getOffsetZ(), nextZPoint.animationStartValue());
    }

    public void addScaleXPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.scaleXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addScaleYPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.scaleYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addScaleZPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.scaleZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addNextScale(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
        this.addScaleXPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getScaleX(), nextXPoint.animationStartValue());
        this.addScaleYPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getScaleY(), nextYPoint.animationStartValue());
        this.addScaleZPoint(keyFrame, lerpedTick, transitionLength, (double) startSnapshot.getScaleZ(), nextZPoint.animationStartValue());
    }

    public void addRotationXPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.rotationXQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addRotationYPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.rotationYQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addRotationZPoint(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, double startValue, double endValue) {
        this.rotationZQueue.add(new AnimationPoint(keyFrame, lerpedTick, transitionLength, startValue, endValue));
    }

    public void addNextRotation(Keyframe<?> keyFrame, double lerpedTick, double transitionLength, BoneSnapshot startSnapshot, BoneSnapshot initialSnapshot, AnimationPoint nextXPoint, AnimationPoint nextYPoint, AnimationPoint nextZPoint) {
        this.addRotationXPoint(keyFrame, lerpedTick, transitionLength, (double) (startSnapshot.getRotX() - initialSnapshot.getRotX()), nextXPoint.animationStartValue());
        this.addRotationYPoint(keyFrame, lerpedTick, transitionLength, (double) (startSnapshot.getRotY() - initialSnapshot.getRotY()), nextYPoint.animationStartValue());
        this.addRotationZPoint(keyFrame, lerpedTick, transitionLength, (double) (startSnapshot.getRotZ() - initialSnapshot.getRotZ()), nextZPoint.animationStartValue());
    }

    public void addPositions(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
        this.positionXQueue.add(xPoint);
        this.positionYQueue.add(yPoint);
        this.positionZQueue.add(zPoint);
    }

    public void addScales(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
        this.scaleXQueue.add(xPoint);
        this.scaleYQueue.add(yPoint);
        this.scaleZQueue.add(zPoint);
    }

    public void addRotations(AnimationPoint xPoint, AnimationPoint yPoint, AnimationPoint zPoint) {
        this.rotationXQueue.add(xPoint);
        this.rotationYQueue.add(yPoint);
        this.rotationZQueue.add(zPoint);
    }
}