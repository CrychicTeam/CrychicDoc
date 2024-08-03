package icyllis.modernui.graphics;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.DrawableInfo;

public interface CustomDrawable {

    CustomDrawable.DrawHandler snapDrawHandler(int var1, Matrix4 var2, Rect2i var3, ImageInfo var4);

    RectF getBounds();

    @FunctionalInterface
    public interface DrawHandler extends AutoCloseable {

        void draw(DirectContext var1, DrawableInfo var2);

        default void close() {
        }
    }
}