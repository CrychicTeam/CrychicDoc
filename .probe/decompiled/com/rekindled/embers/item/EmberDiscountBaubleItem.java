package com.rekindled.embers.item;

import com.rekindled.embers.api.event.EmberRemoveEvent;
import com.rekindled.embers.compat.curios.CuriosCompat;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EmberDiscountBaubleItem extends Item implements IEmbersCurioItem {

    public double reduction;

    public EmberDiscountBaubleItem(Item.Properties properties, double reduction) {
        super(properties);
        this.reduction = reduction;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTake(EmberRemoveEvent event) {
        CuriosCompat.checkForCurios(event.getPlayer(), stack -> {
            if (stack.getItem() == this) {
                event.addReduction(this.reduction);
            }
            return false;
        });
    }
}