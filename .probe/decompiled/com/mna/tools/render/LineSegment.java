package com.mna.tools.render;

import net.minecraft.world.phys.Vec3;

public class LineSegment {

    private Vec3 a;

    private Vec3 b;

    public LineSegment(Vec3 a, Vec3 b) {
        this.a = a;
        this.b = b;
    }

    public Vec3 closestPointOnLine(Vec3 vPoint) {
        Vec3 vVector1 = vPoint.subtract(this.a);
        Vec3 vVector2 = this.b.subtract(this.a).normalize();
        float d = (float) this.a.distanceTo(this.b);
        float t = (float) vVector2.dot(vVector1);
        if (t <= 0.0F) {
            return new Vec3(this.a.x, this.a.y, this.a.z);
        } else if (t >= d) {
            return new Vec3(this.b.x, this.b.y, this.b.z);
        } else {
            Vec3 vVector3 = vVector2.scale((double) t);
            return this.a.add(vVector3);
        }
    }
}