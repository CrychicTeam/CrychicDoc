package com.craisinlord.integrated_villages;

import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStartEvent;
import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_villages.config.ConfigModule;
import com.craisinlord.integrated_villages.lootmanager.StructureModdedLootImporter;
import com.craisinlord.integrated_villages.pooladditions.PoolAdditionMerger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedVillages {

    public static final String MODID = "integrated_villages";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ConfigModule CONFIG = new ConfigModule();

    public static void init() {
        StructureModdedLootImporter.createMap();
        IntegratedVilagesProcessors.STRUCTURE_PROCESSOR.init();
        ServerGoingToStartEvent.EVENT.addListener(IntegratedVillages::serverAboutToStart);
    }

    private static void serverAboutToStart(ServerGoingToStartEvent event) {
        PoolAdditionMerger.mergeAdditionPools(event);
        if (PlatformHooks.isDevEnvironment()) {
            StructureModdedLootImporter.checkLoottables(event.getServer());
        }
    }
}