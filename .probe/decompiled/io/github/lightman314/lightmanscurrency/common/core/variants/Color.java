package io.github.lightman314.lightmanscurrency.common.core.variants;

import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

public enum Color {

    WHITE(0, 16777215, MapColor.SNOW, Tags.Items.DYES_WHITE),
    LIGHT_GRAY(1, 10329495, MapColor.COLOR_LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY),
    GRAY(2, 6579300, MapColor.COLOR_GRAY, Tags.Items.DYES_GRAY),
    BLACK(3, 1315860, MapColor.COLOR_BLACK, Tags.Items.DYES_BLACK),
    BROWN(4, 8606770, MapColor.COLOR_BROWN, Tags.Items.DYES_BROWN),
    RED(5, 16711680, MapColor.COLOR_RED, Tags.Items.DYES_RED),
    ORANGE(6, 16744192, MapColor.COLOR_ORANGE, Tags.Items.DYES_ORANGE),
    YELLOW(7, 16776960, MapColor.COLOR_YELLOW, Tags.Items.DYES_YELLOW),
    LIME(8, 8834086, MapColor.COLOR_LIGHT_GREEN, Tags.Items.DYES_LIME),
    GREEN(9, 32512, MapColor.COLOR_GREEN, Tags.Items.DYES_GREEN),
    CYAN(10, 1481628, MapColor.COLOR_CYAN, Tags.Items.DYES_CYAN),
    LIGHT_BLUE(11, 65535, MapColor.COLOR_LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE),
    BLUE(12, 255, MapColor.COLOR_BLUE, Tags.Items.DYES_BLUE),
    PURPLE(13, 9913293, MapColor.COLOR_PURPLE, Tags.Items.DYES_PURPLE),
    MAGENTA(14, 14049489, MapColor.COLOR_MAGENTA, Tags.Items.DYES_MAGENTA),
    PINK(15, 16036553, MapColor.COLOR_PINK, Tags.Items.DYES_PINK);

    public final int sortIndex;

    public final int hexColor;

    public final MapColor mapColor;

    public final TagKey<Item> dyeTag;

    public final String getResourceSafeName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public final String getPrettyName() {
        StringBuilder builder = new StringBuilder();
        boolean capitalize = true;
        String safeName = this.getResourceSafeName();
        for (int i = 0; i < safeName.length(); i++) {
            char nextChar = safeName.charAt(i);
            if (nextChar == '_') {
                builder.append(' ');
                capitalize = true;
            } else if (capitalize) {
                builder.append((nextChar + "").toUpperCase(Locale.ENGLISH));
                capitalize = false;
            } else {
                builder.append(nextChar);
            }
        }
        return builder.toString();
    }

    public final MutableComponent getComponent() {
        return Component.translatable("color.minecraft." + this.getResourceSafeName());
    }

    private Color(int sortIndex, int hexColor, MapColor mapColor, TagKey<Item> dyeTag) {
        this.sortIndex = sortIndex;
        this.hexColor = hexColor;
        this.mapColor = mapColor;
        this.dyeTag = dyeTag;
    }

    public static Color getFromIndex(long index) {
        index %= 16L;
        for (Color c : values()) {
            if ((long) c.sortIndex == index) {
                return c;
            }
        }
        return WHITE;
    }

    @Nullable
    public static Color getFromPrettyName(String name) {
        for (Color c : values()) {
            if (c.toString().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public static int sortByColor(Color c1, Color c2) {
        return Integer.compare(c1.sortIndex, c2.sortIndex);
    }
}