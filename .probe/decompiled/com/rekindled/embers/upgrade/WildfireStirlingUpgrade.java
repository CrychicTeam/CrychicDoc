package com.rekindled.embers.upgrade;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.CatalyticPlugBlockEntity;
import com.rekindled.embers.blockentity.WildfireStirlingBlockEntity;
import com.rekindled.embers.recipe.FluidHandlerContext;
import com.rekindled.embers.util.Misc;
import java.util.HashSet;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WildfireStirlingUpgrade extends DefaultUpgradeProvider {

    private static HashSet<Class<? extends BlockEntity>> blacklist = new HashSet();

    public static void registerBlacklistedTile(Class<? extends BlockEntity> tile) {
        blacklist.add(tile);
    }

    public WildfireStirlingUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "wildfire_stirling"), tile);
    }

    public static double getMultiplier(double multiplier, int distance, int count) {
        if (distance > 1) {
            multiplier = 1.0 + (multiplier - 1.0) / ((double) distance * 0.5);
        }
        if (count > 2) {
            multiplier = 1.0 + (multiplier - 1.0) / ((double) count * 0.4);
        }
        return multiplier;
    }

    @Override
    public int getLimit(BlockEntity tile) {
        return blacklist.contains(tile.getClass()) ? 0 : super.getLimit(tile);
    }

    @Override
    public double transformEmberConsumption(BlockEntity tile, double ember, int distance, int count) {
        return ember / getMultiplier(this.getCatalystMultiplier(), distance, count);
    }

    @Override
    public boolean doWork(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        if (this.getCatalystMultiplier() != 1.0 && this.tile instanceof WildfireStirlingBlockEntity) {
            this.depleteCatalyst(1);
            ((WildfireStirlingBlockEntity) this.tile).setActive(20);
        }
        return false;
    }

    private double getCatalystMultiplier() {
        if (!(this.tile instanceof CatalyticPlugBlockEntity plug)) {
            return 1.0;
        } else {
            FluidHandlerContext context = new FluidHandlerContext(plug.tank);
            if (plug.burnTime <= 0 || plug.cachedRecipe == null) {
                plug.cachedRecipe = Misc.getRecipe(plug.cachedRecipe, RegistryManager.GASEOUS_FUEL.get(), context, plug.m_58904_());
            }
            return plug.cachedRecipe == null ? 1.0 : plug.cachedRecipe.getPowerMultiplier(context);
        }
    }

    private void depleteCatalyst(int amt) {
        if (this.tile instanceof WildfireStirlingBlockEntity stirling) {
            stirling.burnTime -= amt;
            if (stirling.burnTime < 0) {
                FluidHandlerContext context = new FluidHandlerContext(stirling.tank);
                stirling.cachedRecipe = Misc.getRecipe(stirling.cachedRecipe, RegistryManager.GASEOUS_FUEL.get(), context, stirling.m_58904_());
                while (stirling.burnTime < 0 && stirling.cachedRecipe != null && stirling.cachedRecipe.m_5818_(context, stirling.m_58904_())) {
                    stirling.burnTime = stirling.burnTime + stirling.cachedRecipe.process(context, 1);
                }
                if (stirling.burnTime < 0) {
                    stirling.burnTime = 0;
                }
            }
        }
    }
}