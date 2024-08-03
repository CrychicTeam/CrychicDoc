package icyllis.arc3d.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.system.NativeType;

public sealed interface Matrixc permits Matrix {

    int kIdentity_Mask = 0;

    int kTranslate_Mask = 1;

    int kScale_Mask = 2;

    int kAffine_Mask = 4;

    int kPerspective_Mask = 8;

    int getType();

    boolean isIdentity();

    boolean isScaleTranslate();

    boolean isTranslate();

    boolean isAxisAligned();

    boolean preservesRightAngles();

    boolean hasPerspective();

    boolean isSimilarity();

    float m11();

    float m12();

    float m14();

    float m21();

    float m22();

    float m24();

    float m41();

    float m42();

    float m44();

    float getScaleX();

    float getScaleY();

    float getShearY();

    float getShearX();

    float getTranslateX();

    float getTranslateY();

    float getPerspX();

    float getPerspY();

    void store(@Nonnull Matrix var1);

    void store(@Nonnull float[] var1);

    void store(@Nonnull float[] var1, int var2);

    void store(@Nonnull ByteBuffer var1);

    void storeAligned(@Nonnull ByteBuffer var1);

    void store(@Nonnull FloatBuffer var1);

    void storeAligned(@Nonnull FloatBuffer var1);

    void store(@NativeType("void *") long var1);

    void storeAligned(@NativeType("void *") long var1);

    @CheckReturnValue
    boolean invert(@Nullable Matrix var1);

    default boolean mapRect(@Nonnull Rect2f rect) {
        return this.mapRect(rect, rect);
    }

    boolean mapRect(@Nonnull Rect2fc var1, @Nonnull Rect2f var2);

    default void mapRect(@Nonnull Rect2i r) {
        this.mapRect((float) r.mLeft, (float) r.mTop, (float) r.mRight, (float) r.mBottom, r);
    }

    default void mapRect(@Nonnull Rect2fc r, @Nonnull Rect2i out) {
        this.mapRect(r.left(), r.top(), r.right(), r.bottom(), out);
    }

    default void mapRect(@Nonnull Rect2ic r, @Nonnull Rect2i out) {
        this.mapRect((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom(), out);
    }

    void mapRect(float var1, float var2, float var3, float var4, @Nonnull Rect2i var5);

    default void mapRectOut(@Nonnull Rect2i r) {
        this.mapRectOut((float) r.mLeft, (float) r.mTop, (float) r.mRight, (float) r.mBottom, r);
    }

    default void mapRectOut(@Nonnull Rect2ic r, @Nonnull Rect2i dst) {
        this.mapRectOut((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom(), dst);
    }

    default void mapRectOut(@Nonnull Rect2fc r, @Nonnull Rect2i dst) {
        this.mapRectOut(r.left(), r.top(), r.right(), r.bottom(), dst);
    }

    void mapRectOut(float var1, float var2, float var3, float var4, @Nonnull Rect2i var5);

    default void mapPoint(float[] p) {
        this.mapPoints(p, 0, p, 0, 1);
    }

    default void mapPoints(float[] pts) {
        this.mapPoints(pts, 0, pts, 0, pts.length >> 1);
    }

    default void mapPoints(float[] pts, int count) {
        this.mapPoints(pts, 0, pts, 0, count);
    }

    default void mapPoints(float[] pts, int pos, int count) {
        this.mapPoints(pts, pos, pts, pos, count);
    }

    default void mapPoints(float[] src, float[] dst, int count) {
        this.mapPoints(src, 0, dst, 0, count);
    }

    void mapPoints(float[] var1, int var2, float[] var3, int var4, int var5);

    float getMinScale();

    float getMaxScale();

    boolean isFinite();

    int hashCode();

    boolean equals(Object var1);

    String toString();
}