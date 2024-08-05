package net.minecraftforge.common;

import com.google.common.base.CharMatcher;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeI18n {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String ALLOWED_CHARS = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";

    private static final CharMatcher DISALLOWED_CHAR_MATCHER = CharMatcher.anyOf("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000").negate();

    private static Map<String, String> i18n;

    private static Map<String, FormatFactory> customFactories = new HashMap();

    private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    private static void parseException(String formatString, StringBuffer stringBuffer, Object objectToParse) {
        Throwable t = (Throwable) objectToParse;
        if (Objects.equals(formatString, "msg")) {
            stringBuffer.append(t.getClass().getName()).append(": ").append(t.getMessage());
        } else if (Objects.equals(formatString, "cls")) {
            stringBuffer.append(t.getClass().getName());
        }
    }

    private static void parseModInfo(String formatString, StringBuffer stringBuffer, Object modInfo) {
        IModInfo info = (IModInfo) modInfo;
        if (Objects.equals(formatString, "id")) {
            stringBuffer.append(info.getModId());
        } else if (Objects.equals(formatString, "name")) {
            stringBuffer.append(info.getDisplayName());
        }
    }

    public static String getPattern(String patternName) {
        return i18n == null ? patternName : (String) i18n.getOrDefault(patternName, patternName);
    }

    public static void loadLanguageData(Map<String, String> properties) {
        LOGGER.debug(Logging.CORE, "Loading I18N data entries: {}", properties.size());
        i18n = properties;
    }

    public static String parseMessage(String i18nMessage, Object... args) {
        String pattern = getPattern(i18nMessage);
        try {
            return parseFormat(pattern, args);
        } catch (IllegalArgumentException var4) {
            LOGGER.error(Logging.CORE, "Illegal format found `{}`", pattern);
            return pattern;
        }
    }

    public static String parseFormat(String format, Object... args) {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(format, customFactories);
        return extendedMessageFormat.format(args);
    }

    public static String stripSpecialChars(String message) {
        return DISALLOWED_CHAR_MATCHER.removeFrom(stripControlCodes(message));
    }

    public static String stripControlCodes(String text) {
        return PATTERN_CONTROL_CODE.matcher(text).replaceAll("");
    }

    static {
        customFactories.put("modinfo", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, objectToParse) -> parseModInfo(formatString, stringBuffer, objectToParse)));
        customFactories.put("lower", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, objectToParse) -> stringBuffer.append(StringUtils.toLowerCase(String.valueOf(objectToParse)))));
        customFactories.put("upper", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, objectToParse) -> stringBuffer.append(StringUtils.toUpperCase(String.valueOf(objectToParse)))));
        customFactories.put("exc", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, objectToParse) -> parseException(formatString, stringBuffer, objectToParse)));
        customFactories.put("vr", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat(MavenVersionStringHelper::parseVersionRange));
        customFactories.put("featurebound", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat(MavenVersionStringHelper::parseFeatureBoundValue));
        customFactories.put("i18n", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, o) -> stringBuffer.append(parseMessage(formatString, o))));
        customFactories.put("ornull", (FormatFactory) (name, formatString, locale) -> new ForgeI18n.CustomReadOnlyFormat((stringBuffer, o) -> stringBuffer.append(Objects.equals(String.valueOf(o), "null") ? parseMessage(formatString) : String.valueOf(o))));
    }

    public static class CustomReadOnlyFormat extends Format {

        private final BiConsumer<StringBuffer, Object> formatter;

        CustomReadOnlyFormat(BiConsumer<StringBuffer, Object> formatter) {
            this.formatter = formatter;
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            this.formatter.accept(toAppendTo, obj);
            return toAppendTo;
        }

        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException("Parsing is not supported");
        }
    }
}