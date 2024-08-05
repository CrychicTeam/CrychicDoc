package com.mna.tools.debugging;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.spells.ComponentRecipe;
import com.mna.recipes.spells.ShapeRecipe;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class SpellBalanceCalculator {

    public static void calculateMinMaxComplexityPerTier() {
        for (int i = 1; i <= 5; i++) {
            List<Shape> tierShapes = getShapesForTier(i);
            List<SpellEffect> tierComponents = getComponentsForTier(i);
            float min = calculateMinComplexity(tierShapes, tierComponents);
            float max = calculateMaxComplexity(tierShapes, tierComponents);
            float avg = calculateAverageDefaultComplexity(tierShapes, tierComponents);
            ManaAndArtifice.LOGGER.debug(String.format("Tier %d min complexity %.2f max complexity %.2f avg baseline complexity %.2f", i, min, max, avg));
        }
    }

    private static float calculateMinComplexity(List<Shape> shapes, List<SpellEffect> components) {
        MutableFloat minComplexity = new MutableFloat(9999999.0F);
        shapes.forEach(s -> components.forEach(c -> {
            SpellRecipe sr = new SpellRecipe();
            sr.setShape(s);
            sr.addComponent(c);
            if (sr.getComplexity() < minComplexity.getValue()) {
                minComplexity.setValue(sr.getComplexity());
            }
        }));
        return minComplexity.getValue();
    }

    private static float calculateMaxComplexity(List<Shape> shapes, List<SpellEffect> components) {
        MutableFloat maxComplexity = new MutableFloat(0.0F);
        shapes.forEach(s -> components.forEach(c -> {
            SpellRecipe sr = new SpellRecipe();
            sr.setShape(s);
            sr.addComponent(c);
            sr.maximize();
            if (sr.getComplexity() > maxComplexity.getValue()) {
                maxComplexity.setValue(sr.getComplexity());
            }
        }));
        return maxComplexity.getValue();
    }

    private static float calculateAverageDefaultComplexity(List<Shape> shapes, List<SpellEffect> components) {
        MutableFloat complexity = new MutableFloat(0.0F);
        MutableInt count = new MutableInt(0);
        shapes.forEach(s -> components.forEach(cx -> {
            SpellRecipe sr = new SpellRecipe();
            sr.setShape(s);
            sr.addComponent(cx);
            complexity.add(sr.getComplexity());
            count.add(1);
        }));
        int c = count.getValue();
        if (c == 0) {
            c = 1;
        }
        return complexity.getValue() / (float) c;
    }

    private static List<Shape> getShapesForTier(int tier) {
        Minecraft mc = Minecraft.getInstance();
        RecipeManager rm = mc.level.getRecipeManager();
        return (List<Shape>) rm.getRecipes().stream().filter(r -> r.getType() == RecipeInit.SHAPE_TYPE.get()).map(r -> (ShapeRecipe) r).filter(r -> r.getTier() <= tier).map(r -> (Shape) ((IForgeRegistry) Registries.Shape.get()).getValue(r.m_6423_())).filter(r -> r != null).collect(Collectors.toList());
    }

    private static List<SpellEffect> getComponentsForTier(int tier) {
        Minecraft mc = Minecraft.getInstance();
        RecipeManager rm = mc.level.getRecipeManager();
        return (List<SpellEffect>) rm.getRecipes().stream().filter(r -> r.getType() == RecipeInit.COMPONENT_TYPE.get()).map(r -> (ComponentRecipe) r).filter(r -> r.getTier() <= tier).map(r -> (SpellEffect) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(r.m_6423_())).filter(r -> r != null).collect(Collectors.toList());
    }
}