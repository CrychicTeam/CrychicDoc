package io.github.lightman314.lightmanscurrency.client.gui.overlay;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.client.util.ScreenCorner;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class WalletDisplayOverlay implements IGuiOverlay {

    public static final WalletDisplayOverlay INSTANCE = new WalletDisplayOverlay();

    private boolean sendError = true;

    private WalletDisplayOverlay() {
    }

    @Override
    public void render(ForgeGui fgui, GuiGraphics mcgui, float partialTick, int screenWidth, int screenHeight) {
        if (LCConfig.CLIENT.walletOverlayEnabled.get()) {
            try {
                EasyGuiGraphics gui = EasyGuiGraphics.create(mcgui, fgui.m_93082_(), 0, 0, partialTick);
                ScreenCorner corner = (ScreenCorner) LCConfig.CLIENT.walletOverlayCorner.get();
                ScreenPosition offset = LCConfig.CLIENT.walletOverlayPosition.get();
                ScreenPosition currentPosition = corner.getCorner(screenWidth, screenHeight).offset(offset);
                if (corner.isRightSide) {
                    currentPosition = currentPosition.offset(ScreenPosition.of(-16, 0));
                }
                if (corner.isBottomSide) {
                    currentPosition = currentPosition.offset(ScreenPosition.of(0, -16));
                }
                IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(fgui.getMinecraft().player);
                if (walletHandler == null) {
                    return;
                }
                ItemStack wallet = walletHandler.getWallet();
                if (WalletItem.isWallet(wallet)) {
                    gui.renderItem(wallet, currentPosition.x, currentPosition.y);
                    if (corner.isRightSide) {
                        currentPosition = currentPosition.offset(ScreenPosition.of(-17, 0));
                    } else {
                        currentPosition = currentPosition.offset(ScreenPosition.of(17, 0));
                    }
                    MoneyView contents = walletHandler.getStoredMoney();
                    switch((WalletDisplayOverlay.DisplayType) LCConfig.CLIENT.walletOverlayType.get()) {
                        case ITEMS_NARROW:
                        case ITEMS_WIDE:
                            int offsetAmount = LCConfig.CLIENT.walletOverlayType.get() == WalletDisplayOverlay.DisplayType.ITEMS_WIDE ? 17 : 9;
                            List<ItemStack> walletContents;
                            if (contents.getRandomValue() instanceof CoinValue coinValue) {
                                walletContents = coinValue.getAsItemList();
                            } else {
                                walletContents = new ArrayList();
                            }
                            for (ItemStack coin : walletContents) {
                                gui.renderItem(coin, currentPosition.x, currentPosition.y);
                                if (corner.isRightSide) {
                                    currentPosition = currentPosition.offset(ScreenPosition.of(-offsetAmount, 0));
                                } else {
                                    currentPosition = currentPosition.offset(ScreenPosition.of(offsetAmount, 0));
                                }
                            }
                            break;
                        case TEXT:
                            Component walletText = contents.getRandomValueText();
                            if (corner.isRightSide) {
                                gui.drawString(walletText, currentPosition.offset(-1 * gui.font.width(walletText), 3), 16777215);
                            } else {
                                gui.drawString(walletText, currentPosition.offset(0, 3), 16777215);
                            }
                    }
                }
            } catch (Throwable var18) {
                if (this.sendError) {
                    this.sendError = false;
                    LightmansCurrency.LogError("Error occurred while rendering wallet overlay!", var18);
                }
            }
        }
    }

    public static enum DisplayType {

        ITEMS_WIDE, ITEMS_NARROW, TEXT
    }
}