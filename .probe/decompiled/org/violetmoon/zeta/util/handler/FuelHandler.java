package org.violetmoon.zeta.util.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZLoadComplete;
import org.violetmoon.zeta.event.play.ZFurnaceFuelBurnTime;
import org.violetmoon.zeta.mod.ZetaMod;
import org.violetmoon.zeta.util.BlockUtils;

public class FuelHandler {

    private final Map<Item, Integer> fuelValues = new HashMap();

    private final Zeta zeta;

    public FuelHandler(Zeta zeta) {
        this.zeta = zeta;
    }

    public void addFuel(Item item, int fuel) {
        if (fuel > 0 && item != null && !this.fuelValues.containsKey(item)) {
            this.fuelValues.put(item, fuel);
        }
    }

    public void addFuel(Block block, int fuel) {
        this.addFuel(block.asItem(), fuel);
    }

    public void addWood(Block block) {
        String regname = Objects.toString(ZetaMod.ZETA.registry.getRegistryName(block, BuiltInRegistries.BLOCK));
        if (!regname.contains("crimson") && !regname.contains("warped")) {
            if (block instanceof FuelHandler.ICustomWoodFuelValue fuelBlock) {
                this.addFuel(block, fuelBlock.getBurnTimeInTicksWhenWooden());
            } else if (block instanceof SlabBlock) {
                this.addFuel(block, 150);
            } else {
                this.addFuel(block, 300);
            }
        }
    }

    @LoadEvent
    public void addAllWoods(ZLoadComplete event) {
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation regname = this.zeta.registry.getRegistryName(block, BuiltInRegistries.BLOCK);
            if (block != null && regname.getNamespace().equals(this.zeta.modid) && BlockUtils.isWoodBased(block.defaultBlockState())) {
                this.addWood(block);
            }
        }
    }

    @PlayEvent
    public void getFuel(ZFurnaceFuelBurnTime event) {
        Item item = event.getItemStack().getItem();
        if (this.fuelValues.containsKey(item)) {
            event.setBurnTime((Integer) this.fuelValues.get(item));
        }
    }

    public interface ICustomWoodFuelValue {

        int getBurnTimeInTicksWhenWooden();
    }
}