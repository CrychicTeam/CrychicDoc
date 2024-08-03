package icyllis.arc3d.core;

public interface PathConsumer {

    void moveTo(float var1, float var2);

    void lineTo(float var1, float var2);

    void quadTo(float var1, float var2, float var3, float var4);

    default void quadTo(float[] pts, int off) {
        this.quadTo(pts[off], pts[off + 1], pts[off + 2], pts[off + 3]);
    }

    void cubicTo(float var1, float var2, float var3, float var4, float var5, float var6);

    default void cubicTo(float[] pts, int off) {
        this.cubicTo(pts[off], pts[off + 1], pts[off + 2], pts[off + 3], pts[off + 4], pts[off + 5]);
    }

    void closePath();

    void pathDone();
}