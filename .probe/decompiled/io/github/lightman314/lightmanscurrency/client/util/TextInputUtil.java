package io.github.lightman314.lightmanscurrency.client.util;

import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import io.github.lightman314.lightmanscurrency.util.NumberUtil;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;

public class TextInputUtil {

    private static final String INTEGER_WHITELIST = "0123456789";

    private static final String FLOAT_WHITELIST = "0123456789.";

    public static int getIntegerValue(EditBox textInput) {
        return getIntegerValue(textInput, 0);
    }

    public static int getIntegerValue(EditBox textInput, int defaultValue) {
        return NumberUtil.GetIntegerValue(textInput.getValue(), defaultValue);
    }

    public static boolean isLong(EditBox textInput) {
        return isLong(textInput.getValue());
    }

    public static boolean isLong(String text) {
        if (text == null) {
            return false;
        } else {
            try {
                long nfe = Long.parseLong(text);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

    public static long getLongValue(EditBox textInput) {
        return getLongValue(textInput, 0);
    }

    public static long getLongValue(EditBox textInput, int defaultValue) {
        return isLong(textInput) ? Long.parseLong(textInput.getValue()) : (long) defaultValue;
    }

    public static boolean isFloat(EditBox textInput) {
        return isFloat(textInput.getValue());
    }

    public static boolean isFloat(String text) {
        if (text == null) {
            return false;
        } else {
            try {
                float nfe = Float.parseFloat(text);
                return true;
            } catch (NumberFormatException var2) {
                return false;
            }
        }
    }

    public static float getFloatValue(EditBox textInput) {
        return getFloatValue(textInput, 0.0F);
    }

    public static float getFloatValue(EditBox textInput, float defaultValue) {
        return isFloat(textInput) ? Float.parseFloat(textInput.getValue()) : defaultValue;
    }

    public static boolean isDouble(EditBox textInput) {
        return isDouble(textInput.getValue());
    }

    public static boolean isDouble(String text) {
        if (text == null) {
            return false;
        } else {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException var2) {
                return false;
            }
        }
    }

    public static boolean isPositiveDouble(String text) {
        if (text == null) {
            return false;
        } else if (text.isEmpty()) {
            return true;
        } else {
            try {
                double d = Double.parseDouble(text);
                return d >= 0.0;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

    public static double getDoubleValue(EditBox textInput) {
        return getDoubleValue(textInput, 0.0);
    }

    public static double getDoubleValue(EditBox textInput, double defaultValue) {
        return isDouble(textInput) ? Double.parseDouble(textInput.getValue()) : defaultValue;
    }

    public static void whitelistInteger(EditBox textInput) {
        whitelistText(textInput, "0123456789");
    }

    public static void whitelistInteger(EditBox textInput, long minValue, long maxValue) {
        whitelistInteger(textInput);
        if (textInput.getValue().length() > 0) {
            long currentValue = getLongValue(textInput);
            if (currentValue < minValue || currentValue > maxValue) {
                currentValue = MathUtil.clamp(currentValue, minValue, maxValue);
                textInput.setValue(Long.toString(currentValue));
            }
        }
    }

    public static void whitelistFloat(EditBox textInput) {
        whitelistText(textInput, "0123456789.");
    }

    public static void whitelistText(EditBox textInput, String allowedChars) {
        StringBuilder newText = new StringBuilder(textInput.getValue());
        for (int i = 0; i < newText.length(); i++) {
            boolean allowed = false;
            for (int x = 0; x < allowedChars.length() && !allowed; x++) {
                if (allowedChars.charAt(x) == newText.charAt(i)) {
                    allowed = true;
                }
            }
            if (!allowed) {
                newText.deleteCharAt(i--);
            }
        }
        textInput.setValue(newText.toString());
    }

    public static Object CreateInputHandler(@Nonnull EditBox editBox, int startingValue, int minValue, int maxValue, @Nonnull Consumer<Integer> onValueChange) {
        return new TextInputUtil.IntegerInputHandler(editBox, startingValue, minValue, maxValue, onValueChange);
    }

    private static class IntegerInputHandler implements IEasyTickable {

        private final EditBox editBox;

        private final int minValue;

        private final int maxValue;

        private final Consumer<Integer> onValueChange;

        private int lastValue;

        IntegerInputHandler(@Nonnull EditBox editBox, int startingValue, int minValue, int maxValue, @Nonnull Consumer<Integer> onValueChange) {
            this.editBox = editBox;
            this.lastValue = startingValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.onValueChange = onValueChange;
            this.editBox.setValue(Integer.toString(this.lastValue));
        }

        @Override
        public void tick() {
            TextInputUtil.whitelistInteger(this.editBox, (long) this.minValue, (long) this.maxValue);
            int newVal = MathUtil.clamp(TextInputUtil.getIntegerValue(this.editBox), this.minValue, this.maxValue);
            if (this.lastValue != newVal) {
                this.onValueChange.accept(newVal);
                this.lastValue = newVal;
            }
        }
    }
}