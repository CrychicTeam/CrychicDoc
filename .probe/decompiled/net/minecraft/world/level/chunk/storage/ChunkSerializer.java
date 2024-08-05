package net.minecraft.world.level.chunk.storage;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ProtoChunkTicks;
import org.slf4j.Logger;

public class ChunkSerializer {

    private static final Codec<PalettedContainer<BlockState>> BLOCK_STATE_CODEC = PalettedContainer.codecRW(Block.BLOCK_STATE_REGISTRY, BlockState.CODEC, PalettedContainer.Strategy.SECTION_STATES, Blocks.AIR.defaultBlockState());

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String TAG_UPGRADE_DATA = "UpgradeData";

    private static final String BLOCK_TICKS_TAG = "block_ticks";

    private static final String FLUID_TICKS_TAG = "fluid_ticks";

    public static final String X_POS_TAG = "xPos";

    public static final String Z_POS_TAG = "zPos";

    public static final String HEIGHTMAPS_TAG = "Heightmaps";

    public static final String IS_LIGHT_ON_TAG = "isLightOn";

    public static final String SECTIONS_TAG = "sections";

    public static final String BLOCK_LIGHT_TAG = "BlockLight";

    public static final String SKY_LIGHT_TAG = "SkyLight";

    public static ProtoChunk read(ServerLevel serverLevel0, PoiManager poiManager1, ChunkPos chunkPos2, CompoundTag compoundTag3) {
        ChunkPos $$4 = new ChunkPos(compoundTag3.getInt("xPos"), compoundTag3.getInt("zPos"));
        if (!Objects.equals(chunkPos2, $$4)) {
            LOGGER.error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", new Object[] { chunkPos2, chunkPos2, $$4 });
        }
        UpgradeData $$5 = compoundTag3.contains("UpgradeData", 10) ? new UpgradeData(compoundTag3.getCompound("UpgradeData"), serverLevel0) : UpgradeData.EMPTY;
        boolean $$6 = compoundTag3.getBoolean("isLightOn");
        ListTag $$7 = compoundTag3.getList("sections", 10);
        int $$8 = serverLevel0.m_151559_();
        LevelChunkSection[] $$9 = new LevelChunkSection[$$8];
        boolean $$10 = serverLevel0.m_6042_().hasSkyLight();
        ChunkSource $$11 = serverLevel0.getChunkSource();
        LevelLightEngine $$12 = $$11.getLightEngine();
        Registry<Biome> $$13 = serverLevel0.m_9598_().registryOrThrow(Registries.BIOME);
        Codec<PalettedContainerRO<Holder<Biome>>> $$14 = makeBiomeCodec($$13);
        boolean $$15 = false;
        for (int $$16 = 0; $$16 < $$7.size(); $$16++) {
            CompoundTag $$17 = $$7.getCompound($$16);
            int $$18 = $$17.getByte("Y");
            int $$19 = serverLevel0.m_151566_($$18);
            if ($$19 >= 0 && $$19 < $$9.length) {
                PalettedContainer<BlockState> $$20;
                if ($$17.contains("block_states", 10)) {
                    $$20 = (PalettedContainer<BlockState>) BLOCK_STATE_CODEC.parse(NbtOps.INSTANCE, $$17.getCompound("block_states")).promotePartial(p_188283_ -> logErrors(chunkPos2, $$18, p_188283_)).getOrThrow(false, LOGGER::error);
                } else {
                    $$20 = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
                }
                PalettedContainerRO<Holder<Biome>> $$22;
                if ($$17.contains("biomes", 10)) {
                    $$22 = (PalettedContainerRO<Holder<Biome>>) $$14.parse(NbtOps.INSTANCE, $$17.getCompound("biomes")).promotePartial(p_188274_ -> logErrors(chunkPos2, $$18, p_188274_)).getOrThrow(false, LOGGER::error);
                } else {
                    $$22 = new PalettedContainer<>($$13.asHolderIdMap(), $$13.getHolderOrThrow(Biomes.PLAINS), PalettedContainer.Strategy.SECTION_BIOMES);
                }
                LevelChunkSection $$24 = new LevelChunkSection($$20, $$22);
                $$9[$$19] = $$24;
                SectionPos $$25 = SectionPos.of(chunkPos2, $$18);
                poiManager1.checkConsistencyWithBlocks($$25, $$24);
            }
            boolean $$26 = $$17.contains("BlockLight", 7);
            boolean $$27 = $$10 && $$17.contains("SkyLight", 7);
            if ($$26 || $$27) {
                if (!$$15) {
                    $$12.retainData(chunkPos2, true);
                    $$15 = true;
                }
                if ($$26) {
                    $$12.queueSectionData(LightLayer.BLOCK, SectionPos.of(chunkPos2, $$18), new DataLayer($$17.getByteArray("BlockLight")));
                }
                if ($$27) {
                    $$12.queueSectionData(LightLayer.SKY, SectionPos.of(chunkPos2, $$18), new DataLayer($$17.getByteArray("SkyLight")));
                }
            }
        }
        long $$28 = compoundTag3.getLong("InhabitedTime");
        ChunkStatus.ChunkType $$29 = getChunkTypeFromTag(compoundTag3);
        BlendingData $$30;
        if (compoundTag3.contains("blending_data", 10)) {
            $$30 = (BlendingData) BlendingData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag3.getCompound("blending_data"))).resultOrPartial(LOGGER::error).orElse(null);
        } else {
            $$30 = null;
        }
        ChunkAccess $$34;
        if ($$29 == ChunkStatus.ChunkType.LEVELCHUNK) {
            LevelChunkTicks<Block> $$32 = LevelChunkTicks.load(compoundTag3.getList("block_ticks", 10), p_258988_ -> BuiltInRegistries.BLOCK.m_6612_(ResourceLocation.tryParse(p_258988_)), chunkPos2);
            LevelChunkTicks<Fluid> $$33 = LevelChunkTicks.load(compoundTag3.getList("fluid_ticks", 10), p_258990_ -> BuiltInRegistries.FLUID.m_6612_(ResourceLocation.tryParse(p_258990_)), chunkPos2);
            $$34 = new LevelChunk(serverLevel0.getLevel(), chunkPos2, $$5, $$32, $$33, $$28, $$9, postLoadChunk(serverLevel0, compoundTag3), $$30);
        } else {
            ProtoChunkTicks<Block> $$35 = ProtoChunkTicks.load(compoundTag3.getList("block_ticks", 10), p_258992_ -> BuiltInRegistries.BLOCK.m_6612_(ResourceLocation.tryParse(p_258992_)), chunkPos2);
            ProtoChunkTicks<Fluid> $$36 = ProtoChunkTicks.load(compoundTag3.getList("fluid_ticks", 10), p_258991_ -> BuiltInRegistries.FLUID.m_6612_(ResourceLocation.tryParse(p_258991_)), chunkPos2);
            ProtoChunk $$37 = new ProtoChunk(chunkPos2, $$5, $$9, $$35, $$36, serverLevel0, $$13, $$30);
            $$34 = $$37;
            $$37.setInhabitedTime($$28);
            if (compoundTag3.contains("below_zero_retrogen", 10)) {
                BelowZeroRetrogen.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag3.getCompound("below_zero_retrogen"))).resultOrPartial(LOGGER::error).ifPresent($$37::m_188183_);
            }
            ChunkStatus $$39 = ChunkStatus.byName(compoundTag3.getString("Status"));
            $$37.setStatus($$39);
            if ($$39.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
                $$37.setLightEngine($$12);
            }
        }
        $$34.setLightCorrect($$6);
        CompoundTag $$40 = compoundTag3.getCompound("Heightmaps");
        EnumSet<Heightmap.Types> $$41 = EnumSet.noneOf(Heightmap.Types.class);
        for (Heightmap.Types $$42 : $$34.getStatus().heightmapsAfter()) {
            String $$43 = $$42.getSerializationKey();
            if ($$40.contains($$43, 12)) {
                $$34.setHeightmap($$42, $$40.getLongArray($$43));
            } else {
                $$41.add($$42);
            }
        }
        Heightmap.primeHeightmaps($$34, $$41);
        CompoundTag $$44 = compoundTag3.getCompound("structures");
        $$34.setAllStarts(unpackStructureStart(StructurePieceSerializationContext.fromLevel(serverLevel0), $$44, serverLevel0.getSeed()));
        $$34.setAllReferences(unpackStructureReferences(serverLevel0.m_9598_(), chunkPos2, $$44));
        if (compoundTag3.getBoolean("shouldSave")) {
            $$34.setUnsaved(true);
        }
        ListTag $$45 = compoundTag3.getList("PostProcessing", 9);
        for (int $$46 = 0; $$46 < $$45.size(); $$46++) {
            ListTag $$47 = $$45.getList($$46);
            for (int $$48 = 0; $$48 < $$47.size(); $$48++) {
                $$34.addPackedPostProcess($$47.getShort($$48), $$46);
            }
        }
        if ($$29 == ChunkStatus.ChunkType.LEVELCHUNK) {
            return new ImposterProtoChunk((LevelChunk) $$34, false);
        } else {
            ProtoChunk $$49 = (ProtoChunk) $$34;
            ListTag $$50 = compoundTag3.getList("entities", 10);
            for (int $$51 = 0; $$51 < $$50.size(); $$51++) {
                $$49.addEntity($$50.getCompound($$51));
            }
            ListTag $$52 = compoundTag3.getList("block_entities", 10);
            for (int $$53 = 0; $$53 < $$52.size(); $$53++) {
                CompoundTag $$54 = $$52.getCompound($$53);
                $$34.setBlockEntityNbt($$54);
            }
            CompoundTag $$55 = compoundTag3.getCompound("CarvingMasks");
            for (String $$56 : $$55.getAllKeys()) {
                GenerationStep.Carving $$57 = GenerationStep.Carving.valueOf($$56);
                $$49.setCarvingMask($$57, new CarvingMask($$55.getLongArray($$56), $$34.getMinBuildHeight()));
            }
            return $$49;
        }
    }

    private static void logErrors(ChunkPos chunkPos0, int int1, String string2) {
        LOGGER.error("Recoverable errors when loading section [" + chunkPos0.x + ", " + int1 + ", " + chunkPos0.z + "]: " + string2);
    }

    private static Codec<PalettedContainerRO<Holder<Biome>>> makeBiomeCodec(Registry<Biome> registryBiome0) {
        return PalettedContainer.codecRO(registryBiome0.asHolderIdMap(), registryBiome0.holderByNameCodec(), PalettedContainer.Strategy.SECTION_BIOMES, registryBiome0.getHolderOrThrow(Biomes.PLAINS));
    }

    public static CompoundTag write(ServerLevel serverLevel0, ChunkAccess chunkAccess1) {
        ChunkPos $$2 = chunkAccess1.getPos();
        CompoundTag $$3 = NbtUtils.addCurrentDataVersion(new CompoundTag());
        $$3.putInt("xPos", $$2.x);
        $$3.putInt("yPos", chunkAccess1.m_151560_());
        $$3.putInt("zPos", $$2.z);
        $$3.putLong("LastUpdate", serverLevel0.m_46467_());
        $$3.putLong("InhabitedTime", chunkAccess1.getInhabitedTime());
        $$3.putString("Status", BuiltInRegistries.CHUNK_STATUS.getKey(chunkAccess1.getStatus()).toString());
        BlendingData $$4 = chunkAccess1.getBlendingData();
        if ($$4 != null) {
            BlendingData.CODEC.encodeStart(NbtOps.INSTANCE, $$4).resultOrPartial(LOGGER::error).ifPresent(p_196909_ -> $$3.put("blending_data", p_196909_));
        }
        BelowZeroRetrogen $$5 = chunkAccess1.getBelowZeroRetrogen();
        if ($$5 != null) {
            BelowZeroRetrogen.CODEC.encodeStart(NbtOps.INSTANCE, $$5).resultOrPartial(LOGGER::error).ifPresent(p_188279_ -> $$3.put("below_zero_retrogen", p_188279_));
        }
        UpgradeData $$6 = chunkAccess1.getUpgradeData();
        if (!$$6.isEmpty()) {
            $$3.put("UpgradeData", $$6.write());
        }
        LevelChunkSection[] $$7 = chunkAccess1.getSections();
        ListTag $$8 = new ListTag();
        LevelLightEngine $$9 = serverLevel0.getChunkSource().getLightEngine();
        Registry<Biome> $$10 = serverLevel0.m_9598_().registryOrThrow(Registries.BIOME);
        Codec<PalettedContainerRO<Holder<Biome>>> $$11 = makeBiomeCodec($$10);
        boolean $$12 = chunkAccess1.isLightCorrect();
        for (int $$13 = $$9.getMinLightSection(); $$13 < $$9.getMaxLightSection(); $$13++) {
            int $$14 = chunkAccess1.m_151566_($$13);
            boolean $$15 = $$14 >= 0 && $$14 < $$7.length;
            DataLayer $$16 = $$9.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of($$2, $$13));
            DataLayer $$17 = $$9.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of($$2, $$13));
            if ($$15 || $$16 != null || $$17 != null) {
                CompoundTag $$18 = new CompoundTag();
                if ($$15) {
                    LevelChunkSection $$19 = $$7[$$14];
                    $$18.put("block_states", (Tag) BLOCK_STATE_CODEC.encodeStart(NbtOps.INSTANCE, $$19.getStates()).getOrThrow(false, LOGGER::error));
                    $$18.put("biomes", (Tag) $$11.encodeStart(NbtOps.INSTANCE, $$19.getBiomes()).getOrThrow(false, LOGGER::error));
                }
                if ($$16 != null && !$$16.isEmpty()) {
                    $$18.putByteArray("BlockLight", $$16.getData());
                }
                if ($$17 != null && !$$17.isEmpty()) {
                    $$18.putByteArray("SkyLight", $$17.getData());
                }
                if (!$$18.isEmpty()) {
                    $$18.putByte("Y", (byte) $$13);
                    $$8.add($$18);
                }
            }
        }
        $$3.put("sections", $$8);
        if ($$12) {
            $$3.putBoolean("isLightOn", true);
        }
        ListTag $$20 = new ListTag();
        for (BlockPos $$21 : chunkAccess1.getBlockEntitiesPos()) {
            CompoundTag $$22 = chunkAccess1.getBlockEntityNbtForSaving($$21);
            if ($$22 != null) {
                $$20.add($$22);
            }
        }
        $$3.put("block_entities", $$20);
        if (chunkAccess1.getStatus().getChunkType() == ChunkStatus.ChunkType.PROTOCHUNK) {
            ProtoChunk $$23 = (ProtoChunk) chunkAccess1;
            ListTag $$24 = new ListTag();
            $$24.addAll($$23.getEntities());
            $$3.put("entities", $$24);
            CompoundTag $$25 = new CompoundTag();
            for (GenerationStep.Carving $$26 : GenerationStep.Carving.values()) {
                CarvingMask $$27 = $$23.getCarvingMask($$26);
                if ($$27 != null) {
                    $$25.putLongArray($$26.toString(), $$27.toArray());
                }
            }
            $$3.put("CarvingMasks", $$25);
        }
        saveTicks(serverLevel0, $$3, chunkAccess1.getTicksForSerialization());
        $$3.put("PostProcessing", packOffsets(chunkAccess1.getPostProcessing()));
        CompoundTag $$28 = new CompoundTag();
        for (Entry<Heightmap.Types, Heightmap> $$29 : chunkAccess1.getHeightmaps()) {
            if (chunkAccess1.getStatus().heightmapsAfter().contains($$29.getKey())) {
                $$28.put(((Heightmap.Types) $$29.getKey()).getSerializationKey(), new LongArrayTag(((Heightmap) $$29.getValue()).getRawData()));
            }
        }
        $$3.put("Heightmaps", $$28);
        $$3.put("structures", packStructureData(StructurePieceSerializationContext.fromLevel(serverLevel0), $$2, chunkAccess1.getAllStarts(), chunkAccess1.getAllReferences()));
        return $$3;
    }

    private static void saveTicks(ServerLevel serverLevel0, CompoundTag compoundTag1, ChunkAccess.TicksToSave chunkAccessTicksToSave2) {
        long $$3 = serverLevel0.m_6106_().getGameTime();
        compoundTag1.put("block_ticks", chunkAccessTicksToSave2.blocks().save($$3, p_258987_ -> BuiltInRegistries.BLOCK.getKey(p_258987_).toString()));
        compoundTag1.put("fluid_ticks", chunkAccessTicksToSave2.fluids().save($$3, p_258989_ -> BuiltInRegistries.FLUID.getKey(p_258989_).toString()));
    }

    public static ChunkStatus.ChunkType getChunkTypeFromTag(@Nullable CompoundTag compoundTag0) {
        return compoundTag0 != null ? ChunkStatus.byName(compoundTag0.getString("Status")).getChunkType() : ChunkStatus.ChunkType.PROTOCHUNK;
    }

    @Nullable
    private static LevelChunk.PostLoadProcessor postLoadChunk(ServerLevel serverLevel0, CompoundTag compoundTag1) {
        ListTag $$2 = getListOfCompoundsOrNull(compoundTag1, "entities");
        ListTag $$3 = getListOfCompoundsOrNull(compoundTag1, "block_entities");
        return $$2 == null && $$3 == null ? null : p_196904_ -> {
            if ($$2 != null) {
                serverLevel0.addLegacyChunkEntities(EntityType.loadEntitiesRecursive($$2, serverLevel0));
            }
            if ($$3 != null) {
                for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                    CompoundTag $$5 = $$3.getCompound($$4);
                    boolean $$6 = $$5.getBoolean("keepPacked");
                    if ($$6) {
                        p_196904_.m_5604_($$5);
                    } else {
                        BlockPos $$7 = BlockEntity.getPosFromTag($$5);
                        BlockEntity $$8 = BlockEntity.loadStatic($$7, p_196904_.getBlockState($$7), $$5);
                        if ($$8 != null) {
                            p_196904_.setBlockEntity($$8);
                        }
                    }
                }
            }
        };
    }

    @Nullable
    private static ListTag getListOfCompoundsOrNull(CompoundTag compoundTag0, String string1) {
        ListTag $$2 = compoundTag0.getList(string1, 10);
        return $$2.isEmpty() ? null : $$2;
    }

    private static CompoundTag packStructureData(StructurePieceSerializationContext structurePieceSerializationContext0, ChunkPos chunkPos1, Map<Structure, StructureStart> mapStructureStructureStart2, Map<Structure, LongSet> mapStructureLongSet3) {
        CompoundTag $$4 = new CompoundTag();
        CompoundTag $$5 = new CompoundTag();
        Registry<Structure> $$6 = structurePieceSerializationContext0.registryAccess().registryOrThrow(Registries.STRUCTURE);
        for (Entry<Structure, StructureStart> $$7 : mapStructureStructureStart2.entrySet()) {
            ResourceLocation $$8 = $$6.getKey((Structure) $$7.getKey());
            $$5.put($$8.toString(), ((StructureStart) $$7.getValue()).createTag(structurePieceSerializationContext0, chunkPos1));
        }
        $$4.put("starts", $$5);
        CompoundTag $$9 = new CompoundTag();
        for (Entry<Structure, LongSet> $$10 : mapStructureLongSet3.entrySet()) {
            if (!((LongSet) $$10.getValue()).isEmpty()) {
                ResourceLocation $$11 = $$6.getKey((Structure) $$10.getKey());
                $$9.put($$11.toString(), new LongArrayTag((LongSet) $$10.getValue()));
            }
        }
        $$4.put("References", $$9);
        return $$4;
    }

    private static Map<Structure, StructureStart> unpackStructureStart(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1, long long2) {
        Map<Structure, StructureStart> $$3 = Maps.newHashMap();
        Registry<Structure> $$4 = structurePieceSerializationContext0.registryAccess().registryOrThrow(Registries.STRUCTURE);
        CompoundTag $$5 = compoundTag1.getCompound("starts");
        for (String $$6 : $$5.getAllKeys()) {
            ResourceLocation $$7 = ResourceLocation.tryParse($$6);
            Structure $$8 = $$4.get($$7);
            if ($$8 == null) {
                LOGGER.error("Unknown structure start: {}", $$7);
            } else {
                StructureStart $$9 = StructureStart.loadStaticStart(structurePieceSerializationContext0, $$5.getCompound($$6), long2);
                if ($$9 != null) {
                    $$3.put($$8, $$9);
                }
            }
        }
        return $$3;
    }

    private static Map<Structure, LongSet> unpackStructureReferences(RegistryAccess registryAccess0, ChunkPos chunkPos1, CompoundTag compoundTag2) {
        Map<Structure, LongSet> $$3 = Maps.newHashMap();
        Registry<Structure> $$4 = registryAccess0.registryOrThrow(Registries.STRUCTURE);
        CompoundTag $$5 = compoundTag2.getCompound("References");
        for (String $$6 : $$5.getAllKeys()) {
            ResourceLocation $$7 = ResourceLocation.tryParse($$6);
            Structure $$8 = $$4.get($$7);
            if ($$8 == null) {
                LOGGER.warn("Found reference to unknown structure '{}' in chunk {}, discarding", $$7, chunkPos1);
            } else {
                long[] $$9 = $$5.getLongArray($$6);
                if ($$9.length != 0) {
                    $$3.put($$8, new LongOpenHashSet(Arrays.stream($$9).filter(p_208153_ -> {
                        ChunkPos $$3x = new ChunkPos(p_208153_);
                        if ($$3x.getChessboardDistance(chunkPos1) > 8) {
                            LOGGER.warn("Found invalid structure reference [ {} @ {} ] for chunk {}.", new Object[] { $$7, $$3x, chunkPos1 });
                            return false;
                        } else {
                            return true;
                        }
                    }).toArray()));
                }
            }
        }
        return $$3;
    }

    public static ListTag packOffsets(ShortList[] shortList0) {
        ListTag $$1 = new ListTag();
        for (ShortList $$2 : shortList0) {
            ListTag $$3 = new ListTag();
            if ($$2 != null) {
                ShortListIterator var7 = $$2.iterator();
                while (var7.hasNext()) {
                    Short $$4 = (Short) var7.next();
                    $$3.add(ShortTag.valueOf($$4));
                }
            }
            $$1.add($$3);
        }
        return $$1;
    }
}