package vectorwing.farmersdelight.common.world;

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
import vectorwing.farmersdelight.common.Configuration;

public class VillageStructures {

    public static void addNewVillageBuilding(ServerAboutToStartEvent event) {
        if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
            Registry<StructureTemplatePool> templatePools = (Registry<StructureTemplatePool>) event.getServer().registryAccess().m_6632_(Registries.TEMPLATE_POOL).get();
            Registry<StructureProcessorList> processorLists = (Registry<StructureProcessorList>) event.getServer().registryAccess().m_6632_(Registries.PROCESSOR_LIST).get();
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), "farmersdelight:village/houses/plains_compost_pile", 5);
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), "farmersdelight:village/houses/snowy_compost_pile", 3);
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/savanna/houses"), "farmersdelight:village/houses/savanna_compost_pile", 4);
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/desert/houses"), "farmersdelight:village/houses/desert_compost_pile", 3);
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), "farmersdelight:village/houses/taiga_compost_pile", 4);
        }
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool != null) {
            ResourceLocation emptyProcessor = new ResourceLocation("minecraft", "empty");
            Holder<StructureProcessorList> processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));
            SinglePoolElement piece = (SinglePoolElement) SinglePoolElement.m_210531_(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }
            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList(pool.rawTemplates);
            listOfPieceEntries.add(new Pair(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }
}