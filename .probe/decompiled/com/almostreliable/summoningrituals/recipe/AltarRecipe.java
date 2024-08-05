package com.almostreliable.summoningrituals.recipe;

import com.almostreliable.summoningrituals.Registration;
import com.almostreliable.summoningrituals.inventory.VanillaWrapper;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.IngredientStack;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import com.almostreliable.summoningrituals.util.GameUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AltarRecipe implements Recipe<VanillaWrapper> {

    public static final Set<Ingredient> CATALYST_CACHE = new HashSet();

    private final ResourceLocation id;

    private final Ingredient catalyst;

    private final RecipeOutputs outputs;

    private final NonNullList<IngredientStack> inputs;

    private final RecipeSacrifices sacrifices;

    private final int recipeTime;

    @Nullable
    private final BlockReference blockBelow;

    private final AltarRecipe.DAY_TIME dayTime;

    private final AltarRecipe.WEATHER weather;

    AltarRecipe(ResourceLocation id, Ingredient catalyst, RecipeOutputs outputs, NonNullList<IngredientStack> inputs, RecipeSacrifices sacrifices, int recipeTime, @Nullable BlockReference blockBelow, AltarRecipe.DAY_TIME dayTime, AltarRecipe.WEATHER weather) {
        this.id = id;
        this.outputs = outputs;
        this.inputs = inputs;
        this.catalyst = catalyst;
        this.sacrifices = sacrifices;
        this.recipeTime = recipeTime;
        this.blockBelow = blockBelow;
        this.dayTime = dayTime;
        this.weather = weather;
    }

    public boolean matches(VanillaWrapper inv, Level level) {
        if (!inv.getCatalyst().isEmpty() && this.catalyst.test(inv.getCatalyst())) {
            Ingredient[] matchedItems = new Ingredient[inv.m_6643_()];
            List<Ingredient> matchedIngredients = new ArrayList();
            for (int slot = 0; slot < inv.getItems().size(); slot++) {
                ItemStack stack = (ItemStack) inv.getItems().get(slot);
                if (!stack.isEmpty() && matchedItems[slot] == null) {
                    for (IngredientStack input : this.inputs) {
                        if (!matchedIngredients.contains(input.ingredient()) && input.ingredient().test(stack) && stack.getCount() >= input.count()) {
                            matchedItems[slot] = input.ingredient();
                            matchedIngredients.add(input.ingredient());
                        }
                    }
                }
            }
            return matchedIngredients.size() == this.inputs.size();
        } else {
            return false;
        }
    }

    public ItemStack assemble(VanillaWrapper inv, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer<?>) Registration.ALTAR_RECIPE.serializer().get();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.ALTAR_RECIPE.type().get();
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public RecipeOutputs getOutputs() {
        return this.outputs;
    }

    public NonNullList<IngredientStack> getInputs() {
        return this.inputs;
    }

    public RecipeSacrifices getSacrifices() {
        return this.sacrifices;
    }

    public int getRecipeTime() {
        return this.recipeTime;
    }

    @Nullable
    public BlockReference getBlockBelow() {
        return this.blockBelow;
    }

    public AltarRecipe.DAY_TIME getDayTime() {
        return this.dayTime;
    }

    public AltarRecipe.WEATHER getWeather() {
        return this.weather;
    }

    public static enum DAY_TIME {

        ANY, DAY, NIGHT;

        public boolean check(Level level, @Nullable ServerPlayer player) {
            boolean check = switch(this) {
                case ANY ->
                    true;
                case DAY ->
                    level.isDay();
                case NIGHT ->
                    level.isNight();
            };
            if (!check) {
                GameUtils.sendPlayerMessage(player, this.toString().toLowerCase(), ChatFormatting.YELLOW);
            }
            return check;
        }
    }

    public static enum WEATHER {

        ANY, CLEAR, RAIN, THUNDER;

        public boolean check(Level level, @Nullable ServerPlayer player) {
            boolean check = switch(this) {
                case ANY ->
                    true;
                case CLEAR ->
                    !level.isRaining() && !level.isThundering();
                case RAIN ->
                    level.isRaining();
                case THUNDER ->
                    level.isThundering();
            };
            if (!check) {
                GameUtils.sendPlayerMessage(player, this.toString().toLowerCase(), ChatFormatting.YELLOW);
            }
            return check;
        }
    }
}