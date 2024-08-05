package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class Color4I extends Icon {

    private static final Color4I[] BLACK_A = new Color4I[256];

    private static final Color4I[] WHITE_A = new Color4I[256];

    public static final Color4I BLACK;

    public static final Color4I DARK_GRAY;

    public static final Color4I GRAY;

    public static final Color4I WHITE;

    public static final Color4I RED;

    public static final Color4I GREEN;

    public static final Color4I BLUE;

    public static final Color4I LIGHT_RED;

    public static final Color4I LIGHT_GREEN;

    public static final Color4I LIGHT_BLUE;

    private static final Color4I[] CHAT_FORMATTING_COLORS;

    private static final Color4I[] COLORS_256;

    static final Color4I EMPTY_ICON;

    int red;

    int green;

    int blue;

    int alpha;

    public static Color4I getChatFormattingColor(int id) {
        return CHAT_FORMATTING_COLORS[id & 15];
    }

    public static Color4I getChatFormattingColor(@Nullable ChatFormatting formatting) {
        return formatting == null ? WHITE : getChatFormattingColor(formatting.ordinal());
    }

    public static Color4I get256(int id) {
        return COLORS_256[id & 0xFF];
    }

    public static Color4I fromString(@Nullable String s) {
        if (s != null && !s.isEmpty()) {
            if ((s.length() == 7 || s.length() == 9) && s.charAt(0) == '#') {
                String hex = s.substring(1);
                return hex.length() == 8 ? rgba((int) Long.parseLong(hex, 16)) : rgb((int) Long.parseLong(hex, 16));
            } else if (s.equalsIgnoreCase("transparent")) {
                return WHITE.withAlpha(0);
            } else if (s.equalsIgnoreCase("black")) {
                return BLACK;
            } else if (s.equalsIgnoreCase("dark_gray")) {
                return DARK_GRAY;
            } else if (s.equalsIgnoreCase("gray")) {
                return GRAY;
            } else if (s.equalsIgnoreCase("white")) {
                return WHITE;
            } else if (s.equalsIgnoreCase("red")) {
                return RED;
            } else if (s.equalsIgnoreCase("green")) {
                return GREEN;
            } else if (s.equalsIgnoreCase("blue")) {
                return BLUE;
            } else if (s.equalsIgnoreCase("light_red")) {
                return LIGHT_RED;
            } else if (s.equalsIgnoreCase("light_green")) {
                return LIGHT_GREEN;
            } else {
                return s.equalsIgnoreCase("light_blue") ? LIGHT_BLUE : empty();
            }
        } else {
            return empty();
        }
    }

    public static Color4I fromJson(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return empty();
        } else if (element.isJsonPrimitive()) {
            return fromString(element.getAsString());
        } else {
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                if (array.size() >= 3) {
                    int r = array.get(0).getAsInt();
                    int g = array.get(1).getAsInt();
                    int b = array.get(2).getAsInt();
                    int a = 255;
                    if (array.size() >= 3) {
                        a = array.get(3).getAsInt();
                    }
                    return rgba(r, g, b, a);
                }
            }
            JsonObject object = element.getAsJsonObject();
            if (object.has("red") && object.has("green") && object.has("blue")) {
                int r = object.get("red").getAsInt();
                int g = object.get("green").getAsInt();
                int b = object.get("blue").getAsInt();
                int a = 255;
                if (object.has("alpha")) {
                    a = object.get("alpha").getAsInt();
                }
                return rgba(r, g, b, a);
            } else {
                return empty();
            }
        }
    }

    public static Color4I rgba(int r, int g, int b, int a) {
        r &= 255;
        g &= 255;
        b &= 255;
        a &= 255;
        if (a == 0) {
            return empty();
        } else if (r == 0 && g == 0 && b == 0) {
            return BLACK_A[a];
        } else {
            return r == 255 && g == 255 && b == 255 ? WHITE_A[a] : new Color4I(r, g, b, a);
        }
    }

    public static Color4I rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    public static Color4I hsb(float h, float s, float b) {
        return rgb(HSBtoRGB(h, s, b));
    }

    public static Color4I rgba(int col) {
        return rgba(col >> 16, col >> 8, col, col >> 24);
    }

    public static Color4I rgb(int col) {
        return rgb(col >> 16, col >> 8, col);
    }

    public static Color4I rgb(Vec3 color) {
        return rgb((int) (color.x * 255.0), (int) (color.y * 255.0), (int) (color.z * 255.0));
    }

    Color4I(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public Color4I copy() {
        return this;
    }

    public boolean isMutable() {
        return false;
    }

    public MutableColor4I mutable() {
        return new MutableColor4I(this.redi(), this.greeni(), this.bluei(), this.alphai());
    }

    public Color4I whiteIfEmpty() {
        return this.isEmpty() ? WHITE : this;
    }

    public int redi() {
        return this.red;
    }

    public int greeni() {
        return this.green;
    }

    public int bluei() {
        return this.blue;
    }

    public int alphai() {
        return this.alpha;
    }

    public float redf() {
        return (float) this.redi() / 255.0F;
    }

    public float greenf() {
        return (float) this.greeni() / 255.0F;
    }

    public float bluef() {
        return (float) this.bluei() / 255.0F;
    }

    public float alphaf() {
        return (float) this.alphai() / 255.0F;
    }

    public int rgba() {
        return this.alphai() << 24 | this.redi() << 16 | this.greeni() << 8 | this.bluei();
    }

    public int rgb() {
        return this.redi() << 16 | this.greeni() << 8 | this.bluei();
    }

    @Override
    public int hashCode() {
        return this.rgba();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Color4I && o.hashCode() == this.rgba();
    }

    public String toString() {
        int a = this.alphai();
        char[] chars;
        if (a < 255) {
            chars = new char[9];
            chars[1] = StringUtils.HEX[(a & 240) >> 4];
            chars[2] = StringUtils.HEX[a & 15];
            int r = this.redi();
            chars[3] = StringUtils.HEX[(r & 240) >> 4];
            chars[4] = StringUtils.HEX[r & 15];
            int g = this.greeni();
            chars[5] = StringUtils.HEX[(g & 240) >> 4];
            chars[6] = StringUtils.HEX[g & 15];
            int b = this.bluei();
            chars[7] = StringUtils.HEX[(b & 240) >> 4];
            chars[8] = StringUtils.HEX[b & 15];
        } else {
            chars = new char[7];
            int r = this.redi();
            chars[1] = StringUtils.HEX[(r & 240) >> 4];
            chars[2] = StringUtils.HEX[r & 15];
            int g = this.greeni();
            chars[3] = StringUtils.HEX[(g & 240) >> 4];
            chars[4] = StringUtils.HEX[g & 15];
            int b = this.bluei();
            chars[5] = StringUtils.HEX[(b & 240) >> 4];
            chars[6] = StringUtils.HEX[b & 15];
        }
        chars[0] = '#';
        return new String(chars);
    }

    @Override
    public JsonElement getJson() {
        return (JsonElement) (this.isEmpty() ? JsonNull.INSTANCE : new JsonPrimitive(this.toString()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        if (w > 0 && h > 0) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            GuiHelper.addRectToBuffer(graphics, buffer, x, y, w, h, this);
            tesselator.end();
        }
    }

    @Override
    public Icon withColor(Color4I color) {
        return color;
    }

    public Color4I withTint(Color4I col) {
        if (this.isEmpty()) {
            return this;
        } else if (col.isEmpty()) {
            return empty();
        } else if (col.redi() == 255 && col.greeni() == 255 && col.bluei() == 255) {
            return this;
        } else {
            double a = (double) col.alphaf();
            double r = MathUtils.lerp((double) this.redi(), (double) col.redi(), a);
            double g = MathUtils.lerp((double) this.greeni(), (double) col.greeni(), a);
            double b = MathUtils.lerp((double) this.bluei(), (double) col.bluei(), a);
            return rgba((int) r, (int) g, (int) b, this.alpha);
        }
    }

    public Color4I withAlpha(int a) {
        return this.alpha == a ? this : rgba(this.redi(), this.greeni(), this.bluei(), a);
    }

    public final Color4I withAlphaf(float alpha) {
        return this.withAlpha((int) (alpha * 255.0F));
    }

    public Color4I lerp(Color4I col, float m) {
        m = Mth.clamp(m, 0.0F, 1.0F);
        float r = MathUtils.lerp(this.redf(), col.redf(), m);
        float g = MathUtils.lerp(this.greenf(), col.greenf(), m);
        float b = MathUtils.lerp(this.bluef(), col.bluef(), m);
        float a = MathUtils.lerp(this.alphaf(), col.alphaf(), m);
        return rgba((int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F), (int) (a * 255.0F));
    }

    public Color4I addBrightness(float percent) {
        float[] hsb = new float[3];
        RGBtoHSB(this.redi(), this.greeni(), this.bluei(), hsb);
        return rgb(HSBtoRGB(hsb[0], hsb[1], Mth.clamp(hsb[2] + percent, 0.0F, 1.0F))).withAlpha(this.alphai());
    }

    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        int cmax = r > g ? r : g;
        if (b > cmax) {
            cmax = b;
        }
        int cmin = r < g ? r : g;
        if (b < cmin) {
            cmin = b;
        }
        float brightness = (float) cmax / 255.0F;
        float saturation;
        if (cmax != 0) {
            saturation = (float) (cmax - cmin) / (float) cmax;
        } else {
            saturation = 0.0F;
        }
        float hue;
        if (saturation == 0.0F) {
            hue = 0.0F;
        } else {
            float redc = (float) (cmax - r) / (float) (cmax - cmin);
            float greenc = (float) (cmax - g) / (float) (cmax - cmin);
            float bluec = (float) (cmax - b) / (float) (cmax - cmin);
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = 2.0F + redc - bluec;
            } else {
                hue = 4.0F + greenc - redc;
            }
            hue /= 6.0F;
            if (hue < 0.0F) {
                hue++;
            }
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int) (brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float) Math.floor((double) hue)) * 6.0F;
            float f = h - (float) Math.floor((double) h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch((int) h) {
                case 0:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (t * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int) (q * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (q * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int) (t * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (q * 255.0F + 0.5F);
            }
        }
        return 0xFF000000 | r << 16 | g << 8 | b << 0;
    }

    @Override
    public boolean hasPixelBuffer() {
        return true;
    }

    @Nullable
    @Override
    public PixelBuffer createPixelBuffer() {
        PixelBuffer buffer = new PixelBuffer(1, 1);
        buffer.setRGB(0, 0, this.rgba());
        return buffer;
    }

    public Style toStyle() {
        return Style.EMPTY.withColor(TextColor.fromRgb(this.rgb()));
    }

    static {
        for (int i = 0; i < 256; i++) {
            BLACK_A[i] = new Color4I(0, 0, 0, i) {

                @Override
                public Color4I withAlpha(int a) {
                    return (Color4I) (this.alpha == a ? this : Color4I.BLACK_A[a & 0xFF]);
                }
            };
            WHITE_A[i] = new Color4I(255, 255, 255, i) {

                @Override
                public Color4I withAlpha(int a) {
                    return (Color4I) (this.alpha == a ? this : Color4I.WHITE_A[a & 0xFF]);
                }
            };
        }
        BLACK = rgb(0);
        DARK_GRAY = rgb(2171169);
        GRAY = rgb(10066329);
        WHITE = rgb(16777215);
        RED = rgb(16711680);
        GREEN = rgb(65280);
        BLUE = rgb(255);
        LIGHT_RED = rgb(16733782);
        LIGHT_GREEN = rgb(5701462);
        LIGHT_BLUE = rgb(5658367);
        CHAT_FORMATTING_COLORS = new Color4I[16];
        COLORS_256 = new Color4I[256];
        EMPTY_ICON = new Color4I(255, 255, 255, 255) {

            @Override
            public boolean isEmpty() {
                return true;
            }

            @OnlyIn(Dist.CLIENT)
            @Override
            public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
            }

            @OnlyIn(Dist.CLIENT)
            @Override
            public void draw3D(GuiGraphics graphics) {
            }

            @Override
            public MutableColor4I mutable() {
                return new MutableColor4I.None();
            }

            @Nullable
            @Override
            public PixelBuffer createPixelBuffer() {
                return null;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean equals(Object o) {
                return o == this;
            }
        };
        for (int i = 0; i < 16; i++) {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i & 1) * 170 + j;
            CHAT_FORMATTING_COLORS[i] = rgb(i == 6 ? r + 85 : r, g, b);
        }
        int[] colors256 = new int[] { 0, 2434341, 3421236, 5131854, 6842472, 7697781, 9342606, 10790052, 12105912, 12961221, 13684944, 14145495, 14803425, 15395562, 16053492, 16777215, 4268032, 5515264, 7747328, 10113024, 12806150, 14973703, 16748826, 16755485, 16762143, 16764987, 16767052, 16770641, 16774230, 16775536, 16777104, 16777130, 4528388, 7478801, 10429470, 11745824, 13127968, 14903584, 16548128, 16616485, 16685100, 16756280, 16759110, 16760657, 16762477, 16766343, 16770200, 16770731, 6102796, 8004621, 9972750, 11546383, 12531236, 13848106, 15163966, 15953482, 16611412, 16747114, 16750716, 16753803, 16757662, 16761522, 16765123, 16767696, 4855552, 7479040, 11014912, 13115658, 14624018, 15481636, 16405046, 16539976, 16740447, 16744062, 16748431, 16752030, 16755629, 16759229, 16762830, 16763614, 4784182, 6684747, 8389471, 9768820, 11149960, 12205465, 13258153, 14113462, 14968771, 15692494, 16482010, 16748001, 16752101, 16754151, 16756714, 16759020, 4719468, 6030472, 6622608, 8070055, 9649087, 10307017, 10964947, 11688670, 12412393, 12938737, 13530874, 13992959, 14323967, 14589183, 14854655, 15120127, 335489, 403109, 536522, 2506196, 4476126, 5200620, 5925119, 6649343, 7439359, 8425983, 9478399, 9939455, 10466047, 11517695, 12635135, 13489151, 722809, 2104462, 3486115, 4604596, 5723077, 6381007, 7170523, 8091625, 9012727, 9539071, 10262783, 10986751, 11710463, 12302591, 12829183, 13881855, 1911130, 1914998, 1919122, 1924268, 1929670, 3311311, 4758489, 5155052, 5617407, 6933247, 7654399, 8573951, 9296639, 10474751, 11854591, 12643327, 19289, 23918, 28548, 33948, 39359, 43978, 48350, 53493, 1105151, 4121087, 6612991, 7793407, 9170431, 10153983, 11662335, 13104895, 18432, 21504, 224003, 947726, 1605656, 2593319, 3580982, 5159246, 5360977, 7527026, 8184956, 8777093, 10089113, 11794355, 12843459, 13499597, 1458176, 1856256, 2319872, 2652160, 3050496, 3840012, 4695321, 5353251, 6076974, 7458627, 8774487, 9300831, 9958761, 10550898, 11665290, 12386202, 2897152, 3687424, 4477440, 4806144, 6320384, 7110400, 7965962, 9150236, 10400303, 11255612, 12110921, 12768851, 13492563, 14413676, 15268985, 15925163, 4602377, 5062409, 5522697, 7100425, 9467401, 11242250, 12689696, 13676591, 14597693, 15124037, 15584588, 16111714, 16507510, 16576152, 16642985, 16643006, 4200962, 5775109, 7349256, 9255443, 11227423, 11887655, 12547888, 13665594, 14783300, 15573070, 16362840, 16562012, 16761184, 16763497, 16764798, 16767638 };
        for (int i = 0; i < 256; i++) {
            COLORS_256[i] = rgb(colors256[i]);
        }
    }
}