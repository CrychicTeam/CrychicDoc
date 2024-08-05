package noppes.npcs.controllers.data;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.controllers.BankController;

public class PlayerBankData {

    public HashMap<Integer, BankData> banks = new HashMap();

    public void loadNBTData(CompoundTag compound) {
        HashMap<Integer, BankData> banks = new HashMap();
        ListTag list = compound.getList("BankData", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag nbttagcompound = list.getCompound(i);
                BankData data = new BankData();
                data.readNBT(nbttagcompound);
                banks.put(data.bankId, data);
            }
            this.banks = banks;
        }
    }

    public void saveNBTData(CompoundTag playerData) {
        ListTag list = new ListTag();
        for (BankData data : this.banks.values()) {
            CompoundTag nbttagcompound = new CompoundTag();
            data.writeNBT(nbttagcompound);
            list.add(nbttagcompound);
        }
        playerData.put("BankData", list);
    }

    public BankData getBank(int bankId) {
        return (BankData) this.banks.get(bankId);
    }

    public BankData getBankOrDefault(int bankId) {
        BankData data = (BankData) this.banks.get(bankId);
        if (data != null) {
            return data;
        } else {
            Bank bank = BankController.getInstance().getBank(bankId);
            return (BankData) this.banks.get(bank.id);
        }
    }

    public boolean hasBank(int bank) {
        return this.banks.containsKey(bank);
    }

    public void loadNew(int bank) {
        BankData data = new BankData();
        data.bankId = bank;
        this.banks.put(bank, data);
    }
}