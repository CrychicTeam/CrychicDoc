package icyllis.arc3d.opengl;

public final class GLFramebufferInfo {

    public int mFramebuffer;

    public int mFormat;

    public void set(GLFramebufferInfo info) {
        this.mFramebuffer = info.mFramebuffer;
        this.mFormat = info.mFormat;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GLFramebufferInfo that = (GLFramebufferInfo) o;
            return this.mFramebuffer != that.mFramebuffer ? false : this.mFormat == that.mFormat;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.mFramebuffer;
        return 31 * result + this.mFormat;
    }
}