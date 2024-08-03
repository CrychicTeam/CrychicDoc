package me.srrapero720.embeddiumplus.foundation.nametag;

import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "embeddiumplus", bus = Bus.FORGE, value = { Dist.CLIENT })
public class NameTagToggle {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(RenderNameTagEvent e) {
        if (EmbyConfig.disableNameTagRenderCache) {
            e.setResult(Result.DENY);
        }
    }
}