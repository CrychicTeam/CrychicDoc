package fuzs.puzzleslib.impl.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@EventBusSubscriber(modid = "puzzleslib", bus = Bus.MOD, value = { Dist.CLIENT })
public class PuzzlesLibForgeClient {

    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent evt) {
        ClientModConstructor.construct("puzzleslib", PuzzlesLibClient::new);
    }
}