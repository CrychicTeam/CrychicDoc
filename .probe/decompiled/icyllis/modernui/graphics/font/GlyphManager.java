package icyllis.modernui.graphics.font;

import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.BitmapFactory;
import icyllis.modernui.graphics.text.EmojiFont;
import icyllis.modernui.graphics.text.OutlineFont;
import icyllis.modernui.text.TextUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class GlyphManager {

    public static final Marker MARKER = MarkerManager.getMarker("Glyph");

    public static final int GLYPH_BORDER = 2;

    private static final Color BG_COLOR = new Color(0, 0, 0, 0);

    public static volatile boolean sAntiAliasing = true;

    public static volatile boolean sFractionalMetrics = false;

    public static final int EMOJI_SIZE = 72;

    public static final int EMOJI_ASCENT = 56;

    public static final int EMOJI_SPACING = 4;

    public static final int EMOJI_BASE = 64;

    private static volatile GlyphManager sInstance;

    private GLFontAtlas mFontAtlas;

    private GLFontAtlas mEmojiAtlas;

    private GLDevice mDevice;

    private final Object2IntOpenHashMap<Font> mFontTable = new Object2IntOpenHashMap();

    private final ToIntFunction<Font> mFontTableMapper = f -> this.mFontTable.size() + 1;

    private final Object2IntOpenHashMap<EmojiFont> mEmojiFontTable = new Object2IntOpenHashMap();

    private final ToIntFunction<EmojiFont> mEmojiFontTableMapper = f -> this.mEmojiFontTable.size() + 1;

    private BufferedImage mImage;

    private Graphics2D mGraphics;

    private int[] mImageData;

    private ByteBuffer mImageBuffer;

    private final CopyOnWriteArrayList<Consumer<GlyphManager.AtlasInvalidationInfo>> mAtlasInvalidationCallbacks = new CopyOnWriteArrayList();

    private GlyphManager() {
        this.reload();
    }

    @NonNull
    public static GlyphManager getInstance() {
        if (sInstance == null) {
            synchronized (GlyphManager.class) {
                if (sInstance == null) {
                    sInstance = new GlyphManager();
                }
            }
        }
        return sInstance;
    }

    @RenderThread
    public void reload() {
        if (this.mFontAtlas != null) {
            this.mFontAtlas.close();
        }
        if (this.mEmojiAtlas != null) {
            this.mEmojiAtlas.close();
        }
        this.mFontAtlas = null;
        this.mEmojiAtlas = null;
        this.mFontTable.clear();
        this.mFontTable.trim();
        this.allocateImage(64, 64);
    }

    @NonNull
    public GlyphVector layoutGlyphVector(@NonNull Font awtFont, @NonNull char[] text, int start, int limit, boolean isRtl) {
        return awtFont.layoutGlyphVector(this.mGraphics.getFontRenderContext(), text, start, limit, isRtl ? 1 : 0);
    }

    @NonNull
    public GlyphVector createGlyphVector(@NonNull Font awtFont, @NonNull char[] text) {
        return awtFont.createGlyphVector(this.mGraphics.getFontRenderContext(), text);
    }

    private long computeGlyphKey(@NonNull Font awtFont, int glyphCode) {
        long fontKey = (long) this.mFontTable.computeIfAbsent(awtFont, this.mFontTableMapper);
        return fontKey << 32 | (long) glyphCode;
    }

    private long computeEmojiKey(@NonNull EmojiFont font, int glyphId) {
        long fontKey = (long) this.mEmojiFontTable.computeIfAbsent(font, this.mEmojiFontTableMapper);
        return fontKey << 32 | (long) glyphId;
    }

    @Nullable
    @RenderThread
    public BakedGlyph lookupGlyph(@NonNull icyllis.modernui.graphics.text.Font font, int fontSize, int glyphId) {
        if (glyphId == 0) {
            return null;
        } else if (font instanceof OutlineFont) {
            Font awtFont = ((OutlineFont) font).chooseFont(fontSize);
            long key = this.computeGlyphKey(awtFont, glyphId);
            if (this.mFontAtlas == null) {
                DirectContext context = Core.requireDirectContext();
                this.mFontAtlas = new GLFontAtlas(context, 0, 2);
                this.mDevice = (GLDevice) context.getDevice();
            }
            BakedGlyph glyph = this.mFontAtlas.getGlyph(key);
            return glyph != null && glyph.x == -32768 ? this.cacheGlyph(awtFont, glyphId, this.mFontAtlas, glyph, key) : glyph;
        } else if (font instanceof EmojiFont emojiFont) {
            long key = this.computeEmojiKey(emojiFont, glyphId);
            if (this.mEmojiAtlas == null) {
                DirectContext context = Core.requireDirectContext();
                this.mEmojiAtlas = new GLFontAtlas(context, 2, 0);
                this.mDevice = (GLDevice) context.getDevice();
            }
            BakedGlyph glyph = this.mEmojiAtlas.getGlyph(key);
            return glyph != null && glyph.x == -32768 ? this.cacheEmoji(emojiFont, glyphId, this.mEmojiAtlas, glyph, key) : glyph;
        } else {
            return null;
        }
    }

    @RenderThread
    public int getCurrentTexture(int maskFormat) {
        if (maskFormat == 0) {
            if (this.mFontAtlas != null) {
                GLTexture texture = this.mFontAtlas.mTexture;
                if (this.mFontAtlas.mTexture != null) {
                    this.mDevice.generateMipmaps(texture);
                    return texture.getHandle();
                }
            }
        } else if (maskFormat == 2 && this.mEmojiAtlas != null) {
            GLTexture texture = this.mEmojiAtlas.mTexture;
            if (this.mEmojiAtlas.mTexture != null) {
                this.mDevice.generateMipmaps(texture);
                return texture.getHandle();
            }
        }
        return 0;
    }

    @RenderThread
    public int getCurrentTexture(icyllis.modernui.graphics.text.Font font) {
        if (font instanceof OutlineFont) {
            if (this.mFontAtlas != null) {
                GLTexture texture = this.mFontAtlas.mTexture;
                if (this.mFontAtlas.mTexture != null) {
                    this.mDevice.generateMipmaps(texture);
                    return texture.getHandle();
                }
            }
        } else if (font instanceof EmojiFont && this.mEmojiAtlas != null) {
            GLTexture texture = this.mEmojiAtlas.mTexture;
            if (this.mEmojiAtlas.mTexture != null) {
                this.mDevice.generateMipmaps(texture);
                return texture.getHandle();
            }
        }
        return 0;
    }

    @RenderThread
    public void compact() {
        if (this.mFontAtlas != null && this.mFontAtlas.compact()) {
            GlyphManager.AtlasInvalidationInfo info = new GlyphManager.AtlasInvalidationInfo(0, false);
            for (Consumer<GlyphManager.AtlasInvalidationInfo> callback : this.mAtlasInvalidationCallbacks) {
                callback.accept(info);
            }
        }
        if (this.mEmojiAtlas != null && this.mEmojiAtlas.compact()) {
            GlyphManager.AtlasInvalidationInfo info = new GlyphManager.AtlasInvalidationInfo(2, false);
            for (Consumer<GlyphManager.AtlasInvalidationInfo> callback : this.mAtlasInvalidationCallbacks) {
                callback.accept(info);
            }
        }
    }

    @RenderThread
    public void debug() {
        debug(this.mFontAtlas, "FontAtlas");
        debug(this.mEmojiAtlas, "EmojiAtlas");
    }

    private static void debug(GLFontAtlas atlas, String name) {
        if (atlas != null) {
            String path = Bitmap.saveDialogGet(Bitmap.SaveFormat.PNG, null, name);
            atlas.debug(path);
        }
    }

    public void dumpInfo(PrintWriter pw) {
        dumpInfo(pw, this.mFontAtlas, "FontAtlas");
        dumpInfo(pw, this.mEmojiAtlas, "EmojiAtlas");
    }

    private static void dumpInfo(PrintWriter pw, GLFontAtlas atlas, String name) {
        if (atlas != null) {
            pw.print(name);
            pw.print(": Glyphs=");
            pw.print(atlas.getGlyphCount());
            pw.print(", Coverage=");
            pw.printf("%.4f", atlas.getCoverage());
            pw.print(", GPUMemorySize=");
            long memorySize = atlas.getMemorySize();
            TextUtils.binaryCompact(pw, memorySize);
            pw.print(" (");
            pw.print(memorySize);
            pw.println(" bytes)");
        }
    }

    @Nullable
    @RenderThread
    private BakedGlyph cacheGlyph(@NonNull Font font, int glyphCode, @NonNull GLFontAtlas atlas, @NonNull BakedGlyph glyph, long key) {
        GlyphVector vector = font.createGlyphVector(this.mGraphics.getFontRenderContext(), new int[] { glyphCode });
        Rectangle bounds = vector.getPixelBounds(null, 0.0F, 0.0F);
        if (bounds.width != 0 && bounds.height != 0) {
            glyph.x = (short) bounds.x;
            glyph.y = (short) bounds.y;
            glyph.width = (short) bounds.width;
            glyph.height = (short) bounds.height;
            int borderedWidth = bounds.width + 4;
            int borderedHeight = bounds.height + 4;
            while (borderedWidth > this.mImage.getWidth() || borderedHeight > this.mImage.getHeight()) {
                this.allocateImage(this.mImage.getWidth() << 1, this.mImage.getHeight() << 1);
            }
            this.mGraphics.drawGlyphVector(vector, (float) (2 - bounds.x), (float) (2 - bounds.y));
            this.mImage.getRGB(0, 0, borderedWidth, borderedHeight, this.mImageData, 0, borderedWidth);
            int size = borderedWidth * borderedHeight;
            for (int i = 0; i < size; i++) {
                this.mImageBuffer.put((byte) (this.mImageData[i] >>> 24));
            }
            long src = MemoryUtil.memAddress(this.mImageBuffer.flip());
            boolean invalidated = atlas.stitch(glyph, src);
            if (invalidated) {
                GlyphManager.AtlasInvalidationInfo info = new GlyphManager.AtlasInvalidationInfo(0, true);
                for (Consumer<GlyphManager.AtlasInvalidationInfo> callback : this.mAtlasInvalidationCallbacks) {
                    callback.accept(info);
                }
            }
            this.mGraphics.clearRect(0, 0, this.mImage.getWidth(), this.mImage.getHeight());
            this.mImageBuffer.clear();
            return glyph;
        } else {
            atlas.setNoPixels(key);
            return null;
        }
    }

    @Nullable
    @RenderThread
    private BakedGlyph cacheEmoji(@NonNull EmojiFont font, int glyphId, @NonNull GLFontAtlas atlas, @NonNull BakedGlyph glyph, long key) {
        String path = "emoji/" + font.getFileName(glyphId);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredFormat = Bitmap.Format.RGBA_8888;
        try {
            InputStream inputStream = ModernUI.getInstance().getResourceStream("modernui", path);
            BakedGlyph var23;
            label94: {
                Object var22;
                try (Bitmap bitmap = BitmapFactory.decodeStream(inputStream, opts)) {
                    if (bitmap.getWidth() == 72 && bitmap.getHeight() == 72) {
                        long src = bitmap.getAddress();
                        glyph.x = 0;
                        glyph.y = -56;
                        glyph.width = 72;
                        glyph.height = 72;
                        boolean invalidated = atlas.stitch(glyph, src);
                        if (invalidated) {
                            GlyphManager.AtlasInvalidationInfo info = new GlyphManager.AtlasInvalidationInfo(2, true);
                            for (Consumer<GlyphManager.AtlasInvalidationInfo> callback : this.mAtlasInvalidationCallbacks) {
                                callback.accept(info);
                            }
                        }
                        var23 = glyph;
                        break label94;
                    }
                    atlas.setNoPixels(key);
                    ModernUI.LOGGER.warn(MARKER, "Emoji is not {}x{}: {} {}", 72, 72, font.getFamilyName(), path);
                    var22 = null;
                } catch (Throwable var20) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var17) {
                            var20.addSuppressed(var17);
                        }
                    }
                    throw var20;
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return (BakedGlyph) var22;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return var23;
        } catch (Exception var21) {
            atlas.setNoPixels(key);
            ModernUI.LOGGER.warn(MARKER, "Failed to load emoji: {} {}", font.getFamilyName(), path, var21);
            return null;
        }
    }

    private void allocateImage(int width, int height) {
        this.mImage = new BufferedImage(width, height, 2);
        this.mGraphics = this.mImage.createGraphics();
        this.mImageData = new int[width * height];
        this.mImageBuffer = BufferUtils.createByteBuffer(this.mImageData.length);
        this.mGraphics.setBackground(BG_COLOR);
        this.mGraphics.setComposite(AlphaComposite.Src);
        this.mGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (sAntiAliasing) {
            this.mGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            this.mGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        if (sFractionalMetrics) {
            this.mGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            this.mGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
    }

    public void addAtlasInvalidationCallback(Consumer<GlyphManager.AtlasInvalidationInfo> callback) {
        this.mAtlasInvalidationCallbacks.add((Consumer) Objects.requireNonNull(callback));
    }

    public void removeAtlasInvalidationCallback(Consumer<GlyphManager.AtlasInvalidationInfo> callback) {
        this.mAtlasInvalidationCallbacks.remove(Objects.requireNonNull(callback));
    }

    public static record AtlasInvalidationInfo(int maskFormat, boolean resize) {
    }
}