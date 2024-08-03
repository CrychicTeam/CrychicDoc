package io.github.lightman314.lightmanscurrency.client.gui.screen;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.terminal.TerminalSorter;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.NetworkTraderButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.TerminalMenu;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenTrades;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NetworkTerminalScreen extends EasyMenuScreen<TerminalMenu> implements IScrollable {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/network_terminal.png");

    private EditBox searchField;

    private static int scroll = 0;

    private static String lastSearch = "";

    ScrollBarWidget scrollBar;

    private int columns;

    private int rows;

    List<NetworkTraderButton> traderButtons;

    private List<TraderData> filteredTraderList = new ArrayList();

    private List<TraderData> traderList() {
        List<TraderData> traderList = TraderSaveData.GetAllTerminalTraders(true);
        traderList.sort(TerminalSorter.getDefaultSorter());
        return traderList;
    }

    public NetworkTerminalScreen(TerminalMenu menu, Inventory inventory, Component ignored) {
        super(menu, inventory, LCText.GUI_NETWORK_TERMINAL_TITLE.get());
    }

    private ScreenArea calculateSize() {
        if (this.f_96541_ == null) {
            return this.getArea();
        } else {
            this.columns = 1;
            int columnLimit = LCConfig.CLIENT.terminalColumnLimit.get();
            for (int availableWidth = this.f_96541_.getWindow().getGuiScaledWidth() - 146 - 30; availableWidth >= 146 && this.columns < columnLimit; this.columns++) {
                availableWidth -= 146;
            }
            int availableHeight = this.f_96541_.getWindow().getGuiScaledHeight() - 30 - 37;
            this.rows = 1;
            for (int rowLimit = LCConfig.CLIENT.terminalRowLimit.get(); availableHeight >= 30 && this.rows < rowLimit; this.rows++) {
                availableHeight -= 30;
            }
            this.resize(this.columns * 146 + 30, this.rows * 30 + 36);
            return this.getArea();
        }
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        screenArea = this.calculateSize();
        this.searchField = this.addChild(new EditBox(this.f_96547_, screenArea.x + 28, screenArea.y + 6, 101, 9, this.searchField, LCText.GUI_NETWORK_TERMINAL_SEARCH.get()));
        this.searchField.setBordered(false);
        this.searchField.setMaxLength(32);
        this.searchField.setTextColor(16777215);
        this.searchField.setValue(lastSearch);
        this.searchField.setResponder(this::onSearchChanged);
        this.scrollBar = this.addChild(new ScrollBarWidget(screenArea.pos.offset(16 + 146 * this.columns, 17), 30 * this.rows + 2, this));
        this.initTraderButtons(screenArea);
        this.updateTraderList();
        this.validateScroll();
    }

    private void initTraderButtons(ScreenArea screenArea) {
        this.traderButtons = new ArrayList();
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.columns; x++) {
                NetworkTraderButton newButton = this.addChild(new NetworkTraderButton(screenArea.pos.offset(15 + x * 146, 18 + y * 30), this::OpenTrader));
                this.traderButtons.add(newButton);
            }
        }
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.blitBackgroundOfSize(GUI_TEXTURE, 0, 0, this.f_97726_, this.f_97727_, 0, 0, 100, 100, 25);
        gui.blit(GUI_TEXTURE, 14, 3, 100, 0, 118, 14);
        gui.blitBackgroundOfSize(GUI_TEXTURE, 14, 17, this.f_97726_ - 28, this.f_97727_ - 34, 0, 100, 100, 100, 25);
    }

    protected void onSearchChanged(String newSearch) {
        if (!newSearch.equals(lastSearch)) {
            lastSearch = newSearch;
            this.updateTraderList();
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int mods) {
        String s = this.searchField.getValue();
        if (this.searchField.keyPressed(key, scanCode, mods)) {
            if (!Objects.equals(s, this.searchField.getValue())) {
                this.updateTraderList();
            }
            return true;
        } else {
            return this.searchField.m_93696_() && this.searchField.isVisible() && key != 256 || super.keyPressed(key, scanCode, mods);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.handleScrollWheel(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void OpenTrader(EasyButton button) {
        int index = this.getTraderIndex(button);
        if (index >= 0 && index < this.filteredTraderList.size()) {
            new CPacketOpenTrades(((TraderData) this.filteredTraderList.get(index)).getID()).send();
        }
    }

    private int getTraderIndex(EasyButton button) {
        return button instanceof NetworkTraderButton && this.traderButtons.contains(button) ? this.traderButtons.indexOf(button) + scroll * this.columns : -1;
    }

    private void updateTraderList() {
        this.filteredTraderList = this.searchField.getValue().isBlank() ? this.traderList() : TraderAPI.filterTraders(this.traderList(), this.searchField.getValue());
        this.validateScroll();
        this.updateTraderButtons();
    }

    private void updateTraderButtons() {
        int startIndex = scroll * this.columns;
        for (int i = 0; i < this.traderButtons.size(); i++) {
            if (startIndex + i < this.filteredTraderList.size()) {
                ((NetworkTraderButton) this.traderButtons.get(i)).SetData((TraderData) this.filteredTraderList.get(startIndex + i));
            } else {
                ((NetworkTraderButton) this.traderButtons.get(i)).SetData(null);
            }
        }
    }

    @Override
    public int currentScroll() {
        return scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        scroll = newScroll;
        this.updateTraderButtons();
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(this.columns * this.rows, this.columns, this.filteredTraderList.size());
    }
}