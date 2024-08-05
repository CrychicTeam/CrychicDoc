package net.mehvahdjukaar.moonlight.api.resources.textures;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.util.math.colors.BaseColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HCLColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.LABColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import org.jetbrains.annotations.NotNull;

public class PaletteColor implements Comparable<PaletteColor> {

    private final int value;

    private final RGBColor color;

    private final LABColor lab;

    private final HCLColor hcl;

    private int occurrence = 0;

    public PaletteColor(int color) {
        this(new RGBColor(color));
    }

    public PaletteColor(BaseColor<?> color, int occurrence) {
        this(color);
        this.setOccurrence(occurrence);
    }

    public PaletteColor(BaseColor<?> color) {
        RGBColor c = color.asRGB();
        if (c.alpha() == 0.0F) {
            this.color = new RGBColor(0);
        } else {
            this.color = c;
        }
        this.lab = this.color.asLAB();
        this.value = this.color.toInt();
        this.hcl = this.lab.asHCL();
    }

    public int value() {
        return this.value;
    }

    public RGBColor rgb() {
        return this.color;
    }

    public LABColor lab() {
        return this.lab;
    }

    public HCLColor hcl() {
        return this.hcl;
    }

    public PaletteColor getDarkened() {
        PaletteColor p = new PaletteColor(this.lab.withLuminance(this.lab.luminance() * 0.9F));
        p.setOccurrence(this.getOccurrence());
        return p;
    }

    public PaletteColor getLightened() {
        PaletteColor p = new PaletteColor(this.lab.withLuminance(this.lab.luminance() * 0.9F + 0.1F));
        p.setOccurrence(this.getOccurrence());
        return p;
    }

    public float luminance() {
        return this.lab.luminance();
    }

    public float distanceTo(PaletteColor color) {
        return this.lab.distTo(color.lab);
    }

    public int compareTo(@NotNull PaletteColor o) {
        return Float.compare(this.lab.luminance(), o.lab.luminance());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PaletteColor that = (PaletteColor) o;
            return this.value == that.value;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.value });
    }

    public String toString() {
        return "PaletteColor: " + Integer.toHexString(this.value);
    }

    public int getOccurrence() {
        return this.occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }
}