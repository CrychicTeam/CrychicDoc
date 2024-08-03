package dev.latvian.mods.kubejs.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class ModelGenerator {

    private String parent = "minecraft:block/cube";

    private final JsonObject textures = new JsonObject();

    private final List<ModelGenerator.Element> elements = new ArrayList();

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (!this.parent.isEmpty()) {
            json.addProperty("parent", this.parent);
        }
        if (this.textures.size() > 0) {
            json.add("textures", this.textures);
        }
        if (!this.elements.isEmpty()) {
            JsonArray a = new JsonArray();
            for (ModelGenerator.Element e : this.elements) {
                a.add(e.toJson());
            }
            json.add("elements", a);
        }
        return json;
    }

    public void parent(String s) {
        this.parent = s;
    }

    public void texture(String name, String texture) {
        this.textures.addProperty(name, texture);
    }

    public void textures(JsonObject json) {
        for (Entry<String, JsonElement> entry : json.entrySet()) {
            this.textures.add((String) entry.getKey(), (JsonElement) entry.getValue());
        }
    }

    public void element(Consumer<ModelGenerator.Element> consumer) {
        ModelGenerator.Element e = new ModelGenerator.Element();
        consumer.accept(e);
        this.elements.add(e);
    }

    public static class Element {

        private AABB box = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

        private final JsonObject faces = new JsonObject();

        public ModelGenerator.Element box(AABB b) {
            this.box = b;
            return this;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            JsonArray f = new JsonArray();
            f.add(this.box.minX * 16.0);
            f.add(this.box.minY * 16.0);
            f.add(this.box.minZ * 16.0);
            json.add("from", f);
            JsonArray t = new JsonArray();
            t.add(this.box.maxX * 16.0);
            t.add(this.box.maxY * 16.0);
            t.add(this.box.maxZ * 16.0);
            json.add("to", t);
            json.add("faces", this.faces);
            return json;
        }

        public void face(Direction direction, Consumer<ModelGenerator.Face> consumer) {
            ModelGenerator.Face f = new ModelGenerator.Face();
            f.direction = direction;
            consumer.accept(f);
            this.faces.add(direction.getSerializedName(), f.toJson());
        }
    }

    public static class Face {

        private Direction direction;

        private String texture = "broken";

        private Direction cullface = null;

        private double[] uv = null;

        private int tintindex = -1;

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("texture", this.texture);
            if (this.cullface != null) {
                json.addProperty("cullface", this.cullface.getSerializedName());
            }
            if (this.uv != null) {
                JsonArray a = new JsonArray();
                a.add(this.uv[0]);
                a.add(this.uv[1]);
                a.add(this.uv[2]);
                a.add(this.uv[3]);
                json.add("uv", a);
            }
            if (this.tintindex >= 0) {
                json.addProperty("tintindex", this.tintindex);
            }
            return json;
        }

        public ModelGenerator.Face tex(String t) {
            this.texture = t;
            return this;
        }

        public ModelGenerator.Face cull(Direction d) {
            this.cullface = d;
            return this;
        }

        public ModelGenerator.Face cull() {
            return this.cull(this.direction);
        }

        public ModelGenerator.Face uv(double u0, double v0, double u1, double v1) {
            this.uv = new double[] { u0, v0, u1, v1 };
            return this;
        }

        public ModelGenerator.Face tintindex(int i) {
            this.tintindex = i;
            return this;
        }
    }
}