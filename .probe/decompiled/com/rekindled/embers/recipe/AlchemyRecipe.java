package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.misc.AlchemyResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AlchemyRecipe implements IAlchemyRecipe {

    public static final AlchemyRecipe.Serializer SERIALIZER = new AlchemyRecipe.Serializer();

    public final ResourceLocation id;

    public final Ingredient tablet;

    public final ArrayList<Ingredient> aspects;

    public final ArrayList<Ingredient> inputs;

    public final ItemStack output;

    public final ItemStack failure;

    public Long cachedSeed = null;

    public ArrayList<Ingredient> code = new ArrayList();

    public AlchemyRecipe(ResourceLocation id, Ingredient tablet, ArrayList<Ingredient> aspects, ArrayList<Ingredient> inputs, ItemStack output, ItemStack failure) {
        this.id = id;
        this.tablet = tablet;
        this.aspects = aspects;
        this.inputs = inputs;
        this.output = output;
        this.failure = failure;
    }

    @Override
    public ArrayList<Ingredient> getCode(long seed) {
        if (this.cachedSeed == null || this.cachedSeed != seed) {
            this.code.clear();
            Random rand = new Random(seed - (long) this.id.getPath().hashCode());
            for (int i = 0; i < this.inputs.size(); i++) {
                this.code.add((Ingredient) this.aspects.get(rand.nextInt(this.aspects.size())));
            }
            this.cachedSeed = seed;
        }
        return this.code;
    }

    public boolean matches(AlchemyContext context, Level pLevel) {
        if (this.tablet.test(context.tablet) && this.inputs.size() == context.contents.size()) {
            ArrayList<IAlchemyRecipe.PedestalContents> remaining = new ArrayList(context.contents);
            for (int i = 0; i < this.inputs.size(); i++) {
                boolean matched = false;
                for (int j = 0; j < remaining.size(); j++) {
                    if (((Ingredient) this.inputs.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(j)).input)) {
                        matched = true;
                        remaining.remove(j);
                        break;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean matchesCorrect(AlchemyContext context, Level pLevel) {
        this.getCode(context.seed);
        if (this.tablet.test(context.tablet) && this.code.size() == context.contents.size()) {
            ArrayList<IAlchemyRecipe.PedestalContents> remaining = new ArrayList(context.contents);
            for (int i = 0; i < this.inputs.size(); i++) {
                boolean matched = false;
                for (int j = 0; j < remaining.size(); j++) {
                    if (((Ingredient) this.code.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(j)).aspect) && ((Ingredient) this.inputs.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(j)).input)) {
                        matched = true;
                        remaining.remove(j);
                        break;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(AlchemyContext context, RegistryAccess registry) {
        this.getCode(context.seed);
        int blackPins = 0;
        int whitePins = 0;
        ArrayList<Ingredient> remainingCode = new ArrayList(this.code);
        for (int i = 0; i < context.contents.size(); i++) {
            for (int j = 0; j < remainingCode.size(); j++) {
                if (((Ingredient) remainingCode.get(j)).test(((IAlchemyRecipe.PedestalContents) context.contents.get(i)).aspect)) {
                    whitePins++;
                    remainingCode.remove(j);
                    break;
                }
            }
        }
        ArrayList<IAlchemyRecipe.PedestalContents> remaining = new ArrayList(context.contents);
        for (int i = 0; i < this.inputs.size(); i++) {
            for (int jx = 0; jx < remaining.size(); jx++) {
                if (((Ingredient) this.code.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(jx)).aspect) && ((Ingredient) this.inputs.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(jx)).input)) {
                    blackPins++;
                    remaining.remove(jx);
                    break;
                }
            }
        }
        whitePins -= blackPins;
        if (blackPins >= this.code.size()) {
            return this.output;
        } else {
            ItemStack waste = this.failure.copy();
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("blackPins", blackPins);
            nbt.putInt("whitePins", whitePins);
            ListTag aspectNBT = new ListTag();
            ListTag inputNBT = new ListTag();
            for (IAlchemyRecipe.PedestalContents contents : context.contents) {
                aspectNBT.add(contents.aspect.serializeNBT());
                inputNBT.add(contents.input.serializeNBT());
            }
            nbt.put("aspects", aspectNBT);
            nbt.put("inputs", inputNBT);
            waste.setTag(nbt);
            return waste;
        }
    }

    @Override
    public AlchemyResult getResult(AlchemyContext context) {
        this.getCode(context.seed);
        int blackPins = 0;
        int whitePins = 0;
        ArrayList<Ingredient> remainingCode = new ArrayList(this.code);
        for (int i = 0; i < context.contents.size(); i++) {
            for (int j = 0; j < remainingCode.size(); j++) {
                if (((Ingredient) remainingCode.get(j)).test(((IAlchemyRecipe.PedestalContents) context.contents.get(i)).aspect)) {
                    whitePins++;
                    remainingCode.remove(j);
                    break;
                }
            }
        }
        ArrayList<IAlchemyRecipe.PedestalContents> remaining = new ArrayList(context.contents);
        for (int i = 0; i < this.inputs.size(); i++) {
            for (int jx = 0; jx < remaining.size(); jx++) {
                if (((Ingredient) this.code.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(jx)).aspect) && ((Ingredient) this.inputs.get(i)).test(((IAlchemyRecipe.PedestalContents) remaining.get(jx)).input)) {
                    blackPins++;
                    remaining.remove(jx);
                    break;
                }
            }
        }
        whitePins -= blackPins;
        List<IAlchemyRecipe.PedestalContents> contents = new ArrayList(context.contents);
        List<IAlchemyRecipe.PedestalContents> sortedContents = new ArrayList();
        for (Ingredient input : this.inputs) {
            for (IAlchemyRecipe.PedestalContents pedestal : contents) {
                if (input.test(pedestal.input)) {
                    sortedContents.add(pedestal);
                    contents.remove(pedestal);
                    break;
                }
            }
        }
        return new AlchemyResult(sortedContents, this.getResultItem(), blackPins, whitePins);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Ingredient getCenterInput() {
        return this.tablet;
    }

    @Override
    public List<Ingredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<Ingredient> getAspects() {
        return this.aspects;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ItemStack getfailureItem() {
        return this.failure;
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe> {

        public AlchemyRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient tablet = Ingredient.fromJson(json.get("tablet"));
            ArrayList<Ingredient> inputs = new ArrayList();
            JsonArray inputJson = GsonHelper.getAsJsonArray(json, "inputs", null);
            if (inputJson != null) {
                for (JsonElement element : inputJson) {
                    inputs.add(Ingredient.fromJson(element));
                }
            }
            ArrayList<Ingredient> aspects = new ArrayList();
            JsonArray aspectJson = GsonHelper.getAsJsonArray(json, "aspects", null);
            if (aspectJson != null) {
                for (JsonElement element : aspectJson) {
                    aspects.add(Ingredient.fromJson(element));
                }
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            ItemStack failure;
            if (json.has("failure")) {
                failure = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "failure"));
            } else {
                failure = new ItemStack(RegistryManager.ALCHEMICAL_WASTE.get());
            }
            return new AlchemyRecipe(recipeId, tablet, aspects, inputs, output, failure);
        }

        @Nullable
        public AlchemyRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient tablet = Ingredient.fromNetwork(buffer);
            ArrayList<Ingredient> aspects = buffer.readCollection(i -> new ArrayList(), buf -> Ingredient.fromNetwork(buf));
            ArrayList<Ingredient> inputs = buffer.readCollection(i -> new ArrayList(), buf -> Ingredient.fromNetwork(buf));
            ItemStack output = buffer.readItem();
            ItemStack failure = buffer.readItem();
            return new AlchemyRecipe(recipeId, tablet, aspects, inputs, output, failure);
        }

        public void toNetwork(FriendlyByteBuf buffer, AlchemyRecipe recipe) {
            recipe.tablet.toNetwork(buffer);
            buffer.writeCollection(recipe.aspects, (buf, input) -> input.toNetwork(buf));
            buffer.writeCollection(recipe.inputs, (buf, input) -> input.toNetwork(buf));
            buffer.writeItemStack(recipe.output, false);
            buffer.writeItemStack(recipe.failure, false);
        }
    }
}