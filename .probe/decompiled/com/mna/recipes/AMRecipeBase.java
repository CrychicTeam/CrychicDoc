package com.mna.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.Registries;
import com.mna.api.faction.IFaction;
import com.mna.api.recipes.IMARecipe;
import java.util.ArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AMRecipeBase extends CustomRecipe implements IMARecipe {

    protected int tier = 1;

    protected IFaction requiredFaction = null;

    protected ImmutableList<RecipeByproduct> byproducts = ImmutableList.of();

    public AMRecipeBase(ResourceLocation idIn) {
        super(idIn, CraftingBookCategory.MISC);
    }

    public final void parseJSON(JsonObject object) {
        this.parseExtraJson(object);
        if (object.has("tier")) {
            this.tier = object.get("tier").getAsInt();
        }
        if (object.has("requiredFaction")) {
            ResourceLocation factionRLoc = new ResourceLocation(object.get("requiredFaction").getAsString());
            this.setRequiredFaction(factionRLoc);
        }
        if (object.has("byproducts")) {
            JsonArray byproductArray = object.get("byproducts").getAsJsonArray();
            ArrayList<RecipeByproduct> parsedByproducts = new ArrayList();
            byproductArray.forEach(elem -> {
                if (elem instanceof JsonObject) {
                    RecipeByproduct byproduct = RecipeByproduct.FromJSON((JsonObject) elem);
                    if (byproduct != null) {
                        parsedByproducts.add(byproduct);
                    }
                }
            });
            this.byproducts = ImmutableList.copyOf(parsedByproducts);
        }
        this.runValidation();
    }

    public final ArrayList<ItemStack> rollByproducts(RandomSource randomSource) {
        ArrayList<ItemStack> byproductRolls = new ArrayList();
        this.byproducts.forEach(bp -> {
            if (randomSource.nextFloat() <= bp.chance) {
                byproductRolls.add(bp.stack.copy());
            }
        });
        return byproductRolls;
    }

    public final ImmutableList<RecipeByproduct> getByproducts() {
        return this.byproducts;
    }

    protected abstract void parseExtraJson(JsonObject var1);

    @Override
    public final int getTier() {
        return this.tier;
    }

    @Override
    public abstract ItemStack getGuiRepresentationStack();

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.getResultItem();
    }

    public IFaction getFactionRequirement() {
        return this.requiredFaction;
    }

    public void setRequiredFaction(ResourceLocation factionID) {
        this.requiredFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID);
    }

    protected void runValidation() {
    }
}