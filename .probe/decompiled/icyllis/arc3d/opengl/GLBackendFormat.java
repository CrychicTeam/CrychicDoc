package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.BackendFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.lwjgl.system.NativeType;

@Immutable
public final class GLBackendFormat extends BackendFormat {

    private static final Int2ObjectOpenHashMap<GLBackendFormat> FORMATS = new Int2ObjectOpenHashMap(20, 0.5F);

    private final int mFormat;

    private final boolean mIsExternal;

    GLBackendFormat(@NativeType("GLenum") int format, boolean isExternal) {
        this.mFormat = format;
        this.mIsExternal = isExternal;
    }

    @Nonnull
    public static GLBackendFormat make(@NativeType("GLenum") int format) {
        return make(format, false);
    }

    @Nonnull
    public static GLBackendFormat make(@NativeType("GLenum") int format, boolean isExternal) {
        if (GLCore.glFormatIsSupported(format)) {
            assert format > 0;
            return (GLBackendFormat) FORMATS.computeIfAbsent(format | (isExternal ? Integer.MIN_VALUE : 0), k -> new GLBackendFormat(k & 2147483647, k < 0));
        } else {
            return new GLBackendFormat(format, isExternal);
        }
    }

    @Override
    public int getBackend() {
        return 0;
    }

    @Override
    public int getGLFormat() {
        return this.mFormat;
    }

    @Override
    public boolean isExternal() {
        return this.mIsExternal;
    }

    @Override
    public int getChannelFlags() {
        return GLCore.glFormatChannels(this.mFormat);
    }

    @Nonnull
    @Override
    public BackendFormat makeInternal() {
        return this.mIsExternal ? make(this.mFormat, false) : this;
    }

    @Override
    public boolean isSRGB() {
        return GLCore.glFormatIsSRGB(this.mFormat);
    }

    @Override
    public int getCompressionType() {
        return GLCore.glFormatCompressionType(this.mFormat);
    }

    @Override
    public int getBytesPerBlock() {
        return GLCore.glFormatBytesPerBlock(this.mFormat);
    }

    @Override
    public int getStencilBits() {
        return GLCore.glFormatStencilBits(this.mFormat);
    }

    @Override
    public int getFormatKey() {
        return this.mFormat;
    }

    public int hashCode() {
        return this.mFormat;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null && this.getClass() == o.getClass() ? this.mFormat == ((GLBackendFormat) o).mFormat : false;
        }
    }

    public String toString() {
        return "{mBackend=OpenGL, mFormat=" + GLCore.glFormatName(this.mFormat) + ", mIsExternal=" + this.mIsExternal + "}";
    }
}