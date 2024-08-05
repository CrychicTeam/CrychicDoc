package com.mna.loot.conditions;

import com.mna.api.tools.RLoc;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class MALootConditions {

    @SubscribeEvent
    public static void registerLootData(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE)) {
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, RLoc.create("player-faction"), PlayerFactionCheck.PLAYER_FACTION);
        }
    }
}