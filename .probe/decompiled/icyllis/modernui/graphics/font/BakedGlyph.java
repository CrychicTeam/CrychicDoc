package icyllis.modernui.graphics.font;

public class BakedGlyph {

    public short x = -32768;

    public short y;

    public short width;

    public short height;

    public float u1;

    public float v1;

    public float u2;

    public float v2;

    public String toString() {
        return "Glyph{x=" + this.x + ",y=" + this.y + ",w=" + this.width + ",h=" + this.height + ",u1=" + this.u1 + ",v1=" + this.v1 + ",u2=" + this.u2 + ",v2=" + this.v2 + "}";
    }
}