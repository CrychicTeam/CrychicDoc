package io.github.lightman314.lightmanscurrency.client.gui.widget.button;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketChestQuickCollect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;

public class ChestCoinCollectButton extends IconButton {

    private static ChestCoinCollectButton lastButton;

    private final ContainerScreen screen;

    public ChestCoinCollectButton(ContainerScreen screen) {
        super(0, 0, b -> CPacketChestQuickCollect.sendToServer(), ChestCoinCollectButton::getIcon);
        this.screen = screen;
        lastButton = this;
        this.m_264152_(this.screen.getGuiLeft() + this.screen.getXSize() - this.f_93618_, this.screen.getGuiTop() - this.f_93619_);
    }

    private static IconData getIcon() {
        return IconData.of(CoinAPI.API.getEquippedWallet(Minecraft.getInstance().player));
    }

    private boolean shouldBeVisible() {
        if (!LCConfig.CLIENT.chestButtonVisible.get()) {
            return false;
        } else {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
                ItemStack wallet = CoinAPI.API.getEquippedWallet(mc.player);
                if (WalletItem.isWallet(wallet)) {
                    boolean allowSideChains = LCConfig.CLIENT.chestButtonAllowSideChains.get();
                    Container container = ((ChestMenu) this.screen.m_6262_()).getContainer();
                    for (int i = 0; i < container.getContainerSize(); i++) {
                        if (CoinAPI.API.IsCoin(container.getItem(i), allowSideChains)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void renderTick() {
        super.renderTick();
        this.f_93624_ = this.shouldBeVisible();
    }

    public static void tryRenderTooltip(EasyGuiGraphics gui, int mouseX, int mouseY) {
        if (lastButton != null && lastButton.m_5953_((double) mouseX, (double) mouseY)) {
            gui.renderTooltip(LCText.TOOLTIP_CHEST_COIN_COLLECTION_BUTTON.get());
        }
    }
}