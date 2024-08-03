package snownee.jade.api;

import net.minecraft.nbt.CompoundTag;

public interface IServerDataProvider<T extends Accessor<?>> extends IJadeProvider {

    void appendServerData(CompoundTag var1, T var2);
}