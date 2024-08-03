package net.blay09.mods.craftingtweaks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.impl.CraftingGridBuilderImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CraftingTweaksProviderManager {

    private static final List<CraftingGridProvider> craftingGridProviders = new ArrayList();

    public static void registerProvider(CraftingGridProvider provider) {
        if (provider.getModId().equals("minecraft") || Balm.isModLoaded(provider.getModId())) {
            provider.onInitialize();
            craftingGridProviders.add(provider);
        }
    }

    public static List<CraftingGrid> getCraftingGrids(AbstractContainerMenu menu) {
        CraftingGridBuilderImpl builder = new CraftingGridBuilderImpl();
        for (CraftingGridProvider provider : craftingGridProviders) {
            if (provider.handles(menu)) {
                builder.setActiveModId(provider.getModId());
                if (!provider.requiresServerSide() || CraftingTweaks.isServerSideInstalled) {
                    provider.buildCraftingGrids(builder, menu);
                }
            }
        }
        return builder.getGrids();
    }

    public static Optional<CraftingGrid> getDefaultCraftingGrid(AbstractContainerMenu menu) {
        List<CraftingGrid> grids = getCraftingGrids(menu);
        return grids.isEmpty() ? Optional.empty() : Optional.of((CraftingGrid) grids.stream().filter(it -> it.getId().getPath().equals("default")).findFirst().orElse((CraftingGrid) grids.get(0)));
    }

    public static Optional<CraftingGrid> getCraftingGrid(AbstractContainerMenu menu, ResourceLocation gridId) {
        return getCraftingGrids(menu).stream().filter(it -> gridId.equals(it.getId())).findFirst();
    }

    public static void unregisterProvider(CraftingGridProvider provider) {
        craftingGridProviders.remove(provider);
    }
}