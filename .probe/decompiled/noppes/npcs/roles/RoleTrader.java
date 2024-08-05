package noppes.npcs.roles;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.role.IRoleTrader;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTJsonUtil;

public class RoleTrader extends RoleInterface implements IRoleTrader {

    public String marketName = "";

    public NpcMiscInventory inventoryCurrency;

    public NpcMiscInventory inventorySold;

    public boolean ignoreDamage = false;

    public boolean ignoreNBT = false;

    public boolean toSave = false;

    public RoleTrader(EntityNPCInterface npc) {
        super(npc);
        this.inventoryCurrency = new NpcMiscInventory(36);
        this.inventorySold = new NpcMiscInventory(18);
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putString("TraderMarket", this.marketName);
        this.writeNBT(nbttagcompound);
        if (this.toSave && !this.npc.isClientSide()) {
            save(this, this.marketName);
        }
        this.toSave = false;
        return nbttagcompound;
    }

    public CompoundTag writeNBT(CompoundTag nbttagcompound) {
        nbttagcompound.put("TraderCurrency", this.inventoryCurrency.getToNBT());
        nbttagcompound.put("TraderSold", this.inventorySold.getToNBT());
        nbttagcompound.putBoolean("TraderIgnoreDamage", this.ignoreDamage);
        nbttagcompound.putBoolean("TraderIgnoreNBT", this.ignoreNBT);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.marketName = nbttagcompound.getString("TraderMarket");
        this.readNBT(nbttagcompound);
    }

    public void readNBT(CompoundTag nbttagcompound) {
        this.inventoryCurrency.setFromNBT(nbttagcompound.getCompound("TraderCurrency"));
        this.inventorySold.setFromNBT(nbttagcompound.getCompound("TraderSold"));
        this.ignoreDamage = nbttagcompound.getBoolean("TraderIgnoreDamage");
        this.ignoreNBT = nbttagcompound.getBoolean("TraderIgnoreNBT");
    }

    @Override
    public void interact(Player player) {
        this.npc.say(player, this.npc.advanced.getInteractLine());
        try {
            load(this, this.marketName);
        } catch (Exception var3) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, var3);
        }
        NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTrader, this.npc);
    }

    public boolean hasCurrency(ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else {
            for (ItemStack item : this.inventoryCurrency.items) {
                if (!item.isEmpty() && NoppesUtilPlayer.compareItems(item, itemstack, this.ignoreDamage, this.ignoreNBT)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public IItemStack getSold(int slot) {
        return NpcAPI.Instance().getIItemStack(this.inventorySold.getItem(slot));
    }

    @Override
    public IItemStack getCurrency1(int slot) {
        return NpcAPI.Instance().getIItemStack(this.inventoryCurrency.getItem(slot));
    }

    @Override
    public IItemStack getCurrency2(int slot) {
        return NpcAPI.Instance().getIItemStack(this.inventoryCurrency.getItem(slot + 18));
    }

    @Override
    public void set(int slot, IItemStack currency, IItemStack currency2, IItemStack sold) {
        if (sold == null) {
            throw new CustomNPCsException("Sold item was null");
        } else if (slot < 18 && slot >= 0) {
            if (currency == null) {
                currency = currency2;
                currency2 = null;
            }
            if (currency != null) {
                this.inventoryCurrency.items.set(slot, currency.getMCItemStack());
            } else {
                this.inventoryCurrency.items.set(slot, ItemStack.EMPTY);
            }
            if (currency2 != null) {
                this.inventoryCurrency.items.set(slot + 18, currency2.getMCItemStack());
            } else {
                this.inventoryCurrency.items.set(slot + 18, ItemStack.EMPTY);
            }
            this.inventorySold.items.set(slot, sold.getMCItemStack());
        } else {
            throw new CustomNPCsException("Invalid slot: " + slot);
        }
    }

    @Override
    public void remove(int slot) {
        if (slot < 18 && slot >= 0) {
            this.inventoryCurrency.items.set(slot, ItemStack.EMPTY);
            this.inventoryCurrency.items.set(slot + 18, ItemStack.EMPTY);
            this.inventorySold.items.set(slot, ItemStack.EMPTY);
        } else {
            throw new CustomNPCsException("Invalid slot: " + slot);
        }
    }

    @Override
    public void setMarket(String name) {
        this.marketName = name;
        load(this, name);
    }

    @Override
    public String getMarket() {
        return this.marketName;
    }

    public static void save(RoleTrader r, String name) {
        if (!name.isEmpty()) {
            File file = getFile(name + "_new");
            File file1 = getFile(name);
            try {
                NBTJsonUtil.SaveFile(file, r.writeNBT(new CompoundTag()));
                if (file1.exists()) {
                    file1.delete();
                }
                file.renameTo(file1);
            } catch (Exception var5) {
            }
        }
    }

    public static void load(RoleTrader role, String name) {
        if (!role.npc.m_9236_().isClientSide) {
            File file = getFile(name);
            if (file.exists()) {
                try {
                    role.readNBT(NBTJsonUtil.LoadFile(file));
                } catch (Exception var4) {
                }
            }
        }
    }

    private static File getFile(String name) {
        File dir = new File(CustomNpcs.getLevelSaveDirectory(), "markets");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return new File(dir, name.toLowerCase() + ".json");
    }

    public static void setMarket(EntityNPCInterface npc, String marketName) {
        if (!marketName.isEmpty()) {
            if (!getFile(marketName).exists()) {
                save((RoleTrader) npc.role, marketName);
            }
            load((RoleTrader) npc.role, marketName);
        }
    }

    @Override
    public int getType() {
        return 1;
    }
}