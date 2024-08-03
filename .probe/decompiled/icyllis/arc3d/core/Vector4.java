package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public class Vector4 {

    public float x;

    public float y;

    public float z;

    public float w;

    public void transform(@Nonnull Matrix4 mat) {
        mat.preTransform(this);
    }
}