package com.mna.api.rituals;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class RitualBlockPos {

    private int index;

    private int displayIndex;

    private BlockPos blockPos;

    private IRitualReagent requiredReagent;

    public RitualBlockPos(int index, int displayIndex, BlockPos pos, IRitualReagent requiredReagent) {
        this.index = index;
        this.displayIndex = displayIndex;
        this.blockPos = pos;
        this.requiredReagent = requiredReagent;
    }

    public int getIndex() {
        return this.index;
    }

    public int getDisplayIndex() {
        return this.displayIndex;
    }

    @Nullable
    public IRitualReagent getReagent() {
        return this.requiredReagent;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public boolean isPresent() {
        return this.requiredReagent != null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RitualBlockPos other)) {
            return obj instanceof BlockPos ? ((BlockPos) obj).equals(this.getBlockPos()) : false;
        } else {
            return other.index == this.index && other.displayIndex == this.displayIndex && other.blockPos.equals(this.blockPos);
        }
    }
}