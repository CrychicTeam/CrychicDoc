package icyllis.modernui.graphics;

import icyllis.arc3d.core.Blender;
import icyllis.arc3d.core.Matrix4;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.OutlineFont;
import icyllis.modernui.graphics.text.ShapedText;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Canvas {

    public static final Marker MARKER = MarkerManager.getMarker("Canvas");

    protected Canvas() {
    }

    public abstract int save();

    public final int saveLayer(@Nullable RectF bounds, int alpha) {
        return bounds == null ? this.saveLayer(0.0F, 0.0F, 32768.0F, 32768.0F, alpha) : this.saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom, alpha);
    }

    public abstract int saveLayer(float var1, float var2, float var3, float var4, int var5);

    public abstract void restore();

    public abstract int getSaveCount();

    public abstract void restoreToCount(int var1);

    public final void translate(float dx, float dy) {
        if (dx != 0.0F || dy != 0.0F) {
            this.getMatrix().preTranslate(dx, dy);
        }
    }

    public final void scale(float sx, float sy) {
        if (sx != 1.0F || sy != 1.0F) {
            this.getMatrix().preScale(sx, sy);
        }
    }

    public final void scale(float sx, float sy, float px, float py) {
        if (sx != 1.0F || sy != 1.0F) {
            Matrix4 matrix = this.getMatrix();
            matrix.preTranslate(px, py);
            matrix.preScale(sx, sy);
            matrix.preTranslate(-px, -py);
        }
    }

    public final void skew(float sx, float sy) {
        this.shear(sx, sy);
    }

    public final void skew(float sx, float sy, float px, float py) {
        this.shear(sx, sy, px, py);
    }

    public final void shear(float sx, float sy) {
        if (sx != 0.0F || sy != 0.0F) {
            this.getMatrix().preShear2D(sx, sy);
        }
    }

    public final void shear(float sx, float sy, float px, float py) {
        if (sx != 0.0F || sy != 0.0F) {
            Matrix4 matrix = this.getMatrix();
            matrix.preTranslate(px, py);
            matrix.preShear2D(sx, sy);
            matrix.preTranslate(-px, -py);
        }
    }

    public final void rotate(float degrees) {
        if (degrees != 0.0F) {
            this.getMatrix().preRotateZ((double) (degrees * (float) (Math.PI / 180.0)));
        }
    }

    public final void rotate(float degrees, float px, float py) {
        if (degrees != 0.0F) {
            Matrix4 matrix = this.getMatrix();
            matrix.preTranslate(px, py, 0.0F);
            matrix.preRotateZ((double) (degrees * (float) (Math.PI / 180.0)));
            matrix.preTranslate(-px, -py, 0.0F);
        }
    }

    public final void concat(Matrix4 matrix) {
        if (!matrix.isIdentity()) {
            this.getMatrix().preConcat(matrix);
        }
    }

    public final void concat(Matrix matrix) {
        if (!matrix.isIdentity()) {
            this.getMatrix().preConcat2D(matrix);
        }
    }

    @Deprecated
    @Internal
    @NonNull
    public abstract Matrix4 getMatrix();

    public final boolean clipRect(Rect rect) {
        return this.clipRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom);
    }

    public final boolean clipRect(RectF rect) {
        return this.clipRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    public abstract boolean clipRect(float var1, float var2, float var3, float var4);

    public final boolean quickReject(RectF rect) {
        return this.quickReject(rect.left, rect.top, rect.right, rect.bottom);
    }

    public abstract boolean quickReject(float var1, float var2, float var3, float var4);

    public final void clear(@ColorInt int color) {
        this.drawColor(color, BlendMode.SRC);
    }

    public final void drawColor(@ColorInt int color) {
        this.drawColor(color, BlendMode.SRC_OVER);
    }

    public void drawColor(@ColorInt int color, BlendMode mode) {
    }

    public void drawPaint(Paint paint) {
    }

    public final void drawPoint(PointF p, Paint paint) {
        this.drawPoint(p.x, p.y, paint);
    }

    public void drawPoint(float x, float y, Paint paint) {
    }

    public void drawPoints(float[] pts, int offset, int count, Paint paint) {
        if (count >= 2) {
            count >>= 1;
            for (int i = 0; i < count; i++) {
                this.drawPoint(pts[offset++], pts[offset++], paint);
            }
        }
    }

    public final void drawPoints(float[] pts, Paint paint) {
        this.drawPoints(pts, 0, pts.length, paint);
    }

    public abstract void drawLine(float var1, float var2, float var3, float var4, float var5, @NonNull Paint var6);

    public final void drawLine(@NonNull PointF p0, @NonNull PointF p1, float thickness, @NonNull Paint paint) {
        this.drawLine(p0.x, p0.y, p1.x, p1.y, thickness, paint);
    }

    public final void drawLine(float x0, float y0, float x1, float y1, @NonNull Paint paint) {
        int pStyle = paint.getStyle();
        paint.setStyle(0);
        this.drawLine(x0, y0, x1, y1, paint.getStrokeWidth(), paint);
        paint.setStyle(pStyle);
    }

    public final void drawLine(@NonNull PointF p0, @NonNull PointF p1, @NonNull Paint paint) {
        int pStyle = paint.getStyle();
        paint.setStyle(0);
        this.drawLine(p0.x, p0.y, p1.x, p1.y, paint.getStrokeWidth(), paint);
        paint.setStyle(pStyle);
    }

    public void drawLinePath(float x0, float y0, float x1, float y1, Paint paint) {
    }

    public final void drawRect(RectF r, Paint paint) {
        this.drawRect(r.left, r.top, r.right, r.bottom, paint);
    }

    public final void drawRect(Rect r, Paint paint) {
        this.drawRect((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, paint);
    }

    public abstract void drawRect(float var1, float var2, float var3, float var4, Paint var5);

    @Experimental
    public abstract void drawRectGradient(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8, Paint var9);

    public final void drawRoundRect(RectF rect, float radius, Paint paint) {
        this.drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius, 0, paint);
    }

    public final void drawRoundRect(float left, float top, float right, float bottom, float radius, Paint paint) {
        this.drawRoundRect(left, top, right, bottom, radius, 0, paint);
    }

    public final void drawRoundRect(RectF rect, float radius, int sides, Paint paint) {
        this.drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius, sides, paint);
    }

    public abstract void drawRoundRect(float var1, float var2, float var3, float var4, float var5, int var6, Paint var7);

    @Experimental
    public abstract void drawRoundRectGradient(float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8, float var9, Paint var10);

    public abstract void drawCircle(float var1, float var2, float var3, @NonNull Paint var4);

    public final void drawCircle(@NonNull PointF center, float radius, @NonNull Paint paint) {
        this.drawCircle(center.x, center.y, radius, paint);
    }

    public abstract void drawArc(float var1, float var2, float var3, float var4, float var5, @NonNull Paint var6);

    public final void drawArc(@NonNull PointF center, float radius, float startAngle, float sweepAngle, @NonNull Paint paint) {
        this.drawArc(center.x, center.y, radius, startAngle, sweepAngle, paint);
    }

    public abstract void drawPie(float var1, float var2, float var3, float var4, float var5, @NonNull Paint var6);

    public final void drawPie(@NonNull PointF center, float radius, float startAngle, float sweepAngle, @NonNull Paint paint) {
        this.drawPie(center.x, center.y, radius, startAngle, sweepAngle, paint);
    }

    public void drawArcPath(float cx, float cy, float radius, float startAngle, float sweepAngle, @NonNull Paint paint) {
    }

    public final void drawBezier(PointF p0, PointF p1, PointF p2, Paint paint) {
        this.drawBezier(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, paint);
    }

    public abstract void drawBezier(float var1, float var2, float var3, float var4, float var5, float var6, Paint var7);

    public abstract void drawImage(Image var1, float var2, float var3, @Nullable Paint var4);

    public final void drawImage(Image image, @Nullable Rect src, RectF dst, @Nullable Paint paint) {
        if (src == null) {
            this.drawImage(image, 0.0F, 0.0F, (float) image.getWidth(), (float) image.getHeight(), dst.left, dst.top, dst.right, dst.bottom, paint);
        } else {
            this.drawImage(image, (float) src.left, (float) src.top, (float) src.right, (float) src.bottom, dst.left, dst.top, dst.right, dst.bottom, paint);
        }
    }

    public final void drawImage(Image image, @Nullable Rect src, Rect dst, @Nullable Paint paint) {
        if (src == null) {
            this.drawImage(image, 0.0F, 0.0F, (float) image.getWidth(), (float) image.getHeight(), (float) dst.left, (float) dst.top, (float) dst.right, (float) dst.bottom, paint);
        } else {
            this.drawImage(image, (float) src.left, (float) src.top, (float) src.right, (float) src.bottom, (float) dst.left, (float) dst.top, (float) dst.right, (float) dst.bottom, paint);
        }
    }

    public abstract void drawImage(Image var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, @Nullable Paint var10);

    @Deprecated
    public void drawRoundLines(float[] pts, int offset, int count, boolean strip, Paint paint) {
        if ((offset | count | pts.length - offset - count) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (count >= 4) {
            float thick = paint.getStrokeWidth();
            if (strip) {
                float x;
                float y;
                this.drawLine(pts[offset++], pts[offset++], x = pts[offset++], y = pts[offset++], thick, paint);
                count = count - 4 >> 1;
                for (int i = 0; i < count; i++) {
                    this.drawLine(x, y, x = pts[offset++], y = pts[offset++], thick, paint);
                }
            } else {
                count >>= 2;
                for (int i = 0; i < count; i++) {
                    this.drawLine(pts[offset++], pts[offset++], pts[offset++], pts[offset++], thick, paint);
                }
            }
        }
    }

    public abstract void drawRoundImage(Image var1, float var2, float var3, float var4, Paint var5);

    public abstract void drawGlyphs(@NonNull int[] var1, int var2, @NonNull float[] var3, int var4, int var5, @NonNull Font var6, float var7, float var8, @NonNull Paint var9);

    public final void drawShapedText(@NonNull ShapedText text, float x, float y, @NonNull Paint paint) {
        this.drawShapedText(text, 0, text.getGlyphCount(), x, y, paint);
    }

    public final void drawShapedText(@NonNull ShapedText text, int glyphStart, int glyphCount, float x, float y, @NonNull Paint paint) {
        if ((glyphStart | glyphCount | glyphStart + glyphCount | text.getGlyphCount() - glyphStart - glyphCount) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (glyphCount != 0) {
            Font lastFont = text.getFont(glyphStart);
            int lastPos = glyphStart;
            int currPos = glyphStart + 1;
            for (int end = glyphStart + glyphCount; currPos < end; currPos++) {
                Font curFont = text.getFont(currPos);
                if (lastFont != curFont) {
                    this.drawGlyphs(text.getGlyphs(), lastPos, text.getPositions(), lastPos << 1, currPos - lastPos, lastFont, x, y, paint);
                    lastFont = curFont;
                    lastPos = currPos;
                }
            }
            this.drawGlyphs(text.getGlyphs(), lastPos, text.getPositions(), lastPos << 1, currPos - lastPos, lastFont, x, y, paint);
        }
    }

    public final void drawSimpleText(@NonNull char[] text, @NonNull Font font, float x, float y, @NonNull Paint paint) {
        if (text.length != 0) {
            if (font instanceof OutlineFont of) {
                java.awt.Font f = of.chooseFont(paint.getFontSize());
                FontRenderContext frc = OutlineFont.getFontRenderContext(FontPaint.computeRenderFlags(paint));
                GlyphVector gv = f.createGlyphVector(frc, text);
                int nGlyphs = gv.getNumGlyphs();
                this.drawGlyphs(gv.getGlyphCodes(0, nGlyphs, null), 0, gv.getGlyphPositions(0, nGlyphs, null), 0, nGlyphs, font, x, y, paint);
            }
        }
    }

    public final void drawSimpleText(@NonNull String text, @NonNull Font font, float x, float y, @NonNull Paint paint) {
        if (!text.isBlank()) {
            this.drawSimpleText(text.toCharArray(), font, x, y, paint);
        }
    }

    public void drawMesh(@NonNull Canvas.VertexMode mode, @NonNull FloatBuffer pos, @Nullable IntBuffer color, @Nullable FloatBuffer tex, @Nullable ShortBuffer indices, @Nullable Blender blender, @NonNull Paint paint) {
    }

    public final void drawPointListMesh(@NonNull FloatBuffer pos, @Nullable IntBuffer color, @NonNull Paint paint) {
        this.drawMesh(Canvas.VertexMode.POINTS, pos, color, null, null, null, paint);
    }

    public final void drawLineListMesh(@NonNull FloatBuffer pos, @Nullable IntBuffer color, @NonNull Paint paint) {
        this.drawMesh(Canvas.VertexMode.LINES, pos, color, null, null, null, paint);
    }

    public final void drawTriangleListMesh(@NonNull FloatBuffer pos, @Nullable IntBuffer color, @NonNull Paint paint) {
        this.drawMesh(Canvas.VertexMode.TRIANGLES, pos, color, null, null, null, paint);
    }

    @Experimental
    public void drawCustomDrawable(@NonNull CustomDrawable drawable, @Nullable Matrix4 matrix) {
    }

    public final void drawCustomDrawable(@NonNull CustomDrawable drawable) {
        this.drawCustomDrawable(drawable, null);
    }

    public boolean isClipEmpty() {
        return false;
    }

    public boolean isClipRect() {
        return false;
    }

    public static enum VertexMode {

        POINTS, LINES, LINE_STRIP, TRIANGLES, TRIANGLE_STRIP
    }
}