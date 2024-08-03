package com.mna.tools.math;

public class Matrix4 {

    float[] mat;

    public Matrix4() {
        this.loadIdentity();
    }

    public Matrix4 loadIdentity() {
        this.mat = new float[16];
        float tmp35_34 = this.mat[10] = this.mat[15] = 1.0F;
        this.mat[5] = tmp35_34;
        this.mat[0] = tmp35_34;
        return this;
    }

    public Vector3 translate(Vector3 vec) {
        float x = vec.x * this.mat[0] + vec.y * this.mat[1] + vec.z * this.mat[2] + this.mat[3];
        float y = vec.x * this.mat[4] + vec.y * this.mat[5] + vec.z * this.mat[6] + this.mat[7];
        float z = vec.x * this.mat[8] + vec.y * this.mat[9] + vec.z * this.mat[10] + this.mat[11];
        return new Vector3((double) x, (double) y, (double) z);
    }

    public static Matrix4 eulerAngles(double angle, Vector3 axis) {
        axis = axis.copy().normalize();
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
        angle *= 0.0174532925;
        float cos = (float) Math.cos(angle);
        float ocos = 1.0F - cos;
        float sin = (float) Math.sin(angle);
        Matrix4 rotmat = new Matrix4();
        rotmat.mat[0] = x * x * ocos + cos;
        rotmat.mat[1] = y * x * ocos + z * sin;
        rotmat.mat[2] = x * z * ocos - y * sin;
        rotmat.mat[4] = x * y * ocos - z * sin;
        rotmat.mat[5] = y * y * ocos + cos;
        rotmat.mat[6] = y * z * ocos + x * sin;
        rotmat.mat[8] = x * z * ocos + y * sin;
        rotmat.mat[9] = y * z * ocos - x * sin;
        rotmat.mat[10] = z * z * ocos + cos;
        rotmat.mat[15] = 1.0F;
        return rotmat;
    }
}