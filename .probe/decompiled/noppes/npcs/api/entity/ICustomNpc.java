package noppes.npcs.api.entity;

import net.minecraft.world.entity.Mob;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.entity.data.INPCAdvanced;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.api.item.IItemStack;

public interface ICustomNpc<T extends Mob> extends IMob<T> {

    INPCDisplay getDisplay();

    INPCInventory getInventory();

    INPCStats getStats();

    INPCAi getAi();

    INPCAdvanced getAdvanced();

    IFaction getFaction();

    void setFaction(int var1);

    INPCRole getRole();

    INPCJob getJob();

    ITimers getTimers();

    int getHomeX();

    int getHomeY();

    int getHomeZ();

    IEntityLiving getOwner();

    void setHome(int var1, int var2, int var3);

    void reset();

    void say(String var1);

    void sayTo(IPlayer var1, String var2);

    IProjectile shootItem(IEntityLiving var1, IItemStack var2, int var3);

    IProjectile shootItem(double var1, double var3, double var5, IItemStack var7, int var8);

    void giveItem(IPlayer var1, IItemStack var2);

    void setDialog(int var1, IDialog var2);

    IDialog getDialog(int var1);

    void updateClient();

    String executeCommand(String var1);

    void trigger(int var1, Object... var2);
}