package noppes.npcs.schematics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface ISchematic {

    short getWidth();

    short getHeight();

    short getLength();

    int getBlockEntityDimensions();

    CompoundTag getBlockEntity(int var1);

    String getName();

    BlockState getBlockState(int var1, int var2, int var3);

    BlockState getBlockState(int var1);

    CompoundTag getNBT();
}