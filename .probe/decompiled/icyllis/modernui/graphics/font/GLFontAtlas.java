package icyllis.modernui.graphics.font;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.RectanglePacker;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.opengl.GLBackendFormat;
import icyllis.arc3d.opengl.GLCaps;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Bitmap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RenderThread
public class GLFontAtlas implements AutoCloseable {

    public static final int CHUNK_SIZE = 512;

    public static volatile boolean sLinearSampling = true;

    private final Long2ObjectMap<BakedGlyph> mGlyphs = new Long2ObjectOpenHashMap();

    GLTexture mTexture = null;

    private final List<GLFontAtlas.Chunk> mChunks = new ArrayList();

    private int mWidth = 0;

    private int mHeight = 0;

    private final Rect2i mRect = new Rect2i();

    private final DirectContext mContext;

    private final int mMaskFormat;

    private final int mBorderWidth;

    private final int mMaxTextureSize;

    private int mLastCompactChunkIndex;

    @RenderThread
    public GLFontAtlas(DirectContext context, int maskFormat, int borderWidth) {
        this.mContext = context;
        this.mMaskFormat = maskFormat;
        this.mBorderWidth = borderWidth;
        this.mMaxTextureSize = Math.min(this.mContext.getMaxTextureSize(), maskFormat == 0 ? 8192 : 4096);
        assert this.mMaxTextureSize >= 1024;
    }

    @Nullable
    public BakedGlyph getGlyph(long key) {
        return (BakedGlyph) this.mGlyphs.computeIfAbsent(key, __ -> new BakedGlyph());
    }

    public void setNoPixels(long key) {
        this.mGlyphs.put(key, null);
    }

    public boolean stitch(@NonNull BakedGlyph glyph, long pixels) {
        boolean invalidated = false;
        if (this.mWidth == 0) {
            this.resize();
        }
        Rect2i rect = this.mRect;
        rect.set(0, 0, glyph.width + this.mBorderWidth * 2, glyph.height + this.mBorderWidth * 2);
        boolean inserted = false;
        for (GLFontAtlas.Chunk chunk : this.mChunks) {
            if (chunk.packer.addRect(rect)) {
                inserted = true;
                rect.offset(chunk.x, chunk.y);
                break;
            }
        }
        if (!inserted) {
            invalidated = this.resize();
            for (GLFontAtlas.Chunk chunkx : this.mChunks) {
                if (chunkx.packer.addRect(rect)) {
                    inserted = true;
                    rect.offset(chunkx.x, chunkx.y);
                    break;
                }
            }
        }
        if (!inserted) {
            return invalidated;
        } else {
            int colorType = this.mMaskFormat == 2 ? 6 : 19;
            int rowBytes = rect.width() * ImageInfo.bytesPerPixel(colorType);
            boolean res = this.mContext.getDevice().writePixels(this.mTexture, rect.x(), rect.y(), rect.width(), rect.height(), colorType, colorType, rowBytes, pixels);
            if (!res) {
                ModernUI.LOGGER.warn(GlyphManager.MARKER, "Failed to write glyph pixels");
            }
            glyph.u1 = (float) (rect.mLeft + this.mBorderWidth) / (float) this.mWidth;
            glyph.v1 = (float) (rect.mTop + this.mBorderWidth) / (float) this.mHeight;
            glyph.u2 = (float) (rect.mRight - this.mBorderWidth) / (float) this.mWidth;
            glyph.v2 = (float) (rect.mBottom - this.mBorderWidth) / (float) this.mHeight;
            return invalidated;
        }
    }

    private boolean resize() {
        if (this.mTexture == null) {
            this.mWidth = this.mHeight = 1024;
            this.mTexture = this.createTexture();
            for (int x = 0; x < this.mWidth; x += 512) {
                for (int y = 0; y < this.mHeight; y += 512) {
                    this.mChunks.add(new GLFontAtlas.Chunk(x, y, RectanglePacker.make(512, 512)));
                }
            }
        } else {
            int oldWidth = this.mWidth;
            int oldHeight = this.mHeight;
            if (oldWidth == this.mMaxTextureSize && oldHeight == this.mMaxTextureSize) {
                ModernUI.LOGGER.warn(GlyphManager.MARKER, "Font atlas reached max texture size, mask format: {}, max size: {}, current texture: {}", this.mMaskFormat, this.mMaxTextureSize, this.mTexture);
                return false;
            }
            boolean vertical;
            if (this.mHeight != this.mWidth) {
                this.mWidth <<= 1;
                for (int x = this.mWidth / 2; x < this.mWidth; x += 512) {
                    for (int y = 0; y < this.mHeight; y += 512) {
                        this.mChunks.add(new GLFontAtlas.Chunk(x, y, RectanglePacker.make(512, 512)));
                    }
                }
                vertical = false;
            } else {
                this.mHeight <<= 1;
                for (int x = 0; x < this.mWidth; x += 512) {
                    for (int y = this.mHeight / 2; y < this.mHeight; y += 512) {
                        this.mChunks.add(new GLFontAtlas.Chunk(x, y, RectanglePacker.make(512, 512)));
                    }
                }
                vertical = true;
            }
            GLTexture newTexture = this.createTexture();
            boolean res = this.mContext.getDevice().copySurface(this.mTexture, 0, 0, newTexture, 0, 0, oldWidth, oldHeight);
            if (!res) {
                ModernUI.LOGGER.warn(GlyphManager.MARKER, "Failed to copy to new texture");
            }
            this.mTexture = GpuResource.move(this.mTexture, newTexture);
            if (vertical) {
                ObjectIterator var6 = this.mGlyphs.values().iterator();
                while (var6.hasNext()) {
                    BakedGlyph glyph = (BakedGlyph) var6.next();
                    if (glyph != null) {
                        glyph.v1 *= 0.5F;
                        glyph.v2 *= 0.5F;
                    }
                }
            } else {
                ObjectIterator var15 = this.mGlyphs.values().iterator();
                while (var15.hasNext()) {
                    BakedGlyph glyph = (BakedGlyph) var15.next();
                    if (glyph != null) {
                        glyph.u1 *= 0.5F;
                        glyph.u2 *= 0.5F;
                    }
                }
            }
        }
        int boundTexture = GLCore.glGetInteger(32873);
        GLCore.glBindTexture(3553, this.mTexture.getHandle());
        GLCore.glTexParameteri(3553, 10240, 9728);
        GLCore.glTexParameteri(3553, 10241, sLinearSampling ? 9987 : 9728);
        if (this.mMaskFormat == 0) {
            GLCore.glTexParameteri(3553, 36418, 1);
            GLCore.glTexParameteri(3553, 36419, 1);
            GLCore.glTexParameteri(3553, 36420, 1);
            GLCore.glTexParameteri(3553, 36421, 6403);
        }
        GLCore.glBindTexture(3553, boundTexture);
        return true;
    }

    private GLTexture createTexture() {
        return (GLTexture) Objects.requireNonNull(this.mContext.getResourceProvider().createTexture(this.mWidth, this.mHeight, GLBackendFormat.make(this.mMaskFormat == 2 ? '聘' : '舩'), 1, 5, "FontAtlas" + this.mMaskFormat), "Failed to create font atlas");
    }

    public GLTexture getTexture() {
        return this.mTexture;
    }

    public boolean compact() {
        if (this.mWidth < this.mMaxTextureSize && this.mHeight < this.mMaxTextureSize) {
            return false;
        } else {
            assert this.mChunks.size() > 1;
            double coverage = 0.0;
            for (GLFontAtlas.Chunk chunk : this.mChunks) {
                coverage += chunk.packer.getCoverage();
            }
            int chunksPerDim = this.mMaxTextureSize / 512;
            double maxCoverage = (double) ((float) (chunksPerDim * chunksPerDim) * 0.25F);
            if (coverage <= maxCoverage) {
                return false;
            } else {
                double coverageToClean = Math.max(coverage - maxCoverage, maxCoverage);
                boolean cleared = false;
                for (int iChunk = 0; iChunk < Math.min(16, this.mChunks.size()) && coverageToClean > 0.0; iChunk++) {
                    assert MathUtil.isPow2(this.mChunks.size());
                    int index = this.mLastCompactChunkIndex++ & this.mChunks.size() - 1;
                    GLFontAtlas.Chunk chunk = (GLFontAtlas.Chunk) this.mChunks.get(index);
                    double cc = chunk.packer.getCoverage();
                    if (cc != 0.0) {
                        coverageToClean -= cc;
                        chunk.packer.clear();
                        float cu1 = (float) chunk.x / (float) this.mWidth;
                        float cv1 = (float) chunk.y / (float) this.mHeight;
                        float cu2 = cu1 + 512.0F / (float) this.mWidth;
                        float cv2 = cv1 + 512.0F / (float) this.mHeight;
                        ObjectIterator var18 = this.mGlyphs.values().iterator();
                        while (var18.hasNext()) {
                            BakedGlyph glyph = (BakedGlyph) var18.next();
                            if (glyph.u1 >= cu1 && glyph.u2 < cu2 && glyph.v1 >= cv1 && glyph.v2 < cv2) {
                                glyph.x = -32768;
                            }
                        }
                        cleared = true;
                    }
                }
                return cleared;
            }
        }
    }

    public void debug(@Nullable String path) {
        if (path == null) {
            ObjectIterator var2 = this.mGlyphs.long2ObjectEntrySet().iterator();
            while (var2.hasNext()) {
                Entry<BakedGlyph> glyph = (Entry<BakedGlyph>) var2.next();
                ModernUI.LOGGER.info(GlyphManager.MARKER, "Key 0x{}: {}", Long.toHexString(glyph.getLongKey()), glyph.getValue());
            }
        } else if (Core.isOnRenderThread()) {
            ModernUI.LOGGER.info(GlyphManager.MARKER, "Glyphs: {}", this.mGlyphs.size());
            if (this.mTexture == null) {
                return;
            }
            dumpAtlas((GLCaps) this.mContext.getCaps(), this.mTexture, this.mMaskFormat == 2 ? Bitmap.Format.RGBA_8888 : Bitmap.Format.GRAY_8, path);
        }
    }

    @RenderThread
    public static void dumpAtlas(GLCaps caps, GLTexture texture, Bitmap.Format format, String path) {
        if (caps.hasDSASupport()) {
            int width = texture.getWidth();
            int height = texture.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, format);
            GLCore.glPixelStorei(3330, 0);
            GLCore.glPixelStorei(3331, 0);
            GLCore.glPixelStorei(3332, 0);
            GLCore.glPixelStorei(3333, 1);
            int externalGlFormat = switch(format) {
                case GRAY_8 ->
                    'ᤃ';
                case GRAY_ALPHA_88 ->
                    '舧';
                case RGB_888 ->
                    'ᤇ';
                case RGBA_8888 ->
                    'ᤈ';
                default ->
                    throw new IllegalArgumentException();
            };
            GLCore.glGetTextureImage(texture.getHandle(), 0, externalGlFormat, 5121, (int) bitmap.getSize(), bitmap.getAddress());
            CompletableFuture.runAsync(() -> {
                try {
                    Bitmap e = bitmap;
                    try {
                        bitmap.saveToPath(Bitmap.SaveFormat.PNG, 0, Path.of(path));
                    } catch (Throwable var6x) {
                        if (bitmap != null) {
                            try {
                                e.close();
                            } catch (Throwable var5x) {
                                var6x.addSuppressed(var5x);
                            }
                        }
                        throw var6x;
                    }
                    if (bitmap != null) {
                        bitmap.close();
                    }
                } catch (IOException var7x) {
                    ModernUI.LOGGER.warn(GlyphManager.MARKER, "Failed to save font atlas", var7x);
                }
            });
        }
    }

    public void close() {
        this.mTexture = GpuResource.move(this.mTexture);
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getGlyphCount() {
        return this.mGlyphs.size();
    }

    public long getMemorySize() {
        return this.mTexture != null ? this.mTexture.getMemorySize() : 0L;
    }

    public double getCoverage() {
        if (this.mChunks.isEmpty()) {
            return 0.0;
        } else {
            double coverage = 0.0;
            for (GLFontAtlas.Chunk chunk : this.mChunks) {
                coverage += chunk.packer.getCoverage();
            }
            return coverage / (double) this.mChunks.size();
        }
    }

    private static record Chunk(int x, int y, RectanglePacker packer) {
    }
}