package noppes.npcs.controllers.data;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.NBTTags;
import noppes.npcs.NpcMiscInventory;

public class Bank {

    public int id = -1;

    public String name = "";

    public HashMap<Integer, Integer> slotTypes;

    public int startSlots = 1;

    public int maxSlots = 6;

    public NpcMiscInventory currencyInventory;

    public NpcMiscInventory upgradeInventory;

    public Bank() {
        this.slotTypes = new HashMap();
        this.currencyInventory = new NpcMiscInventory(6);
        this.upgradeInventory = new NpcMiscInventory(6);
        for (int i = 0; i < 6; i++) {
            this.slotTypes.put(i, 0);
        }
    }

    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("BankID", this.id);
        nbttagcompound.put("BankCurrency", this.currencyInventory.getToNBT());
        nbttagcompound.put("BankUpgrade", this.upgradeInventory.getToNBT());
        nbttagcompound.putString("Username", this.name);
        nbttagcompound.putInt("MaxSlots", this.maxSlots);
        nbttagcompound.putInt("StartSlots", this.startSlots);
        nbttagcompound.put("BankTypes", NBTTags.nbtIntegerIntegerMap(this.slotTypes));
    }

    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        this.id = nbttagcompound.getInt("BankID");
        this.name = nbttagcompound.getString("Username");
        this.startSlots = nbttagcompound.getInt("StartSlots");
        this.maxSlots = nbttagcompound.getInt("MaxSlots");
        this.slotTypes = NBTTags.getIntegerIntegerMap(nbttagcompound.getList("BankTypes", 10));
        this.currencyInventory.setFromNBT(nbttagcompound.getCompound("BankCurrency"));
        this.upgradeInventory.setFromNBT(nbttagcompound.getCompound("BankUpgrade"));
    }

    public boolean isUpgraded(int slot) {
        return this.slotTypes.get(slot) != null && (Integer) this.slotTypes.get(slot) == 2;
    }

    public boolean canBeUpgraded(int slot) {
        return this.upgradeInventory.getItem(slot) != null && !this.upgradeInventory.getItem(slot).isEmpty() ? this.slotTypes.get(slot) == null || (Integer) this.slotTypes.get(slot) == 0 : false;
    }

    public int getMaxSlots() {
        for (int i = 0; i < this.maxSlots; i++) {
            if ((this.currencyInventory.getItem(i) == null || this.currencyInventory.getItem(i).isEmpty()) && i > this.startSlots - 1) {
                return i;
            }
        }
        return this.maxSlots;
    }
}