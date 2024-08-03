package icyllis.arc3d.engine;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.lwjgl.system.NativeType;

@Immutable
public abstract class BackendFormat {

    public abstract int getBackend();

    @NativeType("GLenum")
    public int getGLFormat() {
        throw new IllegalStateException();
    }

    @NativeType("VkFormat")
    public int getVkFormat() {
        throw new IllegalStateException();
    }

    public abstract int getChannelFlags();

    public abstract boolean isSRGB();

    public abstract boolean isExternal();

    public abstract int getCompressionType();

    public final boolean isCompressed() {
        return this.getCompressionType() != 0;
    }

    @Nonnull
    public abstract BackendFormat makeInternal();

    public abstract int getBytesPerBlock();

    public abstract int getStencilBits();

    public abstract int getFormatKey();
}