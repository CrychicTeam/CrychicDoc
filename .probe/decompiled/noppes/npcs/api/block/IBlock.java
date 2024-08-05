package noppes.npcs.api.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IBlock {

    int getX();

    int getY();

    int getZ();

    IPos getPos();

    Object getProperty(String var1);

    void setProperty(String var1, Object var2);

    String[] getProperties();

    String getName();

    void remove();

    boolean isRemoved();

    boolean isAir();

    IBlock setBlock(String var1);

    IBlock setBlock(IBlock var1);

    boolean hasTileEntity();

    boolean isContainer();

    IContainer getContainer();

    IData getTempdata();

    IData getStoreddata();

    IWorld getWorld();

    INbt getBlockEntityNBT();

    void setTileEntityNBT(INbt var1);

    BlockEntity getMCTileEntity();

    Block getMCBlock();

    void blockEvent(int var1, int var2);

    String getDisplayName();

    BlockState getMCBlockState();

    void interact(int var1);
}