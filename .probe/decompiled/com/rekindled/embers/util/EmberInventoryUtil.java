package com.rekindled.embers.util;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.EmberRemoveEvent;
import com.rekindled.embers.api.item.IHeldEmberCell;
import com.rekindled.embers.api.item.IInventoryEmberCell;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.compat.curios.CuriosCompat;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class EmberInventoryUtil {

    public static double getEmberCapacityTotal(Player player) {
        double amount = 0.0;
        for (int i = 0; i < 36; i++) {
            IEmberCapability capability = (IEmberCapability) player.getInventory().getItem(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
            if (capability != null && capability instanceof IInventoryEmberCell) {
                amount += capability.getEmberCapacity();
            }
        }
        IEmberCapability capabilityOffhand = (IEmberCapability) player.m_21206_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityOffhand != null && capabilityOffhand instanceof IHeldEmberCell) {
            amount += capabilityOffhand.getEmberCapacity();
        }
        IEmberCapability capabilityMainHand = (IEmberCapability) player.m_21205_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityMainHand != null && capabilityMainHand instanceof IHeldEmberCell) {
            amount += capabilityMainHand.getEmberCapacity();
        }
        if (ModList.get().isLoaded("curios")) {
            amount += CuriosCompat.getEmberCapacityTotal(player);
        }
        return amount;
    }

    public static double getEmberTotal(Player player) {
        double amount = 0.0;
        for (int i = 0; i < 36; i++) {
            IEmberCapability capability = (IEmberCapability) player.getInventory().getItem(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
            if (capability != null && capability instanceof IInventoryEmberCell) {
                amount += capability.getEmber();
            }
        }
        IEmberCapability capabilityOffhand = (IEmberCapability) player.m_21206_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityOffhand != null && capabilityOffhand instanceof IHeldEmberCell) {
            amount += capabilityOffhand.getEmber();
        }
        IEmberCapability capabilityMainHand = (IEmberCapability) player.m_21205_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityMainHand != null && capabilityMainHand instanceof IHeldEmberCell) {
            amount += capabilityMainHand.getEmber();
        }
        if (ModList.get().isLoaded("curios")) {
            amount += CuriosCompat.getEmberTotal(player);
        }
        return amount;
    }

    public static void removeEmber(Player player, double amount) {
        EmberRemoveEvent event = new EmberRemoveEvent(player, amount);
        MinecraftForge.EVENT_BUS.post(event);
        double temp = event.getFinal();
        IEmberCapability capabilityOffhand = (IEmberCapability) player.m_21206_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityOffhand != null && capabilityOffhand instanceof IHeldEmberCell) {
            temp -= capabilityOffhand.removeAmount(temp, true);
            if (temp <= 0.0) {
                return;
            }
        }
        IEmberCapability capabilityMainHand = (IEmberCapability) player.m_21205_().getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
        if (capabilityMainHand != null && capabilityMainHand instanceof IHeldEmberCell) {
            temp -= capabilityMainHand.removeAmount(temp, true);
            if (temp <= 0.0) {
                return;
            }
        }
        if (ModList.get().isLoaded("curios")) {
            temp = CuriosCompat.removeEmber(player, temp);
            if (temp <= 0.0) {
                return;
            }
        }
        for (int i = 0; i < 36; i++) {
            IEmberCapability capability = (IEmberCapability) player.getInventory().getItem(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
            if (capability != null && capability instanceof IInventoryEmberCell) {
                temp -= capability.removeAmount(temp, true);
                if (temp <= 0.0) {
                    return;
                }
            }
        }
    }
}