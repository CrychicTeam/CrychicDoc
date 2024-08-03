package net.minecraft.world.level.levelgen.structure;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.visitors.CollectFields;
import net.minecraft.nbt.visitors.FieldSelector;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

public class StructureCheck {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int NO_STRUCTURE = -1;

    private final ChunkScanAccess storageAccess;

    private final RegistryAccess registryAccess;

    private final Registry<Biome> biomes;

    private final Registry<Structure> structureConfigs;

    private final StructureTemplateManager structureTemplateManager;

    private final ResourceKey<Level> dimension;

    private final ChunkGenerator chunkGenerator;

    private final RandomState randomState;

    private final LevelHeightAccessor heightAccessor;

    private final BiomeSource biomeSource;

    private final long seed;

    private final DataFixer fixerUpper;

    private final Long2ObjectMap<Object2IntMap<Structure>> loadedChunks = new Long2ObjectOpenHashMap();

    private final Map<Structure, Long2BooleanMap> featureChecks = new HashMap();

    public StructureCheck(ChunkScanAccess chunkScanAccess0, RegistryAccess registryAccess1, StructureTemplateManager structureTemplateManager2, ResourceKey<Level> resourceKeyLevel3, ChunkGenerator chunkGenerator4, RandomState randomState5, LevelHeightAccessor levelHeightAccessor6, BiomeSource biomeSource7, long long8, DataFixer dataFixer9) {
        this.storageAccess = chunkScanAccess0;
        this.registryAccess = registryAccess1;
        this.structureTemplateManager = structureTemplateManager2;
        this.dimension = resourceKeyLevel3;
        this.chunkGenerator = chunkGenerator4;
        this.randomState = randomState5;
        this.heightAccessor = levelHeightAccessor6;
        this.biomeSource = biomeSource7;
        this.seed = long8;
        this.fixerUpper = dataFixer9;
        this.biomes = registryAccess1.registryOrThrow(Registries.BIOME);
        this.structureConfigs = registryAccess1.registryOrThrow(Registries.STRUCTURE);
    }

    public StructureCheckResult checkStart(ChunkPos chunkPos0, Structure structure1, boolean boolean2) {
        long $$3 = chunkPos0.toLong();
        Object2IntMap<Structure> $$4 = (Object2IntMap<Structure>) this.loadedChunks.get($$3);
        if ($$4 != null) {
            return this.checkStructureInfo($$4, structure1, boolean2);
        } else {
            StructureCheckResult $$5 = this.tryLoadFromStorage(chunkPos0, structure1, boolean2, $$3);
            if ($$5 != null) {
                return $$5;
            } else {
                boolean $$6 = ((Long2BooleanMap) this.featureChecks.computeIfAbsent(structure1, p_226739_ -> new Long2BooleanOpenHashMap())).computeIfAbsent($$3, p_226728_ -> this.canCreateStructure(chunkPos0, structure1));
                return !$$6 ? StructureCheckResult.START_NOT_PRESENT : StructureCheckResult.CHUNK_LOAD_NEEDED;
            }
        }
    }

    private boolean canCreateStructure(ChunkPos chunkPos0, Structure structure1) {
        return structure1.findValidGenerationPoint(new Structure.GenerationContext(this.registryAccess, this.chunkGenerator, this.biomeSource, this.randomState, this.structureTemplateManager, this.seed, chunkPos0, this.heightAccessor, structure1.biomes()::m_203333_)).isPresent();
    }

    @Nullable
    private StructureCheckResult tryLoadFromStorage(ChunkPos chunkPos0, Structure structure1, boolean boolean2, long long3) {
        CollectFields $$4 = new CollectFields(new FieldSelector(IntTag.TYPE, "DataVersion"), new FieldSelector("Level", "Structures", CompoundTag.TYPE, "Starts"), new FieldSelector("structures", CompoundTag.TYPE, "starts"));
        try {
            this.storageAccess.scanChunk(chunkPos0, $$4).join();
        } catch (Exception var13) {
            LOGGER.warn("Failed to read chunk {}", chunkPos0, var13);
            return StructureCheckResult.CHUNK_LOAD_NEEDED;
        }
        if (!($$4.m_197713_() instanceof CompoundTag $$7)) {
            return null;
        } else {
            int $$8 = ChunkStorage.getVersion($$7);
            if ($$8 <= 1493) {
                return StructureCheckResult.CHUNK_LOAD_NEEDED;
            } else {
                ChunkStorage.injectDatafixingContext($$7, this.dimension, this.chunkGenerator.getTypeNameForDataFixer());
                CompoundTag $$9;
                try {
                    $$9 = DataFixTypes.CHUNK.updateToCurrentVersion(this.fixerUpper, $$7, $$8);
                } catch (Exception var12) {
                    LOGGER.warn("Failed to partially datafix chunk {}", chunkPos0, var12);
                    return StructureCheckResult.CHUNK_LOAD_NEEDED;
                }
                Object2IntMap<Structure> $$12 = this.loadStructures($$9);
                if ($$12 == null) {
                    return null;
                } else {
                    this.storeFullResults(long3, $$12);
                    return this.checkStructureInfo($$12, structure1, boolean2);
                }
            }
        }
    }

    @Nullable
    private Object2IntMap<Structure> loadStructures(CompoundTag compoundTag0) {
        if (!compoundTag0.contains("structures", 10)) {
            return null;
        } else {
            CompoundTag $$1 = compoundTag0.getCompound("structures");
            if (!$$1.contains("starts", 10)) {
                return null;
            } else {
                CompoundTag $$2 = $$1.getCompound("starts");
                if ($$2.isEmpty()) {
                    return Object2IntMaps.emptyMap();
                } else {
                    Object2IntMap<Structure> $$3 = new Object2IntOpenHashMap();
                    Registry<Structure> $$4 = this.registryAccess.registryOrThrow(Registries.STRUCTURE);
                    for (String $$5 : $$2.getAllKeys()) {
                        ResourceLocation $$6 = ResourceLocation.tryParse($$5);
                        if ($$6 != null) {
                            Structure $$7 = $$4.get($$6);
                            if ($$7 != null) {
                                CompoundTag $$8 = $$2.getCompound($$5);
                                if (!$$8.isEmpty()) {
                                    String $$9 = $$8.getString("id");
                                    if (!"INVALID".equals($$9)) {
                                        int $$10 = $$8.getInt("references");
                                        $$3.put($$7, $$10);
                                    }
                                }
                            }
                        }
                    }
                    return $$3;
                }
            }
        }
    }

    private static Object2IntMap<Structure> deduplicateEmptyMap(Object2IntMap<Structure> objectIntMapStructure0) {
        return objectIntMapStructure0.isEmpty() ? Object2IntMaps.emptyMap() : objectIntMapStructure0;
    }

    private StructureCheckResult checkStructureInfo(Object2IntMap<Structure> objectIntMapStructure0, Structure structure1, boolean boolean2) {
        int $$3 = objectIntMapStructure0.getOrDefault(structure1, -1);
        return $$3 == -1 || boolean2 && $$3 != 0 ? StructureCheckResult.START_NOT_PRESENT : StructureCheckResult.START_PRESENT;
    }

    public void onStructureLoad(ChunkPos chunkPos0, Map<Structure, StructureStart> mapStructureStructureStart1) {
        long $$2 = chunkPos0.toLong();
        Object2IntMap<Structure> $$3 = new Object2IntOpenHashMap();
        mapStructureStructureStart1.forEach((p_226749_, p_226750_) -> {
            if (p_226750_.isValid()) {
                $$3.put(p_226749_, p_226750_.getReferences());
            }
        });
        this.storeFullResults($$2, $$3);
    }

    private void storeFullResults(long long0, Object2IntMap<Structure> objectIntMapStructure1) {
        this.loadedChunks.put(long0, deduplicateEmptyMap(objectIntMapStructure1));
        this.featureChecks.values().forEach(p_209956_ -> p_209956_.remove(long0));
    }

    public void incrementReference(ChunkPos chunkPos0, Structure structure1) {
        this.loadedChunks.compute(chunkPos0.toLong(), (p_226745_, p_226746_) -> {
            if (p_226746_ == null || p_226746_.isEmpty()) {
                p_226746_ = new Object2IntOpenHashMap();
            }
            p_226746_.computeInt(structure1, (p_226741_, p_226742_) -> p_226742_ == null ? 1 : p_226742_ + 1);
            return p_226746_;
        });
    }
}