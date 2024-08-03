package io.github.lightman314.lightmanscurrency.api.config.options;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class ListOption<T> extends ConfigOption<List<T>> {

    protected ListOption(@Nonnull NonNullSupplier<List<T>> defaultValue) {
        super(defaultValue);
    }

    public static <T> ConfigParser<List<T>> makeParser(ConfigParser<T> partialParser) {
        return new ListOption.ListParser<>(partialParser);
    }

    @Nonnull
    @Override
    protected ConfigParser<List<T>> getParser() {
        return makeParser(this.getPartialParser());
    }

    protected abstract ConfigParser<T> getPartialParser();

    public final Pair<Boolean, ConfigParsingException> editList(String value, int index, boolean isEdit) {
        if (index < 0 && isEdit) {
            try {
                T newValue = this.getPartialParser().tryParse(cleanWhitespace(value));
                List<T> currentValue = this.getCurrentValue();
                currentValue.add(newValue);
                this.set(currentValue);
                return Pair.of(true, null);
            } catch (ConfigParsingException var6) {
                return Pair.of(false, var6);
            }
        } else if (index >= 0) {
            List<T> currentValue = this.getCurrentValue();
            if (index >= currentValue.size()) {
                return Pair.of(false, new ConfigParsingException("Invalid index. Maximum is " + (currentValue.size() - 1) + "!"));
            } else if (isEdit) {
                try {
                    T newValue = this.getPartialParser().tryParse(cleanWhitespace(value));
                    currentValue.set(index, newValue);
                    this.set(currentValue);
                    return Pair.of(true, null);
                } catch (ConfigParsingException var7) {
                    return Pair.of(false, var7);
                }
            } else {
                currentValue.remove(index);
                this.set(currentValue);
                return Pair.of(true, null);
            }
        } else {
            return Pair.of(false, new ConfigParsingException("Invalid edit action. Cannot have an index of " + index + " without the isEdit flag!"));
        }
    }

    private static class ListParser<T> implements ConfigParser<List<T>> {

        private final ConfigParser<T> parser;

        private ListParser(@Nonnull ConfigParser<T> parser) {
            this.parser = parser;
        }

        @Nonnull
        public List<T> tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            if (cleanLine.length() == 0) {
                throw new ConfigParsingException("Empty input received!");
            } else {
                char c1 = cleanLine.charAt(0);
                if (c1 != '[') {
                    throw new ConfigParsingException("List does not start with '['!");
                } else {
                    char lastChar = cleanLine.charAt(cleanLine.length() - 1);
                    if (lastChar != ']') {
                        throw new ConfigParsingException("List does not end with ']'!");
                    } else {
                        List<String> sections = new ArrayList();
                        StringBuilder temp = new StringBuilder();
                        boolean inQuotes = false;
                        boolean escaped = false;
                        for (int i = 1; i < cleanLine.length() - 1; i++) {
                            char c = cleanLine.charAt(i);
                            if (escaped) {
                                if (c != '\\' && c != '"') {
                                    temp.append('\\').append(c);
                                } else {
                                    temp.append(c);
                                }
                                escaped = false;
                            } else if (c == '\\') {
                                escaped = true;
                            } else if (c == '"') {
                                inQuotes = !inQuotes;
                            } else if (c == ',') {
                                sections.add(temp.toString());
                                temp = new StringBuilder();
                            } else {
                                temp.append(c);
                            }
                        }
                        if (!temp.isEmpty()) {
                            sections.add(temp.toString());
                        }
                        if (sections.size() == 0) {
                            return new ArrayList();
                        } else {
                            List<T> results = new ArrayList();
                            for (int s = 0; s < sections.size(); s++) {
                                String section = (String) sections.get(s);
                                try {
                                    results.add(this.parser.tryParse(section));
                                } catch (ConfigParsingException var12) {
                                    LightmansCurrency.LogWarning("Failed to parse List Config entry #" + (s + 1), var12);
                                }
                            }
                            return results;
                        }
                    }
                }
            }
        }

        @Nonnull
        public String write(@Nonnull List<T> value) {
            StringBuilder builder = new StringBuilder("[");
            boolean comma = false;
            for (T v : value) {
                if (comma) {
                    builder.append(',');
                }
                builder.append(this.parser.write(v));
                comma = true;
            }
            builder.append(']');
            return builder.toString();
        }
    }
}