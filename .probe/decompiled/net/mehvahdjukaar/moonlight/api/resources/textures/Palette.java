package net.mehvahdjukaar.moonlight.api.resources.textures;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.moonlight.api.util.math.colors.BaseColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HCLColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.LABColor;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Palette implements Set<PaletteColor> {

    public static final float BASE_TOLERANCE = 0.0055555557F;

    private float tolerance = 0.0F;

    private final ArrayList<PaletteColor> internal = new ArrayList();

    protected Palette(Collection<PaletteColor> colors) {
        this.internal.addAll(colors);
        this.sort();
    }

    protected Palette(Collection<PaletteColor> colors, float tolerance) {
        this.internal.addAll(colors);
        this.sort();
        this.updateTolerance(tolerance);
    }

    public boolean isEmpty() {
        return this.internal.isEmpty();
    }

    public Palette copy() {
        return new Palette(new ArrayList(this.internal), this.tolerance);
    }

    public static Palette empty() {
        return new Palette(new ArrayList());
    }

    public void updateTolerance(float tolerance) {
        if (this.tolerance != tolerance) {
            this.tolerance = tolerance;
            if (tolerance != 0.0F) {
                boolean recalculate;
                do {
                    recalculate = false;
                    for (int i = 1; i < this.size(); i++) {
                        PaletteColor c0 = this.get(i - 1);
                        PaletteColor c1 = this.get(i);
                        if (c0.distanceTo(c1) <= tolerance) {
                            Palette tempPal = new Palette(List.of(c0, c1));
                            for (int after = i + 1; after < this.size() && tempPal.calculateAverage().distanceTo(this.get(after)) <= tolerance; after++) {
                                tempPal.add(this.get(after));
                            }
                            tempPal.getValues().forEach(this::remove);
                            this.add(tempPal.calculateAverage());
                            recalculate = true;
                        }
                    }
                } while (recalculate);
            }
        }
    }

    public int size() {
        return this.internal.size();
    }

    public List<PaletteColor> getValues() {
        return this.internal;
    }

    private void sort() {
        Collections.sort(this.internal);
    }

    private void addUnchecked(PaletteColor color) {
        if (color.rgb().alpha() != 0.0F) {
            this.internal.add(color);
            this.sort();
        }
    }

    public boolean add(PaletteColor color) {
        if (color.rgb().alpha() == 0.0F) {
            return false;
        } else if (!this.hasColor(color)) {
            this.internal.add(color);
            this.sort();
            return true;
        } else {
            return false;
        }
    }

    public boolean addAll(@NotNull Collection<? extends PaletteColor> colors) {
        boolean added = false;
        for (PaletteColor c : colors) {
            if (!this.hasColor(c)) {
                this.internal.add(c);
                added = true;
            }
        }
        if (added) {
            this.sort();
            return true;
        } else {
            return false;
        }
    }

    public void set(int index, PaletteColor color) {
        if (color.rgb().alpha() != 0.0F) {
            if (!this.hasColor(color)) {
                this.internal.set(index, color);
            }
        }
    }

    public PaletteColor get(int index) {
        return (PaletteColor) this.internal.get(index);
    }

    public int indexOf(PaletteColor color) {
        return this.internal.indexOf(color);
    }

    public boolean hasColor(int rgba) {
        return this.hasColor(new PaletteColor(rgba), 0.0F);
    }

    public boolean hasColor(PaletteColor color) {
        return this.hasColor(color, this.tolerance);
    }

    public boolean hasColor(PaletteColor color, float tolerance) {
        if (color.rgb().alpha() != 0.0F) {
            for (PaletteColor c : this.getValues()) {
                if (tolerance == 0.0F) {
                    if (c.value() == color.value()) {
                        return true;
                    }
                } else if (c.distanceTo(color) <= tolerance) {
                    return true;
                }
            }
        }
        return false;
    }

    public PaletteColor getDarkest() {
        return this.getDarkest(0);
    }

    public PaletteColor getDarkest(int offset) {
        return this.get(offset);
    }

    public PaletteColor getLightest() {
        return this.getLightest(0);
    }

    public PaletteColor getLightest(int offset) {
        return this.get(this.internal.size() - 1 - offset);
    }

    public PaletteColor remove(int index) {
        return (PaletteColor) this.internal.remove(index);
    }

    public boolean remove(PaletteColor color) {
        return this.internal.remove(color);
    }

    public boolean remove(Object o) {
        return this.internal.remove(o);
    }

    public boolean removeAll(Collection<?> colors) {
        if (this.internal.removeAll(colors)) {
            this.sort();
            return true;
        } else {
            return false;
        }
    }

    public PaletteColor calculateAverage() {
        return new PaletteColor(LABColor.averageColors((LABColor[]) this.getValues().stream().map(PaletteColor::lab).toArray(LABColor[]::new)));
    }

    public PaletteColor getColorAtSlope(float slope) {
        int index = Math.round((float) (this.internal.size() - 1) * slope);
        return (PaletteColor) this.internal.get(index);
    }

    public PaletteColor getCenterColor() {
        PaletteColor center = this.calculateAverage();
        return this.getColorClosestTo(center);
    }

    public PaletteColor getColorClosestTo(PaletteColor target) {
        PaletteColor bestMatch = target;
        float lastDist = Float.MAX_VALUE;
        for (PaletteColor c : this.getValues()) {
            float dist = target.distanceTo(c);
            if (dist < lastDist) {
                lastDist = dist;
                bestMatch = c;
            }
        }
        return bestMatch;
    }

    public void matchSize(int targetSize) {
        this.matchSize(targetSize, null);
    }

    public void matchSize(int targetSize, @Nullable Float targetLumStep) {
        if (this.size() != 0 && targetSize > 0) {
            if (this.size() == 2 && targetLumStep == null) {
                PaletteColor lightest = this.getLightest();
                PaletteColor darkest = this.getDarkest();
                Palette other = fromArc(lightest.hcl(), darkest.hcl(), targetSize);
                this.internal.clear();
                this.internal.addAll(other.getValues());
            }
            while (this.size() > targetSize) {
                if (this.size() > 14) {
                    this.removeLeastUsed();
                } else {
                    this.reduceAndAverage();
                }
            }
            boolean down = true;
            boolean canIncreaseDown = true;
            boolean canIncreaseUp = true;
            int currentSize;
            while ((currentSize = this.size()) < targetSize) {
                if ((canIncreaseDown || canIncreaseUp) && this.shouldChangeRange(targetSize, targetLumStep)) {
                    if (down) {
                        this.increaseDown();
                    } else {
                        this.increaseUp();
                    }
                    if (currentSize == this.size()) {
                        if (down) {
                            canIncreaseDown = false;
                        } else {
                            canIncreaseUp = false;
                        }
                        down = !down;
                    }
                    if (canIncreaseDown && canIncreaseUp) {
                        down = !down;
                    }
                } else {
                    this.increaseInner();
                }
            }
        } else {
            throw new UnsupportedOperationException("Palette size can't be 0");
        }
    }

    private boolean shouldChangeRange(int targetSize, @Nullable Float targetStep) {
        if (targetStep == null) {
            return false;
        } else {
            float targetRange = (float) targetSize * targetStep;
            float currentRange = this.getAverageLuminanceStep() * (float) this.size();
            float percentageCutoff = 0.18F;
            float inc = 1.0F + percentageCutoff;
            float dec = 1.0F / inc;
            float ratio = targetRange / currentRange;
            return ratio > inc || ratio < dec;
        }
    }

    public PaletteColor removeLeastUsed() {
        PaletteColor toRemove = (PaletteColor) this.internal.get(0);
        for (PaletteColor p : this.internal) {
            if (p.getOccurrence() < toRemove.getOccurrence()) {
                toRemove = p;
            }
        }
        this.remove(toRemove);
        return toRemove;
    }

    public PaletteColor reduce() {
        int index = 0;
        float minDelta = 10000.0F;
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            float dl = l - lastLum;
            if (dl < minDelta) {
                index = i;
                minDelta = dl;
            }
            lastLum = l;
        }
        return this.remove(index);
    }

    public PaletteColor reduceAndAverage() {
        int index = 0;
        float minDelta = 10000.0F;
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            float dl = l - lastLum;
            if (dl < minDelta) {
                index = i;
                minDelta = dl;
            }
            lastLum = l;
        }
        PaletteColor toRemove = this.get(index);
        PaletteColor toRemove2 = this.get(index - 1);
        this.remove(toRemove);
        this.remove(toRemove2);
        PaletteColor newColor = new PaletteColor(toRemove.lab().mixWith(toRemove2.lab()));
        newColor.setOccurrence(toRemove.getOccurrence() * toRemove2.getOccurrence());
        this.add(newColor);
        return newColor;
    }

    public void changeSizeMatchingLuminanceSpan(float targetLuminanceSpan) {
        for (float currentSpan = this.getLuminanceSpan(); (double) Mth.abs(currentSpan - targetLuminanceSpan) > 0.5 * (double) this.getAverageLuminanceStep(); currentSpan = this.getLuminanceSpan()) {
            if (currentSpan < targetLuminanceSpan) {
                if (this.getLightest().luminance() < 1.0F - this.getDarkest().luminance()) {
                    this.increaseUp();
                } else {
                    this.increaseDown();
                }
            } else {
                if (!(currentSpan > targetLuminanceSpan)) {
                    break;
                }
                if (this.getLightest().luminance() > 1.0F - this.getDarkest().luminance()) {
                    this.reduceUp();
                } else {
                    this.reduceDown();
                }
            }
        }
    }

    public void expandMatchingLuminanceRange(float minLuminance, float maxLuminance) {
        float currentMin = this.getDarkest().luminance();
        float currentMax = this.getLightest().luminance();
        while ((double) Mth.abs(currentMin - minLuminance) > 0.5 * (double) this.getAverageLuminanceStep()) {
            if (currentMin < minLuminance) {
                this.reduceDown();
            } else {
                this.increaseDown();
            }
        }
        while ((double) Mth.abs(currentMax - maxLuminance) > 0.5 * (double) this.getAverageLuminanceStep()) {
            if (currentMax > maxLuminance) {
                this.reduceUp();
            } else {
                this.increaseUp();
            }
        }
    }

    private boolean hasLuminanceGap() {
        return this.hasLuminanceGap(1.7F);
    }

    private boolean hasLuminanceGap(float cutoff) {
        List<Float> list = this.getLuminanceSteps();
        float mean = this.getAverageLuminanceStep();
        for (Float s : list) {
            if (s > cutoff * mean) {
                return true;
            }
        }
        return false;
    }

    public float getLuminanceStepVariationCoeff() {
        List<Float> list = this.getLuminanceSteps();
        float mean = this.getAverageLuminanceStep();
        float sum = 0.0F;
        for (Float s : list) {
            sum += (s - mean) * (s - mean);
        }
        return Mth.sqrt(sum / (float) (list.size() - 1)) / mean;
    }

    public List<Float> getLuminanceSteps() {
        List<Float> list = new ArrayList();
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            list.add(l - lastLum);
            lastLum = l;
        }
        return list;
    }

    public float getAverageLuminanceStep() {
        List<Float> list = this.getLuminanceSteps();
        float total = 0.0F;
        for (Float v : list) {
            total += v;
        }
        return total / (float) list.size();
    }

    public float getLuminanceSpan() {
        return this.getLightest().luminance() - this.getDarkest().luminance();
    }

    public float getCenterLuminance() {
        return (this.getLightest().luminance() + this.getDarkest().luminance()) / 2.0F;
    }

    public PaletteColor reduceUp() {
        PaletteColor c = this.getLightest();
        this.remove(c);
        return c;
    }

    public PaletteColor reduceDown() {
        PaletteColor c = this.getDarkest();
        this.remove(c);
        return c;
    }

    public PaletteColor increaseInner() {
        assert this.size() < 2;
        int index = 1;
        float maxDelta = 0.0F;
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            float dl = l - lastLum;
            if (dl > maxDelta) {
                index = i;
                maxDelta = dl;
            }
            lastLum = l;
        }
        HCLColor c1 = this.get(index).hcl();
        HCLColor c2 = this.get(index - 1).hcl();
        PaletteColor newC = new PaletteColor(c1.mixWith(c2));
        this.addUnchecked(newC);
        return newC;
    }

    public PaletteColor increaseUp() {
        assert this.size() < 2;
        float averageDeltaLum = this.getAverageLuminanceStep();
        HCLColor lightest = this.getLightest().hcl();
        HCLColor secondLightest = this.get(this.size() - 2).hcl();
        HCLColor cc = this.getNextColor(averageDeltaLum, lightest, secondLightest);
        PaletteColor pl = new PaletteColor(cc);
        this.add(pl);
        return pl;
    }

    public PaletteColor increaseDown() {
        assert this.size() < 2;
        float averageDeltaLum = this.getAverageLuminanceStep();
        HCLColor darkest = this.getDarkest().hcl();
        HCLColor secondDarkest = this.get(1).hcl();
        HCLColor cc = this.getNextColor(-averageDeltaLum, darkest, secondDarkest);
        PaletteColor pl = new PaletteColor(cc);
        this.add(pl);
        return pl;
    }

    private HCLColor getNextColor(float lumIncrease, HCLColor source, HCLColor previous) {
        float newLum = source.luminance() + lumIncrease;
        float h1 = source.hue();
        float c1 = source.chroma();
        float a1 = source.alpha();
        float h2 = previous.hue();
        float c2 = previous.chroma();
        float a2 = previous.alpha();
        float hueIncrease = (float) ((double) (-MthUtils.signedAngleDiff((double) h1 * Math.PI * 2.0, (double) h2 * Math.PI * 2.0)) / (Math.PI * 2));
        float newH = h1 + hueIncrease * 0.5F;
        while (newH < 0.0F) {
            newH++;
        }
        float newC = c1 + (c1 - c2);
        float newA = a1 + (a1 - a2);
        return new HCLColor(newH, newC, newLum, newA);
    }

    public static Palette merge(Palette... palettes) {
        if (palettes.length == 1) {
            return new Palette(palettes[0].getValues());
        } else {
            Map<Integer, PaletteColor> map = new HashMap();
            for (Palette p : palettes) {
                for (PaletteColor c : p.getValues()) {
                    int color = c.value();
                    if (map.containsKey(color)) {
                        ((PaletteColor) map.get(color)).setOccurrence(((PaletteColor) map.get(color)).getOccurrence() + c.getOccurrence());
                    } else {
                        map.put(color, c);
                    }
                }
            }
            return map.values().size() == 0 ? new Palette(new ArrayList()) : new Palette(map.values());
        }
    }

    public static <C extends BaseColor<C>> Palette ofColors(Collection<C> colors) {
        return new Palette((Collection<PaletteColor>) colors.stream().map(PaletteColor::new).collect(Collectors.toSet()));
    }

    public static <T extends BaseColor<T>> Palette fromArc(T light, T dark, int size) {
        List<BaseColor<T>> colors = new ArrayList();
        if (size <= 1) {
            throw new IllegalArgumentException("Size must be greater than one");
        } else {
            for (int i = 0; i < size; i++) {
                colors.add(dark.mixWith(light, (float) i / ((float) size - 1.0F)));
            }
            return new Palette((Collection<PaletteColor>) colors.stream().map(PaletteColor::new).collect(Collectors.toSet()));
        }
    }

    public static Palette fromImage(TextureImage image) {
        return fromImage(image, null);
    }

    public static Palette fromImage(TextureImage image, @Nullable TextureImage mask) {
        return fromImage(image, mask, 0.0055555557F);
    }

    public static Palette fromImage(TextureImage textureImage, @Nullable TextureImage textureMask, float tolerance) {
        List<Palette> palettes = fromAnimatedImage(textureImage, textureMask, 0.0F);
        Palette palette = merge((Palette[]) palettes.toArray(new Palette[0]));
        if (tolerance != 0.0F) {
            palette.updateTolerance(tolerance);
        }
        if (palette.isEmpty()) {
            throw new RuntimeException("Palette from image " + textureImage + " ended ub being empty");
        } else {
            return palette;
        }
    }

    public static List<Palette> fromAnimatedImage(TextureImage image) {
        return fromAnimatedImage(image, null);
    }

    public static List<Palette> fromAnimatedImage(TextureImage image, @Nullable TextureImage mask) {
        return fromAnimatedImage(image, mask, 0.0055555557F);
    }

    public static List<Palette> fromAnimatedImage(TextureImage textureImage, @Nullable TextureImage textureMask, float tolerance) {
        if (textureMask == null || textureImage.frameCount() == textureMask.frameCount() && textureMask.frameWidth() >= textureImage.frameWidth() && textureMask.frameHeight() >= textureImage.frameHeight()) {
            List<Palette> palettes = new ArrayList();
            NativeImage mask = textureMask == null ? null : textureMask.getImage();
            NativeImage image = textureImage.getImage();
            List<Map<Integer, PaletteColor>> paletteBuilders = new ArrayList();
            textureImage.forEachFramePixel((index, x, y) -> {
                if (paletteBuilders.size() <= index) {
                    paletteBuilders.add(new HashMap());
                }
                Map<Integer, PaletteColor> builder = (Map<Integer, PaletteColor>) paletteBuilders.get(index);
                if (mask == null || FastColor.ABGR32.alpha(mask.getPixelRGBA(x, y)) == 0) {
                    int color = image.getPixelRGBA(x, y);
                    if (FastColor.ABGR32.alpha(color) != 0) {
                        PaletteColor paletteColor = (PaletteColor) builder.computeIfAbsent(color, px -> new PaletteColor(color));
                        paletteColor.setOccurrence(paletteColor.getOccurrence() + 1);
                    }
                }
            });
            for (Map<Integer, PaletteColor> p : paletteBuilders) {
                Palette pal;
                if (p.size() == 0) {
                    pal = new Palette(new ArrayList());
                } else {
                    pal = new Palette(p.values(), tolerance);
                }
                palettes.add(pal);
            }
            return palettes;
        } else {
            throw new UnsupportedOperationException("Palette mask needs to be at least as large as the target image and have the same format");
        }
    }

    @NotNull
    public Iterator<PaletteColor> iterator() {
        return new Palette.ItrWrapper();
    }

    @NotNull
    public Object[] toArray() {
        return this.internal.toArray();
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return (T[]) this.internal.toArray(a);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return this.internal.containsAll(c);
    }

    @Deprecated
    public boolean contains(Object o) {
        return this.internal.contains(o);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        if (this.internal.retainAll(c)) {
            this.sort();
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        this.internal.clear();
    }

    private class ItrWrapper implements Iterator<PaletteColor> {

        private final Iterator<PaletteColor> itr = Palette.this.internal.iterator();

        public boolean hasNext() {
            return this.itr.hasNext();
        }

        public PaletteColor next() {
            return (PaletteColor) this.itr.next();
        }

        public void remove() {
            this.itr.remove();
            Palette.this.sort();
        }
    }
}