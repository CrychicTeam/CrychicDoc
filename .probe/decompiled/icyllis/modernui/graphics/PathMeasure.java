package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.Objects;
import javax.annotation.CheckReturnValue;

public class PathMeasure extends icyllis.arc3d.core.PathMeasure {

    public PathMeasure() {
    }

    public PathMeasure(@Nullable Path path, boolean forceClose) {
        super(path, forceClose);
    }

    public PathMeasure(@Nullable Path path, boolean forceClose, float resScale) {
        super(path, forceClose, resScale);
    }

    @Override
    public void reset() {
        super.reset();
    }

    public boolean reset(@Nullable Path path, boolean forceClose) {
        return super.reset(path, forceClose);
    }

    public boolean reset(@Nullable Path path, boolean forceClose, float resScale) {
        return super.reset(path, forceClose, resScale);
    }

    @Override
    public boolean nextContour() {
        return super.nextContour();
    }

    public float getLength() {
        return super.getContourLength();
    }

    public boolean isClosed() {
        return super.isContourClosed();
    }

    @CheckReturnValue
    public boolean getPosTan(float distance, @Nullable float[] position, @Nullable float[] tangent) {
        return super.getPosTan(distance, position, 0, tangent, 0);
    }

    @CheckReturnValue
    public boolean getPosTan(float distance, @Nullable PointF position, @Nullable PointF tangent) {
        boolean result = super.getPosTan(distance, position != null ? this.mTmp : null, 0, tangent != null ? this.mTmp : null, 2);
        if (result) {
            if (position != null) {
                position.set(this.mTmp[0], this.mTmp[1]);
            }
            if (tangent != null) {
                tangent.set(this.mTmp[2], this.mTmp[3]);
            }
        }
        return result;
    }

    @CheckReturnValue
    public boolean getMatrix(float distance, @Nullable Matrix matrix, int flags) {
        return super.getMatrix(distance, matrix, flags);
    }

    @CheckReturnValue
    public boolean getSegment(float startDistance, float endDistance, @NonNull Path dst, boolean startWithMoveTo) {
        Objects.requireNonNull(dst);
        return super.getSegment(startDistance, endDistance, dst, startWithMoveTo);
    }
}