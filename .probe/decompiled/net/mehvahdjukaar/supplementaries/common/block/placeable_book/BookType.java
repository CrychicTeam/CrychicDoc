package net.mehvahdjukaar.supplementaries.common.block.placeable_book;

import com.mojang.serialization.Codec;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HSLColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HSVColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public final class BookType {

    public static final Codec<BookType> CODEC = ExtraCodecs.stringResolverCodec(BookType::name, PlaceableBookManager::getByName);

    private final String name;

    private final float hue;

    private final float hueShift;

    private final boolean hasGlint;

    private final ResourceLocation modelPath;

    public BookType(String name, int rgb, float angle, boolean hasGlint) {
        HSVColor col = new RGBColor(rgb).asHSV();
        float hueShift;
        if (angle < 0.0F) {
            hueShift = getLegacyAllowedHueShift(col.asHSL());
        } else {
            hueShift = Math.max(1.0F, angle);
        }
        this.name = name;
        this.hue = col.hue();
        this.hueShift = hueShift;
        this.hasGlint = hasGlint;
        this.modelPath = Supplementaries.res("block/books/book_" + name);
    }

    private static float getAllowedHueShift(HSVColor color) {
        float v = color.value();
        float minAngle = 0.19444445F;
        float addAngle = 0.18055555F;
        return minAngle + addAngle * (1.0F - v);
    }

    private static float getLegacyAllowedHueShift(HSLColor color) {
        float l = color.lightness();
        float s = ColorHelper.normalizeHSLSaturation(color.saturation(), l);
        float minAngle = 0.25F;
        float addAngle = 0.18055555F;
        float distLightSq = 2.0F;
        float distDarkSq = s * s + l * l;
        float distSq = Math.min(1.0F, Math.min(distDarkSq, distLightSq));
        return minAngle + (1.0F - distSq) * addAngle;
    }

    public BookType(DyeColor color, float angle, boolean enchanted) {
        this(color.getName(), ColorUtils.pack(color.getTextureDiffuseColors()), angle, enchanted);
    }

    public BookType(DyeColor color) {
        this(color, -1.0F, false);
    }

    public BookType(String name, int rgb, boolean enchanted) {
        this(name, rgb, -1.0F, enchanted);
    }

    public boolean looksGoodNextTo(BookType other) {
        float diff = Math.abs(Mth.degreesDifference(this.hue * 360.0F, other.hue * 360.0F) / 360.0F);
        return diff < (other.hueShift + this.hueShift) / 2.0F;
    }

    public String name() {
        return this.name;
    }

    public float hue() {
        return this.hue;
    }

    public float hueShift() {
        return this.hueShift;
    }

    public boolean hasGlint() {
        return this.hasGlint;
    }

    public ResourceLocation modelPath() {
        return this.modelPath;
    }
}