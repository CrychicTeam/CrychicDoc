package com.rekindled.embers.upgrade;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.IUpgradeProvider;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.CatalyticPlugBlockEntity;
import com.rekindled.embers.recipe.FluidHandlerContext;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CatalyticPlugUpgrade extends DefaultUpgradeProvider {

    private static HashSet<Class<? extends BlockEntity>> blacklist = new HashSet();

    public static void registerBlacklistedTile(Class<? extends BlockEntity> tile) {
        blacklist.add(tile);
    }

    public CatalyticPlugUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "catalytic_plug"), tile);
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
        return ember * getMultiplier(this.getCatalystMultiplier(), distance, count);
    }

    @Override
    public double getSpeed(BlockEntity tile, double speed, int distance, int count) {
        return speed * getMultiplier(this.getCatalystMultiplier(), distance, count);
    }

    @Override
    public boolean doWork(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        if (this.getCatalystMultiplier() != 1.0 && this.tile instanceof CatalyticPlugBlockEntity) {
            this.depleteCatalyst(1);
            ((CatalyticPlugBlockEntity) this.tile).setActive(20);
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
        if (this.tile instanceof CatalyticPlugBlockEntity plug) {
            plug.burnTime -= amt;
            if (plug.burnTime < 0) {
                FluidHandlerContext context = new FluidHandlerContext(plug.tank);
                plug.cachedRecipe = Misc.getRecipe(plug.cachedRecipe, RegistryManager.GASEOUS_FUEL.get(), context, plug.m_58904_());
                while (plug.burnTime < 0 && plug.cachedRecipe != null && plug.cachedRecipe.m_5818_(context, plug.m_58904_())) {
                    plug.burnTime = plug.burnTime + plug.cachedRecipe.process(context, 1);
                }
                if (plug.burnTime < 0) {
                    plug.burnTime = 0;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof DialInformationEvent dialEvent && "ember".equals(dialEvent.getDialType())) {
            double multiplier = 1.0;
            boolean first = true;
            for (UpgradeContext upgrade : upgrades) {
                IUpgradeProvider var13 = upgrade.upgrade();
                if (var13 instanceof CatalyticPlugUpgrade) {
                    CatalyticPlugUpgrade plug = (CatalyticPlugUpgrade) var13;
                    if (first) {
                        if (plug != this) {
                            return;
                        }
                        first = false;
                    }
                    multiplier = plug.getSpeed(tile, multiplier, upgrade.distance(), upgrade.count());
                }
            }
            DecimalFormat multiplierFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.speed_multiplier");
            dialEvent.getInformation().add(Component.translatable("embers.tooltip.upgrade.catalytic_plug", multiplierFormat.format(multiplier)));
        }
    }
}