package snownee.jade.util;

public class SmoothChasingValue {

    static final float eps = 2.4414062E-4F;

    float speed = 0.4F;

    float target = 0.0F;

    public float value;

    protected float getCurrentDiff() {
        return this.getTarget() - this.value;
    }

    public float getTarget() {
        return this.target;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean isMoving() {
        float diff = this.getCurrentDiff();
        return Math.abs(diff) > 0.0078125F;
    }

    public SmoothChasingValue set(float value) {
        this.value = value;
        return this;
    }

    public SmoothChasingValue start(float value) {
        this.value = value;
        this.target(value);
        return this;
    }

    public SmoothChasingValue target(float target) {
        this.target = target;
        return this;
    }

    public void tick(float pTicks) {
        float diff = this.getCurrentDiff();
        if (!(Math.abs(diff) < 2.4414062E-4F)) {
            this.set(this.value + diff * this.speed * pTicks);
        }
    }

    public SmoothChasingValue withSpeed(float speed) {
        this.speed = speed;
        return this;
    }
}