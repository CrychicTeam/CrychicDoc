package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.CompatHandlerClientImpl;

public class CompatHandlerClient {

    @ExpectPlatform
    @Transformed
    public static void doSetup() {
        CompatHandlerClientImpl.doSetup();
    }

    public static void setup() {
        doSetup();
        if (CompatHandler.DECO_BLOCKS) {
            DecoBlocksCompat.setupClient();
        }
        if (CompatHandler.FARMERS_DELIGHT) {
            FarmersDelightCompat.setupClient();
        }
        if (CompatHandler.CAVE_ENHANCEMENTS) {
            CaveEnhancementsCompat.setupClient();
        }
        if (CompatHandler.BUZZIER_BEES) {
            BuzzierBeesCompat.setupClient();
        }
        if (CompatHandler.INFERNALEXP) {
            InfernalExpCompat.setupClient();
        }
        if (CompatHandler.ARCHITECTS_PALETTE) {
            ArchitectsPalCompat.setupClient();
        }
        if (CompatHandler.ENDERGETIC) {
            EndergeticCompat.setupClient();
        }
    }

    @ExpectPlatform
    @Transformed
    public static void init() {
        CompatHandlerClientImpl.init();
    }
}