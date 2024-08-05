package de.keksuccino.fancymenu.customization.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeserializedPlaceholderString {

    @NotNull
    public String placeholderIdentifier;

    @NotNull
    public HashMap<String, String> values = new HashMap();

    @NotNull
    public String placeholderString;

    @Deprecated(forRemoval = true)
    public static DeserializedPlaceholderString build(@NotNull String placeholderIdentifier, @Nullable Map<String, String> values) {
        if (values != null && !(values instanceof HashMap)) {
            throw new RuntimeException("Values list has to be a HashMap!");
        } else {
            return new DeserializedPlaceholderString(placeholderIdentifier, (HashMap<String, String>) values, "");
        }
    }

    @Deprecated(forRemoval = true)
    public DeserializedPlaceholderString() {
        this("", null, "");
    }

    public DeserializedPlaceholderString(@NotNull String placeholderIdentifier, @Nullable HashMap<String, String> values, @NotNull String placeholderString) {
        this.placeholderIdentifier = (String) Objects.requireNonNull(placeholderIdentifier);
        if (values != null) {
            this.values = values;
        }
        this.placeholderString = (String) Objects.requireNonNull(placeholderString);
    }

    @NotNull
    public String toString() {
        if (!this.values.isEmpty()) {
            StringBuilder values = new StringBuilder();
            for (Entry<String, String> m : this.values.entrySet()) {
                if (values.length() > 0) {
                    values.append(",");
                }
                values.append("\"").append((String) m.getKey()).append("\":\"").append((String) m.getValue()).append("\"");
            }
            return "{\"placeholder\":\"" + this.placeholderIdentifier + "\",\"values\":{" + values + "}}";
        } else {
            return "{\"placeholder\":\"" + this.placeholderIdentifier + "\"}";
        }
    }
}