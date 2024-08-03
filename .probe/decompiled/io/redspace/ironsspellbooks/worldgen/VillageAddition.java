package io.redspace.ironsspellbooks.worldgen;

import com.mojang.datafixers.util.Pair;
import io.redspace.ironsspellbooks.config.ServerConfigs;
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

@EventBusSubscriber
public class VillageAddition {

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"));

    private static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool != null) {
            SinglePoolElement piece = (SinglePoolElement) SinglePoolElement.m_210512_(nbtPieceRL, emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }
            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList(pool.rawTemplates);
            listOfPieceEntries.add(new Pair(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }

    @SubscribeEvent
    public static void addNewVillageBuilding(ServerAboutToStartEvent event) {
        Registry<StructureTemplatePool> templatePoolRegistry = (Registry<StructureTemplatePool>) event.getServer().registryAccess().m_6632_(Registries.TEMPLATE_POOL).orElseThrow();
        Registry<StructureProcessorList> processorListRegistry = (Registry<StructureProcessorList>) event.getServer().registryAccess().m_6632_(Registries.PROCESSOR_LIST).orElseThrow();
        int weight = ServerConfigs.PRIEST_TOWER_SPAWNRATE.get();
        if (weight > 0) {
            addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/houses"), "irons_spellbooks:priest_house", weight);
            addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/houses"), "irons_spellbooks:priest_house_taiga", weight);
        }
    }
}