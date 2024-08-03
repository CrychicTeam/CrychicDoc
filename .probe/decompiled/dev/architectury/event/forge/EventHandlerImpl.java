package dev.architectury.event.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public class EventHandlerImpl {

    @OnlyIn(Dist.CLIENT)
    public static void registerClient() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplClient.class);
        EventBuses.onRegistered("architectury", bus -> bus.register(EventHandlerImplClient.ModBasedEventHandler.class));
    }

    public static void registerCommon() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplCommon.class);
        EventBuses.onRegistered("architectury", bus -> bus.register(EventHandlerImplCommon.ModBasedEventHandler.class));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void registerServer() {
        MinecraftForge.EVENT_BUS.register(EventHandlerImplServer.class);
        EventBuses.onRegistered("architectury", bus -> bus.register(EventHandlerImplServer.ModBasedEventHandler.class));
    }
}