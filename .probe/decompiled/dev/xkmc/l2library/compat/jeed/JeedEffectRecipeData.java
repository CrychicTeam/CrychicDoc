package dev.xkmc.l2library.compat.jeed;

import java.util.ArrayList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.crafting.Ingredient;

public record JeedEffectRecipeData(MobEffect effect, ArrayList<Ingredient> providers) {
}