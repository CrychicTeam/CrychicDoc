package noppes.npcs.api.entity;

import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.item.IItemStack;

public interface IPlayer<T extends ServerPlayer> extends IEntityLiving<T> {

    String getDisplayName();

    boolean hasFinishedQuest(int var1);

    boolean hasActiveQuest(int var1);

    void startQuest(int var1);

    int factionStatus(int var1);

    void finishQuest(int var1);

    void stopQuest(int var1);

    void removeQuest(int var1);

    boolean hasReadDialog(int var1);

    void showDialog(int var1, String var2);

    void removeDialog(int var1);

    void addDialog(int var1);

    void addFactionPoints(int var1, int var2);

    int getFactionPoints(int var1);

    void message(String var1);

    int getGamemode();

    void setGamemode(int var1);

    /**
     * @deprecated
     */
    int inventoryItemCount(IItemStack var1);

    /**
     * @deprecated
     */
    int inventoryItemCount(String var1);

    IContainer getInventory();

    IItemStack getInventoryHeldItem();

    boolean removeItem(IItemStack var1, int var2);

    boolean removeItem(String var1, int var2);

    void removeAllItems(IItemStack var1);

    boolean giveItem(IItemStack var1);

    boolean giveItem(String var1, int var2);

    void setSpawnpoint(int var1, int var2, int var3);

    void resetSpawnpoint();

    boolean hasAdvancement(String var1);

    int getExpLevel();

    void setExpLevel(int var1);

    boolean hasPermission(String var1);

    Object getPixelmonData();

    ITimers getTimers();

    void closeGui();

    T getMCEntity();

    IBlock getSpawnPoint();

    void setSpawnPoint(IBlock var1);

    int getHunger();

    void setHunger(int var1);

    void kick(String var1);

    void sendNotification(String var1, String var2, int var3);

    void sendMail(IPlayerMail var1);

    void clearData();

    IQuest[] getActiveQuests();

    IQuest[] getFinishedQuests();

    void updatePlayerInventory();

    void playSound(String var1, float var2, float var3);

    void playMusic(String var1, boolean var2, boolean var3);

    IContainer getOpenContainer();

    boolean canQuestBeAccepted(int var1);

    void showCustomGui(ICustomGui var1);

    ICustomGui getCustomGui();

    void trigger(int var1, Object... var2);
}