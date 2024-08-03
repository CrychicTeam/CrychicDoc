package org.violetmoon.zeta.registry;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.recipe.FlagIngredient;

public abstract class BrewingRegistry {

    protected final Zeta zeta;

    protected final Map<Potion, String> flaggedPotions = Maps.newHashMap();

    public BrewingRegistry(Zeta zeta) {
        this.zeta = zeta;
    }

    public void addPotionMix(String flag, Supplier<Ingredient> reagent, MobEffect effect) {
        this.addPotionMix(flag, reagent, effect, null);
    }

    public void addPotionMix(String flag, Supplier<Ingredient> reagent, MobEffect effect, int normalTime, int longTime, int strongTime) {
        this.addPotionMix(flag, reagent, effect, null, normalTime, longTime, strongTime);
    }

    public void addPotionMix(String flag, Supplier<Ingredient> reagent, MobEffect effect, @Nullable MobEffect negation) {
        this.addPotionMix(flag, reagent, effect, negation, 3600, 9600, 1800);
    }

    public void addPotionMix(String flag, Supplier<Ingredient> reagent, MobEffect effect, @Nullable MobEffect negation, int normalTime, int longTime, int strongTime) {
        ResourceLocation loc = this.zeta.registry.getRegistryName(effect, BuiltInRegistries.MOB_EFFECT);
        if (loc != null) {
            String baseName = loc.getPath();
            boolean hasStrong = strongTime > 0;
            Potion normalType = this.registerPotion(new MobEffectInstance(effect, normalTime), baseName, baseName);
            Potion longType = this.registerPotion(new MobEffectInstance(effect, longTime), baseName, "long_" + baseName);
            Potion strongType = !hasStrong ? null : this.registerPotion(new MobEffectInstance(effect, strongTime, 1), baseName, "strong_" + baseName);
            this.addPotionMix(flag, reagent, normalType, longType, strongType);
            if (negation != null) {
                ResourceLocation negationLoc = this.zeta.registry.getRegistryName(negation, BuiltInRegistries.MOB_EFFECT);
                if (negationLoc != null) {
                    String negationBaseName = negationLoc.getPath();
                    Potion normalNegationType = this.registerPotion(new MobEffectInstance(negation, normalTime), negationBaseName, negationBaseName);
                    Potion longNegationType = this.registerPotion(new MobEffectInstance(negation, longTime), negationBaseName, "long_" + negationBaseName);
                    Potion strongNegationType = !hasStrong ? null : this.registerPotion(new MobEffectInstance(negation, strongTime, 1), negationBaseName, "strong_" + negationBaseName);
                    this.addNegation(flag, normalType, longType, strongType, normalNegationType, longNegationType, strongNegationType);
                }
            }
        }
    }

    public void addPotionMix(String flag, Supplier<Ingredient> reagent, Potion normalType, Potion longType, @Nullable Potion strongType) {
        boolean hasStrong = strongType != null;
        this.addFlaggedRecipe(flag, Potions.AWKWARD, reagent, normalType);
        this.addFlaggedRecipe(flag, Potions.WATER, reagent, Potions.MUNDANE);
        if (hasStrong) {
            this.addFlaggedRecipe(flag, normalType, BrewingRegistry::glowstone, strongType);
        }
        this.addFlaggedRecipe(flag, normalType, BrewingRegistry::redstone, longType);
    }

    public void addNegation(String flag, Potion normalType, Potion longType, @Nullable Potion strongType, Potion normalNegatedType, Potion longNegatedType, @Nullable Potion strongNegatedType) {
        this.addFlaggedRecipe(flag, normalType, BrewingRegistry::spiderEye, normalNegatedType);
        boolean hasStrong = strongType != null && strongNegatedType != null;
        if (hasStrong) {
            this.addFlaggedRecipe(flag, strongType, BrewingRegistry::spiderEye, strongNegatedType);
            this.addFlaggedRecipe(flag, normalNegatedType, BrewingRegistry::glowstone, strongNegatedType);
        }
        this.addFlaggedRecipe(flag, longType, BrewingRegistry::spiderEye, longNegatedType);
        this.addFlaggedRecipe(flag, normalNegatedType, BrewingRegistry::redstone, longNegatedType);
    }

    protected void addFlaggedRecipe(String flag, Potion potion, Supplier<Ingredient> reagent, Potion to) {
        this.flaggedPotions.put(to, flag);
        Supplier<Ingredient> flagIngredientSupplier = () -> new FlagIngredient((Ingredient) reagent.get(), flag, this.zeta.configManager.getConfigFlagManager(), this.zeta.configManager.getConfigFlagManager().flagIngredientSerializer);
        this.addBrewingRecipe(potion, flagIngredientSupplier, to);
    }

    protected Potion registerPotion(MobEffectInstance eff, String baseName, String name) {
        Potion effect = new Potion(this.zeta.modid + "." + baseName, eff);
        this.zeta.registry.register(effect, name, Registries.POTION);
        return effect;
    }

    public boolean isEnabled(Potion potion) {
        return !this.flaggedPotions.containsKey(potion) ? true : this.zeta.configManager.getConfigFlagManager().getFlag((String) this.flaggedPotions.get(potion));
    }

    public static Ingredient redstone() {
        return Ingredient.of(Items.REDSTONE);
    }

    public static Ingredient glowstone() {
        return Ingredient.of(Items.GLOWSTONE_DUST);
    }

    public static Ingredient spiderEye() {
        return Ingredient.of(Items.FERMENTED_SPIDER_EYE);
    }

    protected abstract void addBrewingRecipe(Potion var1, Supplier<Ingredient> var2, Potion var3);
}