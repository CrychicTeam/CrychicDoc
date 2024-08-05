package icyllis.arc3d.engine;

import java.io.PrintWriter;

public final class ContextOptions {

    public Boolean mSkipGLErrorChecks = null;

    public int mMaxTextureSizeOverride = Integer.MAX_VALUE;

    public boolean mSharpenMipmappedTextures = false;

    public boolean mSupportBilerpFromGlyphAtlas = false;

    public boolean mReducedShaderVariations = false;

    public long mGlyphCacheTextureMaximumBytes = 8388608L;

    public Boolean mAllowMultipleGlyphCacheTextures = null;

    public float mMinDistanceFieldFontSize = 18.0F;

    public float mGlyphsAsPathsFontSize = 384.0F;

    public PrintWriter mErrorWriter = null;

    public int mInternalMultisampleCount = 4;

    public int mMaxRuntimeProgramCacheSize = 256;

    public int mMaxVkSecondaryCommandBufferCacheSize = -1;

    public DriverBugWorkarounds mDriverBugWorkarounds;
}