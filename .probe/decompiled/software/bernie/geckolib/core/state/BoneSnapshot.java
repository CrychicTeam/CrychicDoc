package software.bernie.geckolib.core.state;

import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

public class BoneSnapshot {

    private final CoreGeoBone bone;

    private float scaleX;

    private float scaleY;

    private float scaleZ;

    private float offsetPosX;

    private float offsetPosY;

    private float offsetPosZ;

    private float rotX;

    private float rotY;

    private float rotZ;

    private double lastResetRotationTick = 0.0;

    private double lastResetPositionTick = 0.0;

    private double lastResetScaleTick = 0.0;

    private boolean rotAnimInProgress = true;

    private boolean posAnimInProgress = true;

    private boolean scaleAnimInProgress = true;

    public BoneSnapshot(CoreGeoBone bone) {
        this.rotX = bone.getRotX();
        this.rotY = bone.getRotY();
        this.rotZ = bone.getRotZ();
        this.offsetPosX = bone.getPosX();
        this.offsetPosY = bone.getPosY();
        this.offsetPosZ = bone.getPosZ();
        this.scaleX = bone.getScaleX();
        this.scaleY = bone.getScaleY();
        this.scaleZ = bone.getScaleZ();
        this.bone = bone;
    }

    public static BoneSnapshot copy(BoneSnapshot snapshot) {
        BoneSnapshot newSnapshot = new BoneSnapshot(snapshot.bone);
        newSnapshot.scaleX = snapshot.scaleX;
        newSnapshot.scaleY = snapshot.scaleY;
        newSnapshot.scaleZ = snapshot.scaleZ;
        newSnapshot.offsetPosX = snapshot.offsetPosX;
        newSnapshot.offsetPosY = snapshot.offsetPosY;
        newSnapshot.offsetPosZ = snapshot.offsetPosZ;
        newSnapshot.rotX = snapshot.rotX;
        newSnapshot.rotY = snapshot.rotY;
        newSnapshot.rotZ = snapshot.rotZ;
        return newSnapshot;
    }

    public CoreGeoBone getBone() {
        return this.bone;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public float getScaleZ() {
        return this.scaleZ;
    }

    public float getOffsetX() {
        return this.offsetPosX;
    }

    public float getOffsetY() {
        return this.offsetPosY;
    }

    public float getOffsetZ() {
        return this.offsetPosZ;
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public double getLastResetRotationTick() {
        return this.lastResetRotationTick;
    }

    public double getLastResetPositionTick() {
        return this.lastResetPositionTick;
    }

    public double getLastResetScaleTick() {
        return this.lastResetScaleTick;
    }

    public boolean isRotAnimInProgress() {
        return this.rotAnimInProgress;
    }

    public boolean isPosAnimInProgress() {
        return this.posAnimInProgress;
    }

    public boolean isScaleAnimInProgress() {
        return this.scaleAnimInProgress;
    }

    public void updateScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void updateOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetPosX = offsetX;
        this.offsetPosY = offsetY;
        this.offsetPosZ = offsetZ;
    }

    public void updateRotation(float rotX, float rotY, float rotZ) {
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void startPosAnim() {
        this.posAnimInProgress = true;
    }

    public void stopPosAnim(double tick) {
        this.posAnimInProgress = false;
        this.lastResetPositionTick = tick;
    }

    public void startRotAnim() {
        this.rotAnimInProgress = true;
    }

    public void stopRotAnim(double tick) {
        this.rotAnimInProgress = false;
        this.lastResetRotationTick = tick;
    }

    public void startScaleAnim() {
        this.scaleAnimInProgress = true;
    }

    public void stopScaleAnim(double tick) {
        this.scaleAnimInProgress = false;
        this.lastResetScaleTick = tick;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj != null && this.getClass() == obj.getClass() ? this.hashCode() == obj.hashCode() : false;
        }
    }

    public int hashCode() {
        return this.bone.getName().hashCode();
    }
}