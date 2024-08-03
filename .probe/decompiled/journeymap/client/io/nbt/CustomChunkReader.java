package journeymap.client.io.nbt;

import com.mojang.serialization.Codec;
import java.util.EnumSet;
import journeymap.common.Journeymap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.Logger;

public class CustomChunkReader {

    private static final Logger logger = Journeymap.getLogger();

    public static CustomChunkReader.ProcessedChunk read(ServerLevel level, PoiManager manager, ChunkPos chunkPos, CompoundTag chunkTag) {
        if (ChunkStatus.ChunkType.LEVELCHUNK != ChunkSerializer.getChunkTypeFromTag(chunkTag)) {
            return null;
        } else {
            byte[][][][] lights = new byte[24][16][16][16];
            boolean lightOn = chunkTag.getBoolean("isLightOn");
            ListTag sections = chunkTag.getList("sections", 10);
            int sectionsCount = level.m_151559_();
            LevelChunkSection[] chunkSections = new LevelChunkSection[sectionsCount];
            Registry<Biome> registry = level.m_9598_().registryOrThrow(Registries.BIOME);
            Codec<PalettedContainerRO<Holder<Biome>>> codec = ChunkSerializer.makeBiomeCodec(registry);
            for (int j = 0; j < sections.size(); j++) {
                CompoundTag section = sections.getCompound(j);
                int sectionTopY = section.getByte("Y");
                int sectionIndex = level.m_151566_(sectionTopY);
                if (sectionIndex >= 0 && sectionIndex < chunkSections.length) {
                    PalettedContainer<BlockState> blockStateContainer;
                    if (section.contains("block_states", 10)) {
                        blockStateContainer = (PalettedContainer<BlockState>) ChunkSerializer.BLOCK_STATE_CODEC.parse(NbtOps.INSTANCE, section.getCompound("block_states")).promotePartial(message -> ChunkSerializer.logErrors(chunkPos, sectionTopY, message)).getOrThrow(false, logger::error);
                    } else {
                        blockStateContainer = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
                    }
                    PalettedContainerRO<Holder<Biome>> biomeContainer;
                    if (section.contains("biomes", 10)) {
                        biomeContainer = (PalettedContainerRO<Holder<Biome>>) codec.parse(NbtOps.INSTANCE, section.getCompound("biomes")).promotePartial(message -> ChunkSerializer.logErrors(chunkPos, sectionTopY, message)).getOrThrow(false, logger::error);
                    } else {
                        biomeContainer = new PalettedContainer<>(registry.asHolderIdMap(), registry.getHolderOrThrow(Biomes.PLAINS), PalettedContainer.Strategy.SECTION_BIOMES);
                    }
                    LevelChunkSection chunkSection = new LevelChunkSection(blockStateContainer, biomeContainer);
                    chunkSections[sectionIndex] = chunkSection;
                    manager.checkConsistencyWithBlocks(SectionPos.of(chunkPos, sectionTopY), chunkSection);
                }
                if (lightOn && section.contains("BlockLight", 7)) {
                    SectionPos lightPos = SectionPos.of(chunkPos, sectionTopY);
                    byte[] lightsArray = section.getByteArray("BlockLight");
                    for (int z = 0; z <= 15; z++) {
                        for (int x = 0; x <= 15; x++) {
                            for (int y = lightPos.minBlockY(); y <= lightPos.maxBlockY(); y++) {
                                if (lightsArray.length == 2048) {
                                    int localY = SectionPos.sectionRelative(y);
                                    lights[j][x][localY][z] = getSectionLightValue(lightsArray, x, localY, z);
                                }
                            }
                        }
                    }
                }
            }
            ChunkAccess chunkAccess = new LevelChunk(level.getLevel(), chunkPos, null, null, null, 0L, chunkSections, null, null);
            chunkAccess.setLightCorrect(lightOn);
            CompoundTag heightmaps = chunkTag.getCompound("Heightmaps");
            EnumSet<Heightmap.Types> heightmapEnums = EnumSet.noneOf(Heightmap.Types.class);
            for (Heightmap.Types heightmapTypes : chunkAccess.getStatus().heightmapsAfter()) {
                String key = heightmapTypes.getSerializationKey();
                if (heightmaps.contains(key, 12)) {
                    chunkAccess.setHeightmap(heightmapTypes, heightmaps.getLongArray(key));
                } else {
                    heightmapEnums.add(heightmapTypes);
                }
            }
            Heightmap.primeHeightmaps(chunkAccess, heightmapEnums);
            return new CustomChunkReader.ProcessedChunk((LevelChunk) chunkAccess, lights);
        }
    }

    private static byte getSectionLightValue(byte[] array, int x, int y, int z) {
        try {
            if (array != null) {
                int index = y << 8 | (z << 4) + x;
                int byteIndex = index >> 1;
                int nibbleIndex = index & 1;
                return (byte) (array[byteIndex] >> 4 * nibbleIndex & 15);
            }
        } catch (Exception var7) {
        }
        return 0;
    }

    public static record ProcessedChunk(LevelChunk chunk, byte[][][][] light) {
    }
}