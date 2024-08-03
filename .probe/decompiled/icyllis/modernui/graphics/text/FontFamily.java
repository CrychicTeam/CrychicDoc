package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.UnmodifiableView;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class FontFamily {

    public static final FontFamily SANS_SERIF;

    public static final FontFamily SERIF;

    public static final FontFamily MONOSPACED;

    private static final ConcurrentHashMap<String, FontFamily> sSystemFontMap;

    private static final ConcurrentHashMap<String, String> sSystemFontAliases;

    @UnmodifiableView
    private static final Map<String, FontFamily> sSystemFontMapView;

    @UnmodifiableView
    private static final Map<String, String> sSystemFontAliasesView;

    private static final int[] EAST_ASIAN_TEST_CHARS = new int[] { 4352, 4353, 11904, 11905, 12032, 12033, 12288, 12289, 12353, 12354, 12449, 12450, 12561, 12562, 12593, 12594, 12688, 12689, 12704, 12705, 12736, 12737, 12784, 12785, 12800, 12801, 13056, 13057, 63744, 63745, 93952, 93953, 94208, 94209, 100352, 100353, 101120, 101121, 110592, 110593, 110960, 110961 };

    private final Font mFont;

    private Font mBold;

    private Font mItalic;

    private Font mBoldItalic;

    private final boolean mIsEastAsian;

    private final boolean mIsColorEmoji;

    @UnmodifiableView
    public static Map<String, FontFamily> getSystemFontMap() {
        return sSystemFontMapView;
    }

    @UnmodifiableView
    public static Map<String, String> getSystemFontAliases() {
        return sSystemFontAliasesView;
    }

    @Nullable
    public static FontFamily getSystemFontWithAlias(String name) {
        return (FontFamily) sSystemFontMapView.get(sSystemFontAliasesView.getOrDefault(name, name));
    }

    @NonNull
    public static FontFamily createFamily(@NonNull File file, boolean register) throws FontFormatException, IOException {
        java.awt.Font font = java.awt.Font.createFont(0, file);
        return createFamily(font, register);
    }

    @NonNull
    public static FontFamily createFamily(@NonNull InputStream stream, boolean register) throws FontFormatException, IOException {
        java.awt.Font font = java.awt.Font.createFont(0, stream);
        return createFamily(font, register);
    }

    @NonNull
    private static FontFamily createFamily(@NonNull java.awt.Font font, boolean register) {
        FontFamily family = new FontFamily(font);
        if (register) {
            String name = family.getFamilyName();
            sSystemFontMap.putIfAbsent(name, family);
            String alias = family.getFamilyName(Locale.getDefault());
            if (!Objects.equals(name, alias)) {
                sSystemFontAliases.putIfAbsent(alias, name);
            }
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        }
        return family;
    }

    @NonNull
    public static FontFamily[] createFamilies(@NonNull File file, boolean register) throws FontFormatException, IOException {
        java.awt.Font[] fonts = java.awt.Font.createFonts(file);
        return createFamilies(fonts, register);
    }

    @NonNull
    public static FontFamily[] createFamilies(@NonNull InputStream stream, boolean register) throws FontFormatException, IOException {
        java.awt.Font[] fonts = java.awt.Font.createFonts(stream);
        return createFamilies(fonts, register);
    }

    @NonNull
    private static FontFamily[] createFamilies(@NonNull java.awt.Font[] fonts, boolean register) {
        FontFamily[] families = new FontFamily[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            families[i] = new FontFamily(fonts[i]);
        }
        if (register) {
            Locale defaultLocale = Locale.getDefault();
            for (FontFamily family : families) {
                String name = family.getFamilyName();
                sSystemFontMap.putIfAbsent(name, family);
                String alias = family.getFamilyName(defaultLocale);
                if (!Objects.equals(name, alias)) {
                    sSystemFontAliases.putIfAbsent(alias, name);
                }
            }
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            for (java.awt.Font font : fonts) {
                ge.registerFont(font);
            }
        }
        return families;
    }

    public FontFamily(Font font) {
        this.mFont = (Font) Objects.requireNonNull(font);
        if (font instanceof OutlineFont) {
            throw new IllegalArgumentException();
        } else {
            this.mIsEastAsian = false;
            this.mIsColorEmoji = font instanceof EmojiFont;
        }
    }

    private FontFamily(@NonNull java.awt.Font font) {
        this.mFont = new OutlineFont(font);
        this.mBold = new OutlineFont(font.deriveFont(1));
        this.mItalic = new OutlineFont(font.deriveFont(2));
        this.mBoldItalic = new OutlineFont(font.deriveFont(3));
        this.mIsEastAsian = isEastAsianFont(font);
        this.mIsColorEmoji = false;
    }

    private static boolean isEastAsianFont(java.awt.Font font) {
        for (int ch : EAST_ASIAN_TEST_CHARS) {
            if (font.canDisplay(ch)) {
                return true;
            }
        }
        for (int chx = 13312; chx < 42240; chx += 256) {
            if (font.canDisplay(chx)) {
                return true;
            }
        }
        for (int chxx = 44032; chxx < 55296; chxx += 256) {
            if (font.canDisplay(chxx)) {
                return true;
            }
        }
        return false;
    }

    public Font getClosestMatch(int style) {
        return switch(style) {
            case 0 ->
                this.mFont;
            case 1 ->
                this.mBold != null ? this.mBold : this.mFont;
            case 2 ->
                this.mItalic != null ? this.mItalic : this.mFont;
            case 3 ->
                this.mBoldItalic != null ? this.mBoldItalic : this.mFont;
            default ->
                null;
        };
    }

    public boolean isEastAsianFamily() {
        return this.mIsEastAsian;
    }

    public boolean isColorEmojiFamily() {
        return this.mIsColorEmoji;
    }

    public boolean hasGlyph(int ch) {
        return this.mFont.hasGlyph(ch, 0);
    }

    public boolean hasGlyph(int ch, int vs) {
        return this.mFont.hasGlyph(ch, vs);
    }

    public String getFamilyName() {
        return this.mFont.getFamilyName();
    }

    public String getFamilyName(Locale locale) {
        return this.mFont.getFamilyName(locale);
    }

    public int hashCode() {
        return Objects.hashCode(this.mFont);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FontFamily that = (FontFamily) o;
            return Objects.equals(this.mFont, that.mFont);
        } else {
            return false;
        }
    }

    public String toString() {
        return "FontFamily{mFont=" + this.mFont + ", mBold=" + this.mBold + ", mItalic=" + this.mItalic + ", mBoldItalic=" + this.mBoldItalic + ", mIsEastAsian=" + this.mIsEastAsian + ", mIsColorEmoji=" + this.mIsColorEmoji + "}";
    }

    static {
        GraphicsEnvironment.getLocalGraphicsEnvironment().preferLocaleFonts();
        ConcurrentHashMap<String, FontFamily> map = new ConcurrentHashMap();
        ConcurrentHashMap<String, String> aliases = new ConcurrentHashMap();
        Locale defaultLocale = Locale.getDefault();
        Function<String, FontFamily> mapping = namex -> new FontFamily(new java.awt.Font(namex, 0, 1));
        for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ROOT)) {
            if (!map.containsKey(name)) {
                FontFamily family = (FontFamily) mapping.apply(name);
                map.put(name, family);
                String alias = family.getFamilyName(defaultLocale);
                if (!name.equals(alias)) {
                    aliases.put(alias, name);
                }
            }
        }
        SANS_SERIF = (FontFamily) map.computeIfAbsent("SansSerif", mapping);
        SERIF = (FontFamily) map.computeIfAbsent("Serif", mapping);
        MONOSPACED = (FontFamily) map.computeIfAbsent("Monospaced", mapping);
        sSystemFontMap = map;
        sSystemFontAliases = aliases;
        sSystemFontMapView = Collections.unmodifiableMap(map);
        sSystemFontAliasesView = Collections.unmodifiableMap(aliases);
    }
}