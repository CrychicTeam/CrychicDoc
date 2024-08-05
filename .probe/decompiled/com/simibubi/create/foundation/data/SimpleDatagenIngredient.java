package com.simibubi.create.foundation.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.recipe.Mods;
import java.util.stream.Stream;
import net.minecraft.world.item.crafting.Ingredient;

public class SimpleDatagenIngredient extends Ingredient {

    private Mods mod;

    private String id;

    public SimpleDatagenIngredient(Mods mod, String id) {
        super(Stream.empty());
        this.mod = mod;
        this.id = id;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", this.mod.asResource(this.id).toString());
        return jsonobject;
    }
}