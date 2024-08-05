package com.mna.recipes.manaweaving;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IManaweavingRecipe;
import com.mna.recipes.ItemAndPatternCraftingInventory;
import com.mna.recipes.ItemAndPatternRecipe;
import com.mna.recipes.RecipeInit;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ManaweavingRecipe extends ItemAndPatternRecipe implements IManaweavingRecipe {

    private static final ResourceLocation NO_ENCHANT = new ResourceLocation("mna", "none");

    private static IForgeRegistry<Enchantment> __enchantRegistry;

    private ResourceLocation enchantment;

    private int enchantmentMagnitude;

    private boolean copyNBT = false;

    public ManaweavingRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        super.parseExtraJson(recipeJSON);
        if (recipeJSON.has("enchant")) {
            this.enchantment = new ResourceLocation(recipeJSON.get("enchant").getAsString());
            if (recipeJSON.has("magnitude")) {
                this.enchantmentMagnitude = recipeJSON.get("magnitude").getAsInt();
            } else {
                this.enchantmentMagnitude = 1;
            }
        }
        if (recipeJSON.has("copy_nbt")) {
            this.copyNBT = recipeJSON.get("copy_nbt").getAsBoolean();
        }
    }

    @Override
    public void runValidation() {
        super.runValidation();
        if (this.requiredItems.length > 9) {
            ManaAndArtifice.LOGGER.error("Manaweaving recipe can't have more than 9 items; this will be uncraftable! [" + this.m_6423_().toString() + "]");
        }
        if (this.requiredPatterns.length > 6) {
            ManaAndArtifice.LOGGER.error("Manaweaving recipe can't have more than 6 patterns; this will be uncraftable! [" + this.m_6423_().toString() + "]");
        }
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        if (!(inv instanceof ItemAndPatternCraftingInventory mwInv)) {
            return false;
        } else {
            ArrayList<ItemStack> items = new ArrayList();
            ArrayList<String> patterns = new ArrayList(mwInv.getPatterns());
            for (int i = 0; i < 9; i++) {
                if (!inv.m_8020_(i).isEmpty()) {
                    items.add(inv.m_8020_(i));
                }
            }
            return this.itemsMatchShapeless(items) && this.patternsMatchShaped(patterns);
        }
    }

    public boolean matches(Map<Enchantment, Integer> runeEnchantment) {
        if (runeEnchantment.size() == 1 && this.enchantment != null) {
            for (Enchantment ench : runeEnchantment.keySet()) {
                if (this.enchantment.equals(ForgeRegistries.ENCHANTMENTS.getKey(ench)) && this.enchantmentMagnitude == (Integer) runeEnchantment.get(ench)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
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
                if (this.enchantment != null) {
                    if (__enchantRegistry == null) {
                        __enchantRegistry = ForgeRegistries.ENCHANTMENTS;
                    }
                    if (__enchantRegistry != null) {
                        Enchantment resolved = __enchantRegistry.getValue(this.enchantment);
                        if (resolved != null) {
                            Map<Enchantment, Integer> map = Maps.newLinkedHashMap();
                            map.put(resolved, this.enchantmentMagnitude);
                            EnchantmentHelper.setEnchantments(map, this.__outputItem);
                        }
                    }
                }
            }
        }
        return this.__outputItem;
    }

    @Override
    public boolean isEnchantment() {
        return !this.getEnchantment().equals(NO_ENCHANT);
    }

    @Override
    public ResourceLocation getEnchantment() {
        return this.enchantment == null ? NO_ENCHANT : this.enchantment;
    }

    public void setEnchantment(ResourceLocation location) {
        this.enchantment = location;
    }

    @Override
    public int getEnchantmentMagnitude() {
        return this.enchantmentMagnitude;
    }

    public void setEnchantmentMagnitude(int magnitude) {
        this.enchantmentMagnitude = magnitude;
    }

    @Override
    public boolean getCopyNBT() {
        return this.copyNBT;
    }

    public void setCopyNBT(boolean copy) {
        this.copyNBT = copy;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.MANAWEAVING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.MANAWEAVING_RECIPE_TYPE.get();
    }

    @Override
    protected int maxItems() {
        return 9;
    }

    @Override
    protected int maxPatterns() {
        return 6;
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        ItemStack out = this.getResultItem();
        if (this.isEnchantment()) {
            Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(this.getEnchantment());
            if (ench != null) {
                out.setHoverName(ench.getFullname(this.enchantmentMagnitude));
            }
        }
        return out;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }
}