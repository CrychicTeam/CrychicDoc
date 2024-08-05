package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import java.io.PrintWriter;
import java.util.Objects;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Context extends RefCnt {

    protected final SharedContextInfo mContextInfo;

    private static PrintWriter sDefaultErrorWriter;

    protected Context(SharedContextInfo contextInfo) {
        this.mContextInfo = contextInfo;
    }

    public final int getBackend() {
        return this.mContextInfo.getBackend();
    }

    @Nullable
    public final BackendFormat getDefaultBackendFormat(int colorType, boolean renderable) {
        return this.mContextInfo.getDefaultBackendFormat(colorType, renderable);
    }

    @Nullable
    public final BackendFormat getCompressedBackendFormat(int compressionType) {
        return this.mContextInfo.getCompressedBackendFormat(compressionType);
    }

    public final int getMaxSurfaceSampleCount(int colorType) {
        return this.mContextInfo.getMaxSurfaceSampleCount(colorType);
    }

    public final SharedContextInfo getContextInfo() {
        return this.mContextInfo;
    }

    @Internal
    public final boolean matches(Context c) {
        return this.mContextInfo.matches(c);
    }

    @Internal
    public final ContextOptions getOptions() {
        return this.mContextInfo.getOptions();
    }

    @Internal
    public final int getContextID() {
        return this.mContextInfo.getContextID();
    }

    @Internal
    public final Caps getCaps() {
        return this.mContextInfo.getCaps();
    }

    @Internal
    public final PrintWriter getErrorWriter() {
        return (PrintWriter) Objects.requireNonNullElseGet(this.getOptions().mErrorWriter, Context::getDefaultErrorWriter);
    }

    protected boolean init() {
        return this.mContextInfo.isValid();
    }

    private static PrintWriter getDefaultErrorWriter() {
        PrintWriter err = sDefaultErrorWriter;
        if (err == null) {
            sDefaultErrorWriter = err = new PrintWriter(System.err, true);
        }
        return err;
    }
}