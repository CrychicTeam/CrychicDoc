package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.core;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.SettingsSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.TraderSettingsClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollTextDisplay;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class AllyTab extends SettingsSubTab {

    EditBox nameInput;

    EasyButton buttonAddAlly;

    EasyButton buttonRemoveAlly;

    ScrollTextDisplay display;

    public AllyTab(@Nonnull TraderSettingsClientTab parent) {
        super(parent);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.PLAYER_HEAD);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_SETTINGS_ALLY.get();
    }

    @Override
    public boolean canOpen() {
        return this.menu.hasPermission("addRemoveAllies");
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.nameInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 20, screenArea.y + 10, 160, 20, EasyText.empty()));
        this.nameInput.setMaxLength(16);
        this.buttonAddAlly = this.addChild(new EasyTextButton(screenArea.pos.offset(20, 35), 74, 20, LCText.BUTTON_ADD.get(), this::AddAlly));
        this.buttonRemoveAlly = this.addChild(new EasyTextButton(screenArea.pos.offset(screenArea.width - 93, 35), 74, 20, LCText.BUTTON_REMOVE.get(), this::RemoveAlly));
        this.display = this.addChild(new ScrollTextDisplay(screenArea.pos.offset(5, 60), screenArea.width - 10, 75, this::getAllyList));
        this.display.setColumnCount(2);
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
    }

    private List<Component> getAllyList() {
        List<Component> list = Lists.newArrayList();
        TraderData trader = this.menu.getTrader();
        if (trader != null) {
            trader.getAllies().forEach(ally -> list.add(ally.getNameComponent(true)));
        }
        return list;
    }

    @Override
    public void tick() {
        this.nameInput.tick();
        this.buttonAddAlly.f_93623_ = this.buttonRemoveAlly.f_93623_ = !this.nameInput.getValue().isEmpty();
    }

    private void AddAlly(EasyButton button) {
        String allyName = this.nameInput.getValue();
        this.sendMessage(LazyPacketData.simpleString("AddAlly", allyName));
        this.nameInput.setValue("");
    }

    private void RemoveAlly(EasyButton button) {
        String allyName = this.nameInput.getValue();
        this.sendMessage(LazyPacketData.simpleString("RemoveAlly", allyName));
        this.nameInput.setValue("");
    }
}