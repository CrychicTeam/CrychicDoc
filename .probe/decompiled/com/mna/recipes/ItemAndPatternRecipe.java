package com.mna.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IItemAndPatternRecipe;
import com.mna.api.tools.MATags;
import com.mna.tools.NBTUtilities;
import java.util.ArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class ItemAndPatternRecipe extends AMRecipeBase implements IItemAndPatternRecipe {

    protected ResourceLocation[] requiredItems;

    protected ResourceLocation[] requiredPatterns;

    protected ResourceLocation output;

    protected int outputQuantity;

    protected CompoundTag __outputNBT = null;

    protected ItemStack __outputItem = null;

    public ItemAndPatternRecipe(ResourceLocation idIn) {
        super(idIn);
        this.requiredItems = new ResourceLocation[0];
        this.requiredPatterns = new ResourceLocation[0];
        this.output = new ResourceLocation("");
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        if (recipeJSON.get("output").isJsonObject()) {
            JsonObject outputObject = recipeJSON.get("output").getAsJsonObject();
            this.output = new ResourceLocation(outputObject.get("item").getAsString());
            if (outputObject.has("data")) {
                this.__outputNBT = NBTUtilities.fromJSON(outputObject.get("data").getAsJsonObject());
            }
        } else {
            this.output = new ResourceLocation(recipeJSON.get("output").getAsString());
        }
        if (recipeJSON.has("quantity")) {
            this.outputQuantity = Math.max(1, recipeJSON.get("quantity").getAsInt());
        } else {
            this.outputQuantity = 1;
        }
        JsonElement items = recipeJSON.get("items");
        if (items != null && items.isJsonArray()) {
            JsonArray itemsarray = items.getAsJsonArray();
            this.requiredItems = new ResourceLocation[Math.min(itemsarray.size(), this.maxItems())];
            for (int i = 0; i < this.requiredItems.length; i++) {
                this.requiredItems[i] = new ResourceLocation(itemsarray.get(i).getAsString());
            }
            JsonElement patterns = recipeJSON.get("patterns");
            if (patterns != null && patterns.isJsonArray()) {
                JsonArray patternsarray = patterns.getAsJsonArray();
                this.requiredPatterns = new ResourceLocation[Math.min(patternsarray.size(), this.maxPatterns())];
                for (int i = 0; i < this.requiredPatterns.length; i++) {
                    this.requiredPatterns[i] = new ResourceLocation(patternsarray.get(i).getAsString());
                }
            } else if (this.maxPatterns() > 0) {
                this.output = new ResourceLocation("");
                ManaAndArtifice.LOGGER.error("Error parsing JSON for manaweaving recipe %s, block [patterns].  This must be an array.  The object was not loaded.");
                return;
            }
        } else {
            this.output = new ResourceLocation("");
            ManaAndArtifice.LOGGER.error("Error parsing JSON for manaweaving recipe %s, block [items].  This must be an array.  The object was not loaded.");
        }
    }

    protected abstract int maxItems();

    protected abstract int maxPatterns();

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return true;
    }

    protected boolean patternsMatchShaped(ArrayList<String> patternsIn) {
        if (patternsIn.size() != this.requiredPatterns.length) {
            return false;
        } else {
            for (int i = 0; i < this.requiredPatterns.length; i++) {
                if (!((String) patternsIn.get(i)).equals(this.requiredPatterns[i].toString()) && !((String) patternsIn.get(i)).replace("manaweave_patterns/", "").equals(this.requiredPatterns[i].toString())) {
                    return false;
                }
            }
            return true;
        }
    }

    protected boolean itemsMatchShapeless(ArrayList<ItemStack> inItems) {
        if (inItems.size() != this.requiredItems.length) {
            return false;
        } else {
            for (ResourceLocation rLoc : this.requiredItems) {
                if (rLoc != null) {
                    for (int i = 0; i < inItems.size(); i++) {
                        ItemStack stack = (ItemStack) inItems.get(i);
                        if (!stack.isDamaged() && MATags.isItemEqual(stack, rLoc)) {
                            inItems.remove(i);
                            break;
                        }
                    }
                }
            }
            return inItems.size() == 0;
        }
    }

    protected boolean patternsMatchShapeless(ArrayList<String> patternsIn) {
        if (patternsIn.size() != this.requiredPatterns.length) {
            return false;
        } else {
            for (ResourceLocation rLoc : this.requiredPatterns) {
                if (rLoc != null) {
                    int idx = patternsIn.indexOf(rLoc.toString());
                    if (idx == -1) {
                        return false;
                    }
                    patternsIn.remove(idx);
                }
            }
            return patternsIn.size() == 0;
        }
    }

    public int getOutputQuantity() {
        return this.outputQuantity;
    }

    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return this.getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        if (this.__outputItem == null || this.__outputItem.isEmpty()) {
            IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
            if (itemRegistry != null) {
                Item outputItem = itemRegistry.getValue(this.output);
                if (outputItem == null) {
                    return ItemStack.EMPTY;
                }
                this.__outputItem = new ItemStack(outputItem, this.outputQuantity);
                if (this.__outputNBT != null) {
                    this.__outputItem.setTag(this.__outputNBT.copy());
                }
            }
        }
        return this.__outputItem;
    }

    @Override
    public void runValidation() {
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ResourceLocation[] getRequiredItems() {
        return this.requiredItems;
    }

    public void setRequiredItems(ResourceLocation[] locations) {
        this.requiredItems = locations;
    }

    @Override
    public ResourceLocation[] getRequiredPatterns() {
        return this.requiredPatterns;
    }

    public void setRequiredPatterns(ResourceLocation[] patterns) {
        this.requiredPatterns = patterns;
    }

    public ResourceLocation getOutput() {
        return this.output;
    }

    public void setOutput(ResourceLocation location) {
        this.output = location;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public abstract RecipeType<?> getType();
}