package noppes.npcs;

import net.minecraft.nbt.CompoundTag;

public interface ICompatibilty {

    int getVersion();

    void setVersion(int var1);

    CompoundTag save(CompoundTag var1);
}