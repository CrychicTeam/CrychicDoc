package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.rule_tabs;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRuleSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TimeInputWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextInputUtil;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PriceFluctuation;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;

public class PriceFluctuationTab extends TradeRuleSubTab<PriceFluctuation> {

    EditBox fluctuationInput;

    EasyButton buttonSetFluctuation;

    TimeInputWidget durationInput;

    public PriceFluctuationTab(@Nonnull TradeRulesClientTab<?> parent) {
        super(parent, PriceFluctuation.TYPE);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_PRICE_FLUCTUATION;
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.fluctuationInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 25, screenArea.y + 9, 20, 20, EasyText.empty()));
        this.fluctuationInput.setMaxLength(2);
        PriceFluctuation rule = this.getRule();
        if (rule != null) {
            this.fluctuationInput.setValue(Integer.toString(rule.getFluctuation()));
        }
        this.buttonSetFluctuation = this.addChild(new EasyTextButton(screenArea.pos.offset(125, 10), 50, 20, LCText.BUTTON_SET.get(), this::PressSetFluctuationButton));
        this.durationInput = this.addChild(new TimeInputWidget(screenArea.pos.offset(63, 75), 10, TimeUtil.TimeUnit.DAY, TimeUtil.TimeUnit.MINUTE, this::onTimeSet));
        this.durationInput.setTime(this.getRule().getDuration());
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        PriceFluctuation rule = this.getRule();
        if (rule != null) {
            gui.pushOffset(this.fluctuationInput);
            gui.drawString(LCText.GUI_PRICE_FLUCTUATION_LABEL.get(), this.fluctuationInput.m_5711_() + 4, 3, 16777215);
            gui.popOffset();
            TextRenderUtil.drawCenteredMultilineText(gui, LCText.GUI_PRICE_FLUCTUATION_INFO.get(rule.getFluctuation(), new TimeUtil.TimeData(this.getRule().getDuration()).getShortString()), 10, this.screen.getXSize() - 20, 35, 16777215);
        }
    }

    @Override
    public void tick() {
        TextInputUtil.whitelistInteger(this.fluctuationInput, 1L, 2147483647L);
    }

    void PressSetFluctuationButton(EasyButton button) {
        int fluctuation = TextInputUtil.getIntegerValue(this.fluctuationInput, 1);
        PriceFluctuation rule = this.getRule();
        if (rule != null) {
            rule.setFluctuation(fluctuation);
        }
        this.sendUpdateMessage(LazyPacketData.simpleInt("Fluctuation", fluctuation));
    }

    public void onTimeSet(TimeUtil.TimeData newTime) {
        PriceFluctuation rule = this.getRule();
        if (rule != null) {
            rule.setDuration(newTime.miliseconds);
        }
        this.sendUpdateMessage(LazyPacketData.simpleLong("Duration", newTime.miliseconds));
    }
}