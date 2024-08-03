package me.lucko.spark.lib.adventure.text.serializer.legacy;

import java.util.function.Consumer;
import java.util.regex.Pattern;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.flattener.ComponentFlattener;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.serializer.ComponentSerializer;
import me.lucko.spark.lib.adventure.util.Buildable;
import me.lucko.spark.lib.adventure.util.PlatformAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface LegacyComponentSerializer extends ComponentSerializer<Component, TextComponent, String>, Buildable<LegacyComponentSerializer, LegacyComponentSerializer.Builder> {

    char SECTION_CHAR = '§';

    char AMPERSAND_CHAR = '&';

    char HEX_CHAR = '#';

    @NotNull
    static LegacyComponentSerializer legacySection() {
        return LegacyComponentSerializerImpl.Instances.SECTION;
    }

    @NotNull
    static LegacyComponentSerializer legacyAmpersand() {
        return LegacyComponentSerializerImpl.Instances.AMPERSAND;
    }

    @NotNull
    static LegacyComponentSerializer legacy(final char legacyCharacter) {
        if (legacyCharacter == 167) {
            return legacySection();
        } else {
            return legacyCharacter == '&' ? legacyAmpersand() : builder().character(legacyCharacter).build();
        }
    }

    @Nullable
    static LegacyFormat parseChar(final char character) {
        return LegacyComponentSerializerImpl.legacyFormat(character);
    }

    @NotNull
    static LegacyComponentSerializer.Builder builder() {
        return new LegacyComponentSerializerImpl.BuilderImpl();
    }

    @NotNull
    TextComponent deserialize(@NotNull final String input);

    @NotNull
    String serialize(@NotNull final Component component);

    public interface Builder extends AbstractBuilder<LegacyComponentSerializer>, Buildable.Builder<LegacyComponentSerializer> {

        @NotNull
        LegacyComponentSerializer.Builder character(final char legacyCharacter);

        @NotNull
        LegacyComponentSerializer.Builder hexCharacter(final char legacyHexCharacter);

        @NotNull
        LegacyComponentSerializer.Builder extractUrls();

        @NotNull
        LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern);

        @NotNull
        LegacyComponentSerializer.Builder extractUrls(@Nullable final Style style);

        @NotNull
        LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern, @Nullable final Style style);

        @NotNull
        LegacyComponentSerializer.Builder hexColors();

        @NotNull
        LegacyComponentSerializer.Builder useUnusualXRepeatedCharacterHexFormat();

        @NotNull
        LegacyComponentSerializer.Builder flattener(@NotNull final ComponentFlattener flattener);

        @NotNull
        LegacyComponentSerializer build();
    }

    @PlatformAPI
    @Internal
    public interface Provider {

        @PlatformAPI
        @Internal
        @NotNull
        LegacyComponentSerializer legacyAmpersand();

        @PlatformAPI
        @Internal
        @NotNull
        LegacyComponentSerializer legacySection();

        @PlatformAPI
        @Internal
        @NotNull
        Consumer<LegacyComponentSerializer.Builder> legacy();
    }
}