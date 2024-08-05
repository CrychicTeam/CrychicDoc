package com.simibubi.create.foundation.utility.animation;

import java.util.ArrayList;
import net.minecraft.util.Mth;

public class PhysicalFloat {

    float previousValue;

    float value;

    float previousSpeed;

    float speed;

    float limit = Float.NaN;

    float mass;

    private final ArrayList<Force> forces = new ArrayList();

    public static PhysicalFloat create() {
        return new PhysicalFloat(1.0F);
    }

    public static PhysicalFloat create(float mass) {
        return new PhysicalFloat(mass);
    }

    public PhysicalFloat(float mass) {
        this.mass = mass;
    }

    public PhysicalFloat startAt(double value) {
        this.previousValue = this.value = (float) value;
        return this;
    }

    public PhysicalFloat withDrag(double drag) {
        return this.addForce(new Force.Drag((float) drag));
    }

    public PhysicalFloat zeroing(double g) {
        return this.addForce(new Force.Zeroing((float) g));
    }

    public PhysicalFloat withLimit(float limit) {
        this.limit = limit;
        return this;
    }

    public void tick() {
        this.previousSpeed = this.speed;
        this.previousValue = this.value;
        float totalImpulse = 0.0F;
        for (Force force : this.forces) {
            totalImpulse += force.get(this.mass, this.value, this.speed) / this.mass;
        }
        this.speed += totalImpulse;
        this.forces.removeIf(Force::finished);
        if (Float.isFinite(this.limit)) {
            this.speed = Mth.clamp(this.speed, -this.limit, this.limit);
        }
        this.value = this.value + this.speed;
    }

    public PhysicalFloat addForce(Force f) {
        this.forces.add(f);
        return this;
    }

    public PhysicalFloat bump(double force) {
        return this.addForce(new Force.Impulse((float) force));
    }

    public PhysicalFloat bump(int time, double force) {
        return this.addForce(new Force.OverTime(time, (float) force));
    }

    public float getValue() {
        return this.getValue(1.0F);
    }

    public float getValue(float partialTicks) {
        return Mth.lerp(partialTicks, this.previousValue, this.value);
    }
}