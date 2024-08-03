package net.blay09.mods.craftingtweaks.api.impl;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingGridBuilder;
import net.blay09.mods.craftingtweaks.api.CraftingGridDecorator;
import net.minecraft.resources.ResourceLocation;

public class CraftingGridBuilderImpl implements CraftingGridBuilder {

    private final List<CraftingGrid> grids = new ArrayList();

    private String activeModId;

    @Override
    public CraftingGridDecorator addGrid(String name, int start, int size) {
        DefaultCraftingGrid grid = new DefaultCraftingGrid(new ResourceLocation(this.activeModId, name), start, size);
        this.grids.add(grid);
        return grid;
    }

    @Override
    public void addCustomGrid(CraftingGrid grid) {
        this.grids.add(grid);
    }

    public List<CraftingGrid> getGrids() {
        return this.grids;
    }

    public void setActiveModId(String activeModId) {
        this.activeModId = activeModId;
    }
}