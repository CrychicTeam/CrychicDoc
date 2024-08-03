package com.rekindled.embers.worldgen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class CaveStructurePiece extends PoolElementStructurePiece {

    public Structure.GenerationContext context;

    public int maxHeight;

    public CaveStructurePiece(StructureTemplateManager pStructureTemplateManager, StructurePoolElement pElement, BlockPos pPosition, int pGroundLevelDelta, Rotation pRotation, BoundingBox pBox, Structure.GenerationContext context, int maxHeight) {
        super(pStructureTemplateManager, pElement, pPosition, pGroundLevelDelta, pRotation, pBox);
        this.context = context;
        this.maxHeight = maxHeight;
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        BlockPos center = StructureTemplate.transform(new BlockPos(this.m_73547_().getXSpan() / 2, 0, this.m_73547_().getZSpan() / 2), Mirror.NONE, this.m_6830_(), BlockPos.ZERO).offset(this.f_72598_);
        int caveHeight = getCaveFloor(center, this.maxHeight, worldGenLevel, this.context);
        if (caveHeight != Integer.MIN_VALUE) {
            this.f_72598_ = new BlockPos(this.f_72598_.m_123341_(), caveHeight, this.f_72598_.m_123343_());
            super.postProcess(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, new BlockPos(pos.m_123341_(), caveHeight, pos.m_123343_()));
        }
    }

    public static int getCaveFloor(BlockPos endPos, int maxHeight, WorldGenLevel level, Structure.GenerationContext context) {
        List<Integer> heights = new ArrayList();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(endPos.m_123341_(), level.m_141937_() + 9, endPos.m_123343_());
        for (int i = level.m_141937_() + 9; i <= endPos.m_123342_() && i <= maxHeight; i++) {
            boolean solid = !level.m_8055_(pos).m_247087_();
            pos.setY(i + 1);
            boolean replaceable = level.m_8055_(pos).m_247087_();
            if (solid && replaceable && isValidBiome(pos, context)) {
                heights.add(i);
            }
        }
        if (heights.isEmpty()) {
            return Integer.MIN_VALUE;
        } else {
            return heights.size() > 1 ? (Integer) heights.get(1) : (Integer) heights.get(0);
        }
    }

    public static boolean isValidBiome(BlockPos blockpos, Structure.GenerationContext pContext) {
        return pContext.validBiome().test(pContext.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(blockpos.m_123341_()), QuartPos.fromBlock(blockpos.m_123342_()), QuartPos.fromBlock(blockpos.m_123343_()), pContext.randomState().sampler()));
    }
}