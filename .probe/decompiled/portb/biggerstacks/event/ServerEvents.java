package portb.biggerstacks.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "biggerstacks", value = { Dist.DEDICATED_SERVER })
public class ServerEvents {

    private static ServerLifecycleHandler handler;

    @SubscribeEvent
    public static void serverStarting(ServerAboutToStartEvent event) {
        handler = new ServerLifecycleHandler();
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        MinecraftForge.EVENT_BUS.unregister(handler);
        handler.ensureStopped();
    }
}