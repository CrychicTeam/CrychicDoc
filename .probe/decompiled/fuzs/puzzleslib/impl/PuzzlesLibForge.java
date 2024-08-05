package fuzs.puzzleslib.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod("puzzleslib")
@EventBusSubscriber(bus = Bus.MOD)
public class PuzzlesLibForge {

    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent evt) {
        ModConstructor.construct("puzzleslib", PuzzlesLibMod::new);
    }
}