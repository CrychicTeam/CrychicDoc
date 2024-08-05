package fr.frinn.custommachinery.impl.integration.jei;

import mezz.jei.api.ingredients.IIngredientType;

public class CustomIngredientTypes {

    public static final IIngredientType<Energy> ENERGY = () -> Energy.class;

    public static final IIngredientType<Experience> EXPERIENCE = () -> Experience.class;
}