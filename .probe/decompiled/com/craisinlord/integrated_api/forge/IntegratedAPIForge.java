package com.craisinlord.integrated_api.forge;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.events.RegisterVillagerTradesEvent;
import com.craisinlord.integrated_api.events.RegisterWanderingTradesEvent;
import com.craisinlord.integrated_api.events.lifecycle.RegisterReloadListenerEvent;
import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStartEvent;
import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStopEvent;
import com.craisinlord.integrated_api.events.lifecycle.SetupEvent;
import com.craisinlord.integrated_api.modinit.forge.IABiomeModifiers;
import com.craisinlord.integrated_api.modinit.registry.forge.ResourcefulRegistriesImpl;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("integrated_api")
public class IntegratedAPIForge {

    public IntegratedAPIForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, ResourcefulRegistriesImpl::onRegisterForgeRegistries);
        IntegratedAPI.init();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IABiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(IntegratedAPIForge::onSetup);
        eventBus.addListener(IntegratedAPIForge::onServerStarting);
        eventBus.addListener(IntegratedAPIForge::onServerStopping);
        eventBus.addListener(IntegratedAPIForge::onAddVillagerTrades);
        eventBus.addListener(IntegratedAPIForge::onWanderingTrades);
        eventBus.addListener(IntegratedAPIForge::onAddReloadListeners);
    }

    private static void onSetup(FMLCommonSetupEvent event) {
        SetupEvent.EVENT.invoke(new SetupEvent(event::enqueueWork));
    }

    private static void onServerStarting(ServerAboutToStartEvent event) {
        ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(event.getServer()));
    }

    private static void onServerStopping(ServerStoppingEvent event) {
        ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE);
    }

    private static void onAddVillagerTrades(VillagerTradesEvent event) {
        RegisterVillagerTradesEvent.EVENT.invoke(new RegisterVillagerTradesEvent(event.getType(), (i, listing) -> ((List) event.getTrades().get(i)).add(listing)));
    }

    private static void onWanderingTrades(WandererTradesEvent event) {
        RegisterWanderingTradesEvent.EVENT.invoke(new RegisterWanderingTradesEvent(event.getGenericTrades()::add, event.getRareTrades()::add));
    }

    private static void onAddReloadListeners(AddReloadListenerEvent event) {
        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) -> event.addListener(listener)));
    }
}