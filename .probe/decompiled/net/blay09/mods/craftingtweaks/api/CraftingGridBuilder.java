package net.blay09.mods.craftingtweaks.api;

public interface CraftingGridBuilder {

    default CraftingGridDecorator addGrid(int start, int size) {
        return this.addGrid("default", start, size);
    }

    CraftingGridDecorator addGrid(String var1, int var2, int var3);

    void addCustomGrid(CraftingGrid var1);
}