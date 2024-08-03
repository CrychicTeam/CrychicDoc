package dev.ftb.mods.ftblibrary.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("ftblibrary")
public class FTBLibraryForge {

    public FTBLibraryForge() {
        EventBuses.registerModEventBus("ftblibrary", FMLJavaModLoadingContext.get().getModEventBus());
        new FTBLibrary();
    }
}