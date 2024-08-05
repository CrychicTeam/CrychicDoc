package com.rekindled.embers.util;

import com.google.common.collect.HashMultimap;
import com.rekindled.embers.blockentity.ExplosionPedestalBlockEntity;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExplosionCharmWorldInfo {

    HashMultimap<ChunkPos, BlockPos> data = HashMultimap.create();

    public void put(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        this.data.put(chunkPos, pos);
    }

    public BlockPos getClosestExplosionCharm(Level world, BlockPos pos, int radius) {
        BlockPos chosen = null;
        double minDistance = Double.POSITIVE_INFINITY;
        ChunkPos chunkPosA = new ChunkPos(pos.offset(-radius, 0, -radius));
        ChunkPos chunkPosB = new ChunkPos(pos.offset(radius, 0, radius));
        for (int x = chunkPosA.x; x <= chunkPosB.x; x++) {
            for (int z = chunkPosA.z; z <= chunkPosB.z; z++) {
                ChunkPos chunkPos = new ChunkPos(x, z);
                Iterator<BlockPos> iterator = this.data.get(chunkPos).iterator();
                while (iterator.hasNext()) {
                    BlockPos testpos = (BlockPos) iterator.next();
                    double testdist = testpos.m_203198_((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
                    if (!(testdist >= minDistance) && !(testdist > (double) (radius * radius))) {
                        BlockEntity tile = world.getBlockEntity(testpos);
                        if (tile instanceof ExplosionPedestalBlockEntity && !tile.isRemoved()) {
                            chosen = testpos;
                            minDistance = testdist;
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return chosen;
    }
}