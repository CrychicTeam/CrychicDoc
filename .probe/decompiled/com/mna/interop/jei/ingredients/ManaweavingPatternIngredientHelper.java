package com.mna.interop.jei.ingredients;

import com.mna.interop.jei.JEIInterop;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

public class ManaweavingPatternIngredientHelper implements IIngredientHelper<ManaweavePatternIngredient> {

    public String getDisplayName(ManaweavePatternIngredient ingredient) {
        return I18n.get(ingredient.getWeaveId().toString());
    }

    public String getUniqueId(ManaweavePatternIngredient ingredient, UidContext context) {
        return ingredient.getWeaveId().toString();
    }

    public ManaweavePatternIngredient copyIngredient(ManaweavePatternIngredient ingredient) {
        return ingredient.copy();
    }

    public String getErrorInfo(ManaweavePatternIngredient ingredient) {
        return "";
    }

    @Override
    public IIngredientType<ManaweavePatternIngredient> getIngredientType() {
        return JEIInterop.MANAWEAVE_PATTERN;
    }

    public ResourceLocation getResourceLocation(ManaweavePatternIngredient ingredient) {
        return ingredient.getWeaveId();
    }
}