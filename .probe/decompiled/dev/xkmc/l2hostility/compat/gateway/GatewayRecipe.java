package dev.xkmc.l2hostility.compat.gateway;

import com.google.gson.JsonObject;
import dev.shadowsoffire.gateways.GatewayObjects;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record GatewayRecipe(FinishedRecipe r, ResourceLocation gate) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject data) {
        this.r.serializeRecipeData(data);
        data.addProperty("gateway", this.gate.toString());
    }

    @Override
    public ResourceLocation getId() {
        return this.r.getId();
    }

    @Override
    public RecipeSerializer<?> getType() {
        return (RecipeSerializer<?>) GatewayObjects.GATE_RECIPE.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.r.serializeAdvancement();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.r.getAdvancementId();
    }
}