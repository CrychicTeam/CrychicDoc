package dev.latvian.mods.kubejs.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VariantBlockStateGenerator {

    private final JsonObject variants = new JsonObject();

    public void variant(String key, Consumer<VariantBlockStateGenerator.Variant> consumer) {
        VariantBlockStateGenerator.Variant v = new VariantBlockStateGenerator.Variant();
        v.key = key;
        consumer.accept(v);
        this.variants.add(v.key, v.toJson());
    }

    @HideFromJS
    @Deprecated
    public void variant(String key, String model) {
        this.simpleVariant(key, model);
    }

    public void simpleVariant(String key, String model) {
        this.variant(key, (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(model)));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("variants", this.variants);
        return json;
    }

    public static class Model {

        private String model = "broken";

        private int x = 0;

        private int y = 0;

        private boolean uvlock = false;

        public VariantBlockStateGenerator.Model model(String s) {
            this.model = s;
            return this;
        }

        public VariantBlockStateGenerator.Model x(int _x) {
            this.x = _x;
            return this;
        }

        public VariantBlockStateGenerator.Model y(int _y) {
            this.y = _y;
            return this;
        }

        public VariantBlockStateGenerator.Model uvlock() {
            this.uvlock = true;
            return this;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("model", this.model);
            if (this.x != 0) {
                json.addProperty("x", this.x);
            }
            if (this.y != 0) {
                json.addProperty("y", this.y);
            }
            if (this.uvlock) {
                json.addProperty("uvlock", true);
            }
            return json;
        }
    }

    public static class Variant {

        private String key;

        private final List<VariantBlockStateGenerator.Model> models = new ArrayList();

        public VariantBlockStateGenerator.Model model(String s) {
            VariantBlockStateGenerator.Model model = new VariantBlockStateGenerator.Model();
            model.model(s);
            this.models.add(model);
            return model;
        }

        public JsonElement toJson() {
            if (this.models.size() == 1) {
                return ((VariantBlockStateGenerator.Model) this.models.get(0)).toJson();
            } else {
                JsonArray a = new JsonArray();
                for (VariantBlockStateGenerator.Model m : this.models) {
                    a.add(m.toJson());
                }
                return a;
            }
        }
    }
}