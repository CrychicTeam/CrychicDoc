package net.minecraftforge.common.extensions;

import com.mojang.math.Transformation;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface IForgeTransformation {

    private Transformation self() {
        return (Transformation) this;
    }

    default boolean isIdentity() {
        return this.self().equals(Transformation.identity());
    }

    default void transformPosition(Vector4f position) {
        position.mul(this.self().getMatrix());
    }

    default void transformNormal(Vector3f normal) {
        normal.mul(this.self().getNormalMatrix());
        normal.normalize();
    }

    default Direction rotateTransform(Direction facing) {
        return Direction.rotate(this.self().getMatrix(), facing);
    }

    default Transformation blockCenterToCorner() {
        return this.applyOrigin(new Vector3f(0.5F, 0.5F, 0.5F));
    }

    default Transformation blockCornerToCenter() {
        return this.applyOrigin(new Vector3f(-0.5F, -0.5F, -0.5F));
    }

    default Transformation applyOrigin(Vector3f origin) {
        Transformation transform = this.self();
        if (transform.isIdentity()) {
            return Transformation.identity();
        } else {
            Matrix4f ret = transform.getMatrix();
            Matrix4f tmp = new Matrix4f().translation(origin.x(), origin.y(), origin.z());
            tmp.mul(ret, ret);
            tmp.translation(-origin.x(), -origin.y(), -origin.z());
            ret.mul(tmp);
            return new Transformation(ret);
        }
    }
}