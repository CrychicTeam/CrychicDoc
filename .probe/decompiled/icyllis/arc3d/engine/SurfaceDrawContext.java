package icyllis.arc3d.engine;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.Matrixc;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.engine.ops.DrawOp;
import icyllis.arc3d.engine.ops.OpsTask;
import icyllis.arc3d.engine.ops.RectOp;
import javax.annotation.Nullable;

public class SurfaceDrawContext extends SurfaceFillContext {

    private final Rect2f mTmpBounds = new Rect2f();

    private final ClipResult mTmpClipResult = new ClipResult();

    public SurfaceDrawContext(RecordingContext context, SurfaceView readView, SurfaceView writeView, int colorType, ColorSpace colorSpace) {
        super(context, readView, writeView, colorType, 2, colorSpace);
    }

    public static SurfaceDrawContext make(RecordingContext rContext, int colorType, ColorSpace colorSpace, int width, int height, int sampleCount, int surfaceFlags, int origin) {
        if (rContext != null && !rContext.isDiscarded()) {
            BackendFormat format = rContext.getCaps().getDefaultBackendFormat(colorType, true);
            if (format == null) {
                return null;
            } else {
                TextureProxy texture = rContext.getSurfaceProvider().createRenderTexture(format, width, height, sampleCount, surfaceFlags);
                if (texture == null) {
                    return null;
                } else {
                    short readSwizzle = rContext.getCaps().getReadSwizzle(format, colorType);
                    short writeSwizzle = rContext.getCaps().getWriteSwizzle(format, colorType);
                    texture.ref();
                    SurfaceView readView = new SurfaceView(texture, origin, readSwizzle);
                    SurfaceView writeView = new SurfaceView(texture, origin, writeSwizzle);
                    return new SurfaceDrawContext(rContext, readView, writeView, colorType, colorSpace);
                }
            }
        } else {
            return null;
        }
    }

    public static SurfaceDrawContext make(RecordingContext rContext, int colorType, ColorSpace colorSpace, SurfaceProxy surfaceProxy, int origin) {
        BackendFormat format = surfaceProxy.getBackendFormat();
        short readSwizzle = rContext.getCaps().getReadSwizzle(format, colorType);
        short writeSwizzle = rContext.getCaps().getWriteSwizzle(format, colorType);
        surfaceProxy.ref();
        SurfaceView readView = new SurfaceView(surfaceProxy, origin, readSwizzle);
        SurfaceView writeView = new SurfaceView(surfaceProxy, origin, writeSwizzle);
        return new SurfaceDrawContext(rContext, readView, writeView, colorType, colorSpace);
    }

    public void fillRect(@Nullable Clip clip, int color, Rect2f rect, Matrixc viewMatrix, boolean aa) {
        RectOp op = new RectOp(color, rect, 0.0F, 0.0F, viewMatrix, false, aa);
        this.addDrawOp(clip, op);
    }

    public void addDrawOp(@Nullable Clip clip, DrawOp op) {
        SurfaceProxy surface = this.getReadView().getSurface();
        Rect2f bounds = this.mTmpBounds;
        bounds.set(op);
        if (op.hasZeroArea()) {
            bounds.outset(1.0F, 1.0F);
        }
        ClipResult clipResult;
        boolean rejected;
        if (clip != null) {
            clipResult = this.mTmpClipResult;
            clipResult.init(surface.getWidth(), surface.getHeight(), surface.getBackingWidth(), surface.getBackingHeight());
            rejected = clip.apply(this, op.hasAABloat(), clipResult, bounds) == 2;
        } else {
            clipResult = null;
            rejected = !bounds.intersects(0.0F, 0.0F, (float) surface.getWidth(), (float) surface.getHeight());
        }
        if (!rejected) {
            op.setClippedBounds(bounds);
            OpsTask ops = this.getOpsTask();
            ops.addDrawOp(op, clipResult, 0);
        }
    }
}