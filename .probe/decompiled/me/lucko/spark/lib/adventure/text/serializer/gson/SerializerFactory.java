package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.BlockNBTComponent;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

final class SerializerFactory implements TypeAdapterFactory {

    static final Class<Key> KEY_TYPE = Key.class;

    static final Class<Component> COMPONENT_TYPE = Component.class;

    static final Class<Style> STYLE_TYPE = Style.class;

    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;

    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;

    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;

    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;

    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;

    static final Class<TextColor> COLOR_TYPE = TextColor.class;

    static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;

    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;

    private final boolean downsampleColors;

    private final LegacyHoverEventSerializer legacyHoverSerializer;

    private final boolean emitLegacyHover;

    SerializerFactory(final boolean downsampleColors, @Nullable final LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
        this.downsampleColors = downsampleColors;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
    }

    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ComponentSerializerImpl.create(gson);
        } else if (KEY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) KeySerializer.INSTANCE;
        } else if (STYLE_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) StyleSerializer.create(this.legacyHoverSerializer, this.emitLegacyHover, gson);
        } else if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ClickEventActionSerializer.INSTANCE;
        } else if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return HoverEventActionSerializer.INSTANCE;
        } else if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowItemSerializer.create(gson);
        } else if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) ShowEntitySerializer.create(gson);
        } else if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return TextColorWrapper.Serializer.INSTANCE;
        } else if (COLOR_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) (this.downsampleColors ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE);
        } else if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) TextDecorationSerializer.INSTANCE;
        } else {
            return (TypeAdapter<T>) (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType) ? BlockNBTComponentPosSerializer.INSTANCE : null);
        }
    }
}