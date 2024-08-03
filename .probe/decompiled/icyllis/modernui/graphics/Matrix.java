package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class Matrix extends icyllis.arc3d.core.Matrix {

    public Matrix() {
    }

    public Matrix(@Nullable Matrix m) {
        if (m != null) {
            m.store(this);
        }
    }

    public Matrix(float scaleX, float shearY, float persp0, float shearX, float scaleY, float persp1, float transX, float transY, float persp2) {
        super(scaleX, shearY, persp0, shearX, scaleY, persp1, transX, transY, persp2);
    }

    public void set(@Nullable Matrix m) {
        if (m != null) {
            m.store(this);
        } else {
            this.setIdentity();
        }
    }

    public void mapRect(@NonNull RectF r) {
        int typeMask = this.getType();
        if (typeMask <= 1) {
            r.left = r.left + this.m41;
            r.top = r.top + this.m42;
            r.right = r.right + this.m41;
            r.bottom = r.bottom + this.m42;
        } else if ((typeMask & -4) == 0) {
            r.left = r.left * this.m11 + this.m41;
            r.top = r.top * this.m22 + this.m42;
            r.right = r.right * this.m11 + this.m41;
            r.bottom = r.bottom * this.m22 + this.m42;
        } else {
            float x1 = this.m11 * r.left + this.m21 * r.top + this.m41;
            float y1 = this.m12 * r.left + this.m22 * r.top + this.m42;
            float x2 = this.m11 * r.right + this.m21 * r.top + this.m41;
            float y2 = this.m12 * r.right + this.m22 * r.top + this.m42;
            float x3 = this.m11 * r.left + this.m21 * r.bottom + this.m41;
            float y3 = this.m12 * r.left + this.m22 * r.bottom + this.m42;
            float x4 = this.m11 * r.right + this.m21 * r.bottom + this.m41;
            float y4 = this.m12 * r.right + this.m22 * r.bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * r.left + this.m24 * r.top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * r.right + this.m24 * r.top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * r.left + this.m24 * r.bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * r.right + this.m24 * r.bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            r.left = MathUtil.min(x1, x2, x3, x4);
            r.top = MathUtil.min(y1, y2, y3, y4);
            r.right = MathUtil.max(x1, x2, x3, x4);
            r.bottom = MathUtil.max(y1, y2, y3, y4);
        }
    }

    public void mapRect(@NonNull RectF r, @NonNull Rect out) {
        this.mapRect(r.left, r.top, r.right, r.bottom, out);
    }

    public void mapRect(@NonNull Rect r, @NonNull Rect out) {
        this.mapRect((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, out);
    }

    public void mapRect(float left, float top, float right, float bottom, @NonNull Rect out) {
        int typeMask = this.getType();
        if (typeMask <= 1) {
            out.left = Math.round(left + this.m41);
            out.top = Math.round(top + this.m42);
            out.right = Math.round(right + this.m41);
            out.bottom = Math.round(bottom + this.m42);
        } else if ((typeMask & -4) == 0) {
            out.left = Math.round(left * this.m11 + this.m41);
            out.top = Math.round(top * this.m22 + this.m42);
            out.right = Math.round(right * this.m11 + this.m41);
            out.bottom = Math.round(bottom * this.m22 + this.m42);
        } else {
            float x1 = this.m11 * left + this.m21 * top + this.m41;
            float y1 = this.m12 * left + this.m22 * top + this.m42;
            float x2 = this.m11 * right + this.m21 * top + this.m41;
            float y2 = this.m12 * right + this.m22 * top + this.m42;
            float x3 = this.m11 * left + this.m21 * bottom + this.m41;
            float y3 = this.m12 * left + this.m22 * bottom + this.m42;
            float x4 = this.m11 * right + this.m21 * bottom + this.m41;
            float y4 = this.m12 * right + this.m22 * bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * left + this.m24 * top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * left + this.m24 * bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            out.left = Math.round(MathUtil.min(x1, x2, x3, x4));
            out.top = Math.round(MathUtil.min(y1, y2, y3, y4));
            out.right = Math.round(MathUtil.max(x1, x2, x3, x4));
            out.bottom = Math.round(MathUtil.max(y1, y2, y3, y4));
        }
    }

    public void mapRectOut(@NonNull RectF r, @NonNull Rect out) {
        this.mapRectOut(r.left, r.top, r.right, r.bottom, out);
    }

    public void mapRectOut(@NonNull Rect r, @NonNull Rect out) {
        this.mapRectOut((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, out);
    }

    public void mapRectOut(float left, float top, float right, float bottom, @NonNull Rect out) {
        int typeMask = this.getType();
        if (typeMask <= 1) {
            out.left = (int) Math.floor((double) (left + this.m41));
            out.top = (int) Math.floor((double) (top + this.m42));
            out.right = (int) Math.ceil((double) (right + this.m41));
            out.bottom = (int) Math.ceil((double) (bottom + this.m42));
        } else if ((typeMask & -4) == 0) {
            out.left = (int) Math.floor((double) (left * this.m11 + this.m41));
            out.top = (int) Math.floor((double) (top * this.m22 + this.m42));
            out.right = (int) Math.ceil((double) (right * this.m11 + this.m41));
            out.bottom = (int) Math.ceil((double) (bottom * this.m22 + this.m42));
        } else {
            float x1 = this.m11 * left + this.m21 * top + this.m41;
            float y1 = this.m12 * left + this.m22 * top + this.m42;
            float x2 = this.m11 * right + this.m21 * top + this.m41;
            float y2 = this.m12 * right + this.m22 * top + this.m42;
            float x3 = this.m11 * left + this.m21 * bottom + this.m41;
            float y3 = this.m12 * left + this.m22 * bottom + this.m42;
            float x4 = this.m11 * right + this.m21 * bottom + this.m41;
            float y4 = this.m12 * right + this.m22 * bottom + this.m42;
            if ((typeMask & 8) != 0) {
                float w = 1.0F / (this.m14 * left + this.m24 * top + this.m44);
                x1 *= w;
                y1 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * top + this.m44);
                x2 *= w;
                y2 *= w;
                w = 1.0F / (this.m14 * left + this.m24 * bottom + this.m44);
                x3 *= w;
                y3 *= w;
                w = 1.0F / (this.m14 * right + this.m24 * bottom + this.m44);
                x4 *= w;
                y4 *= w;
            }
            out.left = (int) Math.floor((double) MathUtil.min(x1, x2, x3, x4));
            out.top = (int) Math.floor((double) MathUtil.min(y1, y2, y3, y4));
            out.right = (int) Math.ceil((double) MathUtil.max(x1, x2, x3, x4));
            out.bottom = (int) Math.ceil((double) MathUtil.max(y1, y2, y3, y4));
        }
    }

    public void mapPoint(@NonNull PointF p) {
        if (!this.hasPerspective()) {
            p.set(this.m11 * p.x + this.m21 * p.y + this.m41, this.m12 * p.x + this.m22 * p.y + this.m42);
        } else {
            float x = this.m11 * p.x + this.m21 * p.y + this.m41;
            float y = this.m12 * p.x + this.m22 * p.y + this.m42;
            float w = this.m14 * p.x + this.m24 * p.y + this.m44;
            if (w != 0.0F) {
                w = 1.0F / w;
            }
            p.x = x * w;
            p.y = y * w;
        }
    }
}