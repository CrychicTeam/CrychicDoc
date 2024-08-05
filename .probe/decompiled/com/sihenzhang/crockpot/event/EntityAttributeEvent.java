package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class EntityAttributeEvent {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(CrockPotEntities.VOLT_GOAT.get(), VoltGoat.createAttributes().build());
    }
}