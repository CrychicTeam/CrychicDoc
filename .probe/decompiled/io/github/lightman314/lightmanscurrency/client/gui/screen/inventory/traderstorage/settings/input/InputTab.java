package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.input;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.SettingsSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.TraderSettingsClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.DirectionalSettingsWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.traders.InputTraderData;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class InputTab extends SettingsSubTab {

    DirectionalSettingsWidget inputWidget;

    DirectionalSettingsWidget outputWidget;

    public InputTab(@Nonnull TraderSettingsClientTab parent) {
        super(parent);
    }

    protected InputTraderData getInputTrader() {
        TraderData trader = this.menu.getTrader();
        return trader instanceof InputTraderData ? (InputTraderData) trader : null;
    }

    protected boolean getInputSideValue(Direction side) {
        InputTraderData trader = this.getInputTrader();
        return trader != null ? trader.allowInputSide(side) : false;
    }

    protected boolean getOutputSideValue(Direction side) {
        InputTraderData trader = this.getInputTrader();
        return trader != null ? trader.allowOutputSide(side) : false;
    }

    protected ImmutableList<Direction> getIgnoreList() {
        InputTraderData trader = this.getInputTrader();
        return trader != null ? trader.ignoreSides : ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        InputTraderData trader = this.getInputTrader();
        return trader != null ? trader.inputSettingsTabIcon() : IconData.of(Items.HOPPER);
    }

    public MutableComponent getTooltip() {
        InputTraderData trader = this.getInputTrader();
        return trader != null ? trader.inputSettingsTabTooltip() : LCText.TOOLTIP_TRADER_SETTINGS_INPUT_GENERIC.get();
    }

    public List<? extends InputTabAddon> getAddons() {
        InputTraderData trader = this.getInputTrader();
        return (List<? extends InputTabAddon>) (trader != null ? trader.inputSettingsAddons() : ImmutableList.of());
    }

    @Override
    public boolean canOpen() {
        return this.menu.hasPermission("changeExternalInputs");
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.inputWidget = new DirectionalSettingsWidget(screenArea.pos.offset(20, 25), this::getInputSideValue, this.getIgnoreList(), this::ToggleInputSide, this::addChild);
        this.outputWidget = new DirectionalSettingsWidget(screenArea.pos.offset(110, 25), this::getOutputSideValue, this.getIgnoreList(), this::ToggleOutputSide, this::addChild);
        this.getAddons().forEach(a -> a.onOpen(this, screenArea, firstOpen));
    }

    @Override
    protected void onSubtabClose() {
        this.getAddons().forEach(a -> a.onClose(this));
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(LCText.GUI_SETTINGS_INPUT_SIDE.get(), 20, 7, 4210752);
        gui.drawString(LCText.GUI_SETTINGS_OUTPUT_SIDE.get(), 110, 7, 4210752);
        this.getAddons().forEach(a -> a.renderBG(this, gui));
    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        this.getAddons().forEach(a -> a.renderBG(this, gui));
    }

    @Override
    public void tick() {
        this.getAddons().forEach(a -> a.tick(this));
    }

    private void ToggleInputSide(Direction side) {
        this.sendMessage(LazyPacketData.builder().setBoolean("SetInputSide", !this.getInputSideValue(side)).setInt("Side", side.get3DDataValue()));
    }

    private void ToggleOutputSide(Direction side) {
        this.sendMessage(LazyPacketData.builder().setBoolean("SetOutputSide", !this.getOutputSideValue(side)).setInt("Side", side.get3DDataValue()));
    }
}