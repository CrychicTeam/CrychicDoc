package net.minecraft.data.models.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class DelegatedModel implements Supplier<JsonElement> {

    private final ResourceLocation parent;

    public DelegatedModel(ResourceLocation resourceLocation0) {
        this.parent = resourceLocation0;
    }

    public JsonElement get() {
        JsonObject $$0 = new JsonObject();
        $$0.addProperty("parent", this.parent.toString());
        return $$0;
    }
}