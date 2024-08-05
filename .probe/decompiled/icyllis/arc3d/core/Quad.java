package icyllis.arc3d.core;

public class Quad {

    public static final int kAxisAligned = 0;

    public static final int kRectilinear = 1;

    public static final int kGeneral = 2;

    public static final int kPerspective = 3;

    private float x0;

    private float x1;

    private float x2;

    private float x3;

    private float y0;

    private float y1;

    private float y2;

    private float y3;

    private float w0;

    private float w1;

    private float w2;

    private float w3;

    private int type;

    public Quad(Rect2fc rect) {
        this.x0 = this.x1 = rect.left();
        this.y0 = this.y2 = rect.top();
        this.x2 = this.x3 = rect.right();
        this.y1 = this.y3 = rect.bottom();
        this.w0 = this.w1 = this.w2 = this.w3 = 1.0F;
        this.type = 0;
    }

    public Quad(Rect2fc rect, Matrixc m) {
        int mask = m.getType();
        if (mask <= 3) {
            float r0 = rect.left();
            float r1 = rect.top();
            float r2 = rect.right();
            float r3 = rect.bottom();
            if (mask > 0) {
                if (mask > 1) {
                    r0 *= m.m11();
                    r1 *= m.m22();
                    r2 *= m.m11();
                    r3 *= m.m22();
                }
                r0 += m.m41();
                r1 += m.m42();
                r2 += m.m41();
                r3 += m.m42();
            }
            this.x0 = this.x1 = r0;
            this.y0 = this.y2 = r1;
            this.x2 = this.x3 = r2;
            this.y1 = this.y3 = r3;
            this.w0 = this.w1 = this.w2 = this.w3 = 1.0F;
            this.type = 0;
        } else {
            float rx1;
            float rx0 = rx1 = rect.left();
            float ry2;
            float ry0 = ry2 = rect.top();
            float rx3;
            float rx2 = rx3 = rect.right();
            float ry3;
            float ry1 = ry3 = rect.bottom();
            this.x0 = m.m11() * rx0 + m.m21() * ry0 + m.m41();
            this.x1 = m.m11() * rx1 + m.m21() * ry1 + m.m41();
            this.x2 = m.m11() * rx2 + m.m21() * ry2 + m.m41();
            this.x3 = m.m11() * rx3 + m.m21() * ry3 + m.m41();
            this.y0 = m.m22() * rx0 + m.m12() * ry0 + m.m42();
            this.y1 = m.m22() * rx1 + m.m12() * ry1 + m.m42();
            this.y2 = m.m22() * rx2 + m.m12() * ry2 + m.m42();
            this.y3 = m.m22() * rx3 + m.m12() * ry3 + m.m42();
            if (m.hasPerspective()) {
                this.w0 = m.m14() * rx0 + m.m24() * ry0 + m.m44();
                this.w1 = m.m14() * rx1 + m.m24() * ry1 + m.m44();
                this.w2 = m.m14() * rx2 + m.m24() * ry2 + m.m44();
                this.w3 = m.m14() * rx3 + m.m24() * ry3 + m.m44();
            } else {
                this.w0 = this.w1 = this.w2 = this.w3 = 1.0F;
            }
            if (m.isAxisAligned()) {
                this.type = 0;
            } else if (m.preservesRightAngles()) {
                this.type = 1;
            } else if (m.hasPerspective()) {
                this.type = 3;
            } else {
                this.type = 2;
            }
        }
    }

    public float x0() {
        return this.x0;
    }

    public float x1() {
        return this.x1;
    }

    public float x2() {
        return this.x2;
    }

    public float x3() {
        return this.x3;
    }

    public float y0() {
        return this.y0;
    }

    public float y1() {
        return this.y1;
    }

    public float y2() {
        return this.y2;
    }

    public float y3() {
        return this.y3;
    }

    public float w0() {
        return this.w0;
    }

    public float w1() {
        return this.w1;
    }

    public float w2() {
        return this.w2;
    }

    public float w3() {
        return this.w3;
    }

    public float x(int i) {
        return switch(i) {
            case 0 ->
                this.x0;
            case 1 ->
                this.x1;
            case 2 ->
                this.x2;
            case 3 ->
                this.x3;
            default ->
                throw new IndexOutOfBoundsException(i);
        };
    }

    public float y(int i) {
        return switch(i) {
            case 0 ->
                this.y0;
            case 1 ->
                this.y1;
            case 2 ->
                this.y2;
            case 3 ->
                this.y3;
            default ->
                throw new IndexOutOfBoundsException(i);
        };
    }

    public float w(int i) {
        return switch(i) {
            case 0 ->
                this.w0;
            case 1 ->
                this.w1;
            case 2 ->
                this.w2;
            case 3 ->
                this.w3;
            default ->
                throw new IndexOutOfBoundsException(i);
        };
    }

    public void point(int i, float[] p) {
        if (this.type == 3) {
            p[0] = this.x(i) / this.w(i);
            p[1] = this.y(i) / this.w(i);
        } else {
            p[0] = this.x(i);
            p[1] = this.y(i);
        }
    }
}