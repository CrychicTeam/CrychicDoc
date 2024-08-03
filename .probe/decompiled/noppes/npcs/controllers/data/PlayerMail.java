package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.controllers.QuestController;

public class PlayerMail implements IPlayerMail, Container {

    public String subject = "";

    public String sender = "";

    public CompoundTag message = new CompoundTag();

    public long time = 0L;

    public boolean beenRead = false;

    public int questId = -1;

    public NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public long timePast;

    public void readNBT(CompoundTag compound) {
        this.subject = compound.getString("Subject");
        this.sender = compound.getString("Sender");
        this.time = compound.getLong("Time");
        this.beenRead = compound.getBoolean("BeenRead");
        this.message = compound.getCompound("Message");
        this.timePast = compound.getLong("TimePast");
        if (compound.contains("MailQuest")) {
            this.questId = compound.getInt("MailQuest");
        }
        this.items.clear();
        ListTag nbttaglist = compound.getList("MailItems", 10);
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag nbttagcompound1 = nbttaglist.getCompound(i);
            int j = nbttagcompound1.getByte("Slot") & 255;
            if (j >= 0 && j < this.items.size()) {
                this.items.set(j, ItemStack.of(nbttagcompound1));
            }
        }
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("Subject", this.subject);
        compound.putString("Sender", this.sender);
        compound.putLong("Time", this.time);
        compound.putBoolean("BeenRead", this.beenRead);
        compound.put("Message", this.message);
        compound.putLong("TimePast", System.currentTimeMillis() - this.time);
        compound.putInt("MailQuest", this.questId);
        if (this.hasQuest()) {
            compound.putString("MailQuestTitle", this.getQuest().title);
        }
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.items.size(); i++) {
            if (!this.items.get(i).isEmpty()) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte) i);
                this.items.get(i).save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }
        compound.put("MailItems", nbttaglist);
        return compound;
    }

    public boolean isValid() {
        return !this.subject.isEmpty() && !this.message.isEmpty() && !this.sender.isEmpty();
    }

    public boolean hasQuest() {
        return this.getQuest() != null;
    }

    public Quest getQuest() {
        return QuestController.instance != null ? (Quest) QuestController.instance.quests.get(this.questId) : null;
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, index, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int var1) {
        return this.items.set(var1, ItemStack.EMPTY);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player var1) {
        return true;
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }

    @Override
    public boolean canPlaceItem(int var1, ItemStack var2) {
        return true;
    }

    public PlayerMail copy() {
        PlayerMail mail = new PlayerMail();
        mail.readNBT(this.writeNBT());
        return mail;
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack item = this.getItem(slot);
            if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSender() {
        return this.sender;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String[] getText() {
        List<String> list = new ArrayList();
        ListTag pages = this.message.getList("pages", 8);
        for (int i = 0; i < pages.size(); i++) {
            list.add(pages.getString(i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override
    public void setText(String[] pages) {
        ListTag list = new ListTag();
        if (pages != null && pages.length > 0) {
            for (String page : pages) {
                list.add(StringTag.valueOf(page));
            }
        }
        this.message.put("pages", list);
    }

    @Override
    public void setQuest(int id) {
        this.questId = id;
    }

    @Override
    public IContainer getContainer() {
        return NpcAPI.Instance().getIContainer(this);
    }

    @Override
    public void clearContent() {
    }
}