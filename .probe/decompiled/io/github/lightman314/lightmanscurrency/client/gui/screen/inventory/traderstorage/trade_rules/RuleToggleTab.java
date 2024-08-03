package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class RuleToggleTab extends TradeRulesClientSubTab {

    private final List<EasyButton> toggleRuleButtons = new ArrayList();

    public RuleToggleTab(@Nonnull TradeRulesClientTab<?> parent) {
        super(parent);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.toggleRuleButtons.clear();
        int count = this.getFilteredRules().size();
        for (int i = 0; i < count; i++) {
            int index = i;
            this.toggleRuleButtons.add(this.addChild(IconAndButtonUtil.checkmarkButton(screenArea.pos.offset(20, 25 + 12 * i), this::PressManagerActiveButton, () -> {
                List<TradeRule> rules = this.getFilteredRules();
                return index < rules.size() ? ((TradeRule) rules.get(index)).isActive() : false;
            })));
        }
        if (this.commonTab.getHost().isTrade()) {
            this.addChild(new IconButton(screenArea.pos.offset(screenArea.width - 25, 5), this::ClickBackButton, IconAndButtonUtil.ICON_BACK));
        }
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(LCText.GUI_TRADE_RULES_LIST.getWithStyle(ChatFormatting.BOLD), 20, 10, 16777215);
        List<TradeRule> rules = this.getFilteredRules();
        for (int i = 0; i < this.getFilteredRules().size(); i++) {
            TradeRule rule = (TradeRule) rules.get(i);
            MutableComponent name = rule.getName().withStyle(rule.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
            gui.drawString(name, 32, 26 + 12 * i, 16777215);
        }
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.PAPER);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADE_RULES_MANAGER.get();
    }

    void PressManagerActiveButton(EasyButton button) {
        int ruleIndex = this.toggleRuleButtons.indexOf(button);
        if (ruleIndex >= 0) {
            List<TradeRule> rules = this.getFilteredRules();
            if (ruleIndex < rules.size()) {
                TradeRule rule = (TradeRule) rules.get(ruleIndex);
                this.commonTab.EditTradeRule(rule.type, LazyPacketData.simpleBoolean("SetActive", !rule.isActive()));
            }
        }
    }

    private void ClickBackButton(@Nonnull EasyButton button) {
        this.screen.changeTab(2, true, null);
    }
}