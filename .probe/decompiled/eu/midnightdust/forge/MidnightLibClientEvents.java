package eu.midnightdust.forge;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "midnightlib", bus = Bus.MOD, value = { Dist.CLIENT })
public class MidnightLibClientEvents {

    public static void registerClientTick(TickEvent.ClientTickEvent event) {
        MidnightColorUtil.tick();
    }

    @SubscribeEvent
    public static void onPostInit(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(MidnightLibClientEvents::registerClientTick);
        ModList.get().forEachModContainer((modid, modContainer) -> {
            if (MidnightConfig.configClass.containsKey(modid)) {
                modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> MidnightConfig.getScreen(parent, modid)));
            }
        });
    }
}