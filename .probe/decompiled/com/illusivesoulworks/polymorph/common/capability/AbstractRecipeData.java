package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.PolymorphConstants;
import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.api.common.capability.IRecipeData;
import com.illusivesoulworks.polymorph.common.impl.RecipePair;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class AbstractRecipeData<E> implements IRecipeData<E> {

    private final SortedSet<IRecipePair> recipesList = new TreeSet();

    private final E owner;

    private Recipe<?> lastRecipe;

    private Recipe<?> selectedRecipe;

    private ResourceLocation loadedRecipe;

    private boolean isFailing;

    private NonNullList<Item> input;

    public AbstractRecipeData(E owner) {
        this.owner = owner;
        this.input = NonNullList.create();
    }

    @Override
    public <T extends Recipe<C>, C extends Container> Optional<T> getRecipe(RecipeType<T> type, C inventory, Level level, List<T> recipesList) {
        boolean isEmpty = this.isEmpty(inventory);
        this.getLoadedRecipe().flatMap(id -> level.getRecipeManager().byKey(id)).ifPresent(selected -> {
            try {
                if (selected.getType() == type && (selected.matches(inventory, level) || isEmpty)) {
                    this.setSelectedRecipe(selected);
                }
            } catch (ClassCastException var7x) {
                PolymorphConstants.LOG.error("Recipe {} does not match inventory {}", selected.getId(), inventory);
            }
            this.loadedRecipe = null;
        });
        if (isEmpty) {
            this.setFailing(false);
            this.sendRecipesListToListeners(true);
            return Optional.empty();
        } else {
            AtomicReference<T> ref = new AtomicReference(null);
            this.getLastRecipe().ifPresent(recipex -> {
                try {
                    if (recipex.getType() == type && recipex.matches(inventory, level)) {
                        this.getSelectedRecipe().ifPresent(selected -> {
                            try {
                                if (selected.getType() == type && selected.matches(inventory, level)) {
                                    ref.set(selected);
                                }
                            } catch (ClassCastException var6x) {
                                PolymorphConstants.LOG.error("Recipe {} does not match inventory {}", selected.getId(), inventory);
                            }
                        });
                    }
                } catch (ClassCastException var7x) {
                    PolymorphConstants.LOG.error("Recipe {} does not match inventory {}", recipex.getId(), inventory);
                }
            });
            T result = (T) ref.get();
            if (result != null && !(this instanceof AbstractBlockEntityRecipeData)) {
                boolean inputChanged = false;
                int size = inventory.getContainerSize();
                NonNullList<Item> currentInput = NonNullList.withSize(size, Items.AIR);
                if (size != this.input.size()) {
                    inputChanged = true;
                }
                for (int i = 0; i < size; i++) {
                    ItemStack stack = inventory.getItem(i);
                    Item item = stack.getItem();
                    if (!inputChanged && i < this.input.size() && item != this.input.get(i)) {
                        inputChanged = true;
                    }
                    if (!stack.isEmpty()) {
                        currentInput.set(i, item);
                    }
                }
                this.input = currentInput;
                if (!inputChanged) {
                    this.setFailing(false);
                    this.sendRecipesListToListeners(false);
                    return Optional.of(result);
                }
            }
            SortedSet<IRecipePair> newDataset = new TreeSet();
            List<T> recipes = recipesList.isEmpty() ? level.getRecipeManager().getRecipesFor(type, inventory, level) : recipesList;
            if (recipes.isEmpty()) {
                this.setFailing(true);
                this.sendRecipesListToListeners(true);
                return Optional.empty();
            } else {
                List<T> validRecipes = new ArrayList();
                for (T entry : recipes) {
                    ResourceLocation id = entry.getId();
                    if (ref.get() == null && (Boolean) this.getSelectedRecipe().map(recipex -> recipex.getId().equals(id)).orElse(false)) {
                        ref.set(entry);
                    }
                    ItemStack output = entry.getResultItem(level.registryAccess());
                    if (output == null || output.isEmpty() || entry instanceof CustomRecipe) {
                        output = entry.assemble(inventory, level.registryAccess());
                    }
                    if (!output.isEmpty()) {
                        newDataset.add(new RecipePair(id, output));
                        validRecipes.add(entry);
                    }
                }
                if (validRecipes.isEmpty()) {
                    this.setFailing(true);
                    this.sendRecipesListToListeners(true);
                    return Optional.empty();
                } else {
                    this.setRecipesList(newDataset);
                    result = (T) ref.get();
                    if (result == null) {
                        ResourceLocation rl = ((IRecipePair) newDataset.first()).getResourceLocation();
                        for (T recipe : validRecipes) {
                            if (recipe.getId().equals(rl)) {
                                result = recipe;
                                break;
                            }
                        }
                    }
                    if (result == null) {
                        this.setFailing(true);
                        this.sendRecipesListToListeners(true);
                        return Optional.empty();
                    } else {
                        this.lastRecipe = result;
                        this.setSelectedRecipe(result);
                        this.setFailing(false);
                        this.sendRecipesListToListeners(false);
                        return Optional.of(result);
                    }
                }
            }
        }
    }

    @Override
    public Optional<? extends Recipe<?>> getSelectedRecipe() {
        return Optional.ofNullable(this.selectedRecipe);
    }

    @Override
    public void setSelectedRecipe(@Nonnull Recipe<?> recipe) {
        this.selectedRecipe = recipe;
    }

    public Optional<? extends Recipe<?>> getLastRecipe() {
        return Optional.ofNullable(this.lastRecipe);
    }

    public Optional<ResourceLocation> getLoadedRecipe() {
        return Optional.ofNullable(this.loadedRecipe);
    }

    @Nonnull
    @Override
    public SortedSet<IRecipePair> getRecipesList() {
        return this.recipesList;
    }

    @Override
    public void setRecipesList(@Nonnull SortedSet<IRecipePair> recipesList) {
        this.recipesList.clear();
        this.recipesList.addAll(recipesList);
    }

    @Override
    public boolean isEmpty(Container inventory) {
        if (inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                if (!inventory.getItem(i).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public E getOwner() {
        return this.owner;
    }

    @Override
    public void selectRecipe(@Nonnull Recipe<?> recipe) {
        this.setSelectedRecipe(recipe);
    }

    @Override
    public abstract Set<ServerPlayer> getListeners();

    @Override
    public void sendRecipesListToListeners(boolean isEmpty) {
        Pair<SortedSet<IRecipePair>, ResourceLocation> packetData = isEmpty ? new Pair(new TreeSet(), null) : this.getPacketData();
        for (ServerPlayer listener : this.getListeners()) {
            PolymorphApi.common().getPacketDistributor().sendRecipesListS2C(listener, (SortedSet<IRecipePair>) packetData.getFirst(), (ResourceLocation) packetData.getSecond());
        }
    }

    @Override
    public Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData() {
        return new Pair(this.getRecipesList(), null);
    }

    @Override
    public boolean isFailing() {
        return this.isFailing;
    }

    @Override
    public void setFailing(boolean isFailing) {
        this.isFailing = isFailing;
    }

    @Override
    public void readNBT(CompoundTag compoundTag) {
        if (compoundTag.contains("SelectedRecipe")) {
            this.loadedRecipe = new ResourceLocation(compoundTag.getString("SelectedRecipe"));
        }
        if (compoundTag.contains("RecipeDataSet")) {
            Set<IRecipePair> dataset = this.getRecipesList();
            dataset.clear();
            for (Tag inbt : compoundTag.getList("RecipeDataSet", 10)) {
                CompoundTag tag = (CompoundTag) inbt;
                ResourceLocation id = ResourceLocation.tryParse(tag.getString("Id"));
                ItemStack stack = ItemStack.of(tag.getCompound("ItemStack"));
                dataset.add(new RecipePair(id, stack));
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.getSelectedRecipe().ifPresent(selected -> nbt.putString("SelectedRecipe", this.selectedRecipe.getId().toString()));
        Set<IRecipePair> dataset = this.getRecipesList();
        if (!dataset.isEmpty()) {
            ListTag list = new ListTag();
            for (IRecipePair data : dataset) {
                CompoundTag tag = new CompoundTag();
                tag.put("ItemStack", data.getOutput().save(new CompoundTag()));
                tag.putString("Id", data.getResourceLocation().toString());
                list.add(tag);
            }
            nbt.put("RecipeDataSet", list);
        }
        return nbt;
    }
}