package com.suppergerrie2.alwayseat.alwayseat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;

public class ServerEvents {

    public ServerEvents() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReloaded);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent) {
        AlwaysEat.INSTANCE.send(PacketDistributor.ALL.noArg(), SyncSettings.fromConfig());
    }

    private void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.SERVER_CONFIG) {
            AlwaysEat.INSTANCE.send(PacketDistributor.ALL.noArg(), SyncSettings.fromConfig());
        }
    }
}