package dev.xkmc.l2library.init;

import dev.xkmc.l2library.compat.misc.GeckoLibEventHandlers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2library", bus = Bus.MOD)
public class L2LibraryClient {

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("geckolib")) {
            MinecraftForge.EVENT_BUS.register(GeckoLibEventHandlers.class);
        }
    }
}