package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class RegisterPacketsEvent {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkManager.registerPacket(PacketFoodCounter.class, PacketFoodCounter::serialize, PacketFoodCounter::deserialize, PacketFoodCounter::handle, NetworkDirection.PLAY_TO_CLIENT);
    }
}