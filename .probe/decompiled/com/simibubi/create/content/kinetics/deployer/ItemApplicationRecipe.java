package com.simibubi.create.content.kinetics.deployer;

import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ItemApplicationRecipe extends ProcessingRecipe<RecipeWrapper> {

    private boolean keepHeldItem;

    public ItemApplicationRecipe(AllRecipeTypes type, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(type, params);
        this.keepHeldItem = params.keepHeldItem;
    }

    public boolean matches(RecipeWrapper inv, Level p_77569_2_) {
        return this.ingredients.get(0).test(inv.getItem(0)) && this.ingredients.get(1).test(inv.getItem(1));
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    public boolean shouldKeepHeldItem() {
        return this.keepHeldItem;
    }

    public Ingredient getRequiredHeldItem() {
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("Item Application Recipe: " + this.id.toString() + " has no tool!");
        } else {
            return this.ingredients.get(1);
        }
    }

    public Ingredient getProcessedItem() {
        if (this.ingredients.size() < 2) {
            throw new IllegalStateException("Item Application Recipe: " + this.id.toString() + " has no ingredient!");
        } else {
            return this.ingredients.get(0);
        }
    }

    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.keepHeldItem = GsonHelper.getAsBoolean(json, "keepHeldItem", false);
    }

    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        if (this.keepHeldItem) {
            json.addProperty("keepHeldItem", this.keepHeldItem);
        }
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        this.keepHeldItem = buffer.readBoolean();
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeBoolean(this.keepHeldItem);
    }
}