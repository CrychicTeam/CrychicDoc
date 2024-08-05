package net.minecraft.server.level;

import net.minecraft.core.BlockPos;

public class BlockDestructionProgress implements Comparable<BlockDestructionProgress> {

    private final int id;

    private final BlockPos pos;

    private int progress;

    private int updatedRenderTick;

    public BlockDestructionProgress(int int0, BlockPos blockPos1) {
        this.id = int0;
        this.pos = blockPos1;
    }

    public int getId() {
        return this.id;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setProgress(int int0) {
        if (int0 > 10) {
            int0 = 10;
        }
        this.progress = int0;
    }

    public int getProgress() {
        return this.progress;
    }

    public void updateTick(int int0) {
        this.updatedRenderTick = int0;
    }

    public int getUpdatedRenderTick() {
        return this.updatedRenderTick;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            BlockDestructionProgress $$1 = (BlockDestructionProgress) object0;
            return this.id == $$1.id;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    public int compareTo(BlockDestructionProgress blockDestructionProgress0) {
        return this.progress != blockDestructionProgress0.progress ? Integer.compare(this.progress, blockDestructionProgress0.progress) : Integer.compare(this.id, blockDestructionProgress0.id);
    }
}