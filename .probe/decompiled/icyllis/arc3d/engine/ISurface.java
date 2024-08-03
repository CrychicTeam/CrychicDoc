package icyllis.arc3d.engine;

import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.RefCounted;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ISurface extends RefCounted {

    int FLAG_NONE = 0;

    int FLAG_BUDGETED = 1;

    int FLAG_APPROX_FIT = 2;

    int FLAG_MIPMAPPED = 4;

    int FLAG_RENDERABLE = 8;

    int FLAG_PROTECTED = 16;

    @Internal
    int FLAG_READ_ONLY = 32;

    @Internal
    int FLAG_SKIP_ALLOCATOR = 64;

    @Internal
    int FLAG_DEFERRED_PROVIDER = 128;

    @Internal
    int FLAG_GL_WRAP_DEFAULT_FB = 256;

    @Internal
    int FLAG_MANUAL_MSAA_RESOLVE = 512;

    @Internal
    int FLAG_VK_WRAP_SECONDARY_CB = 1024;

    static int getApproxSize(int size) {
        int MIN_SCRATCH_TEXTURE_SIZE = 16;
        size = Math.max(16, size);
        if (MathUtil.isPow2(size)) {
            return size;
        } else {
            int ceilPow2 = MathUtil.ceilPow2(size);
            if (size <= 1024) {
                return ceilPow2;
            } else if (size <= 16384) {
                int floorPow2 = ceilPow2 >> 1;
                int mid = floorPow2 + (floorPow2 >> 1);
                return size <= mid ? mid : ceilPow2;
            } else {
                return MathUtil.alignTo(size, 4096);
            }
        }
    }

    @Override
    void ref();

    @Override
    void unref();

    int getWidth();

    int getHeight();

    @Nonnull
    BackendFormat getBackendFormat();

    default int getSampleCount() {
        return 1;
    }
}