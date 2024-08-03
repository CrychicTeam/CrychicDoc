package com.mna.recipes.manaweaving;

import com.google.gson.JsonObject;
import com.mna.items.ItemInit;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class TransmutationRecipe extends AMRecipeBase {

    private ResourceLocation targetBlock;

    private ResourceLocation replaceBlock;

    private ResourceLocation lootTable;

    private ResourceLocation representationItem;

    public TransmutationRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        if (recipeJSON.has("targetBlock")) {
            this.targetBlock = new ResourceLocation(recipeJSON.get("targetBlock").getAsString());
        }
        if (recipeJSON.has("replaceBlock")) {
            this.replaceBlock = new ResourceLocation(recipeJSON.get("replaceBlock").getAsString());
        }
        if (recipeJSON.has("lootTable")) {
            this.lootTable = new ResourceLocation(recipeJSON.get("lootTable").getAsString());
        }
        if (recipeJSON.has("representationItem")) {
            this.representationItem = new ResourceLocation(recipeJSON.get("representationItem").getAsString());
        }
    }

    @Override
    public void runValidation() {
        super.runValidation();
        if (this.targetBlock == null) {
            throw new RuntimeException("Transmutation recipe must have targetBlock directive.");
        } else if (this.replaceBlock == null && this.lootTable == null) {
            throw new RuntimeException("Transmutation recipe must have either replaceBlock or lootTable.");
        } else if (this.lootTable != null && this.representationItem == null) {
            throw new RuntimeException("Transmutation recipe using lootTable must specify representationItem (for gui purposes).");
        }
    }

    public boolean hasLootTable() {
        return this.lootTable != null;
    }

    public boolean hasReplaceBlock() {
        return this.replaceBlock != null;
    }

    public boolean hasRepresentationItem() {
        return this.representationItem != null;
    }

    public ResourceLocation getTargetBlock() {
        return this.targetBlock;
    }

    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

    public ResourceLocation getReplaceBlock() {
        return this.replaceBlock;
    }

    public ResourceLocation getRepresentationItem() {
        return this.representationItem;
    }

    public void setTargetBlock(ResourceLocation targetBlock) {
        this.targetBlock = targetBlock;
    }

    public void setReplaceBlock(ResourceLocation replaceBlock) {
        this.replaceBlock = replaceBlock;
    }

    public void setLootTable(ResourceLocation lootTable) {
        this.lootTable = lootTable;
    }

    public void setRepresentationItem(ResourceLocation representationItem) {
        this.representationItem = representationItem;
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return false;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        if (this.representationItem != null) {
            Item item = ForgeRegistries.ITEMS.getValue(this.representationItem);
            if (item != null) {
                return new ItemStack(item);
            }
        } else if (this.replaceBlock != null) {
            Block block = ForgeRegistries.BLOCKS.getValue(this.replaceBlock);
            if (block != null) {
                return new ItemStack(block);
            }
        }
        return new ItemStack(ItemInit.MANAWEAVER_WAND.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.TRANSMUTATION_TYPE.get();
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        return this.getResultItem();
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }
}