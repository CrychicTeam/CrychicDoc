package icyllis.modernui.graphics;

import icyllis.arc3d.core.Rect2fc;
import icyllis.modernui.annotation.NonNull;

public class Path extends icyllis.arc3d.core.Path {

    public Path() {
    }

    public Path(@NonNull Path path) {
        super(path);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void recycle() {
        super.recycle();
    }

    public final void relativeMoveTo(float dx, float dy) {
        super.moveToRel(dx, dy);
    }

    public final void relativeLineTo(float dx, float dy) {
        super.lineToRel(dx, dy);
    }

    public final void relativeQuadTo(float dx1, float dy1, float dx2, float dy2) {
        super.quadToRel(dx1, dy1, dx2, dy2);
    }

    public final void relativeCubicTo(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) {
        super.cubicToRel(dx1, dy1, dx2, dy2, dx3, dy3);
    }

    public final void getBounds(@NonNull RectF out) {
        Rect2fc r = super.getBounds();
        out.set(r.left(), r.top(), r.right(), r.bottom());
    }
}