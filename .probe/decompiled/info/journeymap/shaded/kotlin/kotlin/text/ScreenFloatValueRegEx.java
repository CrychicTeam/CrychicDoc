package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/text/ScreenFloatValueRegEx;", "", "()V", "value", "Linfo/journeymap/shaded/kotlin/kotlin/text/Regex;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class ScreenFloatValueRegEx {

    @NotNull
    public static final ScreenFloatValueRegEx INSTANCE = new ScreenFloatValueRegEx();

    @JvmField
    @NotNull
    public static final Regex value;

    private ScreenFloatValueRegEx() {
    }

    static {
        ScreenFloatValueRegEx var0 = INSTANCE;
        ???;
        String Digits = "(\\p{Digit}+)";
        String HexDigits = "(\\p{XDigit}+)";
        String Exp = Intrinsics.stringPlus("[eE][+-]?", Digits);
        String HexString = "(0[xX]" + HexDigits + "(\\.)?)|(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ')';
        String Number = '(' + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|(\\.(" + Digits + ")(" + Exp + ")?)|((" + HexString + ")[pP][+-]?" + Digits + ')';
        String fpRegex = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((" + Number + ")[fFdD]?))[\\x00-\\x20]*";
        value = new Regex(fpRegex);
    }
}