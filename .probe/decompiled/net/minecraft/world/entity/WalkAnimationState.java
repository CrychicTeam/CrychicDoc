package net.minecraft.world.entity;

import net.minecraft.util.Mth;

public class WalkAnimationState {

    private float speedOld;

    private float speed;

    private float position;

    public void setSpeed(float float0) {
        this.speed = float0;
    }

    public void update(float float0, float float1) {
        this.speedOld = this.speed;
        this.speed = this.speed + (float0 - this.speed) * float1;
        this.position = this.position + this.speed;
    }

    public float speed() {
        return this.speed;
    }

    public float speed(float float0) {
        return Mth.lerp(float0, this.speedOld, this.speed);
    }

    public float position() {
        return this.position;
    }

    public float position(float float0) {
        return this.position - this.speed * (1.0F - float0);
    }

    public boolean isMoving() {
        return this.speed > 1.0E-5F;
    }
}