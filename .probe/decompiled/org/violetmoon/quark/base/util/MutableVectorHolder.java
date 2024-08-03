package org.violetmoon.quark.base.util;

import net.minecraft.world.phys.Vec3;

public class MutableVectorHolder {

    public double x;

    public double y;

    public double z;

    public void importFrom(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
}