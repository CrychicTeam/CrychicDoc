package me.lucko.spark.lib.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.ComponentLike;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.TextReplacementConfig;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.flattener.ComponentFlattener;
import me.lucko.spark.lib.adventure.text.flattener.FlattenerListener;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import me.lucko.spark.lib.adventure.text.format.TextFormat;
import me.lucko.spark.lib.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class LegacyComponentSerializerImpl implements LegacyComponentSerializer {

    static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");

    static final Pattern URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:");

    private static final TextDecoration[] DECORATIONS = TextDecoration.values();

    private static final char LEGACY_BUNGEE_HEX_CHAR = 'x';

    private static final List<TextFormat> FORMATS;

    private static final String LEGACY_CHARS;

    private static final Optional<LegacyComponentSerializer.Provider> SERVICE;

    static final Consumer<LegacyComponentSerializer.Builder> BUILDER;

    private final char character;

    private final char hexCharacter;

    @Nullable
    private final TextReplacementConfig urlReplacementConfig;

    private final boolean hexColours;

    private final boolean useTerriblyStupidHexFormat;

    private final ComponentFlattener flattener;

    LegacyComponentSerializerImpl(final char character, final char hexCharacter, @Nullable final TextReplacementConfig urlReplacementConfig, final boolean hexColours, final boolean useTerriblyStupidHexFormat, final ComponentFlattener flattener) {
        this.character = character;
        this.hexCharacter = hexCharacter;
        this.urlReplacementConfig = urlReplacementConfig;
        this.hexColours = hexColours;
        this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
        this.flattener = flattener;
    }

    @Nullable
    private LegacyComponentSerializerImpl.FormatCodeType determineFormatType(final char legacy, final String input, final int pos) {
        if (pos >= 14) {
            int expectedCharacterPosition = pos - 14;
            int expectedIndicatorPosition = pos - 13;
            if (input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == 'x') {
                return LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
            }
        }
        if (legacy == this.hexCharacter && input.length() - pos >= 6) {
            return LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX;
        } else {
            return LEGACY_CHARS.indexOf(legacy) != -1 ? LegacyComponentSerializerImpl.FormatCodeType.MOJANG_LEGACY : null;
        }
    }

    @Nullable
    static LegacyFormat legacyFormat(final char character) {
        int index = LEGACY_CHARS.indexOf(character);
        if (index != -1) {
            TextFormat format = (TextFormat) FORMATS.get(index);
            if (format instanceof NamedTextColor) {
                return new LegacyFormat((NamedTextColor) format);
            }
            if (format instanceof TextDecoration) {
                return new LegacyFormat((TextDecoration) format);
            }
            if (format instanceof LegacyComponentSerializerImpl.Reset) {
                return LegacyFormat.RESET;
            }
        }
        return null;
    }

    @Nullable
    private LegacyComponentSerializerImpl.DecodedFormat decodeTextFormat(final char legacy, final String input, final int pos) {
        LegacyComponentSerializerImpl.FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
        if (foundFormat == null) {
            return null;
        } else {
            if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX) {
                TextColor parsed = tryParseHexColor(input.substring(pos, pos + 6));
                if (parsed != null) {
                    return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, parsed);
                }
            } else {
                if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.MOJANG_LEGACY) {
                    return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, (TextFormat) FORMATS.get(LEGACY_CHARS.indexOf(legacy)));
                }
                if (foundFormat == LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
                    StringBuilder foundHex = new StringBuilder(6);
                    for (int i = pos - 1; i >= pos - 11; i -= 2) {
                        foundHex.append(input.charAt(i));
                    }
                    TextColor parsed = tryParseHexColor(foundHex.reverse().toString());
                    if (parsed != null) {
                        return new LegacyComponentSerializerImpl.DecodedFormat(foundFormat, parsed);
                    }
                }
            }
            return null;
        }
    }

    @Nullable
    private static TextColor tryParseHexColor(final String hexDigits) {
        try {
            int color = Integer.parseInt(hexDigits, 16);
            return TextColor.color(color);
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    private static boolean isHexTextColor(final TextFormat format) {
        return format instanceof TextColor && !(format instanceof NamedTextColor);
    }

    private String toLegacyCode(TextFormat format) {
        if (isHexTextColor(format)) {
            TextColor color = (TextColor) format;
            if (this.hexColours) {
                String hex = String.format("%06x", color.value());
                if (!this.useTerriblyStupidHexFormat) {
                    return this.hexCharacter + hex;
                }
                StringBuilder legacy = new StringBuilder(String.valueOf('x'));
                int i = 0;
                for (int length = hex.length(); i < length; i++) {
                    legacy.append(this.character).append(hex.charAt(i));
                }
                return legacy.toString();
            }
            format = NamedTextColor.nearestTo(color);
        }
        int index = FORMATS.indexOf(format);
        return Character.toString(LEGACY_CHARS.charAt(index));
    }

    private TextComponent extractUrl(final TextComponent component) {
        if (this.urlReplacementConfig == null) {
            return component;
        } else {
            Component newComponent = component.replaceText(this.urlReplacementConfig);
            return newComponent instanceof TextComponent ? (TextComponent) newComponent : Component.text().append(newComponent).build();
        }
    }

    @NotNull
    @Override
    public TextComponent deserialize(@NotNull final String input) {
        int next = input.lastIndexOf(this.character, input.length() - 2);
        if (next == -1) {
            return this.extractUrl(Component.text(input));
        } else {
            List<TextComponent> parts = new ArrayList();
            TextComponent.Builder current = null;
            boolean reset = false;
            int pos = input.length();
            do {
                LegacyComponentSerializerImpl.DecodedFormat decoded = this.decodeTextFormat(input.charAt(next + 1), input, next + 2);
                if (decoded != null) {
                    int from = next + (decoded.encodedFormat == LegacyComponentSerializerImpl.FormatCodeType.KYORI_HEX ? 8 : 2);
                    if (from != pos) {
                        if (current != null) {
                            if (reset) {
                                parts.add(current.build());
                                reset = false;
                                current = Component.text();
                            } else {
                                current = Component.text().append(current.build());
                            }
                        } else {
                            current = Component.text();
                        }
                        current.content(input.substring(from, pos));
                    } else if (current == null) {
                        current = Component.text();
                    }
                    if (!reset) {
                        reset = applyFormat(current, decoded.format);
                    }
                    if (decoded.encodedFormat == LegacyComponentSerializerImpl.FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
                        next -= 12;
                    }
                    pos = next;
                }
                next = input.lastIndexOf(this.character, next - 1);
            } while (next != -1);
            if (current != null) {
                parts.add(current.build());
            }
            String remaining = pos > 0 ? input.substring(0, pos) : "";
            if (parts.size() == 1 && remaining.isEmpty()) {
                return this.extractUrl((TextComponent) parts.get(0));
            } else {
                Collections.reverse(parts);
                return this.extractUrl(Component.text().content(remaining).append(parts).build());
            }
        }
    }

    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        LegacyComponentSerializerImpl.Cereal state = new LegacyComponentSerializerImpl.Cereal();
        this.flattener.flatten(component, state);
        return state.toString();
    }

    private static boolean applyFormat(@NotNull final TextComponent.Builder builder, @NotNull final TextFormat format) {
        if (format instanceof TextColor) {
            builder.colorIfAbsent((TextColor) format);
            return true;
        } else if (format instanceof TextDecoration) {
            builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
            return false;
        } else if (format instanceof LegacyComponentSerializerImpl.Reset) {
            return true;
        } else {
            throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
        }
    }

    @NotNull
    public LegacyComponentSerializer.Builder toBuilder() {
        return new LegacyComponentSerializerImpl.BuilderImpl(this);
    }

    static {
        Map<TextFormat, String> formats = new LinkedHashMap(22);
        formats.put(NamedTextColor.BLACK, "0");
        formats.put(NamedTextColor.DARK_BLUE, "1");
        formats.put(NamedTextColor.DARK_GREEN, "2");
        formats.put(NamedTextColor.DARK_AQUA, "3");
        formats.put(NamedTextColor.DARK_RED, "4");
        formats.put(NamedTextColor.DARK_PURPLE, "5");
        formats.put(NamedTextColor.GOLD, "6");
        formats.put(NamedTextColor.GRAY, "7");
        formats.put(NamedTextColor.DARK_GRAY, "8");
        formats.put(NamedTextColor.BLUE, "9");
        formats.put(NamedTextColor.GREEN, "a");
        formats.put(NamedTextColor.AQUA, "b");
        formats.put(NamedTextColor.RED, "c");
        formats.put(NamedTextColor.LIGHT_PURPLE, "d");
        formats.put(NamedTextColor.YELLOW, "e");
        formats.put(NamedTextColor.WHITE, "f");
        formats.put(TextDecoration.OBFUSCATED, "k");
        formats.put(TextDecoration.BOLD, "l");
        formats.put(TextDecoration.STRIKETHROUGH, "m");
        formats.put(TextDecoration.UNDERLINED, "n");
        formats.put(TextDecoration.ITALIC, "o");
        formats.put(LegacyComponentSerializerImpl.Reset.INSTANCE, "r");
        FORMATS = Collections.unmodifiableList(new ArrayList(formats.keySet()));
        LEGACY_CHARS = String.join("", formats.values());
        if (FORMATS.size() != LEGACY_CHARS.length()) {
            throw new IllegalStateException("FORMATS length differs from LEGACY_CHARS length");
        } else {
            SERVICE = Services.service(LegacyComponentSerializer.Provider.class);
            BUILDER = (Consumer<LegacyComponentSerializer.Builder>) SERVICE.map(LegacyComponentSerializer.Provider::legacy).orElseGet(() -> builder -> {
            });
        }
    }

    static final class BuilderImpl implements LegacyComponentSerializer.Builder {

        private char character = 167;

        private char hexCharacter = '#';

        private TextReplacementConfig urlReplacementConfig = null;

        private boolean hexColours = false;

        private boolean useTerriblyStupidHexFormat = false;

        private ComponentFlattener flattener = ComponentFlattener.basic();

        BuilderImpl() {
            LegacyComponentSerializerImpl.BUILDER.accept(this);
        }

        BuilderImpl(@NotNull final LegacyComponentSerializerImpl serializer) {
            this();
            this.character = serializer.character;
            this.hexCharacter = serializer.hexCharacter;
            this.urlReplacementConfig = serializer.urlReplacementConfig;
            this.hexColours = serializer.hexColours;
            this.useTerriblyStupidHexFormat = serializer.useTerriblyStupidHexFormat;
            this.flattener = serializer.flattener;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder character(final char legacyCharacter) {
            this.character = legacyCharacter;
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder hexCharacter(final char legacyHexCharacter) {
            this.hexCharacter = legacyHexCharacter;
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder extractUrls() {
            return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, null);
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern) {
            return this.extractUrls(pattern, null);
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder extractUrls(@Nullable final Style style) {
            return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, style);
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern, @Nullable final Style style) {
            Objects.requireNonNull(pattern, "pattern");
            this.urlReplacementConfig = TextReplacementConfig.builder().match(pattern).replacement((Function<TextComponent.Builder, ComponentLike>) (url -> {
                String clickUrl = url.content();
                if (!LegacyComponentSerializerImpl.URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
                    clickUrl = "http://" + clickUrl;
                }
                return (style == null ? url : url.style(style)).clickEvent(ClickEvent.openUrl(clickUrl));
            })).build();
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder hexColors() {
            this.hexColours = true;
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder useUnusualXRepeatedCharacterHexFormat() {
            this.useTerriblyStupidHexFormat = true;
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer.Builder flattener(@NotNull final ComponentFlattener flattener) {
            this.flattener = (ComponentFlattener) Objects.requireNonNull(flattener, "flattener");
            return this;
        }

        @NotNull
        @Override
        public LegacyComponentSerializer build() {
            return new LegacyComponentSerializerImpl(this.character, this.hexCharacter, this.urlReplacementConfig, this.hexColours, this.useTerriblyStupidHexFormat, this.flattener);
        }
    }

    private final class Cereal implements FlattenerListener {

        private final StringBuilder sb = new StringBuilder();

        private final LegacyComponentSerializerImpl.Cereal.StyleState style = new LegacyComponentSerializerImpl.Cereal.StyleState();

        @Nullable
        private TextFormat lastWritten;

        private LegacyComponentSerializerImpl.Cereal.StyleState[] styles = new LegacyComponentSerializerImpl.Cereal.StyleState[8];

        private int head = -1;

        private Cereal() {
        }

        @Override
        public void pushStyle(@NotNull final Style pushed) {
            int idx = ++this.head;
            if (idx >= this.styles.length) {
                this.styles = (LegacyComponentSerializerImpl.Cereal.StyleState[]) Arrays.copyOf(this.styles, this.styles.length * 2);
            }
            LegacyComponentSerializerImpl.Cereal.StyleState state = this.styles[idx];
            if (state == null) {
                this.styles[idx] = state = new LegacyComponentSerializerImpl.Cereal.StyleState();
            }
            if (idx > 0) {
                state.set(this.styles[idx - 1]);
            } else {
                state.clear();
            }
            state.apply(pushed);
        }

        @Override
        public void component(@NotNull final String text) {
            if (!text.isEmpty()) {
                if (this.head < 0) {
                    throw new IllegalStateException("No style has been pushed!");
                }
                this.styles[this.head].applyFormat();
                this.sb.append(text);
            }
        }

        @Override
        public void popStyle(@NotNull final Style style) {
            if (this.head-- < 0) {
                throw new IllegalStateException("Tried to pop beyond what was pushed!");
            }
        }

        void append(@NotNull final TextFormat format) {
            if (this.lastWritten != format) {
                this.sb.append(LegacyComponentSerializerImpl.this.character).append(LegacyComponentSerializerImpl.this.toLegacyCode(format));
            }
            this.lastWritten = format;
        }

        public String toString() {
            return this.sb.toString();
        }

        private final class StyleState {

            @Nullable
            private TextColor color;

            private final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);

            private boolean needsReset;

            StyleState() {
            }

            void set(@NotNull final LegacyComponentSerializerImpl.Cereal.StyleState that) {
                this.color = that.color;
                this.decorations.clear();
                this.decorations.addAll(that.decorations);
            }

            public void clear() {
                this.color = null;
                this.decorations.clear();
            }

            void apply(@NotNull final Style component) {
                TextColor color = component.color();
                if (color != null) {
                    this.color = color;
                }
                int i = 0;
                for (int length = LegacyComponentSerializerImpl.DECORATIONS.length; i < length; i++) {
                    TextDecoration decoration = LegacyComponentSerializerImpl.DECORATIONS[i];
                    switch(component.decoration(decoration)) {
                        case TRUE:
                            this.decorations.add(decoration);
                            break;
                        case FALSE:
                            if (this.decorations.remove(decoration)) {
                                this.needsReset = true;
                            }
                    }
                }
            }

            void applyFormat() {
                boolean colorChanged = this.color != Cereal.this.style.color;
                if (this.needsReset) {
                    if (!colorChanged) {
                        Cereal.this.append(LegacyComponentSerializerImpl.Reset.INSTANCE);
                    }
                    this.needsReset = false;
                }
                if (!colorChanged && Cereal.this.lastWritten != LegacyComponentSerializerImpl.Reset.INSTANCE) {
                    if (!this.decorations.containsAll(Cereal.this.style.decorations)) {
                        this.applyFullFormat();
                    } else {
                        for (TextDecoration decoration : this.decorations) {
                            if (Cereal.this.style.decorations.add(decoration)) {
                                Cereal.this.append(decoration);
                            }
                        }
                    }
                } else {
                    this.applyFullFormat();
                }
            }

            private void applyFullFormat() {
                if (this.color != null) {
                    Cereal.this.append(this.color);
                } else {
                    Cereal.this.append(LegacyComponentSerializerImpl.Reset.INSTANCE);
                }
                Cereal.this.style.color = this.color;
                for (TextDecoration decoration : this.decorations) {
                    Cereal.this.append(decoration);
                }
                Cereal.this.style.decorations.clear();
                Cereal.this.style.decorations.addAll(this.decorations);
            }
        }
    }

    static final class DecodedFormat {

        final LegacyComponentSerializerImpl.FormatCodeType encodedFormat;

        final TextFormat format;

        private DecodedFormat(final LegacyComponentSerializerImpl.FormatCodeType encodedFormat, final TextFormat format) {
            if (format == null) {
                throw new IllegalStateException("No format found");
            } else {
                this.encodedFormat = encodedFormat;
                this.format = format;
            }
        }
    }

    static enum FormatCodeType {

        MOJANG_LEGACY, KYORI_HEX, BUNGEECORD_UNUSUAL_HEX
    }

    static final class Instances {

        static final LegacyComponentSerializer SECTION = (LegacyComponentSerializer) LegacyComponentSerializerImpl.SERVICE.map(LegacyComponentSerializer.Provider::legacySection).orElseGet(() -> new LegacyComponentSerializerImpl('§', '#', null, false, false, ComponentFlattener.basic()));

        static final LegacyComponentSerializer AMPERSAND = (LegacyComponentSerializer) LegacyComponentSerializerImpl.SERVICE.map(LegacyComponentSerializer.Provider::legacyAmpersand).orElseGet(() -> new LegacyComponentSerializerImpl('&', '#', null, false, false, ComponentFlattener.basic()));
    }

    private static enum Reset implements TextFormat {

        INSTANCE
    }
}