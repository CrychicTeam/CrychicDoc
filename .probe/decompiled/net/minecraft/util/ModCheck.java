package net.minecraft.util;

import java.util.function.Supplier;
import org.apache.commons.lang3.ObjectUtils;

public record ModCheck(ModCheck.Confidence f_184592_, String f_184593_) {

    private final ModCheck.Confidence confidence;

    private final String description;

    public ModCheck(ModCheck.Confidence f_184592_, String f_184593_) {
        this.confidence = f_184592_;
        this.description = f_184593_;
    }

    public static ModCheck identify(String p_184601_, Supplier<String> p_184602_, String p_184603_, Class<?> p_184604_) {
        String $$4 = (String) p_184602_.get();
        if (!p_184601_.equals($$4)) {
            return new ModCheck(ModCheck.Confidence.DEFINITELY, p_184603_ + " brand changed to '" + $$4 + "'");
        } else {
            return p_184604_.getSigners() == null ? new ModCheck(ModCheck.Confidence.VERY_LIKELY, p_184603_ + " jar signature invalidated") : new ModCheck(ModCheck.Confidence.PROBABLY_NOT, p_184603_ + " jar signature and brand is untouched");
        }
    }

    public boolean shouldReportAsModified() {
        return this.confidence.shouldReportAsModified;
    }

    public ModCheck merge(ModCheck p_184599_) {
        return new ModCheck((ModCheck.Confidence) ObjectUtils.max(new ModCheck.Confidence[] { this.confidence, p_184599_.confidence }), this.description + "; " + p_184599_.description);
    }

    public String fullDescription() {
        return this.confidence.description + " " + this.description;
    }

    public static enum Confidence {

        PROBABLY_NOT("Probably not.", false), VERY_LIKELY("Very likely;", true), DEFINITELY("Definitely;", true);

        final String description;

        final boolean shouldReportAsModified;

        private Confidence(String p_184622_, boolean p_184623_) {
            this.description = p_184622_;
            this.shouldReportAsModified = p_184623_;
        }
    }
}