package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.walletbank;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.WalletBankScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.BankAccountWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletBankMenu;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class InteractionTab extends WalletBankTab implements BankAccountWidget.IBankAccountWidget {

    BankAccountWidget accountWidget;

    public InteractionTab(WalletBankScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(ModBlocks.COINPILE_GOLD);
    }

    public MutableComponent getTooltip() {
        return Component.translatable("tooltip.lightmanscurrency.atm.interact");
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.accountWidget = this.addChild(new BankAccountWidget(screenArea.y, this, 7, this::addChild));
        this.accountWidget.allowEmptyDeposits = false;
        this.accountWidget.getAmountSelection().drawBG = false;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        Component accountName = Component.literal("ERROR FINDING ACCOUNT");
        IBankAccount ba = ((WalletBankMenu) this.screen.m_6262_()).getBankAccount();
        if (ba != null) {
            accountName = ba.getName();
        }
        gui.drawString(accountName, 8, 69, 4210752);
        this.accountWidget.renderInfo(gui);
    }

    @Override
    public void tick() {
        this.accountWidget.tick();
    }

    @Override
    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public IBankAccount getBankAccount() {
        return ((WalletBankMenu) this.screen.m_6262_()).getBankAccount();
    }

    @Override
    public IMoneyViewer getCoinAccess() {
        return ((WalletBankMenu) this.screen.m_6262_()).getCoinInputHandler();
    }
}