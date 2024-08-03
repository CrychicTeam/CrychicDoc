package noppes.npcs.shared.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import noppes.npcs.shared.common.util.LRUHashMap;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TrueTypeFont {

    private static final int MaxWidth = 512;

    private static final List<Font> allFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());

    private List<Font> usedFonts = new ArrayList();

    private LinkedHashMap<String, TrueTypeFont.GlyphCache> textcache = new LRUHashMap<>(100);

    private Map<Character, TrueTypeFont.Glyph> glyphcache = new HashMap();

    private List<TrueTypeFont.TextureCache> textures = new ArrayList();

    private Font font;

    private int lineHeight = 1;

    private Graphics2D globalG = (Graphics2D) new BufferedImage(1, 1, 2).getGraphics();

    public float scale = 1.0F;

    private int specialChar = 167;

    public TrueTypeFont(Font font, float scale) {
        this.font = font;
        this.scale = scale;
        this.globalG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.lineHeight = this.globalG.getFontMetrics(font).getHeight();
    }

    public TrueTypeFont(ResourceLocation resource, int fontSize, float scale) throws IOException, FontFormatException {
        Resource r = (Resource) Minecraft.getInstance().getResourceManager().m_213713_(resource).orElse(null);
        if (r != null) {
            try {
                InputStream stream = r.open();
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Font font = Font.createFont(0, stream);
                ge.registerFont(font);
                this.font = font.deriveFont(0, (float) fontSize);
                this.scale = scale;
                this.globalG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                this.lineHeight = this.globalG.getFontMetrics(font).getHeight();
            } catch (IOException var8) {
                throw var8;
            }
        }
    }

    public void setSpecial(char c) {
        this.specialChar = c;
    }

    public void draw(PoseStack posestack, String text, float x, float y, int color) {
        TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        RenderSystem.enableBlend();
        posestack.pushPose();
        posestack.translate(x, y, 0.0F);
        posestack.scale(this.scale, this.scale, 1.0F);
        int rr = r;
        int gg = g;
        int bb = b;
        float i = 0.0F;
        for (TrueTypeFont.Glyph gl : cache.glyphs) {
            if (gl.type != TrueTypeFont.GlyphType.NORMAL) {
                if (gl.type == TrueTypeFont.GlyphType.RESET) {
                    rr = r;
                    gg = g;
                    bb = b;
                } else if (gl.type == TrueTypeFont.GlyphType.COLOR) {
                    rr = gl.color >> 16 & 0xFF;
                    gg = gl.color >> 8 & 0xFF;
                    bb = gl.color & 0xFF;
                }
            } else {
                RenderSystem.setShaderTexture(0, gl.texture);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                this.fillGradient(posestack.last().pose(), i, 0.0F, (float) gl.x * this.textureScale(), (float) gl.y * this.textureScale(), (float) gl.width * this.textureScale(), (float) gl.height * this.textureScale(), rr, gg, bb);
                i += (float) gl.width * this.textureScale();
            }
        }
        RenderSystem.disableBlend();
        posestack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void fillGradient(Matrix4f m, float x, float y, float textureX, float textureY, float width, float height, int r, int g, int b) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        int zLevel = 0;
        RenderSystem.setShader(GameRenderer::m_172820_);
        BufferBuilder tessellator = Tesselator.getInstance().getBuilder();
        tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        tessellator.m_252986_(m, x, y + height, (float) zLevel).uv(textureX * f, (textureY + height) * f1).color(r, g, b, 255).endVertex();
        tessellator.m_252986_(m, x + width, y + height, (float) zLevel).uv((textureX + width) * f, (textureY + height) * f1).color(r, g, b, 255).endVertex();
        tessellator.m_252986_(m, x + width, y, (float) zLevel).uv((textureX + width) * f, textureY * f1).color(r, g, b, 255).endVertex();
        tessellator.m_252986_(m, x, y, (float) zLevel).uv(textureX * f, textureY * f1).color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(tessellator.end());
    }

    private TrueTypeFont.GlyphCache getOrCreateCache(String text) {
        TrueTypeFont.GlyphCache cache = (TrueTypeFont.GlyphCache) this.textcache.get(text);
        if (cache != null) {
            return cache;
        } else {
            cache = new TrueTypeFont.GlyphCache();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == this.specialChar && i + 1 < text.length()) {
                    char next = text.toLowerCase(Locale.ENGLISH).charAt(i + 1);
                    int index = "0123456789abcdefklmnor".indexOf(next);
                    if (index >= 0) {
                        TrueTypeFont.Glyph g = new TrueTypeFont.Glyph();
                        if (index < 16) {
                            g.type = TrueTypeFont.GlyphType.COLOR;
                            g.color = ChatFormatting.getByCode(next).getColor();
                        } else if (index == 16) {
                            g.type = TrueTypeFont.GlyphType.RANDOM;
                        } else if (index == 17) {
                            g.type = TrueTypeFont.GlyphType.BOLD;
                        } else if (index == 18) {
                            g.type = TrueTypeFont.GlyphType.STRIKETHROUGH;
                        } else if (index == 19) {
                            g.type = TrueTypeFont.GlyphType.UNDERLINE;
                        } else if (index == 20) {
                            g.type = TrueTypeFont.GlyphType.ITALIC;
                        } else {
                            g.type = TrueTypeFont.GlyphType.RESET;
                        }
                        cache.glyphs.add(g);
                        i++;
                        continue;
                    }
                }
                TrueTypeFont.Glyph g = this.getOrCreateGlyph(c);
                cache.glyphs.add(g);
                cache.width = cache.width + g.width;
                cache.height = Math.max(cache.height, g.height);
            }
            this.textcache.put(text, cache);
            return cache;
        }
    }

    private TrueTypeFont.Glyph getOrCreateGlyph(char c) {
        TrueTypeFont.Glyph g = (TrueTypeFont.Glyph) this.glyphcache.get(c);
        if (g != null) {
            return g;
        } else {
            TrueTypeFont.TextureCache cache = this.getCurrentTexture();
            Font font = this.getFontForChar(c);
            FontMetrics metrics = this.globalG.getFontMetrics(font);
            g = new TrueTypeFont.Glyph();
            g.width = Math.max(metrics.charWidth(c), 1);
            g.height = Math.max(metrics.getHeight(), 1);
            if (cache.x + g.width >= 512) {
                cache.x = 0;
                cache.y = cache.y + this.lineHeight + 1;
                if (cache.y >= 512) {
                    cache.full = true;
                    cache = this.getCurrentTexture();
                }
            }
            g.x = cache.x;
            g.y = cache.y;
            cache.x = cache.x + g.width + 3;
            this.lineHeight = Math.max(this.lineHeight, g.height);
            cache.g.setFont(font);
            cache.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            cache.g.drawString(c + "", g.x, g.y + metrics.getAscent());
            g.texture = cache.textureId;
            int[] aint = new int[262144];
            cache.bufferedImage.getRGB(0, 0, 512, 512, aint, 0, 512);
            IntBuffer intbuffer = BufferUtils.createIntBuffer(262144);
            intbuffer.put(aint);
            intbuffer.flip();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.bindTextureForSetup(cache.textureId);
            GL11.glPixelStorei(3312, 0);
            GL11.glPixelStorei(3313, 0);
            GL11.glPixelStorei(3314, 0);
            GL11.glPixelStorei(3315, 0);
            GL11.glPixelStorei(3316, 0);
            GL11.glPixelStorei(3317, 4);
            GL11.glTexImage2D(3553, 0, 6408, 512, 512, 0, 32993, 33639, intbuffer);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9729);
            this.glyphcache.put(c, g);
            return g;
        }
    }

    private TrueTypeFont.TextureCache getCurrentTexture() {
        TrueTypeFont.TextureCache cache = null;
        for (TrueTypeFont.TextureCache t : this.textures) {
            if (!t.full) {
                cache = t;
                break;
            }
        }
        if (cache == null) {
            this.textures.add(cache = new TrueTypeFont.TextureCache());
        }
        return cache;
    }

    private Font getFontForChar(char c) {
        if (this.font.canDisplay(c)) {
            return this.font;
        } else {
            for (Font f : this.usedFonts) {
                if (f.canDisplay(c)) {
                    return f;
                }
            }
            Font fa = new Font("Arial Unicode MS", 0, this.font.getSize());
            if (fa.canDisplay(c)) {
                return fa;
            } else {
                for (Font fx : allFonts) {
                    if (fx.canDisplay(c)) {
                        Font var7;
                        this.usedFonts.add(var7 = fx.deriveFont(0, (float) this.font.getSize()));
                        return var7;
                    }
                }
                return this.font;
            }
        }
    }

    public int width(String text) {
        TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
        return (int) ((float) cache.width * this.scale * this.textureScale());
    }

    public int height(String text) {
        if (text != null && !text.trim().isEmpty()) {
            TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
            return Math.max(1, (int) ((float) cache.height * this.scale * this.textureScale()));
        } else {
            return (int) ((float) this.lineHeight * this.scale * this.textureScale());
        }
    }

    private float textureScale() {
        return 0.5F;
    }

    public void dispose() {
        for (TrueTypeFont.TextureCache cache : this.textures) {
            RenderSystem.deleteTexture(cache.textureId);
        }
        this.textcache.clear();
    }

    public String getFontName() {
        return this.font.getFontName();
    }

    class Glyph {

        TrueTypeFont.GlyphType type = TrueTypeFont.GlyphType.NORMAL;

        int color = -1;

        int x;

        int y;

        int height;

        int width;

        int texture;
    }

    class GlyphCache {

        public int width;

        public int height;

        List<TrueTypeFont.Glyph> glyphs = new ArrayList();
    }

    static enum GlyphType {

        NORMAL,
        COLOR,
        RANDOM,
        BOLD,
        STRIKETHROUGH,
        UNDERLINE,
        ITALIC,
        RESET,
        OTHER
    }

    class TextureCache {

        int x;

        int y;

        int textureId = GL11.glGenTextures();

        BufferedImage bufferedImage = new BufferedImage(512, 512, 2);

        Graphics2D g = (Graphics2D) this.bufferedImage.getGraphics();

        boolean full;
    }
}