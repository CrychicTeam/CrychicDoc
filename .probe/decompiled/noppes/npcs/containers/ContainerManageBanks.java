package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.controllers.data.Bank;

public class ContainerManageBanks extends AbstractContainerMenu {

    public Bank bank = new Bank();

    public ContainerManageBanks(int containerId, Inventory playerInventory) {
        super(CustomContainer.container_managebanks, containerId);
        for (int i = 0; i < 6; i++) {
            int x = 36;
            int y = 38;
            y += i * 22;
            this.m_38897_(new Slot(this.bank.currencyInventory, i, x, y));
        }
        for (int i = 0; i < 6; i++) {
            int x = 142;
            int y = 38;
            y += i * 22;
            this.m_38897_(new Slot(this.bank.upgradeInventory, i, x, y));
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 8 + j1 * 18, 171));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }

    public void setBank(Bank bank2) {
        for (int i = 0; i < 6; i++) {
            this.bank.currencyInventory.setItem(i, bank2.currencyInventory.getItem(i));
            this.bank.upgradeInventory.setItem(i, bank2.upgradeInventory.getItem(i));
        }
    }
}