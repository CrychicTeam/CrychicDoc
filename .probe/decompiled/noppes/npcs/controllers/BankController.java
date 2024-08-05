package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.Bank;

public class BankController {

    public HashMap<Integer, Bank> banks;

    private String filePath = "";

    private static BankController instance;

    public BankController() {
        instance = this;
        this.banks = new HashMap();
        this.loadBanks();
        if (this.banks.isEmpty()) {
            Bank bank = new Bank();
            bank.id = 0;
            bank.name = "Default Bank";
            for (int i = 0; i < 6; i++) {
                bank.slotTypes.put(i, 0);
            }
            this.banks.put(bank.id, bank);
        }
    }

    public static BankController getInstance() {
        if (newInstance()) {
            instance = new BankController();
        }
        return instance;
    }

    private static boolean newInstance() {
        if (instance == null) {
            return true;
        } else {
            File file = CustomNpcs.getLevelSaveDirectory();
            return file == null ? false : !instance.filePath.equals(file.getAbsolutePath());
        }
    }

    private void loadBanks() {
        File saveDir = CustomNpcs.getLevelSaveDirectory();
        if (saveDir != null) {
            this.filePath = saveDir.getAbsolutePath();
            try {
                File file = new File(saveDir, "bank.dat");
                if (file.exists()) {
                    this.loadBanks(file);
                }
            } catch (Exception var5) {
                try {
                    File filex = new File(saveDir, "bank.dat_old");
                    if (filex.exists()) {
                        this.loadBanks(filex);
                    }
                } catch (Exception var4) {
                }
            }
        }
    }

    private void loadBanks(File file) throws IOException {
        this.loadBanks(NbtIo.readCompressed(new FileInputStream(file)));
    }

    public void loadBanks(CompoundTag nbttagcompound1) throws IOException {
        HashMap<Integer, Bank> banks = new HashMap();
        ListTag list = nbttagcompound1.getList("Data", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag nbttagcompound = list.getCompound(i);
                Bank bank = new Bank();
                bank.readAdditionalSaveData(nbttagcompound);
                banks.put(bank.id, bank);
            }
        }
        this.banks = banks;
    }

    public CompoundTag getNBT() {
        ListTag list = new ListTag();
        for (Bank bank : this.banks.values()) {
            CompoundTag nbtfactions = new CompoundTag();
            bank.addAdditionalSaveData(nbtfactions);
            list.add(nbtfactions);
        }
        CompoundTag nbttagcompound = new CompoundTag();
        nbttagcompound.put("Data", list);
        return nbttagcompound;
    }

    public Bank getBank(int bankId) {
        Bank bank = (Bank) this.banks.get(bankId);
        return bank != null ? bank : (Bank) this.banks.values().iterator().next();
    }

    public void saveBanks() {
        try {
            File saveDir = CustomNpcs.getLevelSaveDirectory();
            File file = new File(saveDir, "bank.dat_new");
            File file1 = new File(saveDir, "bank.dat_old");
            File file2 = new File(saveDir, "bank.dat");
            NbtIo.writeCompressed(this.getNBT(), new FileOutputStream(file));
            if (file1.exists()) {
                file1.delete();
            }
            file2.renameTo(file1);
            if (file2.exists()) {
                file2.delete();
            }
            file.renameTo(file2);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public void saveBank(Bank bank) {
        if (bank.id < 0) {
            bank.id = this.getUnusedId();
        }
        this.banks.put(bank.id, bank);
        this.saveBanks();
    }

    public int getUnusedId() {
        int id = 0;
        while (this.banks.containsKey(id)) {
            id++;
        }
        return id;
    }

    public void removeBank(int bank) {
        if (bank >= 0 && this.banks.size() > 1) {
            this.banks.remove(bank);
            this.saveBanks();
        }
    }
}