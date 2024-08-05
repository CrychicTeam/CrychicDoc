package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountAdvancedMenu;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.ATMAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.bank.BankSaveData;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ATMMenu extends LazyMessageMenu implements IBankAccountAdvancedMenu {

    private final SimpleContainer coinInput = new SimpleContainer(9);

    private final IMoneyHandler moneyHandler = MoneyAPI.API.GetATMMoneyHandler(this.player, this.coinInput);

    private MutableComponent transferMessage = null;

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public SimpleContainer getCoinInput() {
        return this.coinInput;
    }

    public IMoneyHandler getMoneyHandler() {
        return this.moneyHandler;
    }

    public ATMMenu(int windowId, Inventory inventory, MenuValidator validator) {
        super(ModMenus.ATM.get(), windowId, inventory, validator);
        for (int x = 0; x < this.coinInput.getContainerSize(); x++) {
            this.m_38897_(new CoinSlot(this.coinInput, x, 8 + x * 18, 129, false));
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 161 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 8 + x * 18, 219));
        }
    }

    @Override
    protected void onValidationTick(@Nonnull Player player) {
        this.getBankAccountReference();
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        this.m_150411_(player, this.coinInput);
        if (!this.isClient()) {
            BankReference account = this.getBankAccountReference();
            if (!account.canPersist(player)) {
                BankSaveData.SetSelectedBankAccount(this.player, PlayerBankReference.of(this.player));
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < this.coinInput.getContainerSize()) {
                if (!this.m_38903_(slotStack, this.coinInput.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 0, this.coinInput.getContainerSize(), false)) {
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

    public void SendCoinExchangeMessage(String command) {
        this.SendMessageToServer(LazyPacketData.builder().setString("ExchangeCoinCommand", command));
    }

    public void ExchangeCoins(String command) {
        ATMAPI.ExecuteATMExchangeCommand(this.coinInput, command);
    }

    public MutableComponent SetPlayerAccount(String playerName) {
        if (LCAdminMode.isAdminPlayer(this.player)) {
            PlayerReference accountPlayer = PlayerReference.of(false, playerName);
            if (accountPlayer != null) {
                BankSaveData.SetSelectedBankAccount(this.player, PlayerBankReference.of(accountPlayer));
                return LCText.GUI_BANK_SELECT_PLAYER_SUCCESS.get(accountPlayer.getName(false));
            } else {
                return LCText.GUI_BANK_TRANSFER_ERROR_NULL_TARGET.get();
            }
        } else {
            return EasyText.literal("ERROR");
        }
    }

    public boolean hasTransferMessage() {
        return this.transferMessage != null;
    }

    public MutableComponent getTransferMessage() {
        return this.transferMessage;
    }

    @Override
    public void setTransferMessage(MutableComponent message) {
        this.transferMessage = message;
    }

    public void clearMessage() {
        this.transferMessage = null;
    }

    public void SetNotificationValueAndUpdate(@Nonnull String type, @Nonnull MoneyValue newValue) {
        IBankAccount ba = this.getBankAccount();
        if (ba != null) {
            ba.setNotificationLevel(type, newValue);
        }
        this.SendMessageToServer(LazyPacketData.builder().setString("NotificationValueType", type).setMoneyValue("NotificationValueChange", newValue));
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ExchangeCoinCommand")) {
            this.ExchangeCoins(message.getString("ExchangeCoinCommand"));
        }
        if (message.contains("NotificationValueChange")) {
            IBankAccount ba = this.getBankAccount();
            if (ba != null) {
                ba.setNotificationLevel(message.getString("NotificationValueType"), message.getMoneyValue("NotificationValueChange"));
            }
        }
    }
}