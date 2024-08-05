package dev.latvian.mods.kubejs.typings.desc;

import java.util.List;
import java.util.regex.Pattern;

public record ObjectDescJS(List<ObjectDescJS.Entry> types) implements TypeDescJS {

    public ObjectDescJS add(String key, TypeDescJS value) {
        this.types.add(new ObjectDescJS.Entry(key, value, false));
        return this;
    }

    public ObjectDescJS add(String key, TypeDescJS value, boolean optional) {
        this.types.add(new ObjectDescJS.Entry(key, value, optional));
        return this;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append('{');
        for (int i = 0; i < this.types.size(); i++) {
            if (i > 0) {
                builder.append(',');
                builder.append(' ');
            }
            if (((ObjectDescJS.Entry) this.types.get(i)).wrap) {
                builder.append('"');
            }
            builder.append(((ObjectDescJS.Entry) this.types.get(i)).key);
            if (((ObjectDescJS.Entry) this.types.get(i)).wrap) {
                builder.append('"');
            }
            if (((ObjectDescJS.Entry) this.types.get(i)).optional) {
                builder.append('?');
            }
            builder.append(':');
            builder.append(' ');
            ((ObjectDescJS.Entry) this.types.get(i)).value.build(builder);
        }
        builder.append('}');
    }

    public String toString() {
        return this.build();
    }

    public static record Entry(String key, TypeDescJS value, boolean optional, boolean wrap) {

        private static final Pattern ILLEGAL_KEY_PATTERN = Pattern.compile("[^a-zA-Z0-9_$]");

        public Entry(String key, TypeDescJS value, boolean optional) {
            this(key, value, optional, ILLEGAL_KEY_PATTERN.matcher(key).find());
        }
    }
}