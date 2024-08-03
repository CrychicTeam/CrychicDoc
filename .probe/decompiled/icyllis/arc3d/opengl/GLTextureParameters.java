package icyllis.arc3d.opengl;

public final class GLTextureParameters {

    public int baseMipmapLevel;

    public int maxMipmapLevel;

    public int swizzleR = 6403;

    public int swizzleG = 6404;

    public int swizzleB = 6405;

    public int swizzleA = 6406;

    public GLTextureParameters() {
        this.baseMipmapLevel = 0;
        this.maxMipmapLevel = 1000;
    }

    public void invalidate() {
        this.baseMipmapLevel = -1;
        this.maxMipmapLevel = -1;
        this.swizzleR = 0;
        this.swizzleG = 0;
        this.swizzleB = 0;
        this.swizzleA = 0;
    }

    public int getSwizzle(int i) {
        return switch(i) {
            case 0 ->
                this.swizzleR;
            case 1 ->
                this.swizzleG;
            case 2 ->
                this.swizzleB;
            case 3 ->
                this.swizzleA;
            default ->
                throw new IndexOutOfBoundsException(i);
        };
    }

    public void setSwizzle(int i, int swiz) {
        switch(i) {
            case 0:
                this.swizzleR = swiz;
                break;
            case 1:
                this.swizzleG = swiz;
                break;
            case 2:
                this.swizzleB = swiz;
                break;
            case 3:
                this.swizzleA = swiz;
                break;
            default:
                throw new IndexOutOfBoundsException(i);
        }
    }

    public String toString() {
        return "{baseMipmapLevel=" + this.baseMipmapLevel + ", maxMipmapLevel=" + this.maxMipmapLevel + ", swizzleR=" + this.swizzleR + ", swizzleG=" + this.swizzleG + ", swizzleB=" + this.swizzleB + ", swizzleA=" + this.swizzleA + "}";
    }
}