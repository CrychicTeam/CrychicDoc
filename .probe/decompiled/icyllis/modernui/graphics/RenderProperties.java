package icyllis.modernui.graphics;

import icyllis.arc3d.core.Matrix4;
import icyllis.modernui.annotation.FloatRange;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderProperties {

    static final int CLIP_TO_BOUNDS = 1;

    static final int CLIP_TO_CLIP_BOUNDS = 2;

    @Nullable
    private Matrix4 mTransform;

    @Nullable
    private Matrix mMatrix;

    @Nullable
    private Matrix mInverseMatrix;

    @Nullable
    private Rect mClipBounds;

    @Nullable
    private Matrix mAnimationMatrix;

    private boolean mForceToLayer = false;

    private int mLayerAlpha;

    private BlendMode mLayerMode = null;

    private int mLeft = 0;

    private int mTop = 0;

    private int mRight = 0;

    private int mBottom = 0;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mClippingFlags = 1;

    private float mAlpha = 1.0F;

    private float mTranslationX = 0.0F;

    private float mTranslationY = 0.0F;

    private float mTranslationZ = 0.0F;

    private float mElevation;

    private float mRotationX = 0.0F;

    private float mRotationY = 0.0F;

    private float mRotationZ = 0.0F;

    private float mScaleX = 1.0F;

    private float mScaleY = 1.0F;

    private float mPivotX = 0.0F;

    private float mPivotY = 0.0F;

    private boolean mHasOverlappingRendering = false;

    private boolean mPivotExplicitlySet = false;

    private boolean mMatrixOrPivotDirty = false;

    private float mCameraDistance = 1.0F;

    private boolean mCameraDistanceExplicitlySet = false;

    public RenderProperties() {
        this.setLayerPaint(null);
    }

    void computeTransform() {
        this.mMatrixOrPivotDirty = false;
        if (!this.mPivotExplicitlySet) {
            this.mPivotX = (float) this.mWidth / 2.0F;
            this.mPivotY = (float) this.mHeight / 2.0F;
        }
        if (!this.mCameraDistanceExplicitlySet) {
            this.mCameraDistance = (float) Math.max(this.mWidth, this.mHeight);
        }
        if (this.mTransform == null) {
            this.mTransform = new Matrix4();
        }
        Matrix4 matrix = this.mTransform;
        matrix.setIdentity();
        if (Math.abs(this.mRotationX) == 0.0F && Math.abs(this.mRotationY) == 0.0F) {
            matrix.preRotateZ(Math.toRadians((double) this.mRotationZ));
        } else {
            matrix.m34 = -1.0F / this.mCameraDistance;
            matrix.preRotate(Math.toRadians((double) this.mRotationX), Math.toRadians((double) this.mRotationY), Math.toRadians((double) this.mRotationZ));
        }
        matrix.preTranslate(this.mTranslationX, this.mTranslationY, this.mTranslationZ);
        matrix.preScale(this.mScaleX, this.mScaleY);
        matrix.preTranslate(-this.mPivotX, -this.mPivotY);
        matrix.postTranslate(this.mPivotX + this.mTranslationX, this.mPivotY + this.mTranslationY, this.mTranslationZ);
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        matrix.toMatrix(this.mMatrix);
    }

    @Nullable
    public Matrix getMatrix() {
        if (this.mMatrixOrPivotDirty) {
            this.computeTransform();
        }
        return this.mMatrix;
    }

    @Nullable
    public Matrix getInverseMatrix() {
        Matrix matrix = this.getMatrix();
        if (matrix == null) {
            return null;
        } else {
            if (this.mInverseMatrix == null) {
                this.mInverseMatrix = new Matrix();
            }
            return matrix.invert(this.mInverseMatrix) ? this.mInverseMatrix : null;
        }
    }

    public boolean setUseCompositingLayer(boolean forceToLayer, @Nullable Paint paint) {
        boolean changed = this.mForceToLayer != forceToLayer;
        if (changed) {
            this.mForceToLayer = forceToLayer;
        }
        return changed | this.setLayerPaint(forceToLayer ? paint : null);
    }

    public boolean getUseCompositingLayer() {
        return this.mForceToLayer;
    }

    private boolean setLayerPaint(@Nullable Paint paint) {
        boolean changed = false;
        int alpha = Paint.getAlphaDirect(paint);
        if (this.mLayerAlpha != alpha) {
            this.mLayerAlpha = alpha;
            changed = true;
        }
        BlendMode mode = paint != null ? paint.getBlendMode() : BlendMode.SRC_OVER;
        if (this.mLayerMode != mode) {
            this.mLayerMode = mode;
            changed = true;
        }
        return changed;
    }

    public boolean getUseTransientLayer() {
        return this.mAlpha < 1.0F && this.mAlpha > 0.0F && this.mHasOverlappingRendering;
    }

    public int getLayerAlpha() {
        return this.mLayerAlpha;
    }

    public BlendMode getLayerBlendMode() {
        return this.mLayerMode;
    }

    public boolean setClipBounds(@Nullable Rect clipBounds) {
        if (clipBounds == null) {
            if ((this.mClippingFlags & 2) != 0) {
                this.mClippingFlags &= -3;
                return true;
            } else {
                return false;
            }
        } else {
            boolean ret = (this.mClippingFlags & 2) == 0;
            if (ret) {
                this.mClippingFlags |= 2;
            }
            if (this.mClipBounds == null) {
                this.mClipBounds = new Rect(clipBounds);
            } else {
                if (this.mClipBounds.equals(clipBounds)) {
                    return ret;
                }
                this.mClipBounds.set(clipBounds);
            }
            return true;
        }
    }

    public boolean setClipToBounds(boolean clipToBounds) {
        if (clipToBounds) {
            if ((this.mClippingFlags & 1) == 0) {
                this.mClippingFlags |= 1;
                return true;
            }
        } else if ((this.mClippingFlags & 1) != 0) {
            this.mClippingFlags &= -2;
            return true;
        }
        return false;
    }

    public boolean getClipToBounds() {
        return (this.mClippingFlags & 1) != 0;
    }

    public boolean setAnimationMatrix(@Nullable Matrix matrix) {
        if (matrix == null) {
            if (this.mMatrix != null) {
                this.mMatrix = null;
                return true;
            } else {
                return false;
            }
        } else {
            if (this.mAnimationMatrix == null) {
                this.mAnimationMatrix = new Matrix(matrix);
            } else {
                if (this.mAnimationMatrix.isApproxEqual(matrix)) {
                    return false;
                }
                this.mAnimationMatrix.set(matrix);
            }
            return true;
        }
    }

    @Nullable
    public Matrix getAnimationMatrix() {
        return this.mAnimationMatrix;
    }

    public boolean setAlpha(float alpha) {
        if (alpha <= 0.001F) {
            alpha = 0.0F;
        } else if (alpha >= 0.999F) {
            alpha = 1.0F;
        }
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            return true;
        } else {
            return false;
        }
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public boolean setHasOverlappingRendering(boolean hasOverlappingRendering) {
        if (this.mHasOverlappingRendering != hasOverlappingRendering) {
            this.mHasOverlappingRendering = hasOverlappingRendering;
            return true;
        } else {
            return false;
        }
    }

    public boolean getHasOverlappingRendering() {
        return this.mHasOverlappingRendering;
    }

    public boolean setElevation(float lift) {
        if (this.mElevation != lift) {
            this.mElevation = lift;
            return true;
        } else {
            return false;
        }
    }

    public float getElevation() {
        return this.mElevation;
    }

    public boolean setTranslationX(float translationX) {
        if (this.mTranslationX != translationX) {
            this.mTranslationX = translationX;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getTranslationX() {
        return this.mTranslationX;
    }

    public boolean setTranslationY(float translationY) {
        if (this.mTranslationY != translationY) {
            this.mTranslationY = translationY;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getTranslationY() {
        return this.mTranslationY;
    }

    public boolean setTranslationZ(float translationZ) {
        if (this.mTranslationZ != translationZ) {
            this.mTranslationZ = translationZ;
            return true;
        } else {
            return false;
        }
    }

    public float getTranslationZ() {
        return this.mTranslationZ;
    }

    public boolean setX(float x) {
        return this.setTranslationX(x - (float) this.mLeft);
    }

    public float getX() {
        return (float) this.mLeft + this.mTranslationX;
    }

    public boolean setY(float y) {
        return this.setTranslationY(y - (float) this.mTop);
    }

    public float getY() {
        return (float) this.mTop + this.mTranslationY;
    }

    public boolean setZ(float z) {
        return this.setTranslationZ(z - this.mElevation);
    }

    public float getZ() {
        return this.mElevation + this.mTranslationZ;
    }

    public boolean setRotationX(float rotationX) {
        if (this.mRotationX != rotationX) {
            this.mRotationX = rotationX;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getRotationX() {
        return this.mRotationX;
    }

    public boolean setRotationY(float rotationY) {
        if (this.mRotationY != rotationY) {
            this.mRotationY = rotationY;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getRotationY() {
        return this.mRotationY;
    }

    public boolean setRotationZ(float rotationZ) {
        if (this.mRotationZ != rotationZ) {
            this.mRotationZ = rotationZ;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getRotationZ() {
        return this.mRotationZ;
    }

    public boolean setScaleX(float scaleX) {
        if (this.mScaleX != scaleX) {
            this.mScaleX = scaleX;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public boolean setScaleY(float scaleY) {
        if (this.mScaleY != scaleY) {
            this.mScaleY = scaleY;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public boolean setPivotX(float pivotX) {
        boolean dirty = this.mPivotX != pivotX;
        if (dirty) {
            this.mPivotX = pivotX;
        }
        if (!dirty && this.mPivotExplicitlySet) {
            return false;
        } else {
            this.mMatrixOrPivotDirty = true;
            this.mPivotExplicitlySet = true;
            return true;
        }
    }

    public float getPivotX() {
        return !this.mPivotExplicitlySet ? (float) this.mWidth / 2.0F : this.mPivotX;
    }

    public boolean setPivotY(float pivotY) {
        boolean dirty = this.mPivotY != pivotY;
        if (dirty) {
            this.mPivotY = pivotY;
        }
        if (!dirty && this.mPivotExplicitlySet) {
            return false;
        } else {
            this.mMatrixOrPivotDirty = true;
            this.mPivotExplicitlySet = true;
            return true;
        }
    }

    public float getPivotY() {
        return !this.mPivotExplicitlySet ? (float) this.mHeight / 2.0F : this.mPivotY;
    }

    public boolean isPivotExplicitlySet() {
        return this.mPivotExplicitlySet;
    }

    public boolean resetPivot() {
        if (this.mPivotExplicitlySet) {
            this.mPivotExplicitlySet = false;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean setCameraDistance(@FloatRange(from = 0.0, to = Float.MAX_VALUE) float distance) {
        if (Float.isFinite(distance) && !(distance <= 0.0F)) {
            boolean dirty = this.mCameraDistance != distance;
            if (dirty) {
                this.mCameraDistance = distance;
            }
            if (!dirty && this.mCameraDistanceExplicitlySet) {
                return false;
            } else {
                this.mMatrixOrPivotDirty = true;
                this.mCameraDistanceExplicitlySet = true;
                return true;
            }
        } else {
            throw new IllegalArgumentException("distance must be finite & positive, given=" + distance);
        }
    }

    @FloatRange(from = 0.0, to = Float.MAX_VALUE)
    public float getCameraDistance() {
        return !this.mCameraDistanceExplicitlySet ? (float) Math.max(this.mWidth, this.mHeight) : this.mCameraDistance;
    }

    public boolean isCameraDistanceExplicitlySet() {
        return this.mCameraDistanceExplicitlySet;
    }

    public boolean resetCameraDistance() {
        if (this.mCameraDistanceExplicitlySet) {
            this.mCameraDistanceExplicitlySet = false;
            this.mMatrixOrPivotDirty = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean setLeft(int left) {
        if (this.mLeft != left) {
            this.mLeft = left;
            this.mWidth = this.mRight - left;
            if (!this.mPivotExplicitlySet) {
                this.mMatrixOrPivotDirty = true;
            }
            return true;
        } else {
            return false;
        }
    }

    public int getLeft() {
        return this.mLeft;
    }

    public boolean setTop(int top) {
        if (this.mTop != top) {
            this.mTop = top;
            this.mHeight = this.mBottom - top;
            if (!this.mPivotExplicitlySet) {
                this.mMatrixOrPivotDirty = true;
            }
            return true;
        } else {
            return false;
        }
    }

    public int getTop() {
        return this.mTop;
    }

    public boolean setRight(int right) {
        if (this.mRight != right) {
            this.mRight = right;
            this.mWidth = right - this.mLeft;
            if (!this.mPivotExplicitlySet) {
                this.mMatrixOrPivotDirty = true;
            }
            return true;
        } else {
            return false;
        }
    }

    public int getRight() {
        return this.mRight;
    }

    public boolean setBottom(int bottom) {
        if (this.mBottom != bottom) {
            this.mBottom = bottom;
            this.mHeight = bottom - this.mTop;
            if (!this.mPivotExplicitlySet) {
                this.mMatrixOrPivotDirty = true;
            }
            return true;
        } else {
            return false;
        }
    }

    public int getBottom() {
        return this.mBottom;
    }

    public boolean setPosition(int left, int top, int right, int bottom) {
        if (left == this.mLeft && top == this.mTop && right == this.mRight && bottom == this.mBottom) {
            return false;
        } else {
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mWidth = right - left;
            this.mHeight = bottom - top;
            if (!this.mPivotExplicitlySet) {
                this.mMatrixOrPivotDirty = true;
            }
            return true;
        }
    }

    public boolean setPosition(@Nonnull Rect position) {
        return this.setPosition(position.left, position.top, position.right, position.bottom);
    }

    public boolean offsetLeftAndRight(int offset) {
        if (offset != 0) {
            this.mLeft += offset;
            this.mRight += offset;
            return true;
        } else {
            return false;
        }
    }

    public boolean offsetTopAndBottom(int offset) {
        if (offset != 0) {
            this.mTop += offset;
            this.mBottom += offset;
            return true;
        } else {
            return false;
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }
}