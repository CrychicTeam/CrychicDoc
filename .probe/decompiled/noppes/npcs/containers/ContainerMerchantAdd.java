package noppes.npcs.containers;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomContainer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.ServerEventsHandler;

public class ContainerMerchantAdd extends ContainerNpcInterface {

    private Merchant theMerchant = ServerEventsHandler.Merchant;

    private SimpleContainer merchantInventory;

    private final Level level;

    public ContainerMerchantAdd(int containerId, Inventory playerInventory) {
        super(CustomContainer.container_merchantadd, containerId, playerInventory);
        this.level = playerInventory.player.m_9236_();
        this.merchantInventory = new SimpleContainer(3);
        this.m_38897_(new Slot(this.merchantInventory, 0, 36, 53));
        this.m_38897_(new Slot(this.merchantInventory, 1, 62, 53));
        this.m_38897_(new Slot(this.merchantInventory, 2, 120, 53));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int var5 = 0; var5 < 9; var5++) {
            this.m_38897_(new Slot(playerInventory, var5, 8 + var5 * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int limbSwingAmount) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(limbSwingAmount);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (limbSwingAmount != 0 && limbSwingAmount != 1 && limbSwingAmount != 2) {
                if (limbSwingAmount >= 3 && limbSwingAmount < 30) {
                    if (!this.m_38903_(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (limbSwingAmount >= 30 && limbSwingAmount < 39 && !this.m_38903_(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(par1Player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void removed(Player par1Player) {
        super.m_6877_(par1Player);
        super.m_6877_(par1Player);
        if (!this.level.isClientSide) {
            ItemStack itemstack = this.merchantInventory.removeItemNoUpdate(0);
            if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                par1Player.drop(itemstack, false);
            }
            itemstack = this.merchantInventory.removeItemNoUpdate(1);
            if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                par1Player.drop(itemstack, false);
            }
        }
    }
}