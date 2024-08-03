package snownee.jade.util;

import java.text.NumberFormat;
import snownee.jade.api.ui.IDisplayHelper;

public class FluidTextHelper {

    public static String getUnicodeMillibuckets(long amount, boolean simplify) {
        return amount < 100000L ? NumberFormat.getNumberInstance().format(amount) + "mB" : IDisplayHelper.get().humanReadableNumber((double) amount, "B", true, null);
    }
}