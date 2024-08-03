package io.github.lightman314.lightmanscurrency.common.menus.wallet;

import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountMenu;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import javax.annotation.Nonnull;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WalletBankMenu extends WalletMenuBase implements IBankAccountMenu {

    public static final int BANK_WIDGET_SPACING = 128;

    private final IMoneyViewer coinInputHandler;

    public WalletBankMenu(int windowId, Inventory inventory, int walletStackIndex) {
        super(ModMenus.WALLET_BANK.get(), windowId, inventory, walletStackIndex);
        this.addValidator(this::hasBankAccess);
        this.coinInputHandler = MoneyAPI.API.GetContainersMoneyHandler(this.coinInput, this.getPlayer());
        this.addCoinSlots(129);
        this.addDummySlots(WalletMenuBase.getMaxWalletSlots());
    }

    public SimpleContainer getCoinInput() {
        return this.coinInput;
    }

    public IMoneyViewer getCoinInputHandler() {
        return this.coinInputHandler;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onValidationTick(@Nonnull Player player) {
        super.onValidationTick(player);
        this.getBankAccountReference();
    }

    @Override
    public void onDepositOrWithdraw() {
        if (this.getAutoExchange()) {
            this.ExchangeCoints();
        } else {
            this.saveWalletContents();
        }
    }
}