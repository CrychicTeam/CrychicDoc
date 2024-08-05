package noppes.npcs.api.block;

import noppes.npcs.api.ITimers;

public interface IBlockScriptedDoor extends IBlock {

    ITimers getTimers();

    boolean getOpen();

    void setOpen(boolean var1);

    void setBlockModel(String var1);

    String getBlockModel();

    float getHardness();

    void setHardness(float var1);

    float getResistance();

    void setResistance(float var1);

    String executeCommand(String var1);
}