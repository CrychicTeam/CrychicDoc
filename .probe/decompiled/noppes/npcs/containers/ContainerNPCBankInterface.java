package noppes.npcs.containers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerBankData;

public class ContainerNPCBankInterface extends ContainerNpcInterface {

    public InventoryNPC currencyMatrix;

    public SlotNpcBankCurrency currency;

    public int slot = 0;

    public int bankid;

    private PlayerBankData data;

    public ContainerNPCBankInterface(MenuType type, int containerId, Inventory playerInventory, int slot, int bankid) {
        super(type, containerId, playerInventory);
        this.bankid = bankid;
        this.slot = slot;
        this.currencyMatrix = new InventoryNPC("currency", 1, this);
        if (!this.isAvailable() || this.canBeUpgraded()) {
            this.currency = new SlotNpcBankCurrency(this, this.currencyMatrix, 0, 80, 29);
            this.m_38897_(this.currency);
        }
        NpcMiscInventory items = new NpcMiscInventory(54);
        if (!this.player.m_9236_().isClientSide) {
            this.data = PlayerDataController.instance.getBankData(this.player, bankid);
            items = (NpcMiscInventory) this.data.getBankOrDefault(bankid).itemSlots.get(slot);
        }
        int xOffset = this.xOffset();
        for (int j = 0; j < this.getRowNumber(); j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                int id = i1 + j * 9;
                this.m_38897_(new Slot(items, id, 8 + i1 * 18, 17 + xOffset + j * 18));
            }
        }
        if (this.isUpgraded()) {
            xOffset += 54;
        }
        for (int k = 0; k < 3; k++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.m_38897_(new Slot(playerInventory, j1 + k * 9 + 9, 8 + j1 * 18, 86 + xOffset + k * 18));
            }
        }
        for (int l = 0; l < 9; l++) {
            this.m_38897_(new Slot(playerInventory, l, 8 + l * 18, 144 + xOffset));
        }
    }

    public synchronized void setCurrency(ItemStack item) {
        this.currency.item = item;
    }

    public int getRowNumber() {
        return 0;
    }

    public int xOffset() {
        return 0;
    }

    @Override
    public void slotsChanged(Container inv) {
    }

    public boolean isAvailable() {
        return false;
    }

    public boolean isUpgraded() {
        return false;
    }

    public boolean canBeUpgraded() {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player entityplayer) {
        super.m_6877_(entityplayer);
        if (!entityplayer.m_9236_().isClientSide) {
            ItemStack var3 = this.currencyMatrix.getItem(0);
            this.currencyMatrix.setItem(0, ItemStack.EMPTY);
            if (!NoppesUtilServer.IsItemStackNull(var3)) {
                entityplayer.drop(var3, false);
            }
        }
    }
}