package journeymap.common.nbt;

import journeymap.client.JourneymapClient;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.common.helper.BiomeHelper;
import journeymap.common.nbt.cache.CacheStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RegionData {

    public static final String BIOME_TAG_NAME = "biome_name";

    public static final String BLOCK_TAG_NAME = "block";

    public static final String BLOCKSTATES_TAG_NAME = "blockstates";

    public static final String BLOCK_LIGHT_VALUE = "light_value";

    public static final String TOP_Y_TAG_NAME = "top_y";

    public static final String SURFACE_Y_TAG_NAME = "surface_y";

    public static final String BLOCK_COLOR_TAG_NAME = "block_color_";

    public static final String CHUNK_POS_NAME = "pos";

    protected final RegionCoord regionCoord;

    protected final MapType mapType;

    protected static boolean enabled;

    protected boolean loaded = false;

    protected final RegionDataStorageHandler.Key key;

    private final CacheStorage storage;

    public RegionData(RegionDataStorageHandler.Key key, CacheStorage storage) {
        this.regionCoord = key.rCoord;
        this.mapType = key.mapType;
        this.key = key;
        this.storage = storage;
        enabled = JourneymapClient.getInstance().getCoreProperties().dataCachingEnabled.get();
    }

    public Biome getBiome(BlockPos blockPos) {
        if (enabled) {
            CompoundTag blockData = this.getBlockDataFromBlockPos(blockPos);
            if (blockData.contains("biome_name")) {
                String biomeName = blockData.getString("biome_name");
                return BiomeHelper.getBiomeFromResourceString(biomeName);
            }
        }
        return null;
    }

    public Integer getTopY(BlockPos blockPos) {
        if (enabled) {
            CompoundTag blockData = this.getBlockDataFromBlockPos(blockPos);
            if (blockData.contains("top_y")) {
                return blockData.getInt("top_y");
            }
        }
        return blockPos.m_123342_();
    }

    public Integer getColor(BlockPos blockPos) {
        if (enabled) {
            CompoundTag blockData = this.getBlockDataFromBlockPos(blockPos);
            if (blockData.contains("block_color_")) {
                return blockData.getInt("block_color_");
            }
        }
        return null;
    }

    public BlockState getBlockState(BlockPos blockPos) {
        if (enabled) {
            CompoundTag blockData = this.getBlockDataFromBlockPos(blockPos);
            return getBlockState(blockData, blockPos, this.mapType);
        } else {
            return null;
        }
    }

    public static BlockState getBlockState(CompoundTag blockData, BlockPos blockPos, MapType mapType) {
        if (enabled) {
            ClientLevel level = Minecraft.getInstance().level;
            if (blockData.contains("blockstates") && !mapType.isUnderground()) {
                CompoundTag states = getBlockStates(blockData, mapType);
                String key = String.valueOf(blockPos.m_123342_());
                if (states.contains(key)) {
                    CompoundTag blockState = states.getCompound(key);
                    return NbtUtils.readBlockState(level.m_246945_(Registries.BLOCK), blockState);
                }
                if (states.getAllKeys().size() > 0 && states.getAllKeys().stream().findFirst().isPresent()) {
                    CompoundTag blockState = states.getCompound((String) states.getAllKeys().stream().findFirst().get());
                    return NbtUtils.readBlockState(level.m_246945_(Registries.BLOCK), blockState);
                }
            } else if (blockData.contains("block")) {
                CompoundTag blockState = blockData.getCompound("block");
                BlockState state = NbtUtils.readBlockState(level.m_246945_(Registries.BLOCK), blockState);
                if (!mapType.isUnderground()) {
                    setBlockState(blockData, state, blockPos, mapType);
                }
                return state;
            }
        }
        return null;
    }

    public void setBiome(CompoundTag blockData, Biome biome) {
        if (enabled) {
            ResourceLocation biomeResource = BiomeHelper.getBiomeResource(biome);
            if (biomeResource != null) {
                if (blockData.contains("biome_name")) {
                    String biomeName = blockData.getString("biome_name");
                    if (biomeName.equals(biomeResource.toString())) {
                        return;
                    }
                }
                blockData.putString("biome_name", biomeResource.toString());
            }
        }
    }

    public void setBlockState(CompoundTag blockData, ChunkMD chunkMD, BlockPos pos) {
        BlockMD blockMD = chunkMD.getBlockMD(pos);
        if (blockMD.hasNoShadow() && !blockMD.isIgnore() && !blockMD.isWater() && !blockMD.isFluid() && !blockMD.getBlockState().m_61147_().contains(DoublePlantBlock.HALF) && !this.mapType.isUnderground()) {
            setBlockState(blockData, chunkMD.getBlockMD(pos.below()).getBlockState(), pos.below(), this.mapType);
        }
        setBlockState(blockData, blockMD.getBlockState(), pos, this.mapType);
    }

    private static void setBlockState(CompoundTag blockData, BlockState state, BlockPos pos, MapType mapType) {
        if (enabled && blockData != null) {
            if (mapType.isUnderground()) {
                CompoundTag blockState = NbtUtils.writeBlockState(state);
                blockData.put("block", blockState);
            } else {
                CompoundTag states = getBlockStates(blockData, mapType);
                CompoundTag blockState = NbtUtils.writeBlockState(state);
                states.put(String.valueOf(pos.m_123342_()), blockState);
                blockData.put("blockstates", states);
            }
        }
    }

    public void setY(CompoundTag blockData, int topY) {
        if (enabled) {
            if (blockData.contains("top_y")) {
                int savedTopY = blockData.getInt("top_y");
                if (savedTopY == topY) {
                    return;
                }
            }
            blockData.putInt("top_y", topY);
        }
    }

    public void setLightValue(CompoundTag blockData, int light) {
        if (enabled) {
            if (blockData.contains("light_value")) {
                int savedLight = blockData.getInt("light_value");
                if (savedLight == light) {
                    return;
                }
            }
            blockData.putInt("light_value", light);
        }
    }

    public void setSurfaceY(CompoundTag blockData, int surfaceY) {
        if (enabled) {
            if (blockData.contains("surface_y")) {
                int savedTopY = blockData.getInt("surface_y");
                if (savedTopY == surfaceY) {
                    return;
                }
            }
            blockData.putInt("surface_y", surfaceY);
        }
    }

    public void setBlockColor(CompoundTag blockData, int color, MapType.Name mapTypeName) {
    }

    private static CompoundTag getBlockStates(CompoundTag blockData, MapType mapType) {
        if (enabled) {
            if (mapType.isUnderground()) {
                if (blockData.contains("block")) {
                    return blockData.getCompound("block");
                }
            } else if (blockData.contains("blockstates")) {
                return blockData.getCompound("blockstates");
            }
            return new CompoundTag();
        } else {
            return null;
        }
    }

    public CompoundTag getBlockDataFromBlockPos(ChunkPos chunkPos, CompoundTag chunk, int x, int z) {
        return enabled && chunk != null ? getBlockDataForChunk(chunk, (chunkPos.x << 4) + x, (chunkPos.z << 4) + z) : null;
    }

    public CompoundTag getBlockDataFromBlockPos(ChunkPos chunkPos, int x, int z) {
        CompoundTag chunk = this.getChunkNbt(chunkPos);
        return enabled && chunk != null ? getBlockDataForChunk(chunk, (chunkPos.x << 4) + x, (chunkPos.z << 4) + z) : null;
    }

    private CompoundTag getBlockDataFromBlockPos(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        CompoundTag chunk = this.getChunkNbt(chunkPos);
        return getBlockDataForChunk(chunk, pos.m_123341_(), pos.m_123343_());
    }

    public CompoundTag getChunkNbt(ChunkPos chunkPos) {
        if (enabled) {
            CompoundTag chunkTag = this.storage.read(chunkPos);
            if (chunkTag == null) {
                chunkTag = new CompoundTag();
            }
            if (!chunkTag.contains("pos")) {
                chunkTag.putLong("pos", chunkPos.toLong());
            }
            return chunkTag;
        } else {
            return null;
        }
    }

    public static CompoundTag getBlockDataForChunk(CompoundTag chunk, int x, int z) {
        if (chunk != null) {
            String blockTagName = x + "," + z;
            CompoundTag blockTag;
            if (chunk.contains(blockTagName)) {
                blockTag = chunk.getCompound(blockTagName);
            } else {
                blockTag = new CompoundTag();
                chunk.put(blockTagName, blockTag);
            }
            return blockTag;
        } else {
            return null;
        }
    }

    public void writeChunk(ChunkPos chunkPos, CompoundTag tag) {
        this.storage.write(chunkPos, tag);
    }
}