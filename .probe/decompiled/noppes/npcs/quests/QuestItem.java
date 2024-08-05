package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.util.ValueUtil;

public class QuestItem extends QuestInterface {

    public NpcMiscInventory items = new NpcMiscInventory(3);

    public boolean leaveItems = false;

    public boolean ignoreDamage = false;

    public boolean ignoreNBT = false;

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.items.setFromNBT(compound.getCompound("Items"));
        this.leaveItems = compound.getBoolean("LeaveItems");
        this.ignoreDamage = compound.getBoolean("IgnoreDamage");
        this.ignoreNBT = compound.getBoolean("IgnoreNBT");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("Items", this.items.getToNBT());
        compound.putBoolean("LeaveItems", this.leaveItems);
        compound.putBoolean("IgnoreDamage", this.ignoreDamage);
        compound.putBoolean("IgnoreNBT", this.ignoreNBT);
    }

    @Override
    public boolean isCompleted(Player player) {
        for (ItemStack reqItem : NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT)) {
            if (!NoppesUtilPlayer.compareItems(player, reqItem, this.ignoreDamage, this.ignoreNBT)) {
                return false;
            }
        }
        return true;
    }

    public Map<ItemStack, Integer> getProgressSet(Player player) {
        HashMap<ItemStack, Integer> map = new HashMap();
        for (ItemStack item : NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT)) {
            if (!NoppesUtilServer.IsItemStackNull(item)) {
                map.put(item, 0);
            }
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemx = player.getInventory().getItem(i);
            if (!NoppesUtilServer.IsItemStackNull(itemx)) {
                for (Entry<ItemStack, Integer> questItem : map.entrySet()) {
                    if (NoppesUtilPlayer.compareItems((ItemStack) questItem.getKey(), itemx, this.ignoreDamage, this.ignoreNBT)) {
                        map.put((ItemStack) questItem.getKey(), (Integer) questItem.getValue() + itemx.getCount());
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void handleComplete(Player player) {
        if (!this.leaveItems) {
            for (ItemStack questitem : this.items.items) {
                if (!questitem.isEmpty()) {
                    int stacksize = questitem.getCount();
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack item = player.getInventory().getItem(i);
                        if (!NoppesUtilServer.IsItemStackNull(item) && NoppesUtilPlayer.compareItems(item, questitem, this.ignoreDamage, this.ignoreNBT)) {
                            int size = item.getCount();
                            if (stacksize - size >= 0) {
                                player.getInventory().setItem(i, ItemStack.EMPTY);
                                item.split(size);
                            } else {
                                item.split(stacksize);
                            }
                            stacksize -= size;
                            if (stacksize <= 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList();
        for (ItemStack stack : NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT)) {
            if (!stack.isEmpty()) {
                list.add(new QuestItem.QuestItemObjective(player, stack));
            }
        }
        return (IQuestObjective[]) list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestItemObjective implements IQuestObjective {

        private final Player player;

        private final ItemStack questItem;

        public QuestItemObjective(Player player, ItemStack item) {
            this.player = player;
            this.questItem = item;
        }

        @Override
        public int getProgress() {
            int count = 0;
            for (int i = 0; i < this.player.getInventory().getContainerSize(); i++) {
                ItemStack item = this.player.getInventory().getItem(i);
                if (!NoppesUtilServer.IsItemStackNull(item) && NoppesUtilPlayer.compareItems(this.questItem, item, QuestItem.this.ignoreDamage, QuestItem.this.ignoreNBT)) {
                    count += item.getCount();
                }
            }
            return ValueUtil.CorrectInt(count, 0, this.questItem.getCount());
        }

        @Override
        public void setProgress(int progress) {
            throw new CustomNPCsException("Cant set the progress of ItemQuests");
        }

        @Override
        public int getMaxProgress() {
            return this.questItem.getCount();
        }

        @Override
        public boolean isCompleted() {
            return NoppesUtilPlayer.compareItems(this.player, this.questItem, QuestItem.this.ignoreDamage, QuestItem.this.ignoreNBT);
        }

        @Override
        public String getText() {
            return this.getMCText().getString();
        }

        @Override
        public Component getMCText() {
            return Component.literal("").append(this.questItem.getHoverName()).append(": " + this.getProgress() + "/" + this.getMaxProgress());
        }
    }
}