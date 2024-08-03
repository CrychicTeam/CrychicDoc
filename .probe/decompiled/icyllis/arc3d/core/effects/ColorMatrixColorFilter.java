package icyllis.arc3d.core.effects;

import icyllis.arc3d.core.ColorFilter;
import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Size;
import javax.annotation.Nullable;

public class ColorMatrixColorFilter extends ColorFilter {

    private final float[] mMatrix = new float[20];

    private final boolean mAlphaUnchanged;

    public ColorMatrixColorFilter(@Size(20L) float[] matrix) {
        this.mAlphaUnchanged = MathUtil.isApproxZero(matrix[3], matrix[7], matrix[11], matrix[19]) && MathUtil.isApproxEqual(matrix[15], 1.0F);
        System.arraycopy(matrix, 0, this.mMatrix, 0, 20);
    }

    @Nullable
    public static ColorFilter make(@Size(20L) float[] matrix) {
        float prod = 0.0F;
        for (int i = 0; i < 20; i++) {
            prod *= matrix[i];
        }
        return prod != 0.0F ? null : new ColorMatrixColorFilter(matrix);
    }

    public float[] getMatrix() {
        return this.mMatrix;
    }

    @Override
    public boolean isAlphaUnchanged() {
        return this.mAlphaUnchanged;
    }

    @Override
    public void filterColor4f(float[] col, float[] out) {
        float a;
        if (col[3] != 0.0F) {
            a = 1.0F / col[3];
        } else {
            a = 0.0F;
        }
        float[] m = this.mMatrix;
        float x = MathUtil.clamp(m[0] * col[0] * a + m[4] * col[1] * a + m[8] * col[2] * a + m[12] * col[3] + m[16], 0.0F, 1.0F);
        float y = MathUtil.clamp(m[1] * col[0] * a + m[5] * col[1] * a + m[9] * col[2] * a + m[13] * col[3] + m[17], 0.0F, 1.0F);
        float z = MathUtil.clamp(m[2] * col[0] * a + m[6] * col[1] * a + m[10] * col[2] * a + m[14] * col[3] + m[18], 0.0F, 1.0F);
        float w = MathUtil.clamp(m[3] * col[0] * a + m[7] * col[1] * a + m[11] * col[2] * a + m[15] * col[3] + m[19], 0.0F, 1.0F);
        a = col[3];
        out[0] = x * a;
        out[1] = y * a;
        out[2] = z * a;
        out[3] = w;
    }
}