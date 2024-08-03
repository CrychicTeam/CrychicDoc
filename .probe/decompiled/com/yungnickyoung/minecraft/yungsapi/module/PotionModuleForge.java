package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterPotion;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class PotionModuleForge {

    public static final List<IBrewingRecipe> BREWING_RECIPES = new ArrayList();

    public static void processEntries() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PotionModuleForge::registerPotions);
    }

    private static void registerPotions(RegisterEvent event) {
        event.register(Registries.POTION, helper -> AutoRegistrationManager.POTIONS.stream().filter(data -> !data.processed()).forEach(data -> registerPotion(data, helper)));
    }

    private static void registerPotion(AutoRegisterField data, RegisterEvent.RegisterHelper<Potion> helper) {
        AutoRegisterPotion autoRegisterPotion = (AutoRegisterPotion) data.object();
        MobEffectInstance mobEffectInstance = autoRegisterPotion.getMobEffectInstance();
        String name = data.name().getNamespace() + "." + data.name().getPath();
        Potion potion = new Potion(name, mobEffectInstance);
        autoRegisterPotion.setSupplier(() -> potion);
        helper.register(data.name(), potion);
        data.markProcessed();
    }

    public static void registerBrewingRecipes() {
        BREWING_RECIPES.forEach(BrewingRecipeRegistry::addRecipe);
    }

    public static record BrewingRecipe(Supplier<Potion> input, Supplier<Item> ingredient, Supplier<Potion> output) implements IBrewingRecipe {

        @Override
        public boolean isInput(ItemStack input) {
            return PotionUtils.getPotion(input) == this.input.get();
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return ingredient.getItem() == this.ingredient.get();
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            if (this.isInput(input) && this.isIngredient(ingredient)) {
                ItemStack itemStack = new ItemStack(input.getItem());
                itemStack.setTag(new CompoundTag());
                PotionUtils.setPotion(itemStack, (Potion) this.output.get());
                return itemStack;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }
}