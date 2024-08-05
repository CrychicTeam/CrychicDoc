package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {

    private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);

    static final Consumer<GsonComponentSerializer.Builder> BUILDER = (Consumer<GsonComponentSerializer.Builder>) SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> builder -> {
    });

    private final Gson serializer;

    private final UnaryOperator<GsonBuilder> populator;

    private final boolean downsampleColor;

    @Nullable
    private final LegacyHoverEventSerializer legacyHoverSerializer;

    private final boolean emitLegacyHover;

    GsonComponentSerializerImpl(final boolean downsampleColor, @Nullable final LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
        this.downsampleColor = downsampleColor;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
        this.populator = builder -> {
            builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
            return builder;
        };
        this.serializer = ((GsonBuilder) this.populator.apply(new GsonBuilder().disableHtmlEscaping())).create();
    }

    @NotNull
    @Override
    public Gson serializer() {
        return this.serializer;
    }

    @NotNull
    @Override
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @NotNull
    public Component deserialize(@NotNull final String string) {
        Component component = (Component) this.serializer().fromJson(string, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        } else {
            return component;
        }
    }

    @Nullable
    public Component deserializeOr(@Nullable final String input, @Nullable final Component fallback) {
        if (input == null) {
            return fallback;
        } else {
            Component component = (Component) this.serializer().fromJson(input, Component.class);
            return component == null ? fallback : component;
        }
    }

    @NotNull
    public String serialize(@NotNull final Component component) {
        return this.serializer().toJson(component);
    }

    @NotNull
    @Override
    public Component deserializeFromTree(@NotNull final JsonElement input) {
        Component component = (Component) this.serializer().fromJson(input, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        } else {
            return component;
        }
    }

    @NotNull
    @Override
    public JsonElement serializeToTree(@NotNull final Component component) {
        return this.serializer().toJsonTree(component);
    }

    @NotNull
    public GsonComponentSerializer.Builder toBuilder() {
        return new GsonComponentSerializerImpl.BuilderImpl(this);
    }

    static final class BuilderImpl implements GsonComponentSerializer.Builder {

        private boolean downsampleColor = false;

        @Nullable
        private LegacyHoverEventSerializer legacyHoverSerializer;

        private boolean emitLegacyHover = false;

        BuilderImpl() {
            GsonComponentSerializerImpl.BUILDER.accept(this);
        }

        BuilderImpl(final GsonComponentSerializerImpl serializer) {
            this();
            this.downsampleColor = serializer.downsampleColor;
            this.emitLegacyHover = serializer.emitLegacyHover;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
        }

        @NotNull
        @Override
        public GsonComponentSerializer.Builder downsampleColors() {
            this.downsampleColor = true;
            return this;
        }

        @NotNull
        @Override
        public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }

        @NotNull
        @Override
        public GsonComponentSerializer.Builder emitLegacyHoverEvent() {
            this.emitLegacyHover = true;
            return this;
        }

        @NotNull
        @Override
        public GsonComponentSerializer build() {
            if (this.legacyHoverSerializer == null) {
                return this.downsampleColor ? GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE : GsonComponentSerializerImpl.Instances.INSTANCE;
            } else {
                return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
            }
        }
    }

    static final class Instances {

        static final GsonComponentSerializer INSTANCE = (GsonComponentSerializer) GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(false, null, false));

        static final GsonComponentSerializer LEGACY_INSTANCE = (GsonComponentSerializer) GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(true, null, true));
    }
}