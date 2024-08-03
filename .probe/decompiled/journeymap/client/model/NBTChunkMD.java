package journeymap.client.model;

import journeymap.common.helper.BiomeHelper;
import journeymap.common.nbt.RegionData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class NBTChunkMD extends ChunkMD {

    final CompoundTag data;

    final LevelChunk chunk;

    final ChunkPos chunkPos;

    final MapType mapType;

    public NBTChunkMD(LevelChunk chunk, ChunkPos chunkPos, CompoundTag data, MapType mapType) {
        super(chunk, false);
        this.chunk = chunk;
        this.mapType = mapType;
        this.data = data;
        this.chunkPos = chunkPos;
    }

    @Override
    public boolean hasChunk() {
        return this.data != null;
    }

    @Override
    public LevelChunk getChunk() {
        return this.chunk;
    }

    @Override
    public int getPrecipitationHeight(int localX, int localZ) {
        return this.getPrecipitationHeight(this.getBlockPos(localX, 0, localZ));
    }

    @Override
    public BlockState getChunkBlockState(BlockPos blockPos) {
        return this.getBlockState(blockPos);
    }

    @Override
    public int getSavedLightValue(int localX, int y, int localZ) {
        BlockPos pos = this.getBlockPos(localX, y, localZ);
        return this.getGetLightValue(pos);
    }

    @Override
    public BlockMD getBlockMD(BlockPos blockPos) {
        return BlockMD.get(this.getBlockState(blockPos));
    }

    @Override
    public boolean canBlockSeeTheSky(int localX, int y, int localZ) {
        return !this.mapType.isUnderground();
    }

    @Override
    public int getPrecipitationHeight(BlockPos blockPos) {
        return this.getTopY(blockPos);
    }

    @Override
    public int getHeight(BlockPos blockPos) {
        BlockPos pos = new BlockPos(this.toWorldX(blockPos.m_123341_()), blockPos.m_123342_(), this.toWorldZ(blockPos.m_123343_()));
        Integer surfaceY = this.getSurfaceY(pos);
        if (surfaceY == null) {
            surfaceY = this.getTopY(pos);
        }
        return surfaceY;
    }

    @Override
    public BlockMD getBlockMD(int localX, int y, int localZ) {
        return BlockMD.get(this.getBlockState(this.getBlockPos(localX, y, localZ)));
    }

    @Override
    public Biome getBiome(BlockPos blockPos) {
        CompoundTag blockData = this.getBlockNBT(blockPos);
        if (blockData.contains("biome_name")) {
            String biomeName = blockData.getString("biome_name");
            return BiomeHelper.getBiomeFromResourceString(biomeName);
        } else {
            return null;
        }
    }

    public Integer getTopY(BlockPos blockPos) {
        CompoundTag blockData = this.getBlockNBT(blockPos);
        return blockData.contains("top_y") ? blockData.getInt("top_y") : blockPos.m_123342_();
    }

    public Integer getGetLightValue(BlockPos blockPos) {
        CompoundTag blockData = this.getBlockNBT(blockPos);
        return blockData.contains("light_value") ? blockData.getInt("light_value") : 0;
    }

    private Integer getSurfaceY(BlockPos blockPos) {
        CompoundTag blockData = this.getBlockNBT(blockPos);
        return blockData.contains("surface_y") ? blockData.getInt("surface_y") : null;
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos) {
        CompoundTag blockData = this.getBlockNBT(blockPos);
        return RegionData.getBlockState(blockData, blockPos, this.mapType);
    }

    private CompoundTag getBlockNBT(BlockPos blockPos) {
        return RegionData.getBlockDataForChunk(this.data, blockPos.m_123341_(), blockPos.m_123343_());
    }

    @Override
    public boolean fromNbt() {
        return true;
    }
}