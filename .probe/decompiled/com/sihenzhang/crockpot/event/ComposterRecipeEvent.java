package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.item.CrockPotItems;
import java.util.Set;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = "crockpot", bus = Bus.MOD)
public class ComposterRecipeEvent {

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            ((Set) CrockPotItems.SEEDS.get()).forEach(seed -> ComposterBlock.add(0.3F, seed));
            ((Set) CrockPotItems.CROPS.get()).forEach(crop -> ComposterBlock.add(0.65F, crop));
            ((Set) CrockPotItems.COOKED_CROPS.get()).forEach(cookedCrop -> ComposterBlock.add(0.85F, cookedCrop));
        });
    }
}