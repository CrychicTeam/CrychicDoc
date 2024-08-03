package mezz.jei.library.config;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import mezz.jei.common.config.file.IConfigCategoryBuilder;
import mezz.jei.common.config.file.IConfigSchemaBuilder;
import mezz.jei.common.config.file.serializers.ListSerializer;
import mezz.jei.library.color.ColorName;
import mezz.jei.library.color.ColorUtil;
import mezz.jei.library.config.serializers.ColorNameSerializer;

public final class ColorNameConfig {

    private static final List<ColorName> defaultColors = List.of(new ColorName("White", 15658734), new ColorName("LightBlue", 7639756), new ColorName("Cyan", 61166), new ColorName("Blue", 2237149), new ColorName("LapisBlue", 2441611), new ColorName("Teal", 32896), new ColorName("Yellow", 13290328), new ColorName("GoldenYellow", 15652608), new ColorName("Orange", 14251572), new ColorName("Pink", 13732253), new ColorName("HotPink", 16519104), new ColorName("Magenta", 11684795), new ColorName("Purple", 8470201), new ColorName("EvilPurple", 3020361), new ColorName("Lavender", 11894492), new ColorName("Indigo", 4718722), new ColorName("Sand", 14406560), new ColorName("Tan", 12295011), new ColorName("LightBrown", 10506797), new ColorName("Brown", 6507315), new ColorName("DarkBrown", 3812627), new ColorName("LimeGreen", 4436537), new ColorName("SlimeGreen", 8637299), new ColorName("Green", 32768), new ColorName("DarkGreen", 2247970), new ColorName("GrassGreen", 5537865), new ColorName("Red", 9843760), new ColorName("BrickRed", 11558987), new ColorName("NetherBrick", 2757910), new ColorName("Redstone", 13516342), new ColorName("Black", 1578261), new ColorName("CharcoalGray", 4605510), new ColorName("IronGray", 6579300), new ColorName("Gray", 8421504), new ColorName("Silver", 12632256));

    private final Supplier<List<ColorName>> searchColors;

    public ColorNameConfig(IConfigSchemaBuilder schema) {
        IConfigCategoryBuilder colors = schema.addCategory("colors");
        this.searchColors = colors.addList("SearchColors", defaultColors, new ListSerializer<>(ColorNameSerializer.INSTANCE), "Color values to search for");
    }

    public String getClosestColorName(int color) {
        List<ColorName> colorNames = (List<ColorName>) this.searchColors.get();
        if (colorNames.isEmpty()) {
            colorNames = defaultColors;
        }
        return (String) colorNames.stream().min(Comparator.comparing(entry -> {
            int namedColor = entry.color();
            double distance = ColorUtil.slowPerceptualColorDistanceSquared(namedColor, color);
            return Math.abs(distance);
        })).map(ColorName::name).orElseThrow();
    }
}