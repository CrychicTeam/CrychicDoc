package com.mna.interop.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientType;

public class ManaweavePatternIngredientType implements IIngredientType<ManaweavePatternIngredient> {

    @Override
    public Class<? extends ManaweavePatternIngredient> getIngredientClass() {
        return ManaweavePatternIngredient.class;
    }
}