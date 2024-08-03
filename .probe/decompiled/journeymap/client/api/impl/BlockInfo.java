package journeymap.client.api.impl;

import journeymap.client.api.model.IBlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class BlockInfo implements IBlockInfo {

    private BlockPos blockPos;

    private Block block;

    private BlockState blockState;

    private Biome biome;

    private LevelChunk chunk;

    private ChunkPos chunkPos;

    private Integer regionX;

    private Integer regionZ;

    private BlockInfo(BlockInfo.Builder builder) {
        this.blockPos = builder.blockPos;
        this.block = builder.block;
        this.blockState = builder.blockState;
        this.biome = builder.biome;
        this.chunk = builder.chunk;
        this.chunkPos = builder.chunkPos;
        this.regionX = builder.regionX;
        this.regionZ = builder.regionZ;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public Biome getBiome() {
        return this.biome;
    }

    @Override
    public LevelChunk getChunk() {
        return this.chunk;
    }

    @Override
    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    @Override
    public Integer getRegionX() {
        return this.regionX;
    }

    @Override
    public Integer getRegionZ() {
        return this.regionZ;
    }

    public static BlockInfo.Builder builder() {
        return new BlockInfo.Builder();
    }

    public static class Builder {

        private BlockPos blockPos;

        private Block block;

        private BlockState blockState;

        private Biome biome;

        private LevelChunk chunk;

        private ChunkPos chunkPos;

        private Integer regionX;

        private Integer regionZ;

        public BlockInfo build() {
            return new BlockInfo(this);
        }

        public BlockInfo.Builder withBlockPos(BlockPos blockPos) {
            this.blockPos = blockPos;
            return this;
        }

        public BlockInfo.Builder withBlock(Block block) {
            this.block = block;
            return this;
        }

        public BlockInfo.Builder withBlockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public BlockInfo.Builder withBiome(Biome biome) {
            this.biome = biome;
            return this;
        }

        public BlockInfo.Builder withChunk(LevelChunk chunk) {
            this.chunk = chunk;
            return this;
        }

        public BlockInfo.Builder withChunkPos(ChunkPos chunkPos) {
            this.chunkPos = chunkPos;
            return this;
        }

        public BlockInfo.Builder withRegionX(Integer regionX) {
            this.regionX = regionX;
            return this;
        }

        public BlockInfo.Builder withRegionZ(Integer regionZ) {
            this.regionZ = regionZ;
            return this;
        }
    }
}