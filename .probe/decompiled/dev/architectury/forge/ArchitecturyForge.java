package dev.architectury.forge;

import dev.architectury.event.EventHandler;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.level.biome.forge.BiomeModificationsImpl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("architectury")
public class ArchitecturyForge {

    public static final String MOD_ID = "architectury";

    public ArchitecturyForge() {
        EventBuses.registerModEventBus("architectury", FMLJavaModLoadingContext.get().getModEventBus());
        EventHandler.init();
        BiomeModificationsImpl.init();
    }
}