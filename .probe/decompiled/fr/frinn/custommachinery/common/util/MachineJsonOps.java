package fr.frinn.custommachinery.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.RecordBuilder.AbstractStringBuilder;
import java.util.Comparator;
import java.util.Map.Entry;

public class MachineJsonOps extends JsonOps {

    public static final MachineJsonOps INSTANCE = new MachineJsonOps(false);

    private MachineJsonOps(boolean compressed) {
        super(compressed);
    }

    public RecordBuilder<JsonElement> mapBuilder() {
        return new MachineJsonOps.JsonRecordBuilder();
    }

    private class JsonRecordBuilder extends AbstractStringBuilder<JsonElement, JsonObject> {

        protected JsonRecordBuilder() {
            super(MachineJsonOps.this);
        }

        protected JsonObject initBuilder() {
            return new JsonObject();
        }

        protected JsonObject append(String key, JsonElement value, JsonObject builder) {
            builder.add(key, value);
            return builder;
        }

        protected DataResult<JsonElement> build(JsonObject builder, JsonElement prefix) {
            JsonObject output = new JsonObject();
            builder.entrySet().stream().sorted(Comparator.comparingInt(entryx -> this.compareKeys((String) entryx.getKey()))).forEach(entryx -> output.add((String) entryx.getKey(), (JsonElement) entryx.getValue()));
            if (prefix == null || prefix instanceof JsonNull) {
                return DataResult.success(output);
            } else if (!(prefix instanceof JsonObject)) {
                return DataResult.error(() -> "mergeToMap called with not a map: " + prefix, prefix);
            } else {
                JsonObject result = new JsonObject();
                for (Entry<String, JsonElement> entry : prefix.getAsJsonObject().entrySet()) {
                    result.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
                for (Entry<String, JsonElement> entry : output.entrySet()) {
                    result.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
                return DataResult.success(result);
            }
        }

        private int compareKeys(String key) {
            return -(switch(key) {
                case "type" ->
                    10000;
                case "name" ->
                    1000;
                case "appearance" ->
                    900;
                case "components" ->
                    800;
                case "gui" ->
                    700;
                default ->
                    0;
            });
        }
    }
}