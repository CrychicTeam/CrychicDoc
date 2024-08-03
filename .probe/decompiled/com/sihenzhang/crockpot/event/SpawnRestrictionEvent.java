package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class SpawnRestrictionEvent {

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        event.register(CrockPotEntities.VOLT_GOAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, VoltGoat::checkVoltGoatSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}