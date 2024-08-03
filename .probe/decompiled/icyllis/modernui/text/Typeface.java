package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.text.FontCollection;
import icyllis.modernui.graphics.text.FontFamily;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Typeface extends FontCollection {

    public static final Typeface SANS_SERIF;

    public static final Typeface SERIF;

    public static final Typeface MONOSPACED;

    private static final ConcurrentHashMap<String, Typeface> sSystemFontMap;

    public static final int NORMAL = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    public static final int BOLD_ITALIC = 3;

    @NonNull
    public static Typeface createTypeface(@NonNull FontFamily... families) {
        return families.length == 0 ? SANS_SERIF : new Typeface(families);
    }

    @NonNull
    public static Typeface getSystemFont(@NonNull String familyName) {
        Typeface typeface = (Typeface) sSystemFontMap.get(familyName);
        if (typeface != null) {
            return typeface;
        } else {
            String name = (String) FontFamily.getSystemFontAliases().getOrDefault(familyName, familyName);
            FontFamily family = (FontFamily) FontFamily.getSystemFontMap().get(name);
            if (family != null) {
                Typeface newTypeface = createTypeface(family);
                return (Typeface) Objects.requireNonNullElse((Typeface) sSystemFontMap.putIfAbsent(name, newTypeface), newTypeface);
            } else {
                return SANS_SERIF;
            }
        }
    }

    private Typeface(@NonNull FontFamily... families) {
        super(families);
    }

    static {
        ConcurrentHashMap<String, Typeface> map = new ConcurrentHashMap();
        for (Entry<String, FontFamily> entry : FontFamily.getSystemFontMap().entrySet()) {
            map.putIfAbsent((String) entry.getKey(), createTypeface((FontFamily) entry.getValue()));
        }
        SANS_SERIF = (Typeface) Objects.requireNonNull((Typeface) map.get("SansSerif"));
        SERIF = (Typeface) Objects.requireNonNull((Typeface) map.get("Serif"));
        MONOSPACED = (Typeface) Objects.requireNonNull((Typeface) map.get("Monospaced"));
        sSystemFontMap = map;
    }
}