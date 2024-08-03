package com.mna.brewing;

import com.mna.blocks.BlockInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class BrewingInit {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, "mna");

    public static final RegistryObject<Potion> MINOR_MANA_POTION = POTIONS.register("minor_mana_potion", () -> new ManaPotion(0));

    public static final RegistryObject<Potion> LIGHT_MANA_POTION = POTIONS.register("light_mana_potion", () -> new ManaPotion(1));

    public static final RegistryObject<Potion> MANA_POTION = POTIONS.register("mana_potion", () -> new ManaPotion(2));

    public static final RegistryObject<Potion> GREATER_MANA_POTION = POTIONS.register("greater_mana_potion", () -> new ManaPotion(3));

    public static final RegistryObject<Potion> SUPERIOR_MANA_POTION = POTIONS.register("superior_mana_potion", () -> new ManaPotion(4));

    @SubscribeEvent
    public static void initBrewingRecipes(FMLCommonSetupEvent event) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)), Ingredient.of(new ItemStack(BlockInit.CERUBLOSSOM.get())), PotionUtils.setPotion(new ItemStack(Items.POTION), MINOR_MANA_POTION.get())));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)), Ingredient.of(new ItemStack(BlockInit.AUM.get())), PotionUtils.setPotion(new ItemStack(Items.POTION), LIGHT_MANA_POTION.get())));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)), Ingredient.of(new ItemStack(BlockInit.DESERT_NOVA.get())), PotionUtils.setPotion(new ItemStack(Items.POTION), MANA_POTION.get())));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)), Ingredient.of(new ItemStack(BlockInit.WAKEBLOOM.get())), PotionUtils.setPotion(new ItemStack(Items.POTION), GREATER_MANA_POTION.get())));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)), Ingredient.of(new ItemStack(BlockInit.TARMA_ROOT.get())), PotionUtils.setPotion(new ItemStack(Items.POTION), SUPERIOR_MANA_POTION.get())));
    }
}