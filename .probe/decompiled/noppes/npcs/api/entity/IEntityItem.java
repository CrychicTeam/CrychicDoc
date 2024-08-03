package noppes.npcs.api.entity;

import net.minecraft.world.entity.item.ItemEntity;
import noppes.npcs.api.item.IItemStack;

public interface IEntityItem<T extends ItemEntity> extends IEntity<T> {

    String getOwner();

    void setOwner(String var1);

    int getPickupDelay();

    void setPickupDelay(int var1);

    @Override
    long getAge();

    void setAge(long var1);

    int getLifeSpawn();

    void setLifeSpawn(int var1);

    IItemStack getItem();

    void setItem(IItemStack var1);
}