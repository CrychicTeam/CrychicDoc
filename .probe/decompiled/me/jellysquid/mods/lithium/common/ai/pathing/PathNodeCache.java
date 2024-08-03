package me.jellysquid.mods.lithium.common.ai.pathing;

import me.jellysquid.mods.lithium.common.block.BlockCountingSection;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.util.Pos;
import me.jellysquid.mods.lithium.common.world.ChunkView;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public abstract class PathNodeCache {

    private static boolean isChunkSectionDangerousNeighbor(LevelChunkSection section) {
        return section.getStates().maybeHas(state -> getNeighborPathNodeType(state) != BlockPathTypes.OPEN);
    }

    public static BlockPathTypes getPathNodeType(BlockState state) {
        return ((BlockStatePathingCache) state).getPathNodeType();
    }

    public static BlockPathTypes getNeighborPathNodeType(BlockBehaviour.BlockStateBase state) {
        return ((BlockStatePathingCache) state).getNeighborPathNodeType();
    }

    public static boolean isSectionSafeAsNeighbor(LevelChunkSection section) {
        if (section.hasOnlyAir()) {
            return true;
        } else {
            return BlockStateFlags.ENABLED ? !((BlockCountingSection) section).mayContainAny(BlockStateFlags.PATH_NOT_OPEN) : !isChunkSectionDangerousNeighbor(section);
        }
    }

    public static BlockPathTypes getNodeTypeFromNeighbors(BlockGetter world, BlockPos.MutableBlockPos pos, BlockPathTypes type) {
        int x = pos.m_123341_();
        int y = pos.m_123342_();
        int z = pos.m_123343_();
        LevelChunkSection section = null;
        if (world instanceof ChunkView chunkView && WorldHelper.areNeighborsWithinSameChunkSection(pos)) {
            if (!world.m_151562_(y)) {
                ChunkAccess chunk = chunkView.getLoadedChunk(Pos.ChunkCoord.fromBlockCoord(x), Pos.ChunkCoord.fromBlockCoord(z));
                if (chunk != null) {
                    section = chunk.getSections()[Pos.SectionYIndex.fromBlockCoord(world, y)];
                }
            }
            if (section == null || isSectionSafeAsNeighbor(section)) {
                return type;
            }
        }
        int xStart = x - 1;
        int yStart = y - 1;
        int zStart = z - 1;
        int xEnd = x + 1;
        int yEnd = y + 1;
        int zEnd = z + 1;
        for (int adjX = xStart; adjX <= xEnd; adjX++) {
            for (int adjY = yStart; adjY <= yEnd; adjY++) {
                for (int adjZ = zStart; adjZ <= zEnd; adjZ++) {
                    if (adjX != x || adjZ != z) {
                        BlockState state;
                        if (section != null) {
                            state = section.getBlockState(adjX & 15, adjY & 15, adjZ & 15);
                        } else {
                            state = world.getBlockState(pos.set(adjX, adjY, adjZ));
                        }
                        if (!state.m_60795_()) {
                            BlockPathTypes neighborType = getNeighborPathNodeType(state);
                            if (neighborType == null) {
                                neighborType = WalkNodeEvaluator.checkNeighbourBlocks(world, pos, null);
                                if (neighborType == null) {
                                    neighborType = BlockPathTypes.OPEN;
                                }
                            }
                            if (neighborType != BlockPathTypes.OPEN) {
                                return neighborType;
                            }
                        }
                    }
                }
            }
        }
        return type;
    }
}