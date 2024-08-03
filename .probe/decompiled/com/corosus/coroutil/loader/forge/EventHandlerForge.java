package com.corosus.coroutil.loader.forge;

import com.corosus.coroutil.command.CommandCoroConfig;
import com.corosus.coroutil.command.CommandCoroConfigClient;
import com.corosus.modconfig.CoroConfigRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerForge {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandCoroConfig.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void registerCommandsClient(RegisterClientCommandsEvent event) {
        CommandCoroConfigClient.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void serverAboutToStart(ServerAboutToStartEvent event) {
        CoroConfigRegistry.instance().allModsConfigsLoadedAndRegisteredHook();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGameTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CoroConfigRegistry.instance().allModsConfigsLoadedAndRegisteredHook();
        }
    }
}