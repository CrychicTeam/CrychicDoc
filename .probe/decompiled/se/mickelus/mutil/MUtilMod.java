package se.mickelus.mutil;

import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("mutil")
@EventBusSubscriber(bus = Bus.MOD)
public class MUtilMod {

    public static final String MOD_ID = "mutil";

    public MUtilMod() {
        ConfigHandler.setup();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        Perks.init(Minecraft.getInstance().getUser().getUuid());
    }
}