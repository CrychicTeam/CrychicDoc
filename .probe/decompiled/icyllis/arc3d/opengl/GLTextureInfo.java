package icyllis.arc3d.opengl;

public final class GLTextureInfo {

    public int target = 3553;

    public int handle;

    public int format;

    public int levels = 0;

    public int samples = 0;

    public int memoryObject;

    public int memoryHandle = -1;

    public void set(GLTextureInfo info) {
        this.target = info.target;
        this.handle = info.handle;
        this.format = info.format;
        this.levels = info.levels;
        this.samples = info.samples;
        this.memoryObject = info.memoryObject;
        this.memoryHandle = info.memoryHandle;
    }

    public int hashCode() {
        int h = this.target;
        h = 31 * h + this.handle;
        h = 31 * h + this.format;
        h = 31 * h + this.levels;
        h = 31 * h + this.samples;
        h = 31 * h + this.memoryObject;
        return 31 * h + this.memoryHandle;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof GLTextureInfo info) ? false : this.target == info.target && this.handle == info.handle && this.format == info.format && this.levels == info.levels && this.samples == info.samples && this.memoryObject == info.memoryObject && this.memoryHandle == info.memoryHandle;
        }
    }

    public String toString() {
        return "{target=" + this.target + ", handle=" + this.handle + ", format=" + GLCore.glFormatName(this.format) + ", levels=" + this.levels + ", samples=" + this.samples + ", memoryObject=" + this.memoryObject + ", memoryHandle=" + this.memoryHandle + "}";
    }
}