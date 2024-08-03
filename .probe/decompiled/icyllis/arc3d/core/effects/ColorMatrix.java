package icyllis.arc3d.core.effects;

import icyllis.arc3d.core.Size;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import javax.annotation.Nonnull;

public class ColorMatrix {

    private static final int kScaleR = 0;

    private static final int kScaleG = 5;

    private static final int kScaleB = 10;

    private static final int kScaleA = 15;

    private static final int kTransR = 16;

    private static final int kTransG = 17;

    private static final int kTransB = 18;

    private static final int kTransA = 19;

    private final float[] mMat = new float[20];

    public ColorMatrix() {
        this.mMat[0] = 1.0F;
        this.mMat[5] = 1.0F;
        this.mMat[10] = 1.0F;
        this.mMat[15] = 1.0F;
    }

    public ColorMatrix(@Size(20L) float... src) {
        this.set(src);
    }

    public ColorMatrix(@Nonnull ColorMatrix src) {
        this.set(src);
    }

    public void setIdentity() {
        float[] mat = this.mMat;
        for (int i = 0; i < 20; i += 5) {
            mat[i] = 1.0F;
            mat[i + 1] = 0.0F;
            mat[i + 2] = 0.0F;
            mat[i + 3] = 0.0F;
            mat[i + 4] = 0.0F;
        }
    }

    public void set(@Nonnull ColorMatrix src) {
        this.set(src.mMat, 0);
    }

    public void set(@Size(20L) float[] src) {
        this.set(src, 0);
    }

    public void set(@Size(20L) float[] src, int offset) {
        System.arraycopy(src, offset, this.mMat, 0, 20);
    }

    public void set(@Nonnull ByteBuffer src) {
        int offset = src.position();
        for (int i = 0; i < 20; i++) {
            this.mMat[i] = src.getFloat(offset);
            offset += 4;
        }
    }

    public void set(@Nonnull FloatBuffer src) {
        src.get(src.position(), this.mMat);
    }

    public void store(@Nonnull ColorMatrix dst) {
        this.store(dst.mMat, 0);
    }

    public void store(@Size(20L) float[] dst) {
        this.store(dst, 0);
    }

    public void store(@Size(20L) float[] dst, int offset) {
        System.arraycopy(this.mMat, 0, dst, offset, 20);
    }

    public void store(@Nonnull ByteBuffer dst) {
        int offset = dst.position();
        for (int i = 0; i < 20; i++) {
            dst.putFloat(offset, this.mMat[i]);
            offset += 4;
        }
    }

    public void store(@Nonnull FloatBuffer dst) {
        dst.put(dst.position(), this.mMat);
    }

    public void setScale(float scaleR, float scaleG, float scaleB, float scaleA) {
        Arrays.fill(this.mMat, 0.0F);
        this.mMat[0] = scaleR;
        this.mMat[5] = scaleG;
        this.mMat[10] = scaleB;
        this.mMat[15] = scaleA;
    }

    public void setRotateR(float angle) {
        this.setIdentity();
        if (angle != 0.0F) {
            float s = (float) Math.sin((double) angle);
            float c = (float) Math.cos((double) angle);
            this.mMat[5] = this.mMat[10] = c;
            this.mMat[6] = s;
            this.mMat[9] = -s;
        }
    }

    public void setRotateG(float angle) {
        this.setIdentity();
        if (angle != 0.0F) {
            float s = (float) Math.sin((double) angle);
            float c = (float) Math.cos((double) angle);
            this.mMat[0] = this.mMat[10] = c;
            this.mMat[2] = -s;
            this.mMat[8] = s;
        }
    }

    public void setRotateB(float angle) {
        this.setIdentity();
        if (angle != 0.0F) {
            float s = (float) Math.sin((double) angle);
            float c = (float) Math.cos((double) angle);
            this.mMat[0] = this.mMat[5] = c;
            this.mMat[1] = s;
            this.mMat[4] = -s;
        }
    }

    public void setTranslate(float transR, float transG, float transB, float transA) {
        float[] mat = this.mMat;
        for (int i = 0; i < 15; i += 5) {
            mat[i] = 1.0F;
            mat[i + 1] = 0.0F;
            mat[i + 2] = 0.0F;
            mat[i + 3] = 0.0F;
            mat[i + 4] = 0.0F;
        }
        mat[15] = 1.0F;
        mat[16] = transR;
        mat[17] = transG;
        mat[18] = transB;
        mat[19] = transA;
    }

    public void preConcat(@Size(20L) float[] lhs) {
        set_concat(this.mMat, lhs, this.mMat);
    }

    public void preConcat(@Nonnull ColorMatrix lhs) {
        set_concat(this.mMat, lhs.mMat, this.mMat);
    }

    public void postConcat(@Size(20L) float[] rhs) {
        set_concat(this.mMat, this.mMat, rhs);
    }

    public void postConcat(@Nonnull ColorMatrix rhs) {
        set_concat(this.mMat, this.mMat, rhs.mMat);
    }

    public void setConcat(@Size(20L) float[] lhs, @Size(20L) float[] rhs) {
        set_concat(this.mMat, lhs, rhs);
    }

    public void setConcat(@Nonnull ColorMatrix lhs, @Nonnull ColorMatrix rhs) {
        set_concat(this.mMat, lhs.mMat, rhs.mMat);
    }

    private static void set_concat(@Size(20L) float[] result, @Size(20L) float[] lhs, @Size(20L) float[] rhs) {
        float[] target;
        if (lhs != result && rhs != result) {
            target = result;
        } else {
            target = new float[20];
        }
        for (int i = 0; i < 16; i += 4) {
            for (int j = 0; j < 4; j++) {
                target[i + j] = lhs[i] * rhs[j] + lhs[i + 1] * rhs[j + 4] + lhs[i + 2] * rhs[j + 8] + lhs[i + 3] * rhs[j + 12];
            }
        }
        for (int j = 0; j < 4; j++) {
            target[16 + j] = lhs[16] * rhs[j] + lhs[17] * rhs[j + 4] + lhs[18] * rhs[j + 8] + lhs[19] * rhs[j + 12] + rhs[j + 16];
        }
        if (target != result) {
            System.arraycopy(target, 0, result, 0, 20);
        }
    }

    public void setSaturation(float sat) {
        float[] m = this.mMat;
        Arrays.fill(m, 0.0F);
        float R = 0.213F * (1.0F - sat);
        float G = 0.715F * (1.0F - sat);
        float B = 0.072F * (1.0F - sat);
        m[0] = R + sat;
        m[1] = R;
        m[2] = R;
        m[4] = G;
        m[5] = G + sat;
        m[6] = G;
        m[8] = B;
        m[9] = B;
        m[10] = B + sat;
        m[15] = 1.0F;
    }

    public float[] elements() {
        return this.mMat;
    }

    public int hashCode() {
        return Arrays.hashCode(this.mMat);
    }

    public boolean equals(Object o) {
        if (o instanceof ColorMatrix m) {
            for (int i = 0; i < 20; i++) {
                if (this.mMat[i] != m.mMat[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}