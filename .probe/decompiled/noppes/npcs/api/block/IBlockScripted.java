package noppes.npcs.api.block;

import noppes.npcs.api.ITimers;
import noppes.npcs.api.item.IItemStack;

public interface IBlockScripted extends IBlock {

    void setModel(IItemStack var1);

    void setModel(String var1);

    IItemStack getModel();

    ITimers getTimers();

    void setRedstonePower(int var1);

    int getRedstonePower();

    void setIsLadder(boolean var1);

    boolean getIsLadder();

    void setLight(int var1);

    int getLight();

    void setScale(float var1, float var2, float var3);

    float getScaleX();

    float getScaleY();

    float getScaleZ();

    void setRotation(int var1, int var2, int var3);

    int getRotationX();

    int getRotationY();

    int getRotationZ();

    String executeCommand(String var1);

    boolean getIsPassible();

    void setIsPassible(boolean var1);

    float getHardness();

    void setHardness(float var1);

    float getResistance();

    void setResistance(float var1);

    ITextPlane getTextPlane();

    ITextPlane getTextPlane2();

    ITextPlane getTextPlane3();

    ITextPlane getTextPlane4();

    ITextPlane getTextPlane5();

    ITextPlane getTextPlane6();

    void trigger(int var1, Object... var2);
}