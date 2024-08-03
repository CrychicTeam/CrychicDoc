package com.almostreliable.summoningrituals.compat.kubejs;

import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.IngredientStack;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class AltarRecipeJS extends RecipeJS {

    @Override
    public InputItem readInputItem(Object from) {
        if (from instanceof JsonElement jsonElement) {
            IngredientStack stack = IngredientStack.fromJson(jsonElement);
            return InputItem.of(stack.ingredient(), stack.count());
        } else {
            return super.readInputItem(from);
        }
    }

    @Override
    public JsonElement writeInputItem(InputItem value) {
        if (value.count == 1) {
            return value.ingredient.toJson();
        } else {
            JsonObject obj = new JsonObject();
            obj.add("ingredient", value.ingredient.toJson());
            obj.addProperty("count", value.count);
            return obj;
        }
    }

    public AltarRecipeJS itemOutput(RecipeOutputs.ItemOutputBuilder itemOutput) {
        this.getValue(AltarRecipeSchema.OUTPUTS).add(itemOutput.build());
        return this;
    }

    public AltarRecipeJS mobOutput(RecipeOutputs.MobOutputBuilder entityOutput) {
        this.getValue(AltarRecipeSchema.OUTPUTS).add(entityOutput.build());
        return this;
    }

    public AltarRecipeJS input(InputItem... ingredients) {
        InputItem[] arr = (InputItem[]) ArrayUtils.addAll(this.getValue(AltarRecipeSchema.INPUTS), ingredients);
        this.setValue(AltarRecipeSchema.INPUTS, arr);
        return this;
    }

    public AltarRecipeJS sacrifice(ResourceLocation id, int count) {
        Preconditions.checkNotNull(id);
        this.getValue(AltarRecipeSchema.SACRIFICES).add(Platform.mobFromId(id), count);
        return this;
    }

    public AltarRecipeJS sacrifice(ResourceLocation id) {
        Preconditions.checkNotNull(id);
        return this.sacrifice(id, 1);
    }

    public AltarRecipeJS sacrificeRegion(int width, int height) {
        this.getValue(AltarRecipeSchema.SACRIFICES).setRegion(new Vec3i(width, height, width));
        return this;
    }

    public AltarRecipeJS blockBelow(ResourceLocation id, JsonObject properties) {
        Preconditions.checkNotNull(id);
        JsonObject blockJson = new JsonObject();
        blockJson.addProperty("block", id.toString());
        blockJson.add("properties", properties);
        this.setValue(AltarRecipeSchema.BLOCK_BELOW, BlockReference.fromJson(blockJson));
        return this;
    }

    public AltarRecipeJS blockBelow(ResourceLocation id) {
        return this.blockBelow(id, new JsonObject());
    }
}