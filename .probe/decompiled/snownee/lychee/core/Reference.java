package snownee.lychee.core;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import snownee.lychee.util.json.JsonPointer;

public abstract class Reference {

    public static final Reference DEFAULT = new Reference.Constant("default");

    public static Reference create(String value) {
        if ("default".equals(value)) {
            return DEFAULT;
        } else {
            return (Reference) (value.codePointAt(0) == 47 ? new Reference.Pointer(new JsonPointer(value)) : new Reference.Constant(value));
        }
    }

    public static Reference fromJson(JsonObject parent, String key) {
        return create(GsonHelper.getAsString(parent, key, "default"));
    }

    public static void toJson(Reference reference, JsonObject parent, String key) {
        if (reference != DEFAULT) {
            parent.addProperty(key, reference.toString());
        }
    }

    public static Reference fromNetwork(FriendlyByteBuf buf) {
        return create(buf.readUtf());
    }

    public static void toNetwork(Reference reference, FriendlyByteBuf buf) {
        buf.writeUtf(reference.toString());
    }

    public boolean isPointer() {
        return this.getClass() == Reference.Pointer.class;
    }

    public JsonPointer getPointer() {
        return ((Reference.Pointer) this).pointer;
    }

    public static class Constant extends Reference {

        public final String name;

        public Constant(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public static class Pointer extends Reference {

        private final JsonPointer pointer;

        public Pointer(JsonPointer pointer) {
            this.pointer = pointer;
        }

        public String toString() {
            return this.pointer.toString();
        }
    }
}