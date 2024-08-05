package dev.xkmc.l2library.compat.misc;

import dev.xkmc.l2library.init.events.ClientEffectRenderEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib.event.GeoRenderEvent;

public class GeckoLibEventHandlers {

    @SubscribeEvent
    public static void onGeckoRenderPost(GeoRenderEvent.Entity.Post event) {
        if (event.getEntity() instanceof LivingEntity le) {
            ClientEffectRenderEvents.onLivingRenderEvents(le);
        }
    }
}