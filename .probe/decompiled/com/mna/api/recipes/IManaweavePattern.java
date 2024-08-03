package com.mna.api.recipes;

public interface IManaweavePattern extends IMARecipe {

    byte[][] get();

    IManaweavePattern copy();
}