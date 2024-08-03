package net.blay09.mods.craftingtweaks.api;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface CraftingGridProvider {

    String getModId();

    boolean handles(AbstractContainerMenu var1);

    void buildCraftingGrids(CraftingGridBuilder var1, AbstractContainerMenu var2);

    default void onInitialize() {
    }

    default boolean requiresServerSide() {
        return false;
    }
}