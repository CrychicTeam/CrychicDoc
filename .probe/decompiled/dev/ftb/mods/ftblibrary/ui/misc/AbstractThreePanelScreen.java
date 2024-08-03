package dev.ftb.mods.ftblibrary.ui.misc;

import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.ScrollBar;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.ThemeManager;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class AbstractThreePanelScreen<T extends Panel> extends BaseScreen {

    protected static final int BOTTOM_PANEL_H = 25;

    private static final int SCROLLBAR_WIDTH = 12;

    public static final Pair<Integer, Integer> NO_INSET = Pair.of(0, 0);

    protected final Panel topPanel;

    protected final T mainPanel;

    protected final Panel bottomPanel;

    protected final PanelScrollBar scrollBar;

    private boolean showBottomPanel = true;

    private boolean showCloseButton = false;

    protected AbstractThreePanelScreen() {
        this.topPanel = this.createTopPanel();
        this.mainPanel = this.createMainPanel();
        this.bottomPanel = new AbstractThreePanelScreen.BottomPanel();
        this.scrollBar = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, this.mainPanel);
    }

    @Override
    public void addWidgets() {
        this.add(this.topPanel);
        this.add(this.mainPanel);
        this.add(this.scrollBar);
        if (this.showBottomPanel) {
            this.add(this.bottomPanel);
        }
    }

    @Override
    public void alignWidgets() {
        int topPanelHeight = this.getTopPanelHeight();
        this.topPanel.setPosAndSize(0, 0, this.width, topPanelHeight);
        this.topPanel.alignWidgets();
        Pair<Integer, Integer> inset = this.mainPanelInset();
        int bottomPanelHeight = this.showBottomPanel ? 25 + (Integer) inset.getSecond() : 0;
        this.mainPanel.setPosAndSize((Integer) inset.getFirst(), topPanelHeight + (Integer) inset.getSecond(), this.width - (Integer) inset.getFirst() * 2, this.height - topPanelHeight - (Integer) inset.getSecond() * 2 - bottomPanelHeight);
        this.mainPanel.alignWidgets();
        if (this.showBottomPanel) {
            this.bottomPanel.setPosAndSize(0, this.height - 25, this.width, 25);
            this.bottomPanel.alignWidgets();
        }
        this.scrollBar.setPosAndSize(this.mainPanel.getPosX() + this.mainPanel.getWidth() - this.getScrollbarWidth(), this.mainPanel.getPosY(), this.getScrollbarWidth(), this.mainPanel.getHeight());
    }

    @Override
    public Theme getTheme() {
        return ThemeManager.INSTANCE.getActiveTheme();
    }

    @Override
    public void tick() {
        super.tick();
        int prevWidth = this.mainPanel.width;
        int newWidth = (this.scrollBar.shouldDraw() ? this.getGui().width - this.getScrollbarWidth() - 2 : this.getGui().width) - (Integer) this.mainPanelInset().getFirst() * 2;
        if (prevWidth != newWidth) {
            this.mainPanel.setWidth(newWidth);
            this.mainPanel.alignWidgets();
        }
    }

    protected abstract void doCancel();

    protected abstract void doAccept();

    protected abstract int getTopPanelHeight();

    protected abstract T createMainPanel();

    protected Pair<Integer, Integer> mainPanelInset() {
        return NO_INSET;
    }

    protected int getScrollbarWidth() {
        return 12;
    }

    protected Panel createTopPanel() {
        return new AbstractThreePanelScreen.TopPanel();
    }

    public void showBottomPanel(boolean show) {
        this.showBottomPanel = show;
    }

    public void showCloseButton(boolean show) {
        this.showCloseButton = show;
    }

    private class BottomPanel extends Panel {

        private final Button buttonAccept = SimpleTextButton.accept(this, mb -> AbstractThreePanelScreen.this.doAccept(), TextComponentUtils.hotkeyTooltip("⇧ + Enter"));

        private final Button buttonCancel = SimpleTextButton.cancel(this, mb -> AbstractThreePanelScreen.this.doCancel(), TextComponentUtils.hotkeyTooltip("ESC"));

        public BottomPanel() {
            super(AbstractThreePanelScreen.this);
        }

        @Override
        public void addWidgets() {
            this.add(this.buttonAccept);
            this.add(this.buttonCancel);
        }

        @Override
        public void alignWidgets() {
            this.buttonCancel.setPos(this.width - this.buttonCancel.width - 5, 2);
            this.buttonAccept.setPos(this.buttonCancel.posX - this.buttonAccept.width - 5, 2);
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawPanelBackground(graphics, x, y, w, h);
            Color4I.GRAY.withAlpha(64).draw(graphics, x, y, w, 1);
        }
    }

    protected class TopPanel extends Panel {

        private final SimpleButton closeButton = new SimpleButton(this, Component.translatable("gui.close"), Icons.CLOSE, (btn, mb) -> AbstractThreePanelScreen.this.doCancel());

        public TopPanel() {
            super(AbstractThreePanelScreen.this);
        }

        @Override
        public void addWidgets() {
            if (AbstractThreePanelScreen.this.showCloseButton) {
                this.add(this.closeButton);
            }
        }

        @Override
        public void alignWidgets() {
            if (AbstractThreePanelScreen.this.showCloseButton) {
                this.closeButton.setPosAndSize(this.width - 16, 1, 14, 14);
            }
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawPanelBackground(graphics, x, y, w, h);
            Color4I.BLACK.withAlpha(80).draw(graphics, x, y + h - 1, w, 1);
        }
    }
}