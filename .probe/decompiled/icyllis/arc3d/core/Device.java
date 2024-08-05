package icyllis.arc3d.core;

import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceDrawContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Device extends RefCnt implements MatrixProvider {

    protected static final int CLIP_TYPE_EMPTY = 0;

    protected static final int CLIP_TYPE_RECT = 1;

    protected static final int CLIP_TYPE_COMPLEX = 2;

    protected final ImageInfo mInfo;

    protected final Rect2i mBounds = new Rect2i();

    final Matrix4 mLocalToDevice = Matrix4.identity();

    final Matrix mLocalToDevice33 = new Matrix();

    final Matrix4 mDeviceToGlobal = Matrix4.identity();

    final Matrix4 mGlobalToDevice = Matrix4.identity();

    public Device(ImageInfo info) {
        this.mInfo = info;
        this.mBounds.set(0, 0, info.width(), info.height());
    }

    void resize(int width, int height) {
        this.mInfo.resize(width, height);
        this.mBounds.set(0, 0, width, height);
    }

    @Override
    protected void deallocate() {
    }

    @Nonnull
    public final ImageInfo imageInfo() {
        return this.mInfo;
    }

    public final int width() {
        return this.mInfo.width();
    }

    public final int height() {
        return this.mInfo.height();
    }

    public final Rect2ic bounds() {
        return this.mBounds;
    }

    public final void getBounds(@Nonnull Rect2i bounds) {
        bounds.set(this.bounds());
    }

    public final void getGlobalBounds(@Nonnull Rect2i bounds) {
        this.mDeviceToGlobal.mapRectOut(this.bounds(), bounds);
    }

    public final void getClipBounds(@Nonnull Rect2i bounds) {
        bounds.set(this.getClipBounds());
    }

    @Nonnull
    @Override
    public final Matrix4 getLocalToDevice() {
        return this.mLocalToDevice;
    }

    public final Matrix4 getDeviceToGlobal() {
        return this.mDeviceToGlobal;
    }

    public final Matrix4 getGlobalToDevice() {
        return this.mGlobalToDevice;
    }

    public final boolean isPixelAlignedToGlobal() {
        Matrix4 mat = this.mDeviceToGlobal;
        return mat.m11 == 1.0F && mat.m12 == 0.0F && mat.m13 == 0.0F && mat.m14 == 0.0F && mat.m21 == 0.0F && mat.m22 == 1.0F && mat.m23 == 0.0F && mat.m24 == 0.0F && mat.m31 == 0.0F && mat.m32 == 0.0F && mat.m33 == 1.0F && mat.m34 == 0.0F && (double) mat.m41 == Math.floor((double) mat.m41) && (double) mat.m42 == Math.floor((double) mat.m42) && mat.m43 == 0.0F && mat.m44 == 1.0F;
    }

    public final void getRelativeTransform(@Nonnull Device device, @Nonnull Matrix4 dest) {
        dest.set(this.mDeviceToGlobal);
        dest.postConcat(device.mGlobalToDevice);
    }

    public final void save() {
        this.onSave();
    }

    public final void restore(Matrix4 globalTransform) {
        this.onRestore();
        this.setGlobalTransform(globalTransform);
    }

    public final void restoreLocal(Matrix4 localToDevice) {
        this.onRestore();
        this.setLocalToDevice(localToDevice);
    }

    public void clipRect(Rect2f rect, int clipOp, boolean doAA) {
    }

    public final void replaceClip(Rect2i rect) {
        this.onReplaceClip(rect);
    }

    protected void onReplaceClip(Rect2i rect) {
    }

    public abstract boolean clipIsAA();

    public abstract boolean clipIsWideOpen();

    public final void setGlobalTransform(@Nullable Matrix4 globalTransform) {
        if (globalTransform == null) {
            this.mLocalToDevice.setIdentity();
        } else {
            this.mLocalToDevice.set(globalTransform);
            this.mLocalToDevice.normalizePerspective();
        }
        this.mLocalToDevice.postConcat(this.mGlobalToDevice);
    }

    public final void setLocalToDevice(@Nullable Matrix4 localToDevice) {
        if (localToDevice == null) {
            this.mLocalToDevice.setIdentity();
        } else {
            this.mLocalToDevice.set(localToDevice);
        }
    }

    @Nullable
    public RecordingContext getRecordingContext() {
        return null;
    }

    @Nullable
    public SurfaceDrawContext getSurfaceDrawContext() {
        return null;
    }

    final void setCoordinateSystem(@Nullable Matrix4 deviceToGlobal, @Nullable Matrix4 globalToDevice, @Nullable Matrix4 localToDevice, int bufferOriginX, int bufferOriginY) {
        if (deviceToGlobal == null) {
            this.mDeviceToGlobal.setIdentity();
            this.mGlobalToDevice.setIdentity();
        } else {
            assert globalToDevice != null;
            this.mDeviceToGlobal.set(deviceToGlobal);
            this.mDeviceToGlobal.normalizePerspective();
            this.mGlobalToDevice.set(globalToDevice);
            this.mGlobalToDevice.normalizePerspective();
        }
        if (localToDevice == null) {
            this.mLocalToDevice.setIdentity();
        } else {
            this.mLocalToDevice.set(localToDevice);
            this.mLocalToDevice.normalizePerspective();
        }
        if ((bufferOriginX | bufferOriginY) != 0) {
            this.mDeviceToGlobal.preTranslate((float) bufferOriginX, (float) bufferOriginY);
            this.mGlobalToDevice.postTranslate((float) (-bufferOriginX), (float) (-bufferOriginY));
            this.mLocalToDevice.postTranslate((float) (-bufferOriginX), (float) (-bufferOriginY));
        }
    }

    final void setOrigin(@Nullable Matrix4 globalTransform, int x, int y) {
        this.setCoordinateSystem(null, null, globalTransform, x, y);
    }

    protected void onSave() {
    }

    protected void onRestore() {
    }

    protected abstract int getClipType();

    protected abstract Rect2ic getClipBounds();

    public abstract void drawPaint(Paint var1);

    public abstract void drawRect(Rect2f var1, Paint var2);

    @Nullable
    protected Surface makeSurface(ImageInfo info) {
        return null;
    }

    @Nullable
    protected Device createDevice(ImageInfo info, @Nullable Paint paint) {
        return null;
    }
}