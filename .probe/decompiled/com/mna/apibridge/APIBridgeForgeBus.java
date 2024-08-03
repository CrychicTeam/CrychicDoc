package com.mna.apibridge;

import com.mna.Registries;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class APIBridgeForgeBus {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ((IForgeRegistry) Registries.Shape.get()).getValues().stream().forEach(s -> s.lookupAttributeConfig());
        ((IForgeRegistry) Registries.SpellEffect.get()).getValues().stream().forEach(c -> c.lookupAttributeConfig());
    }
}