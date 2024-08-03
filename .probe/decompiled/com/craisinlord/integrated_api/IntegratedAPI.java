package com.craisinlord.integrated_api;

import com.craisinlord.integrated_api.events.RegisterVillagerTradesEvent;
import com.craisinlord.integrated_api.events.RegisterWanderingTradesEvent;
import com.craisinlord.integrated_api.events.lifecycle.RegisterReloadListenerEvent;
import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStartEvent;
import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStopEvent;
import com.craisinlord.integrated_api.events.lifecycle.SetupEvent;
import com.craisinlord.integrated_api.misc.maptrades.StructureMapManager;
import com.craisinlord.integrated_api.misc.maptrades.StructureMapTradesEvents;
import com.craisinlord.integrated_api.misc.mobspawners.MobSpawnerManager;
import com.craisinlord.integrated_api.misc.structurepiececounter.StructurePieceCountsManager;
import com.craisinlord.integrated_api.modinit.IAConditionsRegistry;
import com.craisinlord.integrated_api.modinit.IAPlacements;
import com.craisinlord.integrated_api.modinit.IAPredicates;
import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.craisinlord.integrated_api.modinit.IAStructurePieces;
import com.craisinlord.integrated_api.modinit.IAStructurePlacementType;
import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.modinit.IATags;
import com.craisinlord.integrated_api.utils.AsyncLocator;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedAPI {

    public static final String MODID = "integrated_api";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final int NEW_STRUCTURE_SIZE = 512;

    public static void init() {
        IATags.initTags();
        IAPredicates.RULE_TEST.init();
        IAPredicates.POS_RULE_TEST.init();
        IAStructures.STRUCTURE_TYPE.init();
        IAPlacements.PLACEMENT_MODIFIER.init();
        IAProcessors.STRUCTURE_PROCESSOR.init();
        IAStructurePieces.STRUCTURE_PIECE.init();
        IAStructurePieces.STRUCTURE_POOL_ELEMENT.init();
        IAStructurePlacementType.STRUCTURE_PLACEMENT_TYPE.init();
        IAConditionsRegistry.IA_JSON_CONDITIONS_REGISTRY.init();
        SetupEvent.EVENT.addListener(IntegratedAPI::setup);
        RegisterReloadListenerEvent.EVENT.addListener(IntegratedAPI::registerDatapackListener);
        ServerGoingToStartEvent.EVENT.addListener(IntegratedAPI::serverAboutToStart);
        ServerGoingToStopEvent.EVENT.addListener(IntegratedAPI::onServerStopping);
        RegisterVillagerTradesEvent.EVENT.addListener(IntegratedAPI::onAddVillagerTrades);
        RegisterWanderingTradesEvent.EVENT.addListener(IntegratedAPI::onWanderingTrades);
    }

    private static void setup(SetupEvent event) {
    }

    private static void serverAboutToStart(ServerGoingToStartEvent event) {
        AsyncLocator.handleServerAboutToStartEvent();
    }

    private static void onServerStopping(ServerGoingToStopEvent event) {
        AsyncLocator.handleServerStoppingEvent();
    }

    private static void onAddVillagerTrades(RegisterVillagerTradesEvent event) {
        StructureMapTradesEvents.addVillagerTrades(event);
    }

    private static void onWanderingTrades(RegisterWanderingTradesEvent event) {
        StructureMapTradesEvents.addWanderingTrades(event);
    }

    public static void registerDatapackListener(RegisterReloadListenerEvent event) {
        event.register(new ResourceLocation("integrated_api", "integrated_structure_spawners"), MobSpawnerManager.MOB_SPAWNER_MANAGER);
        event.register(new ResourceLocation("integrated_api", "integrated_structure_map_trades"), StructureMapManager.STRUCTURE_MAP_MANAGER);
        event.register(new ResourceLocation("integrated_api", "integrated_pieces_spawn_counts"), StructurePieceCountsManager.STRUCTURE_PIECE_COUNTS_MANAGER);
    }
}