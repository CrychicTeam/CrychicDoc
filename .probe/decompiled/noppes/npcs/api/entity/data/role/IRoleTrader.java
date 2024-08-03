package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.item.IItemStack;

public interface IRoleTrader extends INPCRole {

    IItemStack getSold(int var1);

    IItemStack getCurrency1(int var1);

    IItemStack getCurrency2(int var1);

    void set(int var1, IItemStack var2, IItemStack var3, IItemStack var4);

    void remove(int var1);

    void setMarket(String var1);

    String getMarket();
}