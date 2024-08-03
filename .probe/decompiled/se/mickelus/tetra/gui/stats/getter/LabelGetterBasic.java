package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;

@ParametersAreNonnullByDefault
public class LabelGetterBasic implements ILabelGetter {

    public static final ILabelGetter integerLabel = new LabelGetterBasic("%.0f", "%+.0f");

    public static final ILabelGetter integerLabelInverted = new LabelGetterBasic("%.0f", "%+.0f", true);

    public static final ILabelGetter decimalLabel = new LabelGetterBasic("%.02f", "%+.02f");

    public static final ILabelGetter singleDecimalLabel = new LabelGetterBasic("%.01f", "%+.01f");

    public static final ILabelGetter decimalLabelInverted = new LabelGetterBasic("%.02f", "%+.02f", true);

    public static final ILabelGetter percentageLabel = new LabelGetterBasic("%.0f%%", "%+.0f%%");

    public static final ILabelGetter percentageLabelInverted = new LabelGetterBasic("%.0f%%", "%+.0f%%", true);

    public static final ILabelGetter percentageLabelDecimal = new LabelGetterBasic("%.01f%%", "%+.01f%%");

    public static final ILabelGetter percentageLabelDecimalInverted = new LabelGetterBasic("%.01f%%", "%+.01f%%", true);

    public static final ILabelGetter noLabel = new ILabelGetter() {

        @Override
        public String getLabel(double value, double diffValue, boolean flipped) {
            return "";
        }

        @Override
        public String getLabelMerged(double value, double diffValue) {
            return "";
        }
    };

    protected static final String increaseColorFont = ChatFormatting.GREEN.toString();

    protected static final String decreaseColorFont = ChatFormatting.RED.toString();

    protected String formatDiff;

    protected String formatDiffFlipped;

    protected String format;

    protected boolean inverted;

    public LabelGetterBasic(String format) {
        this(format, format);
    }

    public LabelGetterBasic(String format, String formatDiff) {
        this.formatDiff = "%s(" + formatDiff + ") %s" + format;
        this.formatDiffFlipped = format + " %s(" + formatDiff + ")";
        this.format = format;
    }

    public LabelGetterBasic(String format, String formatDiff, boolean inverted) {
        this.formatDiff = "%s(" + formatDiff + ") %s" + format;
        this.formatDiffFlipped = format + " %s(" + formatDiff + ")";
        this.format = format;
        this.inverted = inverted;
    }

    @Override
    public String getLabel(double value, double diffValue, boolean flipped) {
        if (value != diffValue) {
            return flipped ? String.format(this.formatDiffFlipped, diffValue, this.getDiffColor(value, diffValue), diffValue - value) : String.format(this.formatDiff, this.getDiffColor(value, diffValue), diffValue - value, ChatFormatting.RESET, diffValue);
        } else {
            return String.format(this.format, diffValue);
        }
    }

    @Override
    public String getLabelMerged(double value, double diffValue) {
        return value != diffValue ? this.getDiffColor(value, diffValue) + String.format(this.format, diffValue) : String.format(this.format, diffValue);
    }

    protected String getDiffColor(double value, double diffValue) {
        return value < diffValue != this.inverted ? increaseColorFont : decreaseColorFont;
    }
}