package io.github.lightman314.lightmanscurrency.client.gui.widget;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankInteraction;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BankAccountWidget implements IEasyTickable {

    public static final int HEIGHT = 109;

    public static final int BUTTON_WIDTH = 70;

    private final BankAccountWidget.IBankAccountWidget parent;

    private final MoneyValueWidget amountSelection;

    private final EasyButton buttonDeposit;

    private final EasyButton buttonWithdraw;

    int y;

    int spacing;

    public boolean allowEmptyDeposits = true;

    public MoneyValueWidget getAmountSelection() {
        return this.amountSelection;
    }

    public BankAccountWidget(int y, BankAccountWidget.IBankAccountWidget parent, Consumer<Object> addWidget) {
        this(y, parent, 0, addWidget);
    }

    public BankAccountWidget(int y, BankAccountWidget.IBankAccountWidget parent, int spacing, Consumer<Object> addWidget) {
        this.parent = parent;
        this.y = y;
        this.spacing = spacing;
        int screenMiddle = this.parent.getScreen().width / 2;
        this.amountSelection = new MoneyValueWidget(screenMiddle - 88, this.y, null, MoneyValue.empty(), value -> {
        });
        this.amountSelection.allowFreeInput = false;
        addWidget.accept(this.amountSelection);
        this.buttonDeposit = new EasyTextButton(screenMiddle - 5 - 70, this.y + 69 + 5 + spacing, 70, 20, LCText.BUTTON_BANK_DEPOSIT.get(), this::OnDeposit);
        addWidget.accept(this.buttonDeposit);
        this.buttonWithdraw = new EasyTextButton(screenMiddle + 5, this.y + 69 + 5 + spacing, 70, 20, LCText.BUTTON_BANK_WITHDRAW.get(), this::OnWithdraw);
        addWidget.accept(this.buttonWithdraw);
        this.buttonDeposit.f_93623_ = this.buttonWithdraw.f_93623_ = false;
    }

    public void renderInfo(@Nonnull EasyGuiGraphics gui) {
        this.renderInfo(gui, 0);
    }

    public void renderInfo(@Nonnull EasyGuiGraphics gui, int yOffset) {
        int screenMiddle = this.parent.getScreen().width / 2;
        IBankAccount ba = this.parent.getBankAccount();
        Component balanceComponent = (Component) (ba == null ? LCText.GUI_BANK_NO_SELECTED_ACCOUNT.get() : ba.getBalanceText());
        int offset = gui.font.width(balanceComponent.getString()) / 2;
        gui.pushOffsetZero().drawString(balanceComponent, screenMiddle - offset, this.y + 69 + 30 + this.spacing + yOffset, 4210752);
        gui.popOffset();
    }

    @Override
    public void tick() {
        if (this.parent.getBankAccount() == null) {
            this.buttonDeposit.f_93623_ = this.buttonWithdraw.f_93623_ = false;
        } else {
            this.buttonDeposit.f_93623_ = !this.parent.getCoinAccess().getStoredMoney().isEmpty() && (this.allowEmptyDeposits || !this.amountSelection.getCurrentValue().isEmpty());
            this.buttonWithdraw.f_93623_ = !this.amountSelection.getCurrentValue().isEmpty();
        }
    }

    private void OnDeposit(EasyButton button) {
        new CPacketBankInteraction(true, this.amountSelection.getCurrentValue()).send();
        this.amountSelection.changeValue(MoneyValue.empty());
    }

    private void OnWithdraw(EasyButton button) {
        new CPacketBankInteraction(false, this.amountSelection.getCurrentValue()).send();
        this.amountSelection.changeValue(MoneyValue.empty());
    }

    public interface IBankAccountWidget {

        Font getFont();

        Screen getScreen();

        IBankAccount getBankAccount();

        IMoneyViewer getCoinAccess();
    }
}