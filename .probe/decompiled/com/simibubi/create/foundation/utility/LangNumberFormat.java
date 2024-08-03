package com.simibubi.create.foundation.utility;

import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class LangNumberFormat {

    private NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);

    public static LangNumberFormat numberFormat = new LangNumberFormat();

    public NumberFormat get() {
        return this.format;
    }

    public void update() {
        this.format = NumberFormat.getInstance(Minecraft.getInstance().getLanguageManager().getJavaLocale());
        this.format.setMaximumFractionDigits(2);
        this.format.setMinimumFractionDigits(0);
        this.format.setGroupingUsed(true);
    }

    public static String format(double d) {
        if (Mth.equal(d, 0.0)) {
            d = 0.0;
        }
        return numberFormat.get().format(d).replace(" ", " ");
    }
}