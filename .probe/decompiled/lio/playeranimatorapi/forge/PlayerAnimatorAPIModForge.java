package lio.playeranimatorapi.forge;

import lio.playeranimatorapi.ModInit;
import lio.playeranimatorapi.ModInitClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("liosplayeranimatorapi")
public class PlayerAnimatorAPIModForge {

    public PlayerAnimatorAPIModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::ClientInit);
        ModInit.init();
    }

    private void ClientInit(FMLClientSetupEvent event) {
        ModInitClient.init();
    }
}