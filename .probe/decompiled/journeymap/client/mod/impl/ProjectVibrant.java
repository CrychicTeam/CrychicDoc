package journeymap.client.mod.impl;

import journeymap.client.mod.IModBlockHandler;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;

public class ProjectVibrant implements IModBlockHandler {

    @Override
    public void initialize(BlockMD blockMD) {
        blockMD.addFlags(BlockFlag.Ignore);
    }
}