package net.mehvahdjukaar.moonlight.api.resources.textures;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import org.jetbrains.annotations.Nullable;

public class Respriter {

    private final TextureImage imageToRecolor;

    private final List<Palette> originalPalettes;

    private final boolean useMergedPalette;

    public static Respriter of(TextureImage imageToRecolor) {
        return new Respriter(imageToRecolor, Palette.fromAnimatedImage(imageToRecolor, null, 0.0F));
    }

    public static Respriter masked(TextureImage imageToRecolor, TextureImage colorMask) {
        return new Respriter(imageToRecolor, List.of(Palette.fromImage(imageToRecolor, colorMask, 0.0F)));
    }

    public static Respriter ofPalette(TextureImage imageToRecolor, List<Palette> colorsToSwap) {
        return new Respriter(imageToRecolor, colorsToSwap);
    }

    public static Respriter ofPalette(TextureImage imageToRecolor, Palette colorsToSwap) {
        return new Respriter(imageToRecolor, List.of(colorsToSwap));
    }

    private Respriter(TextureImage imageToRecolor, List<Palette> colorsToSwap) {
        if (colorsToSwap.size() == 0) {
            throw new UnsupportedOperationException("Respriter must have a non empty target palette");
        } else {
            if (imageToRecolor.frameCount() > colorsToSwap.size()) {
                Palette firstPalette = (Palette) colorsToSwap.get(0);
                colorsToSwap = Collections.nCopies(imageToRecolor.frameCount(), firstPalette);
                this.useMergedPalette = true;
            } else {
                this.useMergedPalette = false;
            }
            this.imageToRecolor = imageToRecolor;
            this.originalPalettes = colorsToSwap;
        }
    }

    public TextureImage recolorWithAnimationOf(TextureImage textureImage) {
        return this.recolorWithAnimation(List.of(Palette.fromImage(textureImage)), textureImage.getMetadata());
    }

    public TextureImage recolorWithAnimation(List<Palette> targetPalettes, @Nullable AnimationMetadataSection targetAnimationData) {
        if (targetAnimationData == null) {
            return this.recolor(targetPalettes);
        } else {
            Palette originalPalette = (Palette) this.originalPalettes.get(0);
            TextureImage texture = this.imageToRecolor.createAnimationTemplate(targetPalettes.size(), targetAnimationData);
            NativeImage img = texture.getImage();
            Map<Integer, Respriter.ColorToColorMap> mapForFrameCache = new HashMap();
            texture.forEachFramePixel((ind, x, y) -> {
                int finalInd = this.useMergedPalette ? 0 : ind;
                Respriter.ColorToColorMap oldToNewMap = (Respriter.ColorToColorMap) mapForFrameCache.computeIfAbsent(finalInd, i -> {
                    Palette toPalette = (Palette) targetPalettes.get(finalInd);
                    return Respriter.ColorToColorMap.create(originalPalette, toPalette);
                });
                if (oldToNewMap != null) {
                    Integer oldValue = img.getPixelRGBA(x, y);
                    Integer newValue = oldToNewMap.mapColor(oldValue);
                    if (newValue != null) {
                        img.setPixelRGBA(x, y, newValue);
                    }
                }
            });
            return texture;
        }
    }

    public TextureImage recolor(Palette targetPalette) {
        return this.recolor(List.of(targetPalette));
    }

    public TextureImage recolor(List<Palette> targetPalettes) {
        boolean onlyUseFirst = targetPalettes.size() < this.originalPalettes.size();
        TextureImage texture = this.imageToRecolor.makeCopy();
        NativeImage img = texture.getImage();
        Map<Integer, Respriter.ColorToColorMap> mapForFrameCache = new HashMap();
        texture.forEachFramePixel((ind, x, y) -> {
            int finalInd = this.useMergedPalette ? 0 : ind;
            Respriter.ColorToColorMap oldToNewMap = (Respriter.ColorToColorMap) mapForFrameCache.computeIfAbsent(ind, i -> {
                Palette toPalette = onlyUseFirst ? (Palette) targetPalettes.get(0) : (Palette) targetPalettes.get(finalInd);
                Palette originalPalette = (Palette) this.originalPalettes.get(finalInd);
                return Respriter.ColorToColorMap.create(originalPalette, toPalette);
            });
            if (oldToNewMap != null) {
                Integer oldValue = img.getPixelRGBA(x, y);
                Integer newValue = oldToNewMap.mapColor(oldValue);
                if (newValue != null) {
                    img.setPixelRGBA(x, y, newValue);
                }
            }
        });
        return texture;
    }

    public static record ColorToColorMap(Map<Integer, Integer> map) {

        @Nullable
        public Integer mapColor(Integer color) {
            return (Integer) this.map.get(color);
        }

        @Nullable
        public static Respriter.ColorToColorMap create(Palette originalPalette, Palette toPalette) {
            Palette copy = toPalette.copy();
            copy.matchSize(originalPalette.size(), originalPalette.getAverageLuminanceStep());
            return copy.size() != originalPalette.size() ? null : new Respriter.ColorToColorMap(zipToMap(originalPalette.getValues(), copy.getValues()));
        }

        private static Map<Integer, Integer> zipToMap(List<PaletteColor> keys, List<PaletteColor> values) {
            return (Map<Integer, Integer>) IntStream.range(0, keys.size()).boxed().collect(Collectors.toMap(i -> ((PaletteColor) keys.get(i)).value(), i -> ((PaletteColor) values.get(i)).value()));
        }
    }
}