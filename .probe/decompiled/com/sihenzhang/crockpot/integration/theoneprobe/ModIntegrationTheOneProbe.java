package com.sihenzhang.crockpot.integration.theoneprobe;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class ModIntegrationTheOneProbe {

    public static final String MOD_ID = "theoneprobe";

    public static final String METHOD_NAME = "getTheOneProbe";

    @SubscribeEvent
    public static void sendIMCMessage(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", CrockPotProbeInfoProvider::new);
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", BirdcageProbeInfoProvider::new);
        }
    }
}