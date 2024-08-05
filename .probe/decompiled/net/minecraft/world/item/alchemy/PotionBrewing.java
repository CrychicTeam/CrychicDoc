package net.minecraft.world.item.alchemy;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.Ingredient;

public class PotionBrewing {

    public static final int BREWING_TIME_SECONDS = 20;

    private static final List<PotionBrewing.Mix<Potion>> POTION_MIXES = Lists.newArrayList();

    private static final List<PotionBrewing.Mix<Item>> CONTAINER_MIXES = Lists.newArrayList();

    private static final List<Ingredient> ALLOWED_CONTAINERS = Lists.newArrayList();

    private static final Predicate<ItemStack> ALLOWED_CONTAINER = p_43528_ -> {
        for (Ingredient $$1 : ALLOWED_CONTAINERS) {
            if ($$1.test(p_43528_)) {
                return true;
            }
        }
        return false;
    };

    public static boolean isIngredient(ItemStack itemStack0) {
        return isContainerIngredient(itemStack0) || isPotionIngredient(itemStack0);
    }

    protected static boolean isContainerIngredient(ItemStack itemStack0) {
        int $$1 = 0;
        for (int $$2 = CONTAINER_MIXES.size(); $$1 < $$2; $$1++) {
            if (((PotionBrewing.Mix) CONTAINER_MIXES.get($$1)).ingredient.test(itemStack0)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isPotionIngredient(ItemStack itemStack0) {
        int $$1 = 0;
        for (int $$2 = POTION_MIXES.size(); $$1 < $$2; $$1++) {
            if (((PotionBrewing.Mix) POTION_MIXES.get($$1)).ingredient.test(itemStack0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBrewablePotion(Potion potion0) {
        int $$1 = 0;
        for (int $$2 = POTION_MIXES.size(); $$1 < $$2; $$1++) {
            if (((PotionBrewing.Mix) POTION_MIXES.get($$1)).to == potion0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMix(ItemStack itemStack0, ItemStack itemStack1) {
        return !ALLOWED_CONTAINER.test(itemStack0) ? false : hasContainerMix(itemStack0, itemStack1) || hasPotionMix(itemStack0, itemStack1);
    }

    protected static boolean hasContainerMix(ItemStack itemStack0, ItemStack itemStack1) {
        Item $$2 = itemStack0.getItem();
        int $$3 = 0;
        for (int $$4 = CONTAINER_MIXES.size(); $$3 < $$4; $$3++) {
            PotionBrewing.Mix<Item> $$5 = (PotionBrewing.Mix<Item>) CONTAINER_MIXES.get($$3);
            if ($$5.from == $$2 && $$5.ingredient.test(itemStack1)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean hasPotionMix(ItemStack itemStack0, ItemStack itemStack1) {
        Potion $$2 = PotionUtils.getPotion(itemStack0);
        int $$3 = 0;
        for (int $$4 = POTION_MIXES.size(); $$3 < $$4; $$3++) {
            PotionBrewing.Mix<Potion> $$5 = (PotionBrewing.Mix<Potion>) POTION_MIXES.get($$3);
            if ($$5.from == $$2 && $$5.ingredient.test(itemStack1)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack mix(ItemStack itemStack0, ItemStack itemStack1) {
        if (!itemStack1.isEmpty()) {
            Potion $$2 = PotionUtils.getPotion(itemStack1);
            Item $$3 = itemStack1.getItem();
            int $$4 = 0;
            for (int $$5 = CONTAINER_MIXES.size(); $$4 < $$5; $$4++) {
                PotionBrewing.Mix<Item> $$6 = (PotionBrewing.Mix<Item>) CONTAINER_MIXES.get($$4);
                if ($$6.from == $$3 && $$6.ingredient.test(itemStack0)) {
                    return PotionUtils.setPotion(new ItemStack($$6.to), $$2);
                }
            }
            $$4 = 0;
            for (int $$8 = POTION_MIXES.size(); $$4 < $$8; $$4++) {
                PotionBrewing.Mix<Potion> $$9 = (PotionBrewing.Mix<Potion>) POTION_MIXES.get($$4);
                if ($$9.from == $$2 && $$9.ingredient.test(itemStack0)) {
                    return PotionUtils.setPotion(new ItemStack($$3), $$9.to);
                }
            }
        }
        return itemStack1;
    }

    public static void bootStrap() {
        addContainer(Items.POTION);
        addContainer(Items.SPLASH_POTION);
        addContainer(Items.LINGERING_POTION);
        addContainerRecipe(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
        addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
        addMix(Potions.WATER, Items.GLISTERING_MELON_SLICE, Potions.MUNDANE);
        addMix(Potions.WATER, Items.GHAST_TEAR, Potions.MUNDANE);
        addMix(Potions.WATER, Items.RABBIT_FOOT, Potions.MUNDANE);
        addMix(Potions.WATER, Items.BLAZE_POWDER, Potions.MUNDANE);
        addMix(Potions.WATER, Items.SPIDER_EYE, Potions.MUNDANE);
        addMix(Potions.WATER, Items.SUGAR, Potions.MUNDANE);
        addMix(Potions.WATER, Items.MAGMA_CREAM, Potions.MUNDANE);
        addMix(Potions.WATER, Items.GLOWSTONE_DUST, Potions.THICK);
        addMix(Potions.WATER, Items.REDSTONE, Potions.MUNDANE);
        addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);
        addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        addMix(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
        addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
        addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        addMix(Potions.AWKWARD, Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        addMix(Potions.AWKWARD, Items.RABBIT_FOOT, Potions.LEAPING);
        addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        addMix(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
        addMix(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        addMix(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        addMix(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
        addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
        addMix(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        addMix(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        addMix(Potions.AWKWARD, Items.SUGAR, Potions.SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
        addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
        addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        addMix(Potions.AWKWARD, Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        addMix(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
        addMix(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        addMix(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
        addMix(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        addMix(Potions.AWKWARD, Items.SPIDER_EYE, Potions.POISON);
        addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        addMix(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
        addMix(Potions.AWKWARD, Items.GHAST_TEAR, Potions.REGENERATION);
        addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        addMix(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
        addMix(Potions.AWKWARD, Items.BLAZE_POWDER, Potions.STRENGTH);
        addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        addMix(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
        addMix(Potions.WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
        addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
        addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
    }

    private static void addContainerRecipe(Item item0, Item item1, Item item2) {
        if (!(item0 instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + BuiltInRegistries.ITEM.getKey(item0));
        } else if (!(item2 instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + BuiltInRegistries.ITEM.getKey(item2));
        } else {
            CONTAINER_MIXES.add(new PotionBrewing.Mix<>(item0, Ingredient.of(item1), item2));
        }
    }

    private static void addContainer(Item item0) {
        if (!(item0 instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + BuiltInRegistries.ITEM.getKey(item0));
        } else {
            ALLOWED_CONTAINERS.add(Ingredient.of(item0));
        }
    }

    private static void addMix(Potion potion0, Item item1, Potion potion2) {
        POTION_MIXES.add(new PotionBrewing.Mix<>(potion0, Ingredient.of(item1), potion2));
    }

    static class Mix<T> {

        final T from;

        final Ingredient ingredient;

        final T to;

        public Mix(T t0, Ingredient ingredient1, T t2) {
            this.from = t0;
            this.ingredient = ingredient1;
            this.to = t2;
        }
    }
}