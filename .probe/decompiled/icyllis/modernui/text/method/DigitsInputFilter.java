package icyllis.modernui.text.method;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.DecimalFormatSymbols;
import icyllis.modernui.text.SpannableStringBuilder;
import icyllis.modernui.text.Spanned;
import it.unimi.dsi.fastutil.chars.CharLinkedOpenHashSet;
import java.util.HashMap;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public class DigitsInputFilter extends NumberInputFilter {

    private static final String DEFAULT_DECIMAL_POINT_CHARS = ".";

    private static final String DEFAULT_SIGN_CHARS = "-+";

    private static final char HYPHEN_MINUS = '-';

    private static final char MINUS_SIGN = '−';

    private static final char EN_DASH = '–';

    private static final int SIGN = 1;

    private static final int DECIMAL = 2;

    private static final char[][] COMPATIBILITY_CHARACTERS = new char[][] { { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }, { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+' }, { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' }, { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.' } };

    private char[] mAccepted;

    private final boolean mSign;

    private final boolean mDecimal;

    private final boolean mStringMode;

    private String mDecimalPointChars = ".";

    private String mSignChars = "-+";

    private static final Object sLocaleCacheLock = new Object();

    @GuardedBy("sLocaleCacheLock")
    private static final HashMap<Locale, DigitsInputFilter[]> sLocaleInstanceCache = new HashMap();

    private static final Object sStringCacheLock = new Object();

    @GuardedBy("sStringCacheLock")
    private static final HashMap<String, DigitsInputFilter> sStringInstanceCache = new HashMap();

    @Nonnull
    private static String stripBidiControls(@Nonnull String sign) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sign.length(); i++) {
            char c = sign.charAt(i);
            if (!UCharacter.hasBinaryProperty(c, 2)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private DigitsInputFilter(@Nonnull String accepted) {
        this.mSign = false;
        this.mDecimal = false;
        this.mStringMode = true;
        this.mAccepted = new char[accepted.length()];
        accepted.getChars(0, accepted.length(), this.mAccepted, 0);
    }

    private DigitsInputFilter(@Nullable Locale locale, boolean sign, boolean decimal) {
        this.mSign = sign;
        this.mDecimal = decimal;
        this.mStringMode = false;
        if (locale == null) {
            this.setToCompat();
        } else {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
            CharLinkedOpenHashSet chars = new CharLinkedOpenHashSet();
            String[] digits = symbols.getDigitStrings();
            for (int i = 0; i < 10; i++) {
                if (digits[i].length() > 1) {
                    this.setToCompat();
                    return;
                }
                chars.add(digits[i].charAt(0));
            }
            if (sign || decimal) {
                if (sign) {
                    String minusString = stripBidiControls(symbols.getMinusSignString());
                    String plusString = stripBidiControls(symbols.getPlusSignString());
                    if (minusString.length() > 1 || plusString.length() > 1) {
                        this.setToCompat();
                        return;
                    }
                    char minus = minusString.charAt(0);
                    char plus = plusString.charAt(0);
                    chars.add(minus);
                    chars.add(plus);
                    this.mSignChars = "" + minus + plus;
                    if (minus == 8722 || minus == 8211) {
                        chars.add('-');
                        this.mSignChars = this.mSignChars + "-";
                    }
                }
                if (decimal) {
                    String separatorString = symbols.getDecimalSeparatorString();
                    if (separatorString.length() > 1) {
                        this.setToCompat();
                        return;
                    }
                    char separatorChar = separatorString.charAt(0);
                    chars.add(separatorChar);
                    this.mDecimalPointChars = String.valueOf(separatorChar);
                }
            }
            this.mAccepted = chars.toCharArray();
        }
    }

    private void setToCompat() {
        this.mDecimalPointChars = ".";
        this.mSignChars = "-+";
        int kind = (this.mSign ? 1 : 0) | (this.mDecimal ? 2 : 0);
        this.mAccepted = COMPATIBILITY_CHARACTERS[kind];
    }

    @Nonnull
    @Override
    protected char[] getAcceptedChars() {
        return this.mAccepted;
    }

    private boolean isSignChar(char c) {
        return this.mSignChars.indexOf(c) != -1;
    }

    private boolean isDecimalPointChar(char c) {
        return this.mDecimalPointChars.indexOf(c) != -1;
    }

    @Nonnull
    public static DigitsInputFilter getInstance(@Nullable Locale locale) {
        return getInstance(locale, false, false);
    }

    @Nonnull
    public static DigitsInputFilter getInstance(@Nullable Locale locale, boolean sign, boolean decimal) {
        int kind = (sign ? 1 : 0) | (decimal ? 2 : 0);
        synchronized (sLocaleCacheLock) {
            DigitsInputFilter[] cachedValue = (DigitsInputFilter[]) sLocaleInstanceCache.get(locale);
            if (cachedValue != null && cachedValue[kind] != null) {
                return cachedValue[kind];
            } else {
                if (cachedValue == null) {
                    cachedValue = new DigitsInputFilter[4];
                    sLocaleInstanceCache.put(locale, cachedValue);
                }
                return cachedValue[kind] = new DigitsInputFilter(locale, sign, decimal);
            }
        }
    }

    @Nonnull
    public static DigitsInputFilter getInstance(@Nonnull String accepted) {
        synchronized (sStringCacheLock) {
            DigitsInputFilter result = (DigitsInputFilter) sStringInstanceCache.get(accepted);
            if (result == null) {
                result = new DigitsInputFilter(accepted);
                sStringInstanceCache.put(accepted, result);
            }
            return result;
        }
    }

    @Nonnull
    public static DigitsInputFilter getInstance(@Nullable Locale locale, @Nonnull DigitsInputFilter listener) {
        return listener.mStringMode ? listener : getInstance(locale, listener.mSign, listener.mDecimal);
    }

    @Override
    public CharSequence filter(@Nonnull CharSequence source, int start, int end, @Nonnull Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (!this.mSign && !this.mDecimal) {
            return out;
        } else {
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }
            int sign = -1;
            int decimal = -1;
            int dlen = dest.length();
            for (int i = 0; i < dstart; i++) {
                char c = dest.charAt(i);
                if (this.isSignChar(c)) {
                    sign = i;
                } else if (this.isDecimalPointChar(c)) {
                    decimal = i;
                }
            }
            for (int ix = dend; ix < dlen; ix++) {
                char c = dest.charAt(ix);
                if (this.isSignChar(c)) {
                    return "";
                }
                if (this.isDecimalPointChar(c)) {
                    decimal = ix;
                }
            }
            SpannableStringBuilder stripped = null;
            for (int ix = end - 1; ix >= start; ix--) {
                char cx = source.charAt(ix);
                boolean strip = false;
                if (this.isSignChar(cx)) {
                    if (ix != start || dstart != 0) {
                        strip = true;
                    } else if (sign >= 0) {
                        strip = true;
                    } else {
                        sign = ix;
                    }
                } else if (this.isDecimalPointChar(cx)) {
                    if (decimal >= 0) {
                        strip = true;
                    } else {
                        decimal = ix;
                    }
                }
                if (strip) {
                    if (end == start + 1) {
                        return "";
                    }
                    if (stripped == null) {
                        stripped = new SpannableStringBuilder(source, start, end);
                    }
                    stripped.delete(ix - start, ix + 1 - start);
                }
            }
            return (CharSequence) (stripped != null ? stripped : out);
        }
    }
}