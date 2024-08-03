package icyllis.arc3d.core;

import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceDrawContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Canvas implements AutoCloseable {

    public static final int INIT_WITH_PREVIOUS_SAVE_LAYER_FLAG = 4;

    public static final int F16_COLOR_TYPE_SAVE_LAYER_FLAG = 16;

    protected static final int FULL_LAYER_SAVE_LAYER_STRATEGY = 0;

    protected static final int NO_LAYER_SAVE_LAYER_STRATEGY = 1;

    private static final int MAX_MC_POOL_SIZE = 32;

    private static final Matrix4 IDENTITY_MATRIX = Matrix4.identity();

    private final Device mBaseDevice;

    private final Rect2f mQuickRejectBounds = new Rect2f();

    Surface mSurface;

    private Canvas.MCRec[] mMCStack = new Canvas.MCRec[32];

    private int mMCIndex = 0;

    private int mSaveCount;

    private final MarkerStack mMarkerStack = new MarkerStack();

    private final Rect2f mTmpRect = new Rect2f();

    private final Matrix4 mTmpMatrix = new Matrix4();

    private final Paint mTmpPaint = new Paint();

    public Canvas() {
        this(0, 0);
    }

    public Canvas(int width, int height) {
        this(new NoPixelsDevice(0, 0, Math.max(width, 0), Math.max(height, 0)));
    }

    Canvas(Device device) {
        this.mSaveCount = 1;
        this.mMCStack[0] = new Canvas.MCRec(device);
        this.mBaseDevice = device;
        this.computeQuickRejectBounds();
    }

    @Nonnull
    public ImageInfo getImageInfo() {
        return this.mBaseDevice.imageInfo();
    }

    public final int getBaseLayerWidth() {
        return this.mBaseDevice.width();
    }

    public final int getBaseLayerHeight() {
        return this.mBaseDevice.height();
    }

    @Nullable
    public Surface makeSurface(ImageInfo info) {
        return this.getTopDevice().makeSurface(info);
    }

    @Nullable
    public RecordingContext getRecordingContext() {
        return this.getTopDevice().getRecordingContext();
    }

    @Nullable
    public final Surface getSurface() {
        return this.mSurface;
    }

    public final int save() {
        int i = this.mSaveCount++;
        this.top().mDeferredSaveCount++;
        return i;
    }

    public final int saveLayer(@Nullable Rect2f bounds, @Nullable Paint paint) {
        return this.saveLayer(bounds, paint, null, 0);
    }

    public final int saveLayer(float left, float top, float right, float bottom, @Nullable Paint paint) {
        this.mTmpRect.set(left, top, right, bottom);
        return this.saveLayer(this.mTmpRect, paint, null, 0);
    }

    public final int saveLayerAlpha(@Nullable Rect2f bounds, int alpha) {
        alpha = MathUtil.clamp(alpha, 0, 255);
        if (alpha == 255) {
            return this.saveLayer(bounds, null, null, 0);
        } else {
            this.mTmpPaint.setAlpha(alpha);
            int i = this.saveLayer(bounds, this.mTmpPaint, null, 0);
            this.mTmpPaint.reset();
            return i;
        }
    }

    public final int saveLayerAlpha(float left, float top, float right, float bottom, int alpha) {
        this.mTmpRect.set(left, top, right, bottom);
        return this.saveLayerAlpha(this.mTmpRect, alpha);
    }

    public final int saveLayer(@Nullable Rect2f bounds, @Nullable Paint paint, @Nullable ImageFilter backdrop, int saveLayerFlags) {
        if (paint != null && paint.nothingToDraw()) {
            int i = this.save();
            this.clipRect(0, 0, 0, 0);
            return i;
        } else {
            int strategy = this.getSaveLayerStrategy(bounds, paint, backdrop, saveLayerFlags);
            int i = this.mSaveCount++;
            this.internalSaveLayer(bounds, paint, backdrop, saveLayerFlags, strategy);
            return i;
        }
    }

    public final int saveLayer(float left, float top, float right, float bottom, @Nullable Paint paint, @Nullable ImageFilter backdrop, int saveLayerFlags) {
        this.mTmpRect.set(left, top, right, bottom);
        return this.saveLayer(this.mTmpRect, paint, backdrop, saveLayerFlags);
    }

    public final void restore() {
        if (this.top().mDeferredSaveCount > 0) {
            this.mSaveCount--;
            this.top().mDeferredSaveCount--;
        } else {
            if (this.mMCIndex <= 0) {
                throw new IllegalStateException("Stack underflow");
            }
            this.willRestore();
            this.mSaveCount--;
            this.internalRestore();
            this.didRestore();
        }
    }

    public final int getSaveCount() {
        return this.mSaveCount;
    }

    public final void restoreToCount(int saveCount) {
        if (saveCount < 1) {
            throw new IllegalStateException("Stack underflow");
        } else {
            int n = this.getSaveCount() - saveCount;
            for (int i = 0; i < n; i++) {
                this.restore();
            }
        }
    }

    public final void translate(float dx, float dy) {
        if (dx != 0.0F || dy != 0.0F) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preTranslate(dx, dy);
            this.getTopDevice().setGlobalTransform(transform);
            this.didTranslate(dx, dy);
        }
    }

    public final void scale(float sx, float sy) {
        if (sx != 1.0F || sy != 1.0F) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preScale(sx, sy);
            this.getTopDevice().setGlobalTransform(transform);
            this.didScale(sx, sy);
        }
    }

    public final void scale(float sx, float sy, float px, float py) {
        if (sx != 1.0F || sy != 1.0F) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preTranslate(px, py);
            transform.preScale(sx, sy);
            transform.preTranslate(-px, -py);
            this.getTopDevice().setGlobalTransform(transform);
            this.didScale(sx, sy, px, py);
        }
    }

    public final void rotate(float degrees) {
        if (degrees != 0.0F) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preRotateZ((double) (degrees * (float) (Math.PI / 180.0)));
            this.getTopDevice().setGlobalTransform(transform);
            this.didRotate(degrees);
        }
    }

    public final void rotate(float degrees, float px, float py) {
        if (degrees != 0.0F) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preTranslate(px, py);
            transform.preRotateZ((double) (degrees * (float) (Math.PI / 180.0)));
            transform.preTranslate(-px, -py);
            this.getTopDevice().setGlobalTransform(transform);
            this.didRotate(degrees, px, py);
        }
    }

    public final void concat(Matrix4 matrix) {
        if (!matrix.isIdentity()) {
            this.checkForDeferredSave();
            Matrix4 transform = this.top().mMatrix;
            transform.preConcat(matrix);
            this.getTopDevice().setGlobalTransform(matrix);
            this.didConcat(matrix);
        }
    }

    public final void setMarker(String name) {
        if (validateMarker(name)) {
            this.mMarkerStack.setMarker(name.hashCode(), this.top().mMatrix, this.mMCIndex);
        }
    }

    public final boolean findMarker(String name, Matrix4 out) {
        return validateMarker(name) && this.mMarkerStack.findMarker(name.hashCode(), out);
    }

    public final void setMatrix(Matrix4 matrix) {
        this.checkForDeferredSave();
        this.internalSetMatrix(matrix);
        this.didSetMatrix(matrix);
    }

    public final void resetMatrix() {
        this.setMatrix(IDENTITY_MATRIX);
    }

    public final void clipRect(Rect2i rect) {
        this.mTmpRect.set(rect);
        this.clipRect(this.mTmpRect, false);
    }

    public final void clipRect(int left, int top, int right, int bottom) {
        this.mTmpRect.set((float) left, (float) top, (float) right, (float) bottom);
        this.clipRect(this.mTmpRect, false);
    }

    public final void clipRect(Rect2f rect) {
        this.clipRect(rect, false);
    }

    public final void clipRect(float left, float top, float right, float bottom) {
        this.mTmpRect.set(left, top, right, bottom);
        this.clipRect(this.mTmpRect, false);
    }

    public final void clipRect(Rect2f rect, boolean doAA) {
        if (rect.isFinite()) {
            this.checkForDeferredSave();
            rect.sort();
            this.onClipRect(rect, doAA);
        }
    }

    public final void clipRect(float left, float top, float right, float bottom, boolean doAA) {
        this.mTmpRect.set(left, top, right, bottom);
        this.clipRect(this.mTmpRect, doAA);
    }

    public final boolean quickReject(Rect2f rect) {
        return this.quickReject(rect.mLeft, rect.mTop, rect.mRight, rect.mBottom);
    }

    public final boolean quickReject(float left, float top, float right, float bottom) {
        this.mTmpRect.set(left, top, right, bottom);
        this.top().mMatrix.mapRect(this.mTmpRect);
        return !this.mTmpRect.isFinite() || !this.mTmpRect.intersects(this.mQuickRejectBounds);
    }

    public final boolean getLocalClipBounds(Rect2f bounds) {
        Device device = this.getTopDevice();
        if (device.getClipType() == 0) {
            bounds.setEmpty();
            return false;
        } else if (!this.top().mMatrix.invert(this.mTmpMatrix)) {
            bounds.setEmpty();
            return false;
        } else {
            bounds.set(device.getClipBounds());
            device.getDeviceToGlobal().mapRect(bounds);
            bounds.roundOut(bounds);
            bounds.inset(-1.0F, -1.0F);
            this.mTmpMatrix.mapRect(bounds);
            return !bounds.isEmpty();
        }
    }

    public final boolean getDeviceClipBounds(Rect2i bounds) {
        Device device = this.getTopDevice();
        if (device.getClipType() == 0) {
            bounds.setEmpty();
            return false;
        } else {
            bounds.set(device.getClipBounds());
            device.getDeviceToGlobal().mapRectOut(bounds, bounds);
            return !bounds.isEmpty();
        }
    }

    public final void clear(@ColorInt int color) {
        this.drawColor(color, BlendMode.SRC);
    }

    public final void clear(Color color) {
        this.drawColor(color, BlendMode.SRC);
    }

    public final void clear(float r, float g, float b, float a) {
        this.drawColor(r, g, b, a, BlendMode.SRC);
    }

    public final void drawColor(@ColorInt int color) {
        this.drawColor(color, BlendMode.SRC_OVER);
    }

    public final void drawColor(Color color) {
        this.drawColor(color, BlendMode.SRC_OVER);
    }

    public final void drawColor(float r, float g, float b, float a) {
        this.drawColor(r, g, b, a, BlendMode.SRC_OVER);
    }

    public final void drawColor(@ColorInt int color, BlendMode mode) {
        Paint paint = this.mTmpPaint;
        paint.setColor(color);
        paint.setBlender(mode);
        this.drawPaint(paint);
        paint.reset();
    }

    public final void drawColor(Color color, BlendMode mode) {
        Paint paint = this.mTmpPaint;
        paint.setRGBA(color.mR, color.mG, color.mB, color.mA);
        paint.setBlender(mode);
        this.drawPaint(paint);
        paint.reset();
    }

    public final void drawColor(float r, float g, float b, float a, BlendMode mode) {
        Paint paint = this.mTmpPaint;
        paint.setRGBA(r, g, b, a);
        paint.setBlender(mode);
        this.drawPaint(paint);
        paint.reset();
    }

    public void drawPaint(Paint paint) {
        this.internalDrawPaint(paint);
    }

    public void drawLine(float x0, float y0, float x1, float y1, float size, Paint paint) {
    }

    public final void drawRect(Rect2f r, Paint paint) {
        this.drawRect(r.mLeft, r.mTop, r.mRight, r.mBottom, paint);
    }

    public final void drawRect(Rect2i r, Paint paint) {
        this.drawRect((float) r.mLeft, (float) r.mTop, (float) r.mRight, (float) r.mBottom, paint);
    }

    public void drawRect(float left, float top, float right, float bottom, Paint paint) {
    }

    public final void drawRoundRect(Rect2f rect, float radius, Paint paint) {
        this.drawRoundRect(rect.mLeft, rect.mTop, rect.mRight, rect.mBottom, radius, radius, radius, radius, paint);
    }

    public final void drawRoundRect(float left, float top, float right, float bottom, float radius, Paint paint) {
        this.drawRoundRect(left, top, right, bottom, radius, radius, radius, radius, paint);
    }

    public final void drawRoundRect(Rect2f rect, float rUL, float rUR, float rLR, float rLL, Paint paint) {
        this.drawRoundRect(rect.mLeft, rect.mTop, rect.mRight, rect.mBottom, rUL, rUR, rLR, rLL, paint);
    }

    public void drawRoundRect(float left, float top, float right, float bottom, float rUL, float rUR, float rLR, float rLL, Paint paint) {
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
    }

    public void drawArc(float cx, float cy, float radius, float startAngle, float sweepAngle, Paint paint) {
    }

    public void drawBezier(float x0, float y0, float x1, float y1, float x2, float y2, Paint paint) {
    }

    public void drawTriangle(float x0, float y0, float x1, float y1, float x2, float y2, Paint paint) {
    }

    public void drawImage(Image image, float left, float top, @Nullable Paint paint) {
    }

    public final void drawImage(Image image, @Nullable Rect2i src, Rect2f dst, @Nullable Paint paint) {
        if (src == null) {
            this.drawImage(image, 0.0F, 0.0F, (float) image.getWidth(), (float) image.getHeight(), dst.mLeft, dst.mTop, dst.mRight, dst.mBottom, paint);
        } else {
            this.drawImage(image, (float) src.mLeft, (float) src.mTop, (float) src.mRight, (float) src.mBottom, dst.mLeft, dst.mTop, dst.mRight, dst.mBottom, paint);
        }
    }

    public final void drawImage(Image image, @Nullable Rect2i src, Rect2i dst, @Nullable Paint paint) {
        if (src == null) {
            this.drawImage(image, 0.0F, 0.0F, (float) image.getWidth(), (float) image.getHeight(), (float) dst.mLeft, (float) dst.mTop, (float) dst.mRight, (float) dst.mBottom, paint);
        } else {
            this.drawImage(image, (float) src.mLeft, (float) src.mTop, (float) src.mRight, (float) src.mBottom, (float) dst.mLeft, (float) dst.mTop, (float) dst.mRight, (float) dst.mBottom, paint);
        }
    }

    public void drawImage(Image image, float srcLeft, float srcTop, float srcRight, float srcBottom, float dstLeft, float dstTop, float dstRight, float dstBottom, @Nullable Paint paint) {
    }

    public void drawRoundImage(Image image, float left, float top, float radius, Paint paint) {
    }

    public final boolean isClipEmpty() {
        return this.getTopDevice().getClipType() == 0;
    }

    public final boolean isClipRect() {
        return this.getTopDevice().getClipType() == 1;
    }

    public final void getLocalToDevice(Matrix4 storage) {
        storage.set(this.top().mMatrix);
    }

    public void close() {
        if (this.mSurface != null) {
            throw new IllegalStateException("Surface-created canvas is owned by Surface, use Surface#close instead");
        }
    }

    protected void willSave() {
    }

    protected int getSaveLayerStrategy(@Nullable Rect2f bounds, @Nullable Paint paint, @Nullable ImageFilter backdrop, int saveLayerFlags) {
        return 0;
    }

    protected void willRestore() {
    }

    protected void didRestore() {
    }

    protected void didTranslate(float dx, float dy) {
    }

    protected void didScale(float sx, float sy) {
    }

    protected void didScale(float sx, float sy, float px, float py) {
    }

    protected void didRotate(float degrees) {
    }

    protected void didRotate(float degrees, float px, float py) {
    }

    protected void didConcat(Matrix4 matrix) {
    }

    protected void didSetMarker(String name) {
    }

    protected void didSetMatrix(Matrix4 matrix) {
    }

    protected void onClipRect(Rect2f rect, boolean doAA) {
        this.getTopDevice().clipRect(rect, 1, doAA);
        this.computeQuickRejectBounds();
    }

    @Nonnull
    private Canvas.MCRec push() {
        int i = ++this.mMCIndex;
        Canvas.MCRec[] stack = this.mMCStack;
        if (i == stack.length) {
            this.mMCStack = new Canvas.MCRec[i + (i >> 1)];
            System.arraycopy(stack, 0, this.mMCStack, 0, i);
            stack = this.mMCStack;
        }
        Canvas.MCRec rec = stack[i];
        if (rec == null) {
            stack[i] = rec = new Canvas.MCRec();
        }
        return rec;
    }

    private void pop() {
        if (this.mMCIndex-- >= 32) {
            this.mMCStack[this.mMCIndex + 1] = null;
        }
    }

    @Nonnull
    private Canvas.MCRec top() {
        return this.mMCStack[this.mMCIndex];
    }

    @Nonnull
    private Device getTopDevice() {
        return this.top().mDevice;
    }

    @Nullable
    private SurfaceDrawContext topDeviceSurfaceDrawContext() {
        return this.getTopDevice().getSurfaceDrawContext();
    }

    private void checkForDeferredSave() {
        if (this.top().mDeferredSaveCount > 0) {
            this.doSave();
        }
    }

    private void doSave() {
        this.willSave();
        this.top().mDeferredSaveCount--;
        this.internalSave();
    }

    private void internalSave() {
        Canvas.MCRec rec = this.top();
        this.push().set(rec);
        this.getTopDevice().save();
    }

    private void internalRestore() {
        this.mMarkerStack.restore(this.mMCIndex);
        this.pop();
        if (this.mMCIndex != -1) {
            this.getTopDevice().restore(this.top().mMatrix);
            this.computeQuickRejectBounds();
        }
    }

    private void internalSaveLayer(@Nullable Rect2f bounds, @Nullable Paint paint, @Nullable ImageFilter backdrop, int saveLayerFlags, int saveLayerStrategy) {
        if (backdrop != null) {
            Rect2f var8 = null;
        }
        ImageFilter imageFilter = paint != null ? paint.getImageFilter() : null;
        Matrix4 stashedMatrix = this.top().mMatrix;
        if (imageFilter != null) {
        }
        this.internalSave();
    }

    private void internalSetMatrix(Matrix4 matrix) {
        Matrix4 transform = this.top().mMatrix;
        transform.set(matrix);
        this.getTopDevice().setGlobalTransform(transform);
    }

    private void internalDrawPaint(Paint paint) {
        if (!paint.nothingToDraw() && !this.isClipEmpty()) {
            this.getTopDevice().drawPaint(paint);
        }
    }

    private void computeQuickRejectBounds() {
        Device device = this.getTopDevice();
        if (device.getClipType() == 0) {
            this.mQuickRejectBounds.setEmpty();
        } else {
            this.mQuickRejectBounds.set(device.getClipBounds());
            device.getDeviceToGlobal().mapRect(this.mQuickRejectBounds);
            this.mQuickRejectBounds.inset(-1.0F, -1.0F);
        }
    }

    private static boolean validateMarker(String name) {
        if (name.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c != '_' && !Character.isLetterOrDigit(c)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final class MCRec {

        Device mDevice;

        final Matrix4 mMatrix = new Matrix4();

        int mDeferredSaveCount;

        MCRec() {
        }

        MCRec(Device device) {
            this.mDevice = device;
            this.mMatrix.setIdentity();
            this.mDeferredSaveCount = 0;
        }

        void set(Canvas.MCRec prev) {
            this.mDevice = prev.mDevice;
            this.mMatrix.set(prev.mMatrix);
            this.mDeferredSaveCount = 0;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SaveLayerFlag {
    }
}