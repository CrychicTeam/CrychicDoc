package dev.ftb.mods.ftbxmodcompat.ftbchunks;

import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.ftbchunks.ftbessentials.EssentialsCompat;
import dev.ftb.mods.ftbxmodcompat.ftbchunks.ftbranks.FTBRanksEventHandler;
import dev.ftb.mods.ftbxmodcompat.ftbchunks.waystones.WaystonesCommon;

public class FTBChunksSetup {

    public static void init() {
        if (FTBXModCompat.isFTBRanksLoaded) {
            FTBRanksEventHandler.registerEvents();
        }
        if (FTBXModCompat.isWaystonesLoaded) {
            WaystonesCommon.init();
        }
        if (FTBXModCompat.isFTBEssentialsLoaded) {
            EssentialsCompat.init();
        }
    }
}