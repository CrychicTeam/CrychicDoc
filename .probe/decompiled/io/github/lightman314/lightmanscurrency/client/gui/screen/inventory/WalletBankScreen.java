package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.walletbank.InteractionTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.walletbank.SelectionTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.walletbank.WalletBankTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.ITab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletBankMenu;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketOpenWallet;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class WalletBankScreen extends EasyMenuScreen<WalletBankMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/wallet_bank.png");

    int currentTabIndex = 0;

    List<WalletBankTab> tabs = Lists.newArrayList(new WalletBankTab[] { new InteractionTab(this), new SelectionTab(this) });

    List<TabButton> tabButtons = new ArrayList();

    boolean logError = true;

    EasyButton buttonOpenWallet;

    public List<WalletBankTab> getTabs() {
        return this.tabs;
    }

    public WalletBankTab currentTab() {
        return (WalletBankTab) this.tabs.get(this.currentTabIndex);
    }

    public WalletBankScreen(WalletBankMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        screenArea = this.resize(176, 128 + ((WalletBankMenu) this.f_97732_).getRowCount() * 18 + 7);
        this.tabButtons = new ArrayList();
        for (int i = 0; i < this.tabs.size(); i++) {
            TabButton button = this.addChild(new TabButton(this::clickedOnTab, (ITab) this.tabs.get(i)));
            button.reposition(this.f_97735_ - 25, this.f_97736_ + i * 25, 3);
            button.f_93623_ = i != this.currentTabIndex;
            this.tabButtons.add(button);
        }
        this.buttonOpenWallet = this.addChild(new IconButton(screenArea.pos.offset(0, -20), this::PressOpenWalletButton, IconData.of(((WalletBankMenu) this.f_97732_).getWallet())).withAddons(EasyAddonHelper.tooltip(LCText.TOOLTIP_WALLET_OPEN_WALLET)));
        this.currentTab().onOpen();
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.resetColor();
        gui.blit(GUI_TEXTURE, 0, 0, 0, 0, this.f_97726_, 128);
        for (int y = 0; y < ((WalletBankMenu) this.f_97732_).getRowCount(); y++) {
            gui.blit(GUI_TEXTURE, 0, 128 + y * 18, 0, 128, this.f_97726_, 18);
        }
        gui.blit(GUI_TEXTURE, 0, 128 + ((WalletBankMenu) this.f_97732_).getRowCount() * 18, 0, 146, this.f_97726_, 7);
        for (int y = 0; y * 9 < ((WalletBankMenu) this.f_97732_).getSlotCount(); y++) {
            for (int x = 0; x < 9 && x + y * 9 < ((WalletBankMenu) this.f_97732_).getSlotCount(); x++) {
                gui.blit(GUI_TEXTURE, 7 + x * 18, 128 + y * 18, 0, 153, 18, 18);
            }
        }
        try {
            this.currentTab().renderBG(gui);
        } catch (Throwable var4) {
            if (this.logError) {
                LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var4);
                this.logError = false;
            }
        }
        gui.drawString(this.getWalletName(), 8, 117, 4210752);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.currentTab().renderAfterWidgets(gui);
        } catch (Throwable var3) {
            if (this.logError) {
                LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var3);
                this.logError = false;
            }
        }
    }

    private Component getWalletName() {
        ItemStack wallet = ((WalletBankMenu) this.f_97732_).getWallet();
        return (Component) (wallet.isEmpty() ? EasyText.empty() : wallet.getHoverName());
    }

    public void changeTab(int tabIndex) {
        this.currentTab().onClose();
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = true;
        this.currentTabIndex = MathUtil.clamp(tabIndex, 0, this.tabs.size() - 1);
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = false;
        this.currentTab().onOpen();
        this.logError = true;
    }

    private void clickedOnTab(EasyButton tab) {
        if (tab instanceof TabButton) {
            int tabIndex = this.tabButtons.indexOf(tab);
            if (tabIndex < 0) {
                return;
            }
            this.changeTab(tabIndex);
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }

    private void PressOpenWalletButton(EasyButton button) {
        new CPacketOpenWallet(((WalletBankMenu) this.f_97732_).getWalletStackIndex()).send();
    }
}