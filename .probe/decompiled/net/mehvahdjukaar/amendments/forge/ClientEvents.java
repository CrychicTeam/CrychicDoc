package net.mehvahdjukaar.amendments.forge;

import net.mehvahdjukaar.amendments.AmendmentsClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        AmendmentsClient.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
    }

    @SubscribeEvent
    public static void e(InputEvent.Key event) {
        if (PlatHelper.isDev()) {
            float f = 0.01F;
            if (event.getKey() == 74) {
                AmendmentsClient.x = AmendmentsClient.x + (event.getModifiers() == 1 ? f : -f);
            }
            if (event.getKey() == 75) {
                AmendmentsClient.y = AmendmentsClient.y + (event.getModifiers() == 1 ? f : -f);
            }
            if (event.getKey() == 76) {
                AmendmentsClient.z = AmendmentsClient.z + (event.getModifiers() == 1 ? f : -f);
            }
        }
    }
}