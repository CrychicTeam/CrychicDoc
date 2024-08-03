package com.mna.network.messages;

import net.minecraft.core.BlockPos;

public abstract class TileEntityMessage extends BaseMessage {

    protected BlockPos pos;

    protected TileEntityMessage(BlockPos pos) {
        this.pos = pos;
    }

    public final BlockPos getPosition() {
        return this.pos;
    }
}