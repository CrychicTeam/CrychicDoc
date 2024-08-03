package com.craisinlord.integrated_villages.pooladditions;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.events.lifecycle.ServerGoingToStartEvent;
import com.craisinlord.integrated_api.mixins.structures.ListPoolElementAccessor;
import com.craisinlord.integrated_api.mixins.structures.SinglePoolElementAccessor;
import com.craisinlord.integrated_api.mixins.structures.StructurePoolAccessor;
import com.craisinlord.integrated_api.mixins.structures.StructureTemplateManagerAccessor;
import com.craisinlord.integrated_api.utils.GeneralUtils;
import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.pools.ListPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public final class PoolAdditionMerger {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().create();

    private static final String DATA_TYPE = "integrated_villages_pool_additions";

    private static final int FILE_SUFFIX_LENGTH = ".json".length();

    private PoolAdditionMerger() {
    }

    public static void mergeAdditionPools(ServerGoingToStartEvent event) {
        ResourceManager resourceManager = ((StructureTemplateManagerAccessor) event.getServer().getStructureManager()).integratedapi_getResourceManager();
        Map<ResourceLocation, List<JsonElement>> poolAdditionJSON = GeneralUtils.getAllDatapacksJSONElement(resourceManager, GSON, "integrated_villages_pool_additions", FILE_SUFFIX_LENGTH);
        parsePoolsAndBeginMerger(poolAdditionJSON, event.getServer().registryAccess(), event.getServer().getStructureManager());
    }

    private static void parsePoolsAndBeginMerger(Map<ResourceLocation, List<JsonElement>> poolAdditionJSON, RegistryAccess dynamicRegistryManager, StructureTemplateManager StructureTemplateManager) {
        Registry<StructureTemplatePool> poolRegistry = dynamicRegistryManager.registryOrThrow(Registries.TEMPLATE_POOL);
        RegistryOps<JsonElement> customRegistryOps = RegistryOps.create(JsonOps.INSTANCE, dynamicRegistryManager);
        for (Entry<ResourceLocation, List<JsonElement>> entry : poolAdditionJSON.entrySet()) {
            if (poolRegistry.get((ResourceLocation) entry.getKey()) != null) {
                for (JsonElement jsonElement : (List) entry.getValue()) {
                    try {
                        PoolAdditionMerger.AdditionalStructureTemplatePool.DIRECT_CODEC.parse(customRegistryOps, jsonElement).resultOrPartial(messageString -> logBadData((ResourceLocation) entry.getKey(), messageString)).ifPresent(validPool -> mergeIntoExistingPool(validPool, poolRegistry.get((ResourceLocation) entry.getKey()), StructureTemplateManager));
                    } catch (Exception var10) {
                        IntegratedAPI.LOGGER.error("\nIntegrated API: Pool Addition json failed to be parsed.\nThis is usually due to using a mod compat datapack without the other mod being on.\nEntry failed to be resolved: %s\nRegistry being used: %s\nError message is: %s".formatted(entry.getKey(), poolRegistry, var10.getMessage()).indent(1));
                    }
                }
            }
        }
    }

    private static void mergeIntoExistingPool(PoolAdditionMerger.AdditionalStructureTemplatePool feedingPool, StructureTemplatePool gluttonyPool, StructureTemplateManager structureTemplateManager) {
        ObjectArrayList<StructurePoolElement> elements = new ObjectArrayList(((StructurePoolAccessor) gluttonyPool).integratedapi_getTemplates());
        List<Pair<StructurePoolElement, Integer>> elementCounts = new ArrayList(((StructurePoolAccessor) gluttonyPool).integratedapi_getRawTemplates());
        elements.addAll(((StructurePoolAccessor) feedingPool).integratedapi_getTemplates());
        elementCounts.addAll(((StructurePoolAccessor) feedingPool).integratedapi_getRawTemplates());
        ObjectListIterator var5 = elements.iterator();
        while (var5.hasNext()) {
            StructurePoolElement element = (StructurePoolElement) var5.next();
            if (element instanceof SinglePoolElement) {
                SinglePoolElement singlePoolElement = (SinglePoolElement) element;
                Optional<ResourceLocation> pieceRL = ((SinglePoolElementAccessor) singlePoolElement).integratedapi_getTemplate().left();
                if (!pieceRL.isEmpty()) {
                    checkIfPieceExists(feedingPool, structureTemplateManager, (ResourceLocation) pieceRL.get());
                }
            } else if (element instanceof ListPoolElement listPoolElement) {
                for (StructurePoolElement listElement : ((ListPoolElementAccessor) listPoolElement).integratedapi_getElements()) {
                    if (listElement instanceof SinglePoolElement) {
                        SinglePoolElement singlePoolElement = (SinglePoolElement) listElement;
                        Optional<ResourceLocation> pieceRL = ((SinglePoolElementAccessor) singlePoolElement).integratedapi_getTemplate().left();
                        if (!pieceRL.isEmpty()) {
                            checkIfPieceExists(feedingPool, structureTemplateManager, (ResourceLocation) pieceRL.get());
                        }
                    }
                }
            }
        }
        ((StructurePoolAccessor) gluttonyPool).integratedapi_setTemplates(elements);
        ((StructurePoolAccessor) gluttonyPool).integratedapi_setRawTemplates(elementCounts);
    }

    private static void checkIfPieceExists(PoolAdditionMerger.AdditionalStructureTemplatePool feedingPool, StructureTemplateManager structureTemplateManager, ResourceLocation pieceRL) {
        ResourceLocation resourcelocation = new ResourceLocation(pieceRL.getNamespace(), "structures/" + pieceRL.getPath() + ".nbt");
        try {
            InputStream inputstream = ((StructureTemplateManagerAccessor) structureTemplateManager).integratedapi_getResourceManager().m_215595_(resourcelocation);
            if (inputstream.available() == 0 || inputstream.read(new byte[1]) == -1) {
                IntegratedAPI.LOGGER.error("(Integrated Villages POOL MERGER) Found an entry in {} that points to the non-existent nbt file called {}", feedingPool.name, pieceRL);
            }
            inputstream.close();
        } catch (Throwable var5) {
            IntegratedAPI.LOGGER.error("(Integrated Villages POOL MERGER) Found an entry in {} that points to the non-existent nbt file called {}", feedingPool.name, pieceRL);
        }
    }

    private static void logBadData(ResourceLocation poolPath, String messageString) {
        IntegratedAPI.LOGGER.error("(Integrated API POOL MERGER) Failed to parse {} additions file. Error is: {}", poolPath, messageString);
    }

    private static class AdditionalStructureTemplatePool extends StructureTemplatePool {

        private static final Codec<PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry> EXPANDED_POOL_ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(StructurePoolElement.CODEC.fieldOf("element").forGetter(PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry::poolElement), Codec.intRange(1, 5000).fieldOf("weight").forGetter(PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry::weight), Codec.STRING.optionalFieldOf("required_mod").forGetter(PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry::requiredMod)).apply(instance, PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry::new));

        public static final Codec<PoolAdditionMerger.AdditionalStructureTemplatePool> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("name").forGetter(structureTemplatePool -> structureTemplatePool.name), ExtraCodecs.lazyInitializedCodec(StructurePoolAccessor.getCODEC_REFERENCE()::getValue).fieldOf("fallback").forGetter(StructureTemplatePool::m_254935_), EXPANDED_POOL_ENTRY_CODEC.listOf().fieldOf("elements").forGetter(structureTemplatePool -> structureTemplatePool.rawTemplatesWithConditions)).apply(instance, PoolAdditionMerger.AdditionalStructureTemplatePool::new));

        protected final List<PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry> rawTemplatesWithConditions;

        protected final ResourceLocation name;

        public AdditionalStructureTemplatePool(ResourceLocation name, Holder<StructureTemplatePool> fallback, List<PoolAdditionMerger.AdditionalStructureTemplatePool.ExpandedPoolEntry> rawTemplatesWithConditions) {
            super(fallback, (List<Pair<StructurePoolElement, Integer>>) rawTemplatesWithConditions.stream().filter(triple -> PlatformHooks.isModLoaded((String) triple.requiredMod.get())).map(triple -> Pair.of(triple.poolElement(), triple.weight())).collect(Collectors.toList()));
            this.rawTemplatesWithConditions = rawTemplatesWithConditions;
            this.name = name;
        }

        public static record ExpandedPoolEntry(StructurePoolElement poolElement, Integer weight, Optional<String> requiredMod) {
        }
    }
}