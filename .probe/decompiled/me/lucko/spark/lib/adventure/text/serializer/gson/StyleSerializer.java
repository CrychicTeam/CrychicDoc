package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import me.lucko.spark.lib.adventure.util.Codec;
import org.jetbrains.annotations.Nullable;

final class StyleSerializer extends TypeAdapter<Style> {

    private static final TextDecoration[] DECORATIONS = new TextDecoration[] { TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED };

    static final String FONT = "font";

    static final String COLOR = "color";

    static final String INSERTION = "insertion";

    static final String CLICK_EVENT = "clickEvent";

    static final String CLICK_EVENT_ACTION = "action";

    static final String CLICK_EVENT_VALUE = "value";

    static final String HOVER_EVENT = "hoverEvent";

    static final String HOVER_EVENT_ACTION = "action";

    static final String HOVER_EVENT_CONTENTS = "contents";

    @Deprecated
    static final String HOVER_EVENT_VALUE = "value";

    private final LegacyHoverEventSerializer legacyHover;

    private final boolean emitLegacyHover;

    private final Gson gson;

    static TypeAdapter<Style> create(@Nullable final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
        return new StyleSerializer(legacyHover, emitLegacyHover, gson).nullSafe();
    }

    private StyleSerializer(@Nullable final LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
        this.legacyHover = legacyHover;
        this.emitLegacyHover = emitLegacyHover;
        this.gson = gson;
    }

    public Style read(final JsonReader in) throws IOException {
        in.beginObject();
        Style.Builder style = Style.style();
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("font")) {
                style.font((Key) this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
            } else if (fieldName.equals("color")) {
                TextColorWrapper color = (TextColorWrapper) this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
                if (color.color != null) {
                    style.color(color.color);
                } else if (color.decoration != null) {
                    style.decoration(color.decoration, TextDecoration.State.TRUE);
                }
            } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
                style.decoration(TextDecoration.NAMES.value(fieldName), this.readBoolean(in));
            } else if (fieldName.equals("insertion")) {
                style.insertion(in.nextString());
            } else if (!fieldName.equals("clickEvent")) {
                if (fieldName.equals("hoverEvent")) {
                    JsonObject hoverEventObject = (JsonObject) this.gson.fromJson(in, JsonObject.class);
                    if (hoverEventObject != null) {
                        JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
                        if (serializedAction != null) {
                            HoverEvent.Action<Object> action = (HoverEvent.Action<Object>) this.gson.fromJson(serializedAction, SerializerFactory.HOVER_ACTION_TYPE);
                            if (action.readable()) {
                                Object value;
                                if (hoverEventObject.has("contents")) {
                                    JsonElement rawValue = hoverEventObject.get("contents");
                                    Class<?> actionType = action.type();
                                    if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                                        value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                                    } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                                        value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
                                    } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                                        value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
                                    } else {
                                        value = null;
                                    }
                                } else if (hoverEventObject.has("value")) {
                                    Component rawValue = (Component) this.gson.fromJson(hoverEventObject.get("value"), SerializerFactory.COMPONENT_TYPE);
                                    value = this.legacyHoverEventContents(action, rawValue);
                                } else {
                                    value = null;
                                }
                                if (value != null) {
                                    style.hoverEvent(HoverEvent.hoverEvent(action, value));
                                }
                            }
                        }
                    }
                } else {
                    in.skipValue();
                }
            } else {
                in.beginObject();
                ClickEvent.Action action = null;
                String valuex = null;
                while (in.hasNext()) {
                    String clickEventField = in.nextName();
                    if (clickEventField.equals("action")) {
                        action = (ClickEvent.Action) this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE);
                    } else if (clickEventField.equals("value")) {
                        valuex = in.peek() == JsonToken.NULL ? null : in.nextString();
                    } else {
                        in.skipValue();
                    }
                }
                if (action != null && action.readable() && valuex != null) {
                    style.clickEvent(ClickEvent.clickEvent(action, valuex));
                }
                in.endObject();
            }
        }
        in.endObject();
        return style.build();
    }

    private boolean readBoolean(final JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.BOOLEAN) {
            return in.nextBoolean();
        } else if (peek != JsonToken.STRING && peek != JsonToken.NUMBER) {
            throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
        } else {
            return Boolean.parseBoolean(in.nextString());
        }
    }

    private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue) {
        if (action == HoverEvent.Action.SHOW_TEXT) {
            return rawValue;
        } else {
            if (this.legacyHover != null) {
                try {
                    if (action == HoverEvent.Action.SHOW_ENTITY) {
                        return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
                    }
                    if (action == HoverEvent.Action.SHOW_ITEM) {
                        return this.legacyHover.deserializeShowItem(rawValue);
                    }
                } catch (IOException var4) {
                    throw new JsonParseException(var4);
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    private Codec.Decoder<Component, String, JsonParseException> decoder() {
        return string -> (Component) this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
    }

    private Codec.Encoder<Component, String, JsonParseException> encoder() {
        return component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
    }

    public void write(final JsonWriter out, final Style value) throws IOException {
        out.beginObject();
        int i = 0;
        for (int length = DECORATIONS.length; i < length; i++) {
            TextDecoration decoration = DECORATIONS[i];
            TextDecoration.State state = value.decoration(decoration);
            if (state != TextDecoration.State.NOT_SET) {
                String name = TextDecoration.NAMES.key(decoration);
                assert name != null;
                out.name(name);
                out.value(state == TextDecoration.State.TRUE);
            }
        }
        TextColor color = value.color();
        if (color != null) {
            out.name("color");
            this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
        }
        String insertion = value.insertion();
        if (insertion != null) {
            out.name("insertion");
            out.value(insertion);
        }
        ClickEvent clickEvent = value.clickEvent();
        if (clickEvent != null) {
            out.name("clickEvent");
            out.beginObject();
            out.name("action");
            this.gson.toJson(clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, out);
            out.name("value");
            out.value(clickEvent.value());
            out.endObject();
        }
        HoverEvent<?> hoverEvent = value.hoverEvent();
        if (hoverEvent != null) {
            out.name("hoverEvent");
            out.beginObject();
            out.name("action");
            HoverEvent.Action<?> action = hoverEvent.action();
            this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
            out.name("contents");
            if (action == HoverEvent.Action.SHOW_ITEM) {
                this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
            } else if (action == HoverEvent.Action.SHOW_ENTITY) {
                this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
            } else {
                if (action != HoverEvent.Action.SHOW_TEXT) {
                    throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
                }
                this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
            }
            if (this.emitLegacyHover) {
                out.name("value");
                this.serializeLegacyHoverEvent(hoverEvent, out);
            }
            out.endObject();
        }
        Key font = value.font();
        if (font != null) {
            out.name("font");
            this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
        }
        out.endObject();
    }

    private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
            this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
        } else if (this.legacyHover != null) {
            Component serialized = null;
            try {
                if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
                    serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.encoder());
                } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
                    serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
                }
            } catch (IOException var5) {
                throw new JsonSyntaxException(var5);
            }
            if (serialized != null) {
                this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
            } else {
                out.nullValue();
            }
        } else {
            out.nullValue();
        }
    }

    static {
        Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
        for (TextDecoration decoration : DECORATIONS) {
            knownDecorations.remove(decoration);
        }
        if (!knownDecorations.isEmpty()) {
            throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
        }
    }
}