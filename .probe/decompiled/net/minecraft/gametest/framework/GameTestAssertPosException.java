package net.minecraft.gametest.framework;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class GameTestAssertPosException extends GameTestAssertException {

    private final BlockPos absolutePos;

    private final BlockPos relativePos;

    private final long tick;

    public GameTestAssertPosException(String string0, BlockPos blockPos1, BlockPos blockPos2, long long3) {
        super(string0);
        this.absolutePos = blockPos1;
        this.relativePos = blockPos2;
        this.tick = long3;
    }

    public String getMessage() {
        String $$0 = this.absolutePos.m_123341_() + "," + this.absolutePos.m_123342_() + "," + this.absolutePos.m_123343_() + " (relative: " + this.relativePos.m_123341_() + "," + this.relativePos.m_123342_() + "," + this.relativePos.m_123343_() + ")";
        return super.getMessage() + " at " + $$0 + " (t=" + this.tick + ")";
    }

    @Nullable
    public String getMessageToShowAtBlock() {
        return super.getMessage();
    }

    @Nullable
    public BlockPos getRelativePos() {
        return this.relativePos;
    }

    @Nullable
    public BlockPos getAbsolutePos() {
        return this.absolutePos;
    }
}