package com.mna.villagers;

import com.mna.api.tools.RLoc;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "mna")
public class VillageStructures {

    private static Registry<StructureTemplatePool> templatePoolRegistry;

    private static Registry<StructureProcessorList> processorListRegistry;

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"));

    @SubscribeEvent
    public static void addNewVillageBuilding(ServerAboutToStartEvent event) {
        templatePoolRegistry = (Registry<StructureTemplatePool>) event.getServer().registryAccess().m_6632_(Registries.TEMPLATE_POOL).orElseThrow();
        processorListRegistry = (Registry<StructureProcessorList>) event.getServer().registryAccess().m_6632_(Registries.PROCESSOR_LIST).orElseThrow();
        registerDesertVillageStructure(RLoc.create("village/desert/spellmonger"), 4);
        registerDesertVillageStructure(RLoc.create("village/desert/manaweaver"), 2);
        registerDesertVillageStructure(RLoc.create("village/desert/runeforge_and_runescribe"), 2);
        registerDesertVillageStructure(RLoc.create("village/desert/runescribe"), 2);
        registerPlainsVillageStructure(RLoc.create("village/plains/spellmonger"), 4);
        registerPlainsVillageStructure(RLoc.create("village/plains/manaweaver"), 2);
        registerPlainsVillageStructure(RLoc.create("village/plains/runeforge_and_runescribe"), 2);
        registerPlainsVillageStructure(RLoc.create("village/plains/runescribe"), 2);
        registerSavannaVillageStructure(RLoc.create("village/savanna/spellmonger"), 4);
        registerSavannaVillageStructure(RLoc.create("village/savanna/manaweaver"), 2);
        registerSavannaVillageStructure(RLoc.create("village/savanna/runeforge_and_runescribe"), 2);
        registerSavannaVillageStructure(RLoc.create("village/savanna/runescribe"), 2);
        registerSnowyVillageStructure(RLoc.create("village/snowy/spellmonger"), 4);
        registerSnowyVillageStructure(RLoc.create("village/snowy/manaweaver"), 2);
        registerSnowyVillageStructure(RLoc.create("village/snowy/runeforge_and_runescribe"), 2);
        registerSnowyVillageStructure(RLoc.create("village/snowy/runescribe"), 2);
        registerTaigaVillageStructure(RLoc.create("village/taiga/spellmonger"), 4);
        registerTaigaVillageStructure(RLoc.create("village/taiga/manaweaver"), 2);
        registerTaigaVillageStructure(RLoc.create("village/taiga/runeforge_and_runescribe"), 2);
        registerTaigaVillageStructure(RLoc.create("village/taiga/runescribe"), 2);
    }

    private static void registerTaigaVillageStructure(ResourceLocation pieceNBTLocation, int weight) {
        addBuildingToPool(new ResourceLocation("village/taiga/houses"), pieceNBTLocation, weight);
    }

    private static void registerDesertVillageStructure(ResourceLocation pieceNBTLocation, int weight) {
        addBuildingToPool(new ResourceLocation("village/desert/houses"), pieceNBTLocation, weight);
    }

    private static void registerSavannaVillageStructure(ResourceLocation pieceNBTLocation, int weight) {
        addBuildingToPool(new ResourceLocation("village/savanna/houses"), pieceNBTLocation, weight);
    }

    private static void registerSnowyVillageStructure(ResourceLocation pieceNBTLocation, int weight) {
        addBuildingToPool(new ResourceLocation("village/snowy/houses"), pieceNBTLocation, weight);
    }

    private static void registerPlainsVillageStructure(ResourceLocation pieceNBTLocation, int weight) {
        addBuildingToPool(new ResourceLocation("village/plains/houses"), pieceNBTLocation, weight);
    }

    private static void addBuildingToPool(ResourceLocation poolRL, ResourceLocation nbtPieceRL, int weight) {
        Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool != null) {
            SinglePoolElement piece = (SinglePoolElement) SinglePoolElement.m_210512_(nbtPieceRL.toString(), emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }
            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList(pool.rawTemplates);
            listOfPieceEntries.add(new Pair(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }
}