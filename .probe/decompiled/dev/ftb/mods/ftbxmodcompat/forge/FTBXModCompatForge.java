package dev.ftb.mods.ftbxmodcompat.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.forge.ftbchunks.waystones.WaystonesCompat;
import dev.ftb.mods.ftbxmodcompat.forge.ftbquests.gamestages.GameStagesEventHandlerQuests;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("ftbxmodcompat")
public class FTBXModCompatForge {

    public FTBXModCompatForge() {
        EventBuses.registerModEventBus("ftbxmodcompat", FMLJavaModLoadingContext.get().getModEventBus());
        FTBXModCompat.init();
        if (FTBXModCompat.isFTBQuestsLoaded && FTBXModCompat.isGameStagesLoaded && !FTBXModCompat.isKubeJSLoaded) {
            GameStagesEventHandlerQuests.register();
        }
        if (FTBXModCompat.isFTBChunksLoaded && FTBXModCompat.isWaystonesLoaded) {
            WaystonesCompat.init();
        }
    }
}