package portb.biggerstacks.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import portb.biggerstacks.net.PacketHandler;

@EventBusSubscriber(modid = "biggerstacks", bus = Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void serverStarting(FMLCommonSetupEvent event) {
        PacketHandler.register();
    }
}