package noppes.npcs.api;

import noppes.npcs.api.block.IBlock;

public interface IRayTrace {

    IPos getPos();

    IBlock getBlock();

    int getSideHit();
}