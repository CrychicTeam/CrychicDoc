package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.LoadingScreen;
import dev.ftb.mods.ftblibrary.util.BooleanConsumer;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public abstract class BaseScreen extends Panel {

    private final Screen prevScreen;

    private int mouseX;

    private int mouseY;

    private float partialTicks;

    private boolean refreshWidgets;

    private Window screen;

    private long lastClickTime = 0L;

    private final Deque<ModalPanel> modalPanels;

    private Widget focusedWidget = null;

    public BaseScreen() {
        super(null);
        this.setSize(176, 166);
        this.setOnlyRenderWidgetsInside(false);
        this.setOnlyInteractWithWidgetsInside(false);
        this.prevScreen = Minecraft.getInstance().screen;
        this.modalPanels = new ArrayDeque();
    }

    @Override
    public final BaseScreen getGui() {
        return this;
    }

    @Override
    public void alignWidgets() {
    }

    public final void initGui() {
        if (this.parent instanceof BaseScreen) {
            this.screen = this.parent.getScreen();
        } else {
            this.screen = Minecraft.getInstance().getWindow();
        }
        if (this.onInit()) {
            super.refreshWidgets();
            this.alignWidgets();
            this.onPostInit();
        }
    }

    public Theme getTheme() {
        return ThemeManager.INSTANCE.getActiveTheme();
    }

    @Override
    public int getX() {
        return (this.getScreen().getGuiScaledWidth() - this.width) / 2;
    }

    @Override
    public int getY() {
        return (this.getScreen().getGuiScaledHeight() - this.height) / 2;
    }

    @Override
    public void setScrollX(double scroll) {
    }

    @Override
    public void setScrollY(double scroll) {
    }

    @Override
    public double getScrollX() {
        return 0.0;
    }

    @Override
    public double getScrollY() {
        return 0.0;
    }

    public boolean onInit() {
        return true;
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    protected boolean setFullscreen() {
        return this.setSizeProportional(1.0F, 1.0F);
    }

    protected boolean setSizeProportional(float w, float h) {
        Validate.isTrue(w > 0.0F && w <= 1.0F && h > 0.0F && h <= 1.0F, "width and height must be > 0 and <= 1", new Object[0]);
        if (this.screen == null) {
            return false;
        } else {
            this.setWidth((int) ((float) this.screen.getGuiScaledWidth() * w));
            this.setHeight((int) ((float) this.screen.getGuiScaledHeight() * h));
            return true;
        }
    }

    public void onPostInit() {
    }

    public void pushModalPanel(ModalPanel modalPanel) {
        this.modalPanels.addFirst(modalPanel);
        modalPanel.refreshWidgets();
        modalPanel.setX(Math.min(modalPanel.getX(), this.getWidth() - modalPanel.getWidth() - 10));
        modalPanel.setY(Math.min(modalPanel.getY(), this.getHeight() - modalPanel.getHeight() - 10));
    }

    public ModalPanel popModalPanel() {
        if (this.modalPanels.isEmpty()) {
            return null;
        } else {
            ModalPanel panel = (ModalPanel) this.modalPanels.removeFirst();
            panel.onClosed();
            this.focusedWidget = null;
            return panel;
        }
    }

    public void closeModalPanel(ModalPanel panel) {
        if (this.modalPanels.contains(panel)) {
            while (!this.modalPanels.isEmpty() && this.popModalPanel() != panel) {
            }
        }
    }

    public boolean anyModalPanelOpen() {
        return !this.modalPanels.isEmpty();
    }

    @Nullable
    public Screen getPrevScreen() {
        if (this.prevScreen instanceof ScreenWrapper sw && sw.getGui() instanceof LoadingScreen) {
            return sw.getGui().getPrevScreen();
        }
        return this.prevScreen instanceof ChatScreen ? null : this.prevScreen;
    }

    @Override
    public final void closeGui(boolean openPrevScreen) {
        double mx = Minecraft.getInstance().mouseHandler.xpos();
        double my = Minecraft.getInstance().mouseHandler.ypos();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.closeContainer();
            if (mc.screen == null) {
                mc.setWindowActive(true);
            }
        }
        if (openPrevScreen && this.getPrevScreen() != null) {
            mc.setScreen(this.getPrevScreen());
            GLFW.glfwSetCursorPos(this.getScreen().getWindow(), mx, my);
        }
        this.modalPanels.clear();
        this.onClosed();
    }

    public boolean onClosedByKey(Key key) {
        return key.escOrInventory();
    }

    public void onBack() {
        this.closeGui(true);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public final void refreshWidgets() {
        this.refreshWidgets = true;
    }

    public final void updateGui(int mx, int my, float pt) {
        this.mouseX = mx;
        this.mouseY = my;
        this.partialTicks = pt;
        if (this.refreshWidgets) {
            super.refreshWidgets();
            this.modalPanels.forEach(Panel::refreshWidgets);
            this.refreshWidgets = false;
        }
        this.posX = this.getX();
        this.posY = this.getY();
        this.updateMouseOver(this.mouseX, this.mouseY);
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        this.isMouseOver = this.checkMouseOver(mouseX, mouseY);
        this.setOffset(true);
        this.modalPanels.forEach(p -> p.updateMouseOver(mouseX, mouseY));
        this.widgets.forEach(w -> w.updateMouseOver(mouseX, mouseY));
        this.setOffset(false);
    }

    @Override
    public final void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        if (!this.modalPanels.isEmpty()) {
            boolean r = this.getOnlyRenderWidgetsInside();
            boolean i = this.getOnlyInteractWithWidgetsInside();
            this.setOnlyRenderWidgetsInside(false);
            this.setOnlyInteractWithWidgetsInside(false);
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 10.0F);
            Iterator<ModalPanel> iter = this.modalPanels.descendingIterator();
            while (iter.hasNext()) {
                ModalPanel p = (ModalPanel) iter.next();
                if (!iter.hasNext()) {
                    graphics.pose().translate(0.0, 0.0, -0.05);
                    Color4I.rgba(-1608507360).draw(graphics, 0, 0, this.getScreen().getGuiScaledWidth(), this.getScreen().getGuiScaledHeight());
                    graphics.pose().translate(0.0, 0.0, 0.05);
                }
                graphics.pose().translate(0.0F, 0.0F, (float) p.getExtraZlevel());
                p.draw(graphics, theme, p.getX(), p.getY(), p.getWidth(), p.getHeight());
                graphics.pose().translate(0.0F, 0.0F, 1.0F);
            }
            graphics.pose().popPose();
            this.setOnlyRenderWidgetsInside(r);
            this.setOnlyRenderWidgetsInside(i);
        }
    }

    public Optional<ModalPanel> getContextMenu() {
        return this.modalPanels.stream().filter(p -> p instanceof ContextMenu).findFirst();
    }

    public void openContextMenu(@Nullable ContextMenu newContextMenu) {
        if (newContextMenu == null) {
            this.modalPanels.removeIf(p -> p instanceof ContextMenu);
        } else {
            this.pushModalPanel(newContextMenu);
            int x = this.getX();
            int y = this.getY();
            int px = Math.min(this.getMouseX() - x, this.screen.getGuiScaledWidth() - newContextMenu.width - x) - 3;
            int py = Math.min(this.getMouseY() - y, this.screen.getGuiScaledHeight() - newContextMenu.height - y) - 3;
            newContextMenu.setPos(px, py);
        }
    }

    public ContextMenu openContextMenu(@NotNull List<ContextMenuItem> menuItems) {
        ContextMenu contextMenu = new ContextMenu(this, menuItems);
        this.openContextMenu(contextMenu);
        return contextMenu;
    }

    @Override
    public void closeContextMenu() {
        this.openContextMenu((ContextMenu) null);
    }

    @Override
    public void onClosed() {
        super.onClosed();
        this.closeContextMenu();
        CursorType.set(null);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawGui(graphics, x, y, w, h, WidgetType.NORMAL);
    }

    public boolean drawDefaultBackground(GuiGraphics graphics) {
        return true;
    }

    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    private Panel getDoubleClickTarget() {
        return (Panel) (this.modalPanels.isEmpty() ? this : (Panel) this.modalPanels.peekFirst());
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (button == MouseButton.BACK) {
            this.closeGui(true);
            return true;
        } else {
            long now = System.currentTimeMillis();
            if (this.lastClickTime != 0L && now - this.lastClickTime <= 300L && this.getDoubleClickTarget().mouseDoubleClicked(button)) {
                this.lastClickTime = 0L;
                return true;
            } else {
                this.lastClickTime = now;
                if (this.modalPanels.isEmpty()) {
                    return super.mousePressed(button);
                } else if (((ModalPanel) this.modalPanels.peekFirst()).isMouseOver()) {
                    return ((ModalPanel) this.modalPanels.peekFirst()).mousePressed(button);
                } else {
                    this.popModalPanel();
                    return false;
                }
            }
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (this.focusedWidget != null && this.focusedWidget.keyPressed(key)) {
            return true;
        } else if (!this.modalPanels.isEmpty()) {
            if (key.esc()) {
                this.popModalPanel();
                return true;
            } else {
                return ((ModalPanel) this.modalPanels.peekFirst()).keyPressed(key);
            }
        } else if (super.keyPressed(key)) {
            return true;
        } else if (InputConstants.isKeyDown(this.getGui().screen.getWindow(), 292) && key.is(66)) {
            Theme.renderDebugBoxes = !Theme.renderDebugBoxes;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void keyReleased(Key key) {
        if (this.modalPanels.isEmpty()) {
            super.keyReleased(key);
        } else {
            ((ModalPanel) this.modalPanels.peekFirst()).keyReleased(key);
        }
    }

    @Override
    public boolean mouseDoubleClicked(MouseButton button) {
        return this.modalPanels.isEmpty() ? super.mouseDoubleClicked(button) : ((ModalPanel) this.modalPanels.peekFirst()).mouseDoubleClicked(button);
    }

    @Override
    public void mouseReleased(MouseButton button) {
        if (this.modalPanels.isEmpty()) {
            super.mouseReleased(button);
        } else {
            ((ModalPanel) this.modalPanels.peekFirst()).mouseReleased(button);
        }
    }

    @Override
    public boolean mouseScrolled(double scroll) {
        if (this.focusedWidget != null && this.focusedWidget.mouseScrolled(scroll)) {
            return true;
        } else {
            return this.modalPanels.isEmpty() ? super.mouseScrolled(scroll) : ((ModalPanel) this.modalPanels.peekFirst()).mouseScrolled(scroll);
        }
    }

    @Override
    public boolean mouseDragged(int button, double dragX, double dragY) {
        return this.modalPanels.isEmpty() ? super.mouseDragged(button, dragX, dragY) : ((ModalPanel) this.modalPanels.peekFirst()).mouseDragged(button, dragX, dragY);
    }

    @Override
    public boolean charTyped(char c, KeyModifiers modifiers) {
        if (this.focusedWidget != null && this.focusedWidget.charTyped(c, modifiers)) {
            return true;
        } else {
            return this.modalPanels.isEmpty() ? super.charTyped(c, modifiers) : ((ModalPanel) this.modalPanels.peekFirst()).charTyped(c, modifiers);
        }
    }

    @Override
    public boolean shouldAddMouseOverText() {
        return this.getContextMenu().isEmpty();
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (!this.modalPanels.isEmpty()) {
            ((ModalPanel) this.modalPanels.peekFirst()).addMouseOverText(list);
        } else {
            super.addMouseOverText(list);
        }
    }

    @Override
    public final void openGui() {
        this.openContextMenu((ContextMenu) null);
        Minecraft.getInstance().setScreen(new ScreenWrapper(this));
    }

    @Override
    public final Window getScreen() {
        return this.screen == null ? this.parent.getScreen() : this.screen;
    }

    @Override
    public final int getMouseX() {
        return this.mouseX;
    }

    @Override
    public final int getMouseY() {
        return this.mouseY;
    }

    @Override
    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public boolean isMouseOver(int x, int y, int w, int h) {
        return this.getMouseX() >= x && this.getMouseY() >= y && this.getMouseX() < x + w && this.getMouseY() < y + h;
    }

    public boolean isMouseOver(Widget widget) {
        if (widget == this) {
            return this.isMouseOver(this.getX(), this.getY(), this.width, this.height);
        } else if (this.isMouseOver(widget.getX(), widget.getY(), widget.width, widget.height)) {
            boolean offset = widget.parent.isOffset();
            widget.parent.setOffset(false);
            boolean b = this.isMouseOver(widget.parent);
            widget.parent.setOffset(offset);
            return b;
        } else {
            return false;
        }
    }

    @Override
    public boolean handleClick(String scheme, String path) {
        return ClientUtils.handleClick(scheme, path);
    }

    public void openYesNoFull(Component title, Component desc, BooleanConsumer callback) {
        Minecraft.getInstance().setScreen(new ConfirmScreen(result -> {
            this.openGui();
            callback.accept(result);
            this.refreshWidgets();
        }, title, desc));
    }

    public final void openYesNo(Component title, Component desc, Runnable callback) {
        this.openYesNoFull(title, desc, result -> {
            if (result) {
                callback.run();
            }
        });
    }

    public void setFocusedWidget(Widget widget) {
        Validate.isTrue(widget instanceof IFocusableWidget);
        if (this.focusedWidget instanceof IFocusableWidget f && this.focusedWidget != widget) {
            f.setFocused(false);
        }
        this.focusedWidget = widget;
    }

    public static class PositionedTextData {

        public final int posX;

        public final int posY;

        public final int width;

        public final int height;

        public final ClickEvent clickEvent;

        public final HoverEvent hoverEvent;

        public final String insertion;

        public PositionedTextData(int x, int y, int w, int h, Style s) {
            this.posX = x;
            this.posY = y;
            this.width = w;
            this.height = h;
            this.clickEvent = s.getClickEvent();
            this.hoverEvent = s.getHoverEvent();
            this.insertion = s.getInsertion();
        }
    }
}