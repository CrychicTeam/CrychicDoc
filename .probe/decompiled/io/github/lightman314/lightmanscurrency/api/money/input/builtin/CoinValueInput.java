package io.github.lightman314.lightmanscurrency.api.money.input.builtin;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyInputHandler;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.types.builtin.CoinCurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class CoinValueInput extends MoneyInputHandler implements IScrollable {

    public static final int MAX_BUTTON_COUNT = 6;

    private static final int SEGMENT_WIDTH = 20;

    private static final int SEGMENT_SPACING = 5;

    private static final int SEGMENT_TOTAL = 25;

    private final ChainData chain;

    private final List<CoinEntry> coinData;

    private int scroll = 0;

    private final List<EasyButton> increaseButtons = new ArrayList();

    private final List<EasyButton> decreaseButtons = new ArrayList();

    private EasyButton buttonScrollLeft = null;

    private EasyButton buttonScrollRight = null;

    public CoinValueInput(@Nonnull ChainData chain) {
        this.chain = chain;
        this.coinData = this.chain.getAllEntries(false, ChainData.SORT_HIGHEST_VALUE_FIRST);
        this.scroll = this.getMaxScroll();
    }

    @Nonnull
    @Override
    public MutableComponent inputName() {
        return this.chain.getDisplayName();
    }

    @Nonnull
    @Override
    public String getUniqueName() {
        return MoneyValue.generateCustomUniqueName(CoinCurrencyType.TYPE, this.chain.chain);
    }

    @Override
    public void initialize(@Nonnull ScreenArea widgetArea) {
        this.increaseButtons.clear();
        this.decreaseButtons.clear();
        int buttonCount = this.coinData.size();
        if (buttonCount > 6) {
            buttonCount = 6;
            this.addChild(new ScrollListener(widgetArea, this));
            this.buttonScrollLeft = this.addChild(new PlainButton(widgetArea.pos.offset(4, 29), b -> this.scrollLeft(), MoneyValueWidget.SPRITE_LEFT_ARROW));
            this.buttonScrollRight = this.addChild(new PlainButton(widgetArea.pos.offset(widgetArea.width - 14, 29), b -> this.scrollRight(), MoneyValueWidget.SPRITE_RIGHT_ARROW));
        }
        int startX = this.getStartX(widgetArea);
        for (int x = 0; x < buttonCount; x++) {
            EasyButton newButton = this.addChild(new PlainButton(widgetArea.pos.offset(startX + x * 25, 19), this::IncreaseButtonHit, MoneyValueWidget.SPRITE_UP_ARROW));
            this.increaseButtons.add(newButton);
            newButton = this.addChild(new PlainButton(widgetArea.pos.offset(startX + x * 25, 57), this::DecreaseButtonHit, MoneyValueWidget.SPRITE_DOWN_ARROW));
            this.decreaseButtons.add(newButton);
        }
    }

    private int getStartX(ScreenArea widgetSize) {
        int buttonCount = Math.min(this.coinData.size(), 6);
        int space = widgetSize.width - buttonCount * 25 + 5;
        return space / 2;
    }

    @Override
    protected void renderBG(@Nonnull ScreenArea widgetArea, @Nonnull EasyGuiGraphics gui) {
        this.validateScroll();
        int buttonCount = Math.min(this.coinData.size(), 6);
        int startX = this.getStartX(widgetArea);
        for (int x = 0; x < buttonCount; x++) {
            CoinEntry coin = (CoinEntry) this.coinData.get(x + this.scroll);
            gui.renderItem(new ItemStack(coin.getCoin()), startX + x * 25 + 2, 30);
            String countString = String.valueOf(this.getQuantityOfCoin(coin));
            int width = gui.font.width(countString);
            gui.drawString(countString, startX + x * 25 + 10 - width / 2, 47, 4210752);
        }
    }

    @Override
    public void renderTick() {
        if (this.decreaseButtons != null && this.increaseButtons != null) {
            if (this.buttonScrollLeft != null) {
                this.buttonScrollLeft.f_93624_ = this.isVisible();
                this.buttonScrollLeft.f_93623_ = this.scroll > 0;
            }
            if (this.buttonScrollRight != null) {
                this.buttonScrollRight.f_93624_ = this.isVisible();
                this.buttonScrollRight.f_93623_ = this.scroll < this.getMaxScroll();
            }
            for (int i = 0; i < this.decreaseButtons.size(); i++) {
                ((EasyButton) this.decreaseButtons.get(i)).f_93624_ = this.isVisible();
                if (i + this.scroll >= this.coinData.size()) {
                    ((EasyButton) this.decreaseButtons.get(i)).f_93623_ = false;
                } else {
                    ((EasyButton) this.decreaseButtons.get(i)).f_93623_ = this.getQuantityOfCoin((CoinEntry) this.coinData.get(i + this.scroll)) > 0L && !this.isLocked() && !this.isFree();
                }
            }
            for (EasyButton button : this.increaseButtons) {
                button.f_93624_ = this.isVisible();
                button.f_93623_ = !this.isLocked() && !this.isFree();
            }
        }
    }

    private long getQuantityOfCoin(@Nonnull CoinEntry coin) {
        return this.currentValue() instanceof CoinValue coinValue ? coinValue.getEntry(coin.getCoin()) : 0L;
    }

    @Override
    public void onValueChanged(@Nonnull MoneyValue newValue) {
    }

    private void scrollLeft() {
        this.scroll--;
        this.validateScroll();
    }

    private void scrollRight() {
        this.scroll++;
        this.validateScroll();
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
        this.validateScroll();
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(6, this.coinData.size());
    }

    public void IncreaseButtonHit(EasyButton button) {
        int temp = this.increaseButtons.indexOf(button);
        if (temp >= 0) {
            int coinIndex = temp + this.scroll;
            boolean shiftHeld = Screen.hasShiftDown();
            boolean ctrlHeld = Screen.hasControlDown();
            if (coinIndex >= 0 && coinIndex < this.coinData.size()) {
                new Thread(() -> {
                    CoinEntry coin = (CoinEntry) this.coinData.get(coinIndex);
                    int addAmount = 1;
                    if (shiftHeld) {
                        addAmount = this.getLargeIncreaseAmount(coin);
                    }
                    if (ctrlHeld) {
                        addAmount *= 10;
                    }
                    MoneyValue currentValue = this.currentValue();
                    long value = 0L;
                    if (currentValue instanceof CoinValue cv && cv.getChain().equals(this.chain.chain)) {
                        value = currentValue.getCoreValue();
                    }
                    MoneyValue newValue = CoinValue.fromNumber(this.chain.chain, value + coin.getCoreValue() * (long) addAmount);
                    this.changeValue(newValue);
                }).start();
            } else {
                LightmansCurrency.LogError("Invalid index (" + coinIndex + ") found for the increasing button.");
            }
        }
    }

    public void DecreaseButtonHit(EasyButton button) {
        if (this.decreaseButtons.contains(button)) {
            int temp = this.decreaseButtons.indexOf(button);
            if (temp >= 0) {
                int coinIndex = temp + this.scroll;
                if (coinIndex >= 0 && coinIndex < this.coinData.size()) {
                    new Thread(() -> {
                        CoinEntry coin = (CoinEntry) this.coinData.get(coinIndex);
                        int removeAmount = 1;
                        if (Screen.hasShiftDown()) {
                            removeAmount = this.getLargeIncreaseAmount(coin);
                        }
                        if (Screen.hasControlDown()) {
                            removeAmount *= 10;
                        }
                        long value = 0L;
                        MoneyValue currentValue = this.currentValue();
                        if (currentValue instanceof CoinValue cv && cv.getChain().equals(this.chain.chain)) {
                            value = currentValue.getCoreValue();
                        }
                        MoneyValue newValue = CoinValue.fromNumber(this.chain.chain, value - coin.getCoreValue() * (long) removeAmount);
                        this.changeValue(newValue);
                    }).start();
                } else {
                    LightmansCurrency.LogError("Invalid index (" + coinIndex + ") found for the decreasing button.");
                }
            }
        }
    }

    private int getLargeIncreaseAmount(@Nonnull CoinEntry coin) {
        Pair<CoinEntry, Integer> upperExchange = coin.getUpperExchange();
        if (upperExchange != null) {
            return this.getLargeAmount(upperExchange);
        } else {
            Pair<CoinEntry, Integer> downwardExchange = coin.getLowerExchange();
            return downwardExchange != null ? this.getLargeAmount(downwardExchange) : 10;
        }
    }

    private int getLargeAmount(@Nonnull Pair<CoinEntry, Integer> exchange) {
        if ((Integer) exchange.getSecond() >= 64) {
            return 16;
        } else if ((Integer) exchange.getSecond() > 10) {
            return 10;
        } else {
            return exchange.getSecond() > 5 ? 5 : 2;
        }
    }
}