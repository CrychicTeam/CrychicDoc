package dev.kosmx.playerAnim.core.util;

import java.util.Objects;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Vector3<N extends Number> {

    N x;

    N y;

    N z;

    public Vector3(N x, N y, N z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public N getX() {
        return this.x;
    }

    public N getY() {
        return this.y;
    }

    public N getZ() {
        return this.z;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Vector3)) {
            return false;
        } else {
            Vector3<?> vector3 = (Vector3<?>) o;
            return Objects.equals(this.x, vector3.x) && Objects.equals(this.y, vector3.y) && Objects.equals(this.z, vector3.z);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.x, this.y, this.z });
    }

    public String toString() {
        return "Vec3f[" + this.getX() + "; " + this.getY() + "; " + this.getZ() + "]";
    }
}