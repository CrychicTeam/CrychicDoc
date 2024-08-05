package noppes.npcs.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.handler.data.IRecipe;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.controllers.data.RecipesDefault;

public class RecipeController implements IRecipeHandler {

    public HashMap<ResourceLocation, RecipeCarpentry> globalRecipes = new HashMap();

    public HashMap<ResourceLocation, RecipeCarpentry> anvilRecipes = new HashMap();

    public static RecipeController instance;

    public static final int version = 1;

    public int nextId = 1;

    public static HashMap<Integer, RecipeCarpentry> syncRecipes = new HashMap();

    public RecipeController() {
        instance = this;
    }

    public void load() {
        this.loadCategories();
        this.reloadGlobalRecipes();
        EventHooks.onGlobalRecipesLoaded(this);
    }

    public void reloadGlobalRecipes() {
    }

    private void loadCategories() {
        File saveDir = CustomNpcs.getLevelSaveDirectory();
        try {
            File file = new File(saveDir, "recipes.dat");
            if (file.exists()) {
                this.loadCategories(file);
            } else {
                this.globalRecipes.clear();
                this.anvilRecipes.clear();
                this.loadDefaultRecipes(-1);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            try {
                File filex = new File(saveDir, "recipes.dat_old");
                if (filex.exists()) {
                    this.loadCategories(filex);
                }
            } catch (Exception var4) {
                var5.printStackTrace();
            }
        }
    }

    private void loadDefaultRecipes(int i) {
        if (i != 1) {
            RecipesDefault.loadDefaultRecipes(i);
            this.saveCategories();
        }
    }

    private void loadCategories(File file) throws Exception {
    }

    private void saveCategories() {
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            ListTag list = new ListTag();
            for (RecipeCarpentry recipe : this.globalRecipes.values()) {
                if (recipe.savesRecipe) {
                    list.add(recipe.writeNBT());
                }
            }
            for (RecipeCarpentry recipex : this.anvilRecipes.values()) {
                if (recipex.savesRecipe) {
                    list.add(recipex.writeNBT());
                }
            }
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.put("Data", list);
            nbttagcompound.putInt("LastId", this.nextId);
            nbttagcompound.putInt("Version", 1);
            File file = new File(saveDir, "recipes.dat_new");
            File file1 = new File(saveDir, "recipes.dat_old");
            File file2 = new File(saveDir, "recipes.dat");
            NbtIo.writeCompressed(nbttagcompound, new FileOutputStream(file));
            if (file1.exists()) {
                file1.delete();
            }
            file2.renameTo(file1);
            if (file2.exists()) {
                file2.delete();
            }
            file.renameTo(file2);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    public RecipeCarpentry findMatchingRecipe(CraftingContainer inventoryCrafting) {
        for (RecipeCarpentry recipe : this.anvilRecipes.values()) {
            if (recipe.isValid() && recipe.matches(inventoryCrafting, null)) {
                return recipe;
            }
        }
        return null;
    }

    public RecipeCarpentry getRecipe(int id) {
        if (this.globalRecipes.containsKey(id)) {
            return (RecipeCarpentry) this.globalRecipes.get(id);
        } else {
            return this.anvilRecipes.containsKey(id) ? (RecipeCarpentry) this.anvilRecipes.get(id) : null;
        }
    }

    public RecipeCarpentry saveRecipe(RecipeCarpentry recipe) {
        return null;
    }

    private int getUniqueId() {
        this.nextId++;
        return this.nextId;
    }

    private boolean containsRecipeName(String name) {
        return false;
    }

    public RecipeCarpentry delete(int id) {
        RecipeCarpentry recipe = this.getRecipe(id);
        return recipe == null ? null : recipe;
    }

    @Override
    public List<IRecipe> getGlobalList() {
        return new ArrayList(this.globalRecipes.values());
    }

    @Override
    public List<IRecipe> getCarpentryList() {
        return new ArrayList(this.anvilRecipes.values());
    }

    @Override
    public IRecipe addRecipe(String name, boolean global, ItemStack result, Object... objects) {
        return null;
    }

    @Override
    public IRecipe addRecipe(String name, boolean global, ItemStack result, int width, int height, ItemStack... objects) {
        NonNullList<Ingredient> list = NonNullList.create();
        for (ItemStack item : objects) {
            if (!item.isEmpty()) {
                list.add(Ingredient.of(item));
            }
        }
        return null;
    }
}