package org.embeddedt.embeddium.compat.ccl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "embeddium", value = { Dist.CLIENT }, bus = Bus.MOD)
public class CCLCompatBootstrap {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("codechickenlib")) {
            CCLCompat.onClientSetup(event);
        }
    }
}