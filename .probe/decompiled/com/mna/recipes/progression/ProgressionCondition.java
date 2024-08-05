package com.mna.recipes.progression;

import com.google.gson.JsonObject;
import com.mna.api.config.GeneralConfigValues;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ProgressionCondition extends AMRecipeBase {

    private ResourceLocation advancementID;

    private String _desc;

    private Component _description;

    public ProgressionCondition(ResourceLocation id) {
        super(id);
    }

    @Override
    protected void parseExtraJson(JsonObject recipeJSON) {
        this.advancementID = new ResourceLocation(recipeJSON.get("advancement").getAsString());
        if (recipeJSON.has("description")) {
            this._desc = recipeJSON.get("description").getAsString();
        } else {
            this._desc = this.advancementID.toString() + ".description";
        }
    }

    public ResourceLocation getAdvancementID() {
        return this.advancementID;
    }

    public String getDescriptionID() {
        return this._desc;
    }

    public Component getDescription() {
        if (this._description == null) {
            this._description = Component.translatable(this._desc);
        }
        return this._description;
    }

    public void setAdvancementID(ResourceLocation advancementID) {
        this.advancementID = advancementID;
    }

    public void setDescriptionID(String id) {
        this._desc = id;
        this._description = null;
    }

    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }

    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.PROGRESSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.PROGRESSION_TYPE.get();
    }

    public static Optional<ProgressionCondition> get(Level world, ResourceLocation rLoc) {
        return world.getRecipeManager().<CraftingContainer, ProgressionCondition>getAllRecipesFor(RecipeInit.PROGRESSION_TYPE.get()).stream().filter(r -> r.m_6423_().equals(rLoc)).findFirst();
    }

    public static List<ProgressionCondition> get(Level world, int tier) {
        return get(world, tier, new ArrayList());
    }

    public static List<ProgressionCondition> get(Level world, int tier, List<ResourceLocation> except) {
        return (List<ProgressionCondition>) world.getRecipeManager().<CraftingContainer, ProgressionCondition>getAllRecipesFor(RecipeInit.PROGRESSION_TYPE.get()).stream().filter(r -> r.getTier() == tier && !except.contains(r.m_6423_())).collect(Collectors.toList());
    }

    public static int getCompletionRequirementForTier(Level world, int tier) {
        double pctRequired = GeneralConfigValues.TierUpPercentTasksComplete;
        List<ProgressionCondition> conditions = get(world, tier);
        return (int) Math.max(Math.round((double) conditions.size() * pctRequired), 1L);
    }
}