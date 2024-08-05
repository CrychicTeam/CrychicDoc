package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.rule_tabs;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRuleSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules.TradeRulesClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollTextDisplay;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextInputUtil;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerDiscounts;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class PlayerDiscountTab extends TradeRuleSubTab<PlayerDiscounts> {

    EditBox nameInput;

    EditBox discountInput;

    EasyButton buttonAddPlayer;

    EasyButton buttonRemovePlayer;

    EasyButton buttonSetDiscount;

    ScrollTextDisplay playerList;

    public PlayerDiscountTab(@Nonnull TradeRulesClientTab<?> parent) {
        super(parent, PlayerDiscounts.TYPE);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_DISCOUNT_LIST;
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.nameInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 10, screenArea.y + 34, screenArea.width - 20, 20, EasyText.empty()));
        this.buttonAddPlayer = this.addChild(new EasyTextButton(screenArea.pos.offset(10, 55), 78, 20, LCText.BUTTON_ADD.get(), this::PressAddButton));
        this.buttonRemovePlayer = this.addChild(new EasyTextButton(screenArea.pos.offset(screenArea.width - 88, 55), 78, 20, LCText.BUTTON_REMOVE.get(), this::PressForgetButton));
        this.discountInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 10, screenArea.y + 9, 20, 20, EasyText.empty()));
        this.discountInput.setMaxLength(2);
        PlayerDiscounts rule = this.getRule();
        if (rule != null) {
            this.discountInput.setValue(Integer.toString(rule.getDiscount()));
        }
        this.buttonSetDiscount = this.addChild(new EasyTextButton(screenArea.pos.offset(110, 10), 50, 20, LCText.BUTTON_SET.get(), this::PressSetDiscountButton));
        this.playerList = this.addChild(new ScrollTextDisplay(screenArea.pos.offset(7, 78), screenArea.width - 14, 61, this::getPlayerList));
        this.playerList.setColumnCount(2);
    }

    private List<Component> getPlayerList() {
        List<Component> playerList = Lists.newArrayList();
        PlayerDiscounts rule = this.getRule();
        if (rule == null) {
            return playerList;
        } else {
            UnmodifiableIterator var3 = rule.getPlayerList().iterator();
            while (var3.hasNext()) {
                PlayerReference player = (PlayerReference) var3.next();
                playerList.add(player.getNameComponent(true));
            }
            return playerList;
        }
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        if (this.getRule() != null) {
            gui.pushOffset(this.discountInput);
            gui.drawString(LCText.GUI_PLAYER_DISCOUNTS_INFO.get(), this.discountInput.m_5711_() + 4, 3, 16777215);
            gui.popOffset();
        }
    }

    @Override
    public void tick() {
        TextInputUtil.whitelistInteger(this.discountInput, 0L, 100L);
    }

    void PressAddButton(EasyButton button) {
        String name = this.nameInput.getValue();
        if (!name.isBlank()) {
            this.nameInput.setValue("");
            this.sendUpdateMessage(LazyPacketData.builder().setBoolean("Add", true).setString("Name", name));
        }
    }

    void PressForgetButton(EasyButton button) {
        String name = this.nameInput.getValue();
        if (!name.isBlank()) {
            this.nameInput.setValue("");
            this.sendUpdateMessage(LazyPacketData.builder().setBoolean("Add", false).setString("Name", name));
        }
    }

    void PressSetDiscountButton(EasyButton button) {
        int discount = TextInputUtil.getIntegerValue(this.discountInput, 1);
        PlayerDiscounts rule = this.getRule();
        if (rule != null) {
            rule.setDiscount(discount);
        }
        this.sendUpdateMessage(LazyPacketData.simpleInt("Discount", discount));
    }
}