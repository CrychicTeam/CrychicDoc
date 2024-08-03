package me.lucko.spark.lib.adventure.text.serializer.gson;

import java.io.IOException;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

public interface LegacyHoverEventSerializer {

    @NotNull
    HoverEvent.ShowItem deserializeShowItem(@NotNull final Component input) throws IOException;

    @NotNull
    HoverEvent.ShowEntity deserializeShowEntity(@NotNull final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentDecoder) throws IOException;

    @NotNull
    Component serializeShowItem(@NotNull final HoverEvent.ShowItem input) throws IOException;

    @NotNull
    Component serializeShowEntity(@NotNull final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException;
}