package com.mna.api.recipes;

public interface IRunescribeRecipe extends IMARecipe {

    long getHMutex();

    long getVMutex();
}