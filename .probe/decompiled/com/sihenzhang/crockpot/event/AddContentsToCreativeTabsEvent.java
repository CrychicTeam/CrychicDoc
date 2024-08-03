package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class AddContentsToCreativeTabsEvent {

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(CrockPotItems.VOLT_GOAT_SPAWN_EGG);
        }
    }
}