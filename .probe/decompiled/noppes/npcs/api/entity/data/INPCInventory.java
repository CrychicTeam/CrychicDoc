package noppes.npcs.api.entity.data;

import noppes.npcs.api.item.IItemStack;

public interface INPCInventory {

    IItemStack getRightHand();

    void setRightHand(IItemStack var1);

    IItemStack getLeftHand();

    void setLeftHand(IItemStack var1);

    IItemStack getProjectile();

    void setProjectile(IItemStack var1);

    IItemStack getArmor(int var1);

    void setArmor(int var1, IItemStack var2);

    void setDropItem(int var1, IItemStack var2, float var3);

    IItemStack getDropItem(int var1);

    int getExpMin();

    int getExpMax();

    int getExpRNG();

    void setExp(int var1, int var2);

    IItemStack[] getItemsRNG();
}