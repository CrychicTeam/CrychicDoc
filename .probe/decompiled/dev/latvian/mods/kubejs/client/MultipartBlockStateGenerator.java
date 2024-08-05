package dev.latvian.mods.kubejs.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultipartBlockStateGenerator {

    private final JsonArray multipart = new JsonArray();

    public void part(String when, Consumer<MultipartBlockStateGenerator.Part> consumer) {
        MultipartBlockStateGenerator.Part v = new MultipartBlockStateGenerator.Part();
        v.when = when;
        consumer.accept(v);
        this.multipart.add(v.toJson());
    }

    public void part(String when, String model) {
        this.part(when, (Consumer<MultipartBlockStateGenerator.Part>) (v -> v.model(model)));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("multipart", this.multipart);
        return json;
    }

    public static class Part {

        private String when;

        private final List<VariantBlockStateGenerator.Model> apply = new ArrayList();

        public VariantBlockStateGenerator.Model model(String s) {
            VariantBlockStateGenerator.Model model = new VariantBlockStateGenerator.Model();
            model.model(s);
            this.apply.add(model);
            return model;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            if (!this.when.isEmpty()) {
                JsonObject whenJson = new JsonObject();
                for (String s : this.when.split(",")) {
                    String[] s1 = s.split("=", 2);
                    if (s1.length == 2 && !s1[0].isEmpty() && !s1[1].isEmpty()) {
                        whenJson.addProperty(s1[0], s1[1]);
                    }
                }
                json.add("when", whenJson);
            }
            if (this.apply.size() == 1) {
                json.add("apply", ((VariantBlockStateGenerator.Model) this.apply.get(0)).toJson());
            } else {
                JsonArray a = new JsonArray();
                for (VariantBlockStateGenerator.Model m : this.apply) {
                    a.add(m.toJson());
                }
                json.add("apply", a);
            }
            return json;
        }
    }
}