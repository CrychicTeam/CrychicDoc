package net.mehvahdjukaar.moonlight.api.resources.textures;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HSVColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.moonlight.api.util.math.kmeans.DataSet;
import net.mehvahdjukaar.moonlight.api.util.math.kmeans.IDataEntry;
import net.mehvahdjukaar.moonlight.api.util.math.kmeans.KMeans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class SpriteUtils {

    @Deprecated(forRemoval = true)
    @NotNull
    public static final Predicate<String> LOOKS_LIKE_TOP_LOG_TEXTURE = s -> {
        s = new ResourceLocation(s).getPath();
        return s.contains("_overlay") ? false : s.contains("_top") || s.contains("_end") || s.contains("_up");
    };

    @Deprecated(forRemoval = true)
    @NotNull
    public static final Predicate<String> LOOKS_LIKE_SIDE_LOG_TEXTURE = s -> !LOOKS_LIKE_TOP_LOG_TEXTURE.test(s) && !new ResourceLocation(s).getPath().contains("_overlay");

    @Deprecated(forRemoval = true)
    @NotNull
    public static final Predicate<String> LOOKS_LIKE_LEAF_TEXTURE = s -> {
        s = new ResourceLocation(s).getPath();
        return !s.contains("_bushy") && !s.contains("_snow") && !s.contains("_overlay");
    };

    public static NativeImage readImage(ResourceManager manager, ResourceLocation resourceLocation) throws IOException, NoSuchElementException {
        try {
            InputStream res = ((Resource) manager.m_213713_(resourceLocation).get()).open();
            NativeImage var3;
            try {
                var3 = NativeImage.read(res);
            } catch (Throwable var6) {
                if (res != null) {
                    try {
                        res.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (res != null) {
                res.close();
            }
            return var3;
        } catch (Exception var7) {
            throw new IOException(var7);
        }
    }

    public static void forEachPixel(NativeImage image, BiConsumer<Integer, Integer> function) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                function.accept(x, y);
            }
        }
    }

    public static void grayscaleImage(NativeImage image) {
        forEachPixel(image, (x, y) -> image.setPixelRGBA(x, y, new RGBColor(image.getPixelRGBA(x, y)).asHCL().withChroma(0.0F).asRGB().toInt()));
    }

    public static RGBColor averageColor(NativeImage image) {
        Palette p = Palette.fromImage(TextureImage.of(image, null), null, 0.0F);
        if (p.size() == 0) {
            return new RGBColor(-1);
        } else {
            DataSet<DataSet.ColorPoint> data = DataSet.fromPalette(p);
            KMeans.kMeans(data, 1);
            return ((DataSet.ColorPoint) ((IDataEntry) data.getLastCentroids().get(0)).cast()).getColor().rgb();
        }
    }

    public static List<Palette> extrapolateSignBlockPalette(TextureImage planksTexture) {
        List<Palette> newPalettes = new ArrayList();
        for (Palette palette : Palette.fromAnimatedImage(planksTexture, null, 0.0033333334F)) {
            extrapolateSignBlockPalette(palette);
            newPalettes.add(palette);
        }
        return newPalettes;
    }

    public static void extrapolateSignBlockPalette(Palette palette) {
        int size = palette.size();
        if (size == 7) {
            PaletteColor color = palette.get(size - 3);
            HSVColor hsv = color.rgb().asHSV();
            float satIncrease = 1.0638298F;
            float brightnessIncrease = 1.0638298F;
            HSVColor newCol = new HSVColor(hsv.hue(), Mth.clamp(hsv.saturation() * satIncrease, 0.0F, 1.0F), Mth.clamp(hsv.value() * brightnessIncrease, 0.0F, 1.0F), hsv.alpha());
            PaletteColor newP = new PaletteColor(newCol);
            newP.setOccurrence(color.getOccurrence());
            palette.set(size - 1, newP);
            palette.remove(size - 2);
        }
    }

    public static Palette extrapolateWoodItemPalette(TextureImage planksTexture) {
        Palette palette = (Palette) Palette.fromAnimatedImage(planksTexture, null).get(0);
        extrapolateWoodItemPalette(palette);
        return palette;
    }

    public static void extrapolateWoodItemPalette(Palette palette) {
        PaletteColor color = palette.get(0);
        HSVColor hsv = color.rgb().asHSV();
        float satMult = 1.11F;
        float brightnessMult = 0.94F;
        HSVColor newCol = new HSVColor(hsv.hue(), Mth.clamp(hsv.saturation() * satMult, 0.0F, 1.0F), Mth.clamp(hsv.value() * brightnessMult, 0.0F, 1.0F), hsv.alpha());
        PaletteColor newP = new PaletteColor(newCol);
        newP.setOccurrence(color.getOccurrence());
        palette.set(0, newP);
    }

    @Deprecated
    public static float getLuminance(int r, int g, int b) {
        return 0.299F * (float) r + 0.587F * (float) g + 0.114F * (float) b;
    }

    @Deprecated(forRemoval = true)
    public static void reduceColors(NativeImage image, UnaryOperator<Integer> sizeFn) {
        reduceColors(image, sizeFn::apply);
    }

    public static void reduceColors(NativeImage image, IntUnaryOperator sizeFn) {
        Palette p = Palette.fromImage(TextureImage.of(image, null), null, 0.0F);
        if (p.size() != 0) {
            DataSet<DataSet.ColorPoint> data = DataSet.fromPalette(p);
            int size = sizeFn.applyAsInt(p.size());
            if (size < p.size()) {
                KMeans.kMeans(data, size);
                Map<Integer, Integer> colorToColorMap = new HashMap();
                for (IDataEntry<DataSet.ColorPoint> c : data.getColorPoints()) {
                    IDataEntry<DataSet.ColorPoint> centroid = (IDataEntry<DataSet.ColorPoint>) data.getLastCentroids().get(c.getClusterNo());
                    colorToColorMap.put(c.cast().getColor().value(), centroid.cast().getColor().value());
                }
                forEachPixel(image, (x, y) -> {
                    int i = image.getPixelRGBA(x, y);
                    if (colorToColorMap.containsKey(i)) {
                        image.setPixelRGBA(x, y, (Integer) colorToColorMap.get(i));
                    }
                });
            }
        }
    }

    public static void mergeSimilarColors(NativeImage image, float tolerance) {
        TextureImage texture = TextureImage.of(image, null);
        Palette originalPalette = Palette.fromImage(texture, null, 0.0F);
        Palette targetPalette = originalPalette.copy();
        targetPalette.updateTolerance(tolerance);
        originalPalette.removeAll(targetPalette);
        Map<Integer, Integer> removedColors = new HashMap();
        for (PaletteColor i : originalPalette) {
            PaletteColor replacement = targetPalette.getColorClosestTo(i);
            removedColors.put(i.value(), replacement.value());
        }
        forEachPixel(image, (x, y) -> {
            int ix = image.getPixelRGBA(x, y);
            Integer replacementx = (Integer) removedColors.get(ix);
            if (replacementx != null) {
                image.setPixelRGBA(x, y, replacementx);
            }
        });
    }

    public static List<Integer> parsePaletteStrip(ResourceManager manager, ResourceLocation fullTexturePath, int expectColors) {
        try {
            Object var5;
            try (NativeImage image = readImage(manager, fullTexturePath)) {
                List<Integer> list = new ArrayList();
                forEachPixel(image, (x, y) -> {
                    int i = image.getPixelRGBA(x, y);
                    if (i != 0 && list.size() < expectColors) {
                        list.add(i);
                    }
                });
                if (list.size() < expectColors) {
                    throw new RuntimeException("Image at " + fullTexturePath + " has too few colors! Expected at least " + expectColors + " and got " + list.size());
                }
                var5 = list;
            }
            return (List<Integer>) var5;
        } catch (NoSuchElementException | IOException var8) {
            throw new RuntimeException("Failed to find image at location " + fullTexturePath, var8);
        }
    }

    public static TextureImage savePaletteStrip(ResourceManager manager, List<Integer> colors) {
        try {
            TextureImage var4;
            try (TextureImage image = TextureImage.createNew(16, 16, null)) {
                Iterator<Integer> it = colors.iterator();
                image.forEachFramePixel((x, y, f) -> {
                    if (it.hasNext()) {
                        image.getImage().setPixelRGBA(x, y, (Integer) it.next());
                    }
                });
                var4 = image;
            }
            return var4;
        } catch (Exception var7) {
            throw new RuntimeException("Failed to create palette strip");
        }
    }
}