package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.BackendFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.lwjgl.system.NativeType;

@Immutable
public final class VkBackendFormat extends BackendFormat {

    private static final Int2ObjectMap<VkBackendFormat> FORMATS = new Int2ObjectOpenHashMap(25);

    private final int mFormat;

    private final boolean mIsExternal;

    VkBackendFormat(@NativeType("VkFormat") int format, boolean isExternal) {
        this.mFormat = format;
        this.mIsExternal = isExternal;
    }

    @Nonnull
    public static VkBackendFormat make(@NativeType("VkFormat") int format, boolean isExternal) {
        Objects.checkIndex(format, Integer.MAX_VALUE);
        int key = format | (isExternal ? Integer.MIN_VALUE : 0);
        VkBackendFormat backendFormat = (VkBackendFormat) FORMATS.get(key);
        if (backendFormat != null) {
            return backendFormat;
        } else {
            backendFormat = new VkBackendFormat(format, isExternal);
            if (backendFormat.getBytesPerBlock() != 0) {
                FORMATS.put(key, backendFormat);
            }
            return backendFormat;
        }
    }

    @Override
    public int getBackend() {
        return 1;
    }

    @Override
    public boolean isExternal() {
        return this.mIsExternal;
    }

    @Override
    public int getChannelFlags() {
        return VKCore.vkFormatChannels(this.mFormat);
    }

    @Override
    public int getVkFormat() {
        return this.mFormat;
    }

    @Nonnull
    @Override
    public BackendFormat makeInternal() {
        return this.mIsExternal ? make(this.mFormat, false) : this;
    }

    @Override
    public boolean isSRGB() {
        return this.mFormat == 43;
    }

    @Override
    public int getCompressionType() {
        return VKCore.vkFormatCompressionType(this.mFormat);
    }

    @Override
    public int getBytesPerBlock() {
        return VKCore.vkFormatBytesPerBlock(this.mFormat);
    }

    @Override
    public int getStencilBits() {
        return VKCore.vkFormatStencilBits(this.mFormat);
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
            return o != null && this.getClass() == o.getClass() ? this.mFormat == ((VkBackendFormat) o).mFormat : false;
        }
    }

    public String toString() {
        return "{backend=Vulkan, format=" + VKCore.vkFormatName(this.mFormat) + ", isExternal=" + this.mIsExternal + "}";
    }
}