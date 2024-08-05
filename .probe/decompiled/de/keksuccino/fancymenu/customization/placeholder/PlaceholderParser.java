package de.keksuccino.fancymenu.customization.placeholder;

import com.google.common.collect.Lists;
import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import de.keksuccino.fancymenu.util.rendering.text.TextFormattingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.resources.language.I18n;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final HashMap<String, Long> LOG_COOLDOWN = new HashMap();

    private static final long LOG_COOLDOWN_MS = 10000L;

    private static final HashSet<String> TOO_LONG_TO_PARSE = new HashSet();

    private static final HashMap<String, Boolean> CONTAINS_PLACEHOLDERS = new HashMap();

    private static final HashMap<String, Pair<String, Long>> PLACEHOLDER_CACHE = new HashMap();

    private static final Long PLACEHOLDER_CACHE_DURATION_MS = 30L;

    private static final int MAX_TEXT_LENGTH = 17000;

    private static final String PLACEHOLDER_PREFIX = "{\"placeholder\":\"";

    private static final String EMPTY_STRING = "";

    private static final char OPEN_CURLY_BRACKETS_CHAR = '{';

    private static final char CLOSE_CURLY_BRACKETS_CHAR = '}';

    private static final String FORMATTING_PREFIX_AND = "&";

    private static final String FORMATTING_PREFIX_PARAGRAPH = "§";

    private static final String SHORT_VARIABLE_PLACEHOLDER_PREFIX = "$$";

    private static final char DOLLAR_CHAR = '$';

    private static final String APOSTROPHE = "\"";

    private static final char APOSTROPHE_CHAR = '"';

    private static final char NEWLINE_CHAR = '\n';

    private static final char BACKSLASH_CHAR = '\\';

    private static final String BACKSLASH = "\\";

    private static final String COMMA = ",";

    private static final String COMMA_WRAPPED_IN_APOSTROPHES = "\",\"";

    private static final String COLON_WRAPPED_IN_APOSTROPHES = "\":\"";

    private static final String TOO_LONG_TO_PARSE_LOCALIZATION = "fancymenu.placeholders.error.text_too_long";

    public static boolean containsPlaceholders(@Nullable String in, boolean checkForVariableReferences, boolean checkForFormattingCodes) {
        if (in == null) {
            return false;
        } else if (in.length() <= 2) {
            return false;
        } else if (StringUtils.contains(in, "{\"placeholder\":\"")) {
            return true;
        } else {
            return checkForFormattingCodes && in.hashCode() != TextFormattingUtils.replaceFormattingCodes(in, "&", "§").hashCode() ? true : checkForVariableReferences && in.hashCode() != replaceVariableReferences(in).hashCode();
        }
    }

    public static boolean containsPlaceholders(@Nullable String in, boolean checkForVariableReferences) {
        return containsPlaceholders(in, checkForVariableReferences, true);
    }

    @NotNull
    public static String replacePlaceholders(@Nullable String in) {
        return replacePlaceholders(in, true);
    }

    @NotNull
    public static String replacePlaceholders(@Nullable String in, boolean replaceFormattingCodes) {
        return replacePlaceholders(in, null, replaceFormattingCodes);
    }

    @NotNull
    protected static String replacePlaceholders(@Nullable String in, @Nullable HashMap<String, String> parsed, boolean replaceFormattingCodes) {
        if (in == null) {
            return "";
        } else if (in.length() <= 2) {
            return in;
        } else {
            Boolean containsPlaceholders = (Boolean) CONTAINS_PLACEHOLDERS.get(in);
            if (containsPlaceholders == null) {
                containsPlaceholders = containsPlaceholders(in, true, replaceFormattingCodes);
                CONTAINS_PLACEHOLDERS.put(in, containsPlaceholders);
            }
            if (!containsPlaceholders) {
                return in;
            } else if (TOO_LONG_TO_PARSE.contains(in)) {
                return I18n.get("fancymenu.placeholders.error.text_too_long");
            } else if (in.length() >= 17000) {
                TOO_LONG_TO_PARSE.add(in);
                return I18n.get("fancymenu.placeholders.error.text_too_long");
            } else {
                Pair<String, Long> cached = (Pair<String, Long>) PLACEHOLDER_CACHE.get(in);
                if (cached != null && (Long) cached.getValue() + PLACEHOLDER_CACHE_DURATION_MS > System.currentTimeMillis()) {
                    return (String) cached.getKey();
                } else {
                    String original = in;
                    if (parsed == null) {
                        parsed = new HashMap();
                    }
                    int hash = in.hashCode();
                    while (true) {
                        for (PlaceholderParser.ParsedPlaceholder p : Lists.reverse(findPlaceholders(in, parsed, replaceFormattingCodes))) {
                            String replacement = (String) parsed.get(p.placeholderString);
                            if (replacement == null) {
                                replacement = p.getReplacement();
                                parsed.put(p.placeholderString, replacement);
                            }
                            in = StringUtils.replace(in, p.placeholderString, replacement);
                        }
                        int hashNew = in.hashCode();
                        if (hashNew == hash) {
                            if (replaceFormattingCodes) {
                                in = TextFormattingUtils.replaceFormattingCodes(in, "&", "§");
                            }
                            in = replaceVariableReferences(in);
                            PLACEHOLDER_CACHE.put(original, Pair.of(in, System.currentTimeMillis()));
                            return in;
                        }
                        hash = hashNew;
                    }
                }
            }
        }
    }

    @NotNull
    public static List<PlaceholderParser.ParsedPlaceholder> findPlaceholders(@Nullable String in, @NotNull HashMap<String, String> parsed, boolean replaceFormattingCodes) {
        List<PlaceholderParser.ParsedPlaceholder> placeholders = new ArrayList();
        if (in == null) {
            return placeholders;
        } else {
            int index = -1;
            for (char c : in.toCharArray()) {
                index++;
                if (c == '{') {
                    String sub = StringUtils.substring(in, index);
                    if (StringUtils.startsWith(sub, "{\"placeholder\":\"")) {
                        int endIndex = findPlaceholderEndIndex(sub, index);
                        if (endIndex != -1) {
                            placeholders.add(new PlaceholderParser.ParsedPlaceholder(StringUtils.substring(in, index, endIndex), index, ++endIndex, parsed, replaceFormattingCodes));
                        }
                    }
                }
            }
            return placeholders;
        }
    }

    private static int findPlaceholderEndIndex(@NotNull String placeholderStartSubString, int startIndex) {
        int currentIndex = startIndex;
        int depth = 0;
        boolean backslash = false;
        for (char c : placeholderStartSubString.toCharArray()) {
            if (currentIndex != startIndex) {
                if (c == '\n') {
                    return -1;
                }
                if (c == '{' && !backslash) {
                    depth++;
                } else if (c == '}' && !backslash) {
                    if (depth <= 0) {
                        return currentIndex;
                    }
                    depth--;
                }
                backslash = c == '\\';
            }
            currentIndex++;
        }
        return -1;
    }

    @NotNull
    public static String replaceVariableReferences(@NotNull String in) {
        String replaced = in;
        int index = 0;
        for (char c : in.toCharArray()) {
            if (c == '$') {
                String sub = StringUtils.substring(in, index);
                if (StringUtils.startsWith(sub, "$$")) {
                    for (Variable variable : VariableHandler.getVariables()) {
                        if (StringUtils.startsWith(sub, "$$" + variable.getName())) {
                            replaced = StringUtils.replace(replaced, "$$" + variable.getName(), variable.getValue());
                            break;
                        }
                    }
                }
            }
            index++;
        }
        return replaced;
    }

    private static void logError(@NotNull String error, @Nullable Exception ex) {
        long now = System.currentTimeMillis();
        Long last = (Long) LOG_COOLDOWN.get(error);
        if (last != null && last + 10000L < now) {
            last = null;
            LOG_COOLDOWN.remove(error);
        }
        if (last == null) {
            if (ex != null) {
                LOGGER.error(error, ex);
            } else {
                LOGGER.error(error);
            }
            LOG_COOLDOWN.put(error, now);
        }
    }

    public static class ParsedPlaceholder {

        public final String placeholderString;

        public final int startIndex;

        public final int endIndex;

        private final HashMap<String, String> parsed;

        private final boolean replaceFormattingCodes;

        private Integer hashcode;

        private String identifier;

        private boolean identifierFailed = false;

        private Placeholder placeholder;

        private boolean placeholderFailed = false;

        protected ParsedPlaceholder(@NotNull String placeholderString, int startIndex, int endIndex, @NotNull HashMap<String, String> parsed, boolean replaceFormattingCodes) {
            this.placeholderString = placeholderString;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.parsed = parsed;
            this.replaceFormattingCodes = replaceFormattingCodes;
        }

        @Nullable
        public String getIdentifier() {
            if (this.identifierFailed) {
                return null;
            } else if (this.identifier != null) {
                return this.identifier;
            } else {
                try {
                    this.identifier = StringUtils.split(StringUtils.substring(this.placeholderString, "{\"placeholder\":\"".length()), "\"", 2)[0];
                    return this.identifier;
                } catch (Exception var2) {
                    PlaceholderParser.logError("[FANCYMENU] Failed to parse identifier of placeholder: " + this.placeholderString, var2);
                    this.identifierFailed = true;
                    return null;
                }
            }
        }

        @NotNull
        public String getReplacement() {
            String identifier = this.getIdentifier();
            if (identifier == null) {
                return this.placeholderString;
            } else {
                Placeholder p = this.getPlaceholder();
                if (p == null) {
                    return this.placeholderString;
                } else {
                    HashMap<String, String> values = this.getValues();
                    if (!this.hasValues() || values != null && !values.isEmpty()) {
                        DeserializedPlaceholderString deserialized = new DeserializedPlaceholderString(identifier, null, this.placeholderString);
                        if (values != null) {
                            for (Entry<String, String> value : values.entrySet()) {
                                deserialized.values.put((String) value.getKey(), PlaceholderParser.replacePlaceholders((String) value.getValue(), this.parsed, this.replaceFormattingCodes));
                            }
                        }
                        return p.getReplacementFor(deserialized);
                    } else {
                        return this.placeholderString;
                    }
                }
            }
        }

        @Nullable
        public HashMap<String, String> getValues() {
            HashMap<String, String> values = new HashMap();
            try {
                Placeholder placeholder = this.getPlaceholder();
                if (placeholder != null && this.hasValues()) {
                    String valueString = "," + StringUtils.split(this.placeholderString, ",", 2)[1];
                    int currentIndex = 0;
                    int inValueDepth = 0;
                    String currentValueName = null;
                    int currentValueStartIndex = 0;
                    for (char c : valueString.toCharArray()) {
                        if (currentIndex >= currentValueStartIndex) {
                            if (c == '"') {
                                if (currentValueName != null) {
                                    if (inValueDepth == 0 && !StringUtils.startsWith(StringUtils.substring(valueString, currentIndex - 1), "\\") && isEndOfValueContent(placeholder, valueString, currentIndex)) {
                                        String valueContent = StringUtils.substring(valueString, currentValueStartIndex, currentIndex);
                                        values.put(currentValueName, valueContent);
                                        currentValueName = null;
                                        currentValueStartIndex = 0;
                                    }
                                } else {
                                    currentValueName = getValueNameIfStartingWithValue(placeholder, StringUtils.substring(valueString, currentIndex));
                                    if (currentValueName != null) {
                                        currentValueStartIndex = currentIndex + currentValueName.length() + 4;
                                        inValueDepth = 0;
                                    }
                                }
                            }
                            if (c == '{' && currentValueName != null && !StringUtils.startsWith(StringUtils.substring(valueString, currentIndex - 1), "\\")) {
                                inValueDepth++;
                            }
                            if (c == '}' && currentValueName != null && !StringUtils.startsWith(StringUtils.substring(valueString, currentIndex - 1), "\\") && inValueDepth > 0) {
                                inValueDepth--;
                            }
                        }
                        currentIndex++;
                    }
                    return values;
                } else {
                    return null;
                }
            } catch (Exception var13) {
                PlaceholderParser.logError("[FANCYMENU] Failed to parse values of placeholder: " + this.placeholderString, var13);
                return null;
            }
        }

        private static boolean isEndOfValueContent(@NotNull Placeholder placeholder, @NotNull String valueString, int currentIndex) {
            if (valueString.length() == currentIndex + 3) {
                return true;
            } else if (StringUtils.startsWith(StringUtils.substring(valueString, currentIndex), "\",\"")) {
                String nextValue = getValueNameIfStartingWithValue(placeholder, StringUtils.substring(valueString, currentIndex + 2));
                return nextValue != null;
            } else {
                return false;
            }
        }

        @Nullable
        private static String getValueNameIfStartingWithValue(@NotNull Placeholder placeholder, @NotNull String s) {
            if (placeholder.getValueNames() != null && !placeholder.getValueNames().isEmpty()) {
                for (String name : placeholder.getValueNames()) {
                    if (StringUtils.startsWith(s, "\"" + name + "\":\"")) {
                        return name;
                    }
                }
                return null;
            } else {
                return null;
            }
        }

        public boolean hasValues() {
            Placeholder p = this.getPlaceholder();
            return p == null ? false : p.getValueNames() != null && !p.getValueNames().isEmpty();
        }

        @Nullable
        public Placeholder getPlaceholder() {
            if (this.placeholderFailed) {
                return null;
            } else {
                if (this.placeholder == null) {
                    this.placeholder = PlaceholderRegistry.getPlaceholder(this.getIdentifier());
                }
                this.placeholderFailed = this.placeholder == null;
                return this.placeholder;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else {
                return !(obj instanceof PlaceholderParser.ParsedPlaceholder p) ? false : this.placeholderString.equals(p.placeholderString) && this.startIndex == p.startIndex && this.endIndex == p.endIndex;
            }
        }

        public int hashCode() {
            if (this.hashcode == null) {
                this.hashcode = Objects.hash(new Object[] { this.placeholderString, this.startIndex, this.endIndex });
            }
            return this.hashcode;
        }
    }
}