package journeymap.client.mod.impl;

import journeymap.client.mod.IModBlockHandler;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;

public class Streams implements IModBlockHandler {

    private static final int WATER_COLOR = 4210943;

    @Override
    public void initialize(BlockMD blockMD) {
        String name = blockMD.getBlockId().toLowerCase();
        if (name.contains("water")) {
            blockMD.setAlpha(0.25F);
            blockMD.addFlags(BlockFlag.Water, BlockFlag.NoShadow);
            blockMD.setColor(4210943);
        }
    }
}