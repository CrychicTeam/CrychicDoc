package io.github.lightman314.lightmanscurrency.client.gui.widget;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.ITraderSource;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.TradeButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TradeButtonArea extends EasyWidgetWithChildren implements IScrollable, ITooltipSource, IEasyTickable {

    public static final Function<TradeData, Boolean> FILTER_VALID = TradeData::isValid;

    public static final Function<TradeData, Boolean> FILTER_ANY = trade -> true;

    private final Supplier<? extends ITraderSource> traderSource;

    private final Function<TraderData, TradeContext> getContext;

    private BiFunction<TraderData, TradeData, Boolean> isSelected = (trader, trade) -> false;

    private TradeButtonArea.InteractionConsumer interactionConsumer = null;

    private final List<TradeButton> allButtons = new ArrayList();

    private final Font font;

    private final BiConsumer<TraderData, TradeData> onPress;

    private final Function<TradeData, Boolean> tradeFilter;

    private int scroll = 0;

    ScrollBarWidget scrollBar;

    private boolean hasTitlePosition = false;

    private ScreenPosition titlePosition = ScreenPosition.ZERO;

    private int titleWidth = 0;

    private boolean renderNameOnly = false;

    private ScreenPosition scrollBarOffset = ScreenPosition.of(-9, 0);

    private int scrollBarHeight;

    public void setSelectionDefinition(@Nonnull BiFunction<TraderData, TradeData, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public void setInteractionConsumer(TradeButtonArea.InteractionConsumer consumer) {
        this.interactionConsumer = consumer;
    }

    @Deprecated
    public ScrollBarWidget getScrollBar() {
        return this.scrollBar;
    }

    public TradeButtonArea withTitle(ScreenPosition titlePosition, int titleWidth, boolean renderNameOnly) {
        this.hasTitlePosition = true;
        this.titlePosition = titlePosition;
        this.titleWidth = titleWidth;
        this.renderNameOnly = renderNameOnly;
        return this;
    }

    public TradeButtonArea withScrollBarOffset(@Nonnull ScreenPosition scrollBarOffset) {
        this.scrollBarOffset = scrollBarOffset;
        return this;
    }

    public TradeButtonArea withScrollBarHeight(int height) {
        this.scrollBarHeight = height;
        return this;
    }

    public int getMinAvailableWidth() {
        return this.scrollBarOffset.x < 0 ? this.f_93618_ + this.scrollBarOffset.x : this.f_93618_;
    }

    public int getAvailableWidth() {
        return this.scrollBar.visible() ? (this.scrollBarOffset.x < 0 ? this.f_93618_ + this.scrollBarOffset.x : this.f_93618_) : this.f_93618_;
    }

    public TradeButtonArea(Supplier<? extends ITraderSource> traderSource, Function<TraderData, TradeContext> getContext, int x, int y, int width, int height, BiConsumer<TraderData, TradeData> onPress, Function<TradeData, Boolean> tradeFilter) {
        super(x, y, width, height);
        this.traderSource = traderSource;
        this.getContext = getContext;
        this.onPress = onPress;
        this.tradeFilter = tradeFilter;
        Minecraft mc = Minecraft.getInstance();
        this.font = mc.font;
        this.scrollBarHeight = this.f_93619_ - 5;
    }

    public TradeButtonArea withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void addChildren() {
        this.scrollBar = this.addChild(this.scrollBar = new ScrollBarWidget(this.m_252754_() + this.f_93618_ + this.scrollBarOffset.x, this.m_252907_() + this.scrollBarOffset.y, this.scrollBarHeight, this));
        this.resetButtons();
    }

    @Nullable
    public TradeButton getHoveredButton(ScreenPosition mousePos) {
        for (TradeButton button : this.allButtons) {
            if (button.m_5953_((double) mousePos.x, (double) mousePos.y)) {
                return button;
            }
        }
        return null;
    }

    public TraderData getTrader(int traderIndex) {
        ITraderSource source = (ITraderSource) this.traderSource.get();
        if (source == null) {
            return null;
        } else {
            List<TraderData> traders = source.getTraders();
            return traderIndex >= 0 && traderIndex < traders.size() ? (TraderData) traders.get(traderIndex) : null;
        }
    }

    public List<List<Pair<TraderData, TradeData>>> getTradesInRows() {
        List<List<Pair<TraderData, TradeData>>> result = new ArrayList();
        ITraderSource source = (ITraderSource) this.traderSource.get();
        if (source == null) {
            return new ArrayList();
        } else {
            List<TraderData> traders = source.getTraders();
            int currentRowWidth = 0;
            List<Pair<TraderData, TradeData>> currentRow = new ArrayList();
            for (TraderData trader : traders) {
                TradeContext context = (TradeContext) this.getContext.apply(trader);
                for (TradeData trade : trader.getTradeData()) {
                    if ((Boolean) this.tradeFilter.apply(trade)) {
                        TradeRenderManager<?> trm = trade.getButtonRenderer();
                        int tradeWidth = trm.tradeButtonWidth(context);
                        if (currentRowWidth + tradeWidth > this.getMinAvailableWidth() && !currentRow.isEmpty()) {
                            result.add(currentRow);
                            currentRow = new ArrayList();
                            currentRowWidth = 0;
                        }
                        currentRow.add(Pair.of(trader, trade));
                        currentRowWidth += tradeWidth;
                    }
                }
            }
            result.add(currentRow);
            return result;
        }
    }

    public Pair<TraderData, TradeData> getTradeAndTrader(int displayIndex) {
        return this.getTradeAndTrader(this.scroll, displayIndex);
    }

    public Pair<TraderData, TradeData> getTradeAndTrader(int assumedScroll, int displayIndex) {
        ITraderSource source = (ITraderSource) this.traderSource.get();
        if (source == null) {
            return Pair.of(null, null);
        } else {
            List<List<Pair<TraderData, TradeData>>> rows = this.getTradesInRows();
            for (int r = assumedScroll; r < rows.size(); r++) {
                for (Pair<TraderData, TradeData> traderDataTradeDataPair : (List) rows.get(r)) {
                    if (displayIndex <= 0) {
                        return traderDataTradeDataPair;
                    }
                    displayIndex--;
                }
            }
            return Pair.of(null, null);
        }
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        if (this.validTrades() <= 0) {
            Component text = LCText.GUI_TRADER_NO_TRADES.get();
            int textWidth = gui.font.width(text);
            gui.drawString(text, this.f_93618_ / 2 - textWidth / 2, this.f_93619_ / 2 - 9 / 2, 4210752);
        }
        if (this.hasTitlePosition) {
            ITraderSource ts = (ITraderSource) this.traderSource.get();
            if (ts == null) {
                return;
            }
            MutableComponent text = EasyText.empty();
            for (TraderData trader : ts.getTraders()) {
                if (text.getString().isEmpty()) {
                    text.append(this.renderNameOnly ? trader.getName() : trader.getTitle());
                } else {
                    text.append(LCText.GUI_SEPERATOR.get()).append(this.renderNameOnly ? trader.getName() : trader.getTitle());
                }
            }
            gui.pushOffsetZero();
            gui.drawString(TextRenderUtil.fitString(text, this.titleWidth), this.titlePosition, 4210752);
            gui.popOffset();
        }
    }

    @Override
    public void renderTick() {
        this.validateScroll();
    }

    @Override
    public void tick() {
        if (this.allButtons.size() < this.requiredButtons()) {
            this.resetButtons();
        } else {
            this.repositionButtons();
        }
    }

    private void resetButtons() {
        this.allButtons.forEach(x$0 -> this.removeChild(x$0));
        this.allButtons.clear();
        int requiredButtons = this.requiredButtons();
        for (int i = 0; i < requiredButtons; i++) {
            int di = i;
            TradeButton newButton = this.addChild(new TradeButton(() -> (TradeContext) this.getContext.apply((TraderData) this.getTradeAndTrader(di).getFirst()), () -> (TradeData) this.getTradeAndTrader(di).getSecond(), button -> this.OnTraderPress(di)));
            this.allButtons.add(newButton);
        }
        this.repositionButtons();
    }

    private int validTrades() {
        ITraderSource ts = (ITraderSource) this.traderSource.get();
        if (ts == null) {
            return 0;
        } else {
            int count = 0;
            for (TraderData trader : ts.getTraders()) {
                for (TradeData trade : trader.getTradeData()) {
                    if (trade != null && (Boolean) this.tradeFilter.apply(trade)) {
                        count++;
                    }
                }
            }
            return count;
        }
    }

    private int requiredButtons() {
        List<List<Pair<TraderData, TradeData>>> rows = this.getTradesInRows();
        int count = 0;
        int lines = this.fittableLines();
        for (int r = this.scroll; r < rows.size() && r < this.scroll + lines; r++) {
            count += ((List) rows.get(r)).size();
        }
        return count;
    }

    private int fittableLines() {
        return this.f_93619_ / 22;
    }

    private void repositionButtons() {
        int displayIndex = 0;
        int yOffset = 0;
        int fittableLines = this.fittableLines();
        List<List<Pair<TraderData, TradeData>>> rows = this.getTradesInRows();
        for (int line = 0; line < fittableLines && line + this.scroll < rows.size(); line++) {
            List<Pair<TraderData, TradeData>> row = (List<Pair<TraderData, TradeData>>) rows.get(line + this.scroll);
            int visibleButtons = 0;
            int totalWidth = 0;
            for (Pair<TraderData, TradeData> trade : row) {
                if (trade.getFirst() != null && trade.getSecond() != null) {
                    TradeContext context = (TradeContext) this.getContext.apply((TraderData) trade.getFirst());
                    visibleButtons++;
                    totalWidth += ((TradeData) trade.getSecond()).getButtonRenderer().tradeButtonWidth(context);
                }
            }
            int spacing = (this.getAvailableWidth() - totalWidth) / (visibleButtons + 1);
            int xOffset = spacing;
            for (Pair<TraderData, TradeData> tradex : row) {
                TradeButton button = (TradeButton) this.allButtons.get(displayIndex);
                if (tradex.getFirst() != null && tradex.getSecond() != null) {
                    TradeContext context = (TradeContext) this.getContext.apply((TraderData) tradex.getFirst());
                    button.setPosition(this.getPosition().offset(xOffset, yOffset));
                    button.f_93624_ = true;
                    button.f_93623_ = !(Boolean) this.isSelected.apply((TraderData) tradex.getFirst(), (TradeData) tradex.getSecond());
                    xOffset += ((TradeData) tradex.getSecond()).getButtonRenderer().tradeButtonWidth(context) + spacing;
                } else {
                    button.f_93624_ = false;
                }
                displayIndex++;
            }
            yOffset += 22;
        }
        for (int i = displayIndex; i < this.allButtons.size(); i++) {
            ((TradeButton) this.allButtons.get(i)).f_93624_ = false;
        }
    }

    private void OnTraderPress(int displayIndex) {
        if (this.onPress != null) {
            Pair<TraderData, TradeData> data = this.getTradeAndTrader(displayIndex);
            this.onPress.accept((TraderData) data.getFirst(), (TradeData) data.getSecond());
        }
    }

    @Override
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        if (this.hasTitlePosition && this.titlePosition.isMouseInArea(mouseX, mouseY, this.titleWidth, 9)) {
            List<Component> tooltips = new ArrayList();
            ITraderSource ts = (ITraderSource) this.traderSource.get();
            if (ts == null) {
                return null;
            } else {
                for (TraderData trader : ts.getTraders()) {
                    tooltips.add(trader.getTitle());
                }
                return tooltips;
            }
        } else {
            return null;
        }
    }

    private boolean canScrollDown() {
        return this.canScrollDown(this.scroll);
    }

    private boolean canScrollDown(int assumedScroll) {
        return this.getTradesInRows().size() - assumedScroll > this.fittableLines();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta < 0.0) {
            if (!this.canScrollDown()) {
                return false;
            }
            this.scroll++;
            this.resetButtons();
        } else if (delta > 0.0) {
            if (this.scroll <= 0) {
                return false;
            }
            this.scroll--;
            this.resetButtons();
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < this.allButtons.size(); i++) {
            TradeButton b = (TradeButton) this.allButtons.get(i);
            if (b.m_5953_(mouseX, mouseY)) {
                Pair<TraderData, TradeData> traderPair = this.getTradeAndTrader(i);
                TradeContext context = (TradeContext) this.getContext.apply((TraderData) traderPair.getFirst());
                if (!context.isStorageMode) {
                    return b.m_6375_(mouseX, mouseY, button);
                }
                if (this.interactionConsumer != null) {
                    b.onInteractionClick((int) mouseX, (int) mouseY, button, this.interactionConsumer);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return true;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        if (newScroll != this.scroll) {
            this.scroll = MathUtil.clamp(newScroll, 0, this.getMaxScroll());
            this.resetButtons();
        }
    }

    @Override
    public int getMaxScroll() {
        int s = 0;
        while (this.canScrollDown(s)) {
            s++;
        }
        return s;
    }

    public interface InteractionConsumer {

        void onTradeButtonInputInteraction(TraderData var1, TradeData var2, int var3, int var4);

        void onTradeButtonOutputInteraction(TraderData var1, TradeData var2, int var3, int var4);

        void onTradeButtonInteraction(TraderData var1, TradeData var2, int var3, int var4, int var5);
    }
}