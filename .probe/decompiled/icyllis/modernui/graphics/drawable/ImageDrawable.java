package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.BitmapFactory;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.Gravity;
import java.io.IOException;
import java.io.InputStream;

public class ImageDrawable extends Drawable {

    private Rect mSrcRect;

    private final Rect mDstRect = new Rect();

    private ImageDrawable.ImageState mImageState;

    private int mBlendColor;

    private boolean mFullImage = true;

    private boolean mDstRectAndInsetsDirty = true;

    private boolean mMutated;

    public ImageDrawable(@Nullable Image image) {
        this.init(new ImageDrawable.ImageState(image));
    }

    public ImageDrawable(@NonNull String namespace, @NonNull String path) {
        Image image = Image.create(namespace, path);
        this.init(new ImageDrawable.ImageState(image));
    }

    public ImageDrawable(@NonNull InputStream stream) {
        Image image = null;
        try (Bitmap bitmap = BitmapFactory.decodeStream(stream)) {
            image = Image.createTextureFromBitmap(bitmap);
        } catch (IOException var8) {
        }
        this.init(new ImageDrawable.ImageState(image));
    }

    @NonNull
    public final Paint getPaint() {
        return this.mImageState.mPaint;
    }

    public final Image getImage() {
        return this.mImageState.mImage;
    }

    public void setImage(@Nullable Image image) {
        if (this.mImageState.mImage != image) {
            this.mImageState.mImage = image;
            this.invalidateSelf();
        }
    }

    public int getGravity() {
        return this.mImageState.mGravity;
    }

    public void setGravity(int gravity) {
        if (this.mImageState.mGravity != gravity) {
            this.mImageState.mGravity = gravity;
            this.mDstRectAndInsetsDirty = true;
            this.invalidateSelf();
        }
    }

    public void setSrcRect(int left, int top, int right, int bottom) {
        if (this.mSrcRect == null) {
            this.mSrcRect = new Rect(left, top, right, bottom);
            this.invalidateSelf();
        } else {
            Rect oldBounds = this.mSrcRect;
            if (oldBounds.left != left || oldBounds.top != top || oldBounds.right != right || oldBounds.bottom != bottom) {
                this.mSrcRect.set(left, top, right, bottom);
                this.invalidateSelf();
            }
        }
        this.mFullImage = false;
    }

    public void setSrcRect(@Nullable Rect srcRect) {
        if (srcRect == null) {
            this.mFullImage = true;
        } else {
            if (this.mSrcRect == null) {
                this.mSrcRect = srcRect.copy();
            } else {
                this.mSrcRect.set(srcRect);
            }
            this.mFullImage = false;
        }
    }

    public void setMipmap(boolean mipmap) {
    }

    public boolean hasMipmap() {
        return true;
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        if (this.mImageState.mAutoMirrored != mirrored) {
            this.mImageState.mAutoMirrored = mirrored;
            this.invalidateSelf();
        }
    }

    @Override
    public final boolean isAutoMirrored() {
        return this.mImageState.mAutoMirrored;
    }

    private boolean needMirroring() {
        return this.isAutoMirrored() && this.getLayoutDirection() == 1;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        this.mDstRectAndInsetsDirty = true;
    }

    private void updateDstRectAndInsetsIfDirty() {
        if (this.mDstRectAndInsetsDirty) {
            Image image = this.mImageState.mImage;
            if (image != null) {
                int layoutDirection = this.getLayoutDirection();
                Gravity.apply(this.mImageState.mGravity, image.getWidth(), image.getHeight(), this.getBounds(), this.mDstRect, layoutDirection);
            }
            this.mDstRectAndInsetsDirty = false;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Image image = this.mImageState.mImage;
        if (image != null) {
            ImageDrawable.ImageState state = this.mImageState;
            Paint paint = state.mPaint;
            int restoreAlpha;
            if (this.mBlendColor >>> 24 != 0) {
                restoreAlpha = paint.getAlpha();
                paint.setColor(this.mBlendColor);
                paint.setAlpha(restoreAlpha * (this.mBlendColor >>> 24) >>> 8);
            } else {
                restoreAlpha = -1;
            }
            this.updateDstRectAndInsetsIfDirty();
            boolean needMirroring = this.needMirroring();
            if (needMirroring) {
                canvas.save();
                canvas.translate((float) this.mDstRect.width(), 0.0F);
                canvas.scale(-1.0F, 1.0F);
            }
            canvas.drawImage(image, this.mFullImage ? null : this.mSrcRect, this.mDstRect, paint);
            if (needMirroring) {
                canvas.restore();
            }
            if (restoreAlpha >= 0) {
                paint.setAlpha(restoreAlpha);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        int oldAlpha = this.mImageState.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            this.mImageState.mPaint.setAlpha(alpha);
            this.invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return this.mImageState.mPaint.getAlpha();
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        ImageDrawable.ImageState state = this.mImageState;
        if (state.mTint != tint) {
            state.mTint = tint;
            if (tint == null) {
                this.mBlendColor = -1;
            } else {
                this.mBlendColor = tint.getColorForState(this.getState(), -1);
            }
            this.invalidateSelf();
        }
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mImageState = new ImageDrawable.ImageState(this.mImageState);
            this.mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    protected boolean onStateChange(@NonNull int[] stateSet) {
        ImageDrawable.ImageState state = this.mImageState;
        if (state.mTint != null) {
            this.mBlendColor = state.mTint.getColorForState(stateSet, -1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isStateful() {
        return this.mImageState.mTint != null && this.mImageState.mTint.isStateful() || super.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mImageState.mTint != null && this.mImageState.mTint.hasFocusStateSpecified();
    }

    @Override
    public int getIntrinsicWidth() {
        if (this.mImageState.mImage == null) {
            return super.getIntrinsicWidth();
        } else {
            return this.mFullImage ? this.mImageState.mImage.getWidth() : Math.min(this.mSrcRect.width(), this.mImageState.mImage.getWidth());
        }
    }

    @Override
    public int getIntrinsicHeight() {
        if (this.mImageState.mImage == null) {
            return super.getIntrinsicHeight();
        } else {
            return this.mFullImage ? this.mImageState.mImage.getHeight() : Math.min(this.mSrcRect.height(), this.mImageState.mImage.getHeight());
        }
    }

    @Override
    public final Drawable.ConstantState getConstantState() {
        return this.mImageState;
    }

    private ImageDrawable(ImageDrawable.ImageState state) {
        this.init(state);
    }

    private void init(ImageDrawable.ImageState state) {
        this.mImageState = state;
        this.updateLocalState();
    }

    private void updateLocalState() {
        if (this.mImageState.mTint == null) {
            this.mBlendColor = -1;
        } else {
            this.mBlendColor = this.mImageState.mTint.getColorForState(this.getState(), -1);
        }
    }

    static final class ImageState extends Drawable.ConstantState {

        final Paint mPaint;

        Image mImage;

        ColorStateList mTint = null;

        int mGravity = 119;

        boolean mAutoMirrored = false;

        ImageState(Image image) {
            this.mImage = image;
            this.mPaint = new Paint();
        }

        ImageState(@NonNull ImageDrawable.ImageState imageState) {
            this.mImage = imageState.mImage;
            this.mTint = imageState.mTint;
            this.mGravity = imageState.mGravity;
            this.mPaint = new Paint(imageState.mPaint);
            this.mAutoMirrored = imageState.mAutoMirrored;
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new ImageDrawable(this);
        }
    }
}