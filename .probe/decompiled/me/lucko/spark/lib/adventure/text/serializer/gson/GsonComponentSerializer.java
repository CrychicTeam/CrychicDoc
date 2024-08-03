package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.serializer.ComponentSerializer;
import me.lucko.spark.lib.adventure.util.Buildable;
import me.lucko.spark.lib.adventure.util.PlatformAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface GsonComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder> {

    @NotNull
    static GsonComponentSerializer gson() {
        return GsonComponentSerializerImpl.Instances.INSTANCE;
    }

    @NotNull
    static GsonComponentSerializer colorDownsamplingGson() {
        return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
    }

    static GsonComponentSerializer.Builder builder() {
        return new GsonComponentSerializerImpl.BuilderImpl();
    }

    @NotNull
    Gson serializer();

    @NotNull
    UnaryOperator<GsonBuilder> populator();

    @NotNull
    Component deserializeFromTree(@NotNull final JsonElement input);

    @NotNull
    JsonElement serializeToTree(@NotNull final Component component);

    public interface Builder extends AbstractBuilder<GsonComponentSerializer>, Buildable.Builder<GsonComponentSerializer> {

        @NotNull
        GsonComponentSerializer.Builder downsampleColors();

        @NotNull
        GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer);

        @NotNull
        GsonComponentSerializer.Builder emitLegacyHoverEvent();

        @NotNull
        GsonComponentSerializer build();
    }

    @PlatformAPI
    @Internal
    public interface Provider {

        @PlatformAPI
        @Internal
        @NotNull
        GsonComponentSerializer gson();

        @PlatformAPI
        @Internal
        @NotNull
        GsonComponentSerializer gsonLegacy();

        @PlatformAPI
        @Internal
        @NotNull
        Consumer<GsonComponentSerializer.Builder> builder();
    }
}