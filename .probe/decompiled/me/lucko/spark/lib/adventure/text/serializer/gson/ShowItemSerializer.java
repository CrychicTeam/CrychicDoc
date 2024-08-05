package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.nbt.api.BinaryTagHolder;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem> {

    static final String ID = "id";

    static final String COUNT = "count";

    static final String TAG = "tag";

    private final Gson gson;

    static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson) {
        return new ShowItemSerializer(gson).nullSafe();
    }

    private ShowItemSerializer(final Gson gson) {
        this.gson = gson;
    }

    public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
        in.beginObject();
        Key key = null;
        int count = 1;
        BinaryTagHolder nbt = null;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("id")) {
                key = (Key) this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
            } else if (fieldName.equals("count")) {
                count = in.nextInt();
            } else if (fieldName.equals("tag")) {
                JsonToken token = in.peek();
                if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
                    nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
                } else if (token == JsonToken.BOOLEAN) {
                    nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                } else {
                    if (token != JsonToken.NULL) {
                        throw new JsonParseException("Expected tag to be a string");
                    }
                    in.nextNull();
                }
            } else {
                in.skipValue();
            }
        }
        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        } else {
            in.endObject();
            return HoverEvent.ShowItem.of(key, count, nbt);
        }
    }

    public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        out.beginObject();
        out.name("id");
        this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, out);
        int count = value.count();
        if (count != 1) {
            out.name("count");
            out.value((long) count);
        }
        BinaryTagHolder nbt = value.nbt();
        if (nbt != null) {
            out.name("tag");
            out.value(nbt.string());
        }
        out.endObject();
    }
}