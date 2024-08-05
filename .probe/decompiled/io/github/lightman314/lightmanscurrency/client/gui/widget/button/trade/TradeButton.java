package io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TradeButtonArea;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

public class TradeButton extends EasyButton implements ITooltipSource {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/trade.png");

    public static final int ARROW_WIDTH = 22;

    public static final int ARROW_HEIGHT = 18;

    public static final int TEMPLATE_WIDTH = 212;

    public static final int BUTTON_HEIGHT = 18;

    private final Supplier<TradeData> tradeSource;

    private final Supplier<TradeContext> contextSource;

    public boolean displayOnly = false;

    public TradeData getTrade() {
        return (TradeData) this.tradeSource.get();
    }

    public TradeRenderManager<?> getTradeRenderer() {
        TradeData trade = this.getTrade();
        return trade != null ? trade.getButtonRenderer() : null;
    }

    public TradeContext getContext() {
        return (TradeContext) this.contextSource.get();
    }

    public TradeButton(@Nonnull Supplier<TradeContext> contextSource, @Nonnull Supplier<TradeData> tradeSource, Consumer<EasyButton> press) {
        super(0, 0, 0, 18, press);
        this.tradeSource = tradeSource;
        this.contextSource = contextSource;
        this.recalculateSize();
    }

    public TradeButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    private void recalculateSize() {
        TradeRenderManager<?> tr = this.getTradeRenderer();
        if (tr != null) {
            TradeContext context = this.getContext();
            this.m_93674_(tr.tradeButtonWidth(context));
        }
    }

    @Deprecated
    public void move(int x, int y) {
        this.m_264152_(x, y);
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        TradeRenderManager<?> tr = this.getTradeRenderer();
        if (tr != null) {
            TradeContext context = this.getContext();
            this.recalculateSize();
            this.renderBackground(gui, !context.isStorageMode && !this.displayOnly && this.f_93622_);
            LazyOptional<ScreenPosition> arrowPosOptional = tr.arrowPosition(context);
            arrowPosOptional.ifPresent(arrowPos -> this.renderArrow(gui, arrowPos, !context.isStorageMode && !this.displayOnly && this.f_93622_));
            try {
                tr.renderAdditional(this, gui, context);
            } catch (Exception var6) {
                LightmansCurrency.LogError("Error on additional Trade Button rendering.", var6);
            }
            this.renderAlert(gui, tr.alertPosition(context), tr.getAlertData(context));
            this.renderDisplays(gui, tr, context);
            gui.resetColor();
        }
    }

    private void renderBackground(@Nonnull EasyGuiGraphics gui, boolean isHovered) {
        if (this.f_93618_ < 8) {
            LightmansCurrency.LogError("Cannot renderBG a trade button that is less than 8 pixels wide!");
        } else {
            if (this.f_93623_) {
                gui.resetColor();
            } else {
                gui.setColor(0.5F, 0.5F, 0.5F);
            }
            int vOffset = isHovered ? 18 : 0;
            gui.blit(GUI_TEXTURE, 0, 0, 0, vOffset, 4, 18);
            int xOff = 4;
            while (xOff < this.f_93618_ - 4) {
                int xRend = Math.min(this.f_93618_ - 4 - xOff, 204);
                gui.blit(GUI_TEXTURE, xOff, 0, 4, vOffset, xRend, 18);
                xOff += xRend;
            }
            gui.blit(GUI_TEXTURE, this.f_93618_ - 4, 0, 208, vOffset, 4, 18);
        }
    }

    private void renderArrow(@Nonnull EasyGuiGraphics gui, @Nonnull ScreenPosition position, boolean isHovered) {
        if (this.f_93623_) {
            gui.resetColor();
        } else {
            gui.setColor(0.5F, 0.5F, 0.5F);
        }
        int vOffset = isHovered ? 18 : 0;
        gui.blit(GUI_TEXTURE, position, 212, vOffset, 22, 18);
    }

    private void renderAlert(@Nonnull EasyGuiGraphics gui, @Nonnull ScreenPosition position, @Nullable List<AlertData> alerts) {
        if (alerts != null && !alerts.isEmpty()) {
            alerts.sort(AlertData::compare);
            ((AlertData) alerts.get(0)).setShaderColor(gui, this.f_93623_ ? 1.0F : 0.5F);
            gui.blit(GUI_TEXTURE, position, 234, 0, 22, 18);
        }
    }

    public void renderDisplays(EasyGuiGraphics gui, TradeRenderManager<?> tr, TradeContext context) {
        for (Pair<DisplayEntry, DisplayData> display : getInputDisplayData(tr, context)) {
            ((DisplayEntry) display.getFirst()).render(gui, 0, 0, (DisplayData) display.getSecond());
        }
        for (Pair<DisplayEntry, DisplayData> display : getOutputDisplayData(tr, context)) {
            ((DisplayEntry) display.getFirst()).render(gui, 0, 0, (DisplayData) display.getSecond());
        }
    }

    @Override
    public void renderTooltip(EasyGuiGraphics gui) {
        if (this.isMouseOver(gui.mousePos)) {
            TradeRenderManager<?> tr = this.getTradeRenderer();
            if (tr != null) {
                TradeContext context = this.getContext();
                int mouseX = gui.mousePos.x;
                int mouseY = gui.mousePos.y;
                List<Component> tooltips = new ArrayList();
                this.tryAddTooltip(tooltips, tr.getAdditionalTooltips(context, mouseX - this.m_252754_(), mouseY - this.m_252907_()));
                for (Pair<DisplayEntry, DisplayData> display : getInputDisplayData(tr, context)) {
                    if (((DisplayEntry) display.getFirst()).isMouseOver(this.m_252754_(), this.m_252907_(), (DisplayData) display.getSecond(), mouseX, mouseY)) {
                        if (((DisplayEntry) display.getFirst()).trySelfRenderTooltip(gui)) {
                            return;
                        }
                        this.tryAddTooltip(tooltips, ((DisplayEntry) display.getFirst()).getTooltip());
                    }
                }
                for (Pair<DisplayEntry, DisplayData> displayx : getOutputDisplayData(tr, context)) {
                    if (((DisplayEntry) displayx.getFirst()).isMouseOver(this.m_252754_(), this.m_252907_(), (DisplayData) displayx.getSecond(), mouseX, mouseY)) {
                        if (((DisplayEntry) displayx.getFirst()).trySelfRenderTooltip(gui)) {
                            return;
                        }
                        this.tryAddTooltip(tooltips, ((DisplayEntry) displayx.getFirst()).getTooltip());
                    }
                }
                if (this.isMouseOverAlert(mouseX, mouseY, tr, context)) {
                    List<AlertData> alerts = tr.getAlertData(context);
                    if (alerts != null && !alerts.isEmpty()) {
                        this.tryAddAlertTooltips(tooltips, alerts);
                    }
                }
                if (!tooltips.isEmpty()) {
                    gui.renderComponentTooltip(tooltips);
                }
            }
        }
    }

    @Override
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        return null;
    }

    private void tryAddTooltip(@Nonnull List<Component> tooltips, @Nullable List<Component> add) {
        if (add != null) {
            tooltips.addAll(add);
        }
    }

    private void tryAddAlertTooltips(@Nonnull List<Component> tooltips, @Nullable List<AlertData> alerts) {
        if (alerts != null) {
            alerts.sort(AlertData::compare);
            for (AlertData alert : alerts) {
                tooltips.add(alert.getFormattedMessage());
            }
        }
    }

    public void onInteractionClick(int mouseX, int mouseY, int button, TradeButtonArea.InteractionConsumer consumer) {
        if (this.f_93624_ && this.m_5953_((double) mouseX, (double) mouseY)) {
            TradeData trade = this.getTrade();
            if (trade != null) {
                TradeRenderManager<?> tr = trade.getButtonRenderer();
                if (tr != null) {
                    TradeContext context = this.getContext();
                    List<Pair<DisplayEntry, DisplayData>> inputDisplays = getInputDisplayData(tr, context);
                    for (int i = 0; i < inputDisplays.size(); i++) {
                        Pair<DisplayEntry, DisplayData> display = (Pair<DisplayEntry, DisplayData>) inputDisplays.get(i);
                        if (((DisplayEntry) display.getFirst()).isMouseOver(this.m_252754_(), this.m_252907_(), (DisplayData) display.getSecond(), mouseX, mouseY)) {
                            consumer.onTradeButtonInputInteraction(context.getTrader(), trade, i, button);
                            return;
                        }
                    }
                    List<Pair<DisplayEntry, DisplayData>> outputDisplays = getOutputDisplayData(tr, context);
                    for (int ix = 0; ix < outputDisplays.size(); ix++) {
                        Pair<DisplayEntry, DisplayData> display = (Pair<DisplayEntry, DisplayData>) outputDisplays.get(ix);
                        if (((DisplayEntry) display.getFirst()).isMouseOver(this.m_252754_(), this.m_252907_(), (DisplayData) display.getSecond(), mouseX, mouseY)) {
                            consumer.onTradeButtonOutputInteraction(context.getTrader(), trade, ix, button);
                            return;
                        }
                    }
                    consumer.onTradeButtonInteraction(context.getTrader(), trade, mouseX - this.m_252754_(), mouseY - this.m_252907_(), button);
                }
            }
        }
    }

    public boolean isMouseOverAlert(int mouseX, int mouseY, TradeRenderManager<?> tr, TradeContext context) {
        ScreenPosition position = tr.alertPosition(context);
        int left = this.m_252754_() + position.x;
        int top = this.m_252907_() + position.y;
        return mouseX >= left && mouseX < left + 22 && mouseY >= top && mouseY < top + 18;
    }

    public static List<Pair<DisplayEntry, DisplayData>> getInputDisplayData(TradeRenderManager<?> tr, TradeContext context) {
        List<Pair<DisplayEntry, DisplayData>> results = new ArrayList();
        List<DisplayEntry> entries = tr.getInputDisplays(context);
        List<DisplayData> display = tr.inputDisplayArea(context).divide(entries.size());
        for (int i = 0; i < entries.size() && i < display.size(); i++) {
            results.add(Pair.of((DisplayEntry) entries.get(i), (DisplayData) display.get(i)));
        }
        return results;
    }

    public static List<Pair<DisplayEntry, DisplayData>> getOutputDisplayData(TradeRenderManager<?> tr, TradeContext context) {
        List<Pair<DisplayEntry, DisplayData>> results = new ArrayList();
        List<DisplayEntry> entries = tr.getOutputDisplays(context);
        List<DisplayData> display = tr.outputDisplayArea(context).divide(entries.size());
        for (int i = 0; i < entries.size() && i < display.size(); i++) {
            results.add(Pair.of((DisplayEntry) entries.get(i), (DisplayData) display.get(i)));
        }
        return results;
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return !this.getContext().isStorageMode && !this.displayOnly ? super.isValidClickButton(button) : false;
    }
}