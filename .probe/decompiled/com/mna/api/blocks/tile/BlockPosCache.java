package com.mna.api.blocks.tile;

import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class BlockPosCache {

    public static final int MAX_BLOCKS_CHECKED_PER_TICK = 50;

    private AABB powerSearchArea = null;

    private NonNullList<BlockPos> cachedBlocks;

    private boolean searchingSurroundings = false;

    private BlockPos lastSearchPos;

    int searchRadius;

    boolean finishedSearchingThisTick = false;

    private final BlockEntity parent;

    private final BiPredicate<BlockPos, Level> blockValidCheck;

    public BlockPosCache(BlockEntity parent, int radius, BiPredicate<BlockPos, Level> blockValid) {
        this.searchRadius = radius;
        this.cachedBlocks = NonNullList.create();
        this.parent = parent;
        this.blockValidCheck = blockValid;
    }

    public void tick() {
        this.finishedSearchingThisTick = false;
        if (this.searchingSurroundings) {
            this.search();
        }
    }

    public void search() {
        int x = this.lastSearchPos.m_123341_();
        int y = this.lastSearchPos.m_123342_();
        int z = this.lastSearchPos.m_123343_();
        for (int count = 0; (double) x <= this.powerSearchArea.maxX; x++) {
            while ((double) y <= this.powerSearchArea.maxY) {
                for (; (double) z <= this.powerSearchArea.maxZ; z++) {
                    BlockPos curSearch = new BlockPos(x, y, z);
                    if (!curSearch.equals(this.parent.getBlockPos())) {
                        if (this.parent.getLevel().isLoaded(curSearch) && this.blockValidCheck.test(curSearch, this.parent.getLevel())) {
                            this.cachedBlocks.add(curSearch);
                        }
                        if (++count >= 50) {
                            this.lastSearchPos = new BlockPos(x, y, z);
                            return;
                        }
                    }
                }
                z = (int) this.powerSearchArea.minZ;
                y++;
            }
            y = (int) this.powerSearchArea.minY;
        }
        this.searchingSurroundings = false;
        this.finishedSearchingThisTick = true;
    }

    public NonNullList<BlockPos> getCachedPositions() {
        return this.cachedBlocks;
    }

    public void queueRecheck() {
        this.searchingSurroundings = true;
        this.cachedBlocks.clear();
        BlockPos myPos = this.parent.getBlockPos();
        this.powerSearchArea = new AABB(myPos).inflate((double) this.searchRadius);
        this.lastSearchPos = new BlockPos((int) this.powerSearchArea.minX, (int) this.powerSearchArea.minY, (int) this.powerSearchArea.minZ);
    }

    public boolean isSearching() {
        return this.searchingSurroundings;
    }

    public boolean finishedSearchingThisTick() {
        return this.finishedSearchingThisTick;
    }

    public void insert(BlockPos blockPos) {
        if (!this.cachedBlocks.contains(blockPos) && !blockPos.equals(this.parent.getBlockPos()) && this.blockValidCheck.test(blockPos, this.parent.getLevel())) {
            this.cachedBlocks.add(blockPos);
        }
    }

    public void remove(BlockPos blockpos) {
        this.cachedBlocks.remove(blockpos);
    }
}