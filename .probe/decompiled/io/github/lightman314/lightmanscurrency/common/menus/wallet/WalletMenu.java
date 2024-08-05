package io.github.lightman314.lightmanscurrency.common.menus.wallet;

import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WalletMenu extends WalletMenuBase {

    public WalletMenu(int windowId, Inventory inventory, int walletStackIndex) {
        super(ModMenus.WALLET.get(), windowId, inventory, walletStackIndex);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + y * 9 + 9;
                this.addInventorySlot(8 + x * 18, 32 + (y + this.getRowCount()) * 18, index);
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addInventorySlot(8 + x * 18, 90 + this.getRowCount() * 18, x);
        }
        this.addCoinSlots(18);
        this.addDummySlots(37 + getMaxWalletSlots());
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerEntity, int index) {
        if (index + this.coinInput.getContainerSize() == this.walletStackIndex) {
            return ItemStack.EMPTY;
        } else {
            ItemStack clickedStack = ItemStack.EMPTY;
            Slot slot = (Slot) this.f_38839_.get(index);
            if (slot != null && slot.hasItem()) {
                ItemStack slotStack = slot.getItem();
                clickedStack = slotStack.copy();
                if (index < 36) {
                    if (!this.m_38903_(slotStack, 36, this.f_38839_.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_(slotStack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                if (slotStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
            }
            return clickedStack;
        }
    }

    public void QuickCollectCoins() {
        Inventory inv = this.player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (CoinAPI.API.IsCoin(item, false)) {
                ItemStack result = this.PickupCoins(item);
                inv.setItem(i, result);
            }
        }
    }
}