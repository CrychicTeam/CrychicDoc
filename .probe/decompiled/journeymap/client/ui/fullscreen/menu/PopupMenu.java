package journeymap.client.ui.fullscreen.menu;

import java.util.ArrayList;
import java.util.List;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.impl.ModPopupMenuImpl;
import journeymap.client.event.dispatchers.CustomEventDispatcher;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.GuiHooks;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.DropDownItem;
import journeymap.client.ui.component.Removable;
import journeymap.client.ui.component.ScrollPaneScreen;
import journeymap.client.ui.component.SelectableParent;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.Waypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;

public class PopupMenu extends ScrollPaneScreen implements Removable, SelectableParent {

    private static final int MAX_DISPLAY_SIZE = 6;

    private int mouseX;

    private int mouseY;

    private final Screen parent;

    private DropDownItem selected;

    private boolean pass = false;

    private boolean isSub = false;

    private boolean mouseOver = false;

    public PopupMenu(Fullscreen parent) {
        this((Screen) parent);
    }

    public PopupMenu(PopupMenu parent) {
        this((Screen) parent);
        this.isSub = true;
    }

    private PopupMenu(Screen parent) {
        super(null, null, 0, 0, 0, 0);
        this.parent = parent;
        this.setParent(this);
    }

    public void displayBasicOptions(BlockPos blockPos) {
        ModPopupMenu menu = new ModPopupMenuImpl(((Fullscreen) this.parent).popupMenu);
        if (CustomEventDispatcher.getInstance().popupMenuEvent((Fullscreen) this.parent, menu)) {
            this.displayOptions(blockPos, menu);
        }
    }

    public void displayWaypointOptions(BlockPos blockPos, Waypoint wp) {
        ModPopupMenu menu = new ModPopupMenuImpl(((Fullscreen) this.parent).popupMenu);
        if (CustomEventDispatcher.getInstance().popupWaypointMenuEvent((Fullscreen) this.parent, menu, wp)) {
            this.displayOptions(blockPos, menu);
        }
    }

    public void displayOptions(BlockPos blockPos, ModPopupMenu popupMenu) {
        ModPopupMenuImpl menu = (ModPopupMenuImpl) popupMenu;
        if (menu.getMenuItemList() != null && !menu.getMenuItemList().isEmpty()) {
            List<DropDownItem> items = new ArrayList();
            menu.getMenuItemList().forEach(menuItem -> items.add(this.dropDownItemBuilder(menuItem, menu, blockPos)));
            this.display(items);
        }
    }

    private DropDownItem dropDownItemBuilder(ModPopupMenuImpl.MenuItem menuItem, ModPopupMenuImpl menu, BlockPos blockPos) {
        DropDownItem dropDownItem;
        if (menuItem.isAutoCloseable()) {
            dropDownItem = new DropDownItem(this, menuItem, menuItem.isAutoCloseable(), menuItem.getLabel(), b -> {
                if (menu.isSub()) {
                    this.closeStack();
                }
                menuItem.getAction().doAction(blockPos);
            });
        } else {
            dropDownItem = new DropDownItem(this, menuItem, menuItem.isAutoCloseable(), menuItem.getLabel(), b -> menuItem.getSubMenuAction().doAction(blockPos, (Button) b));
            dropDownItem.setOnHover((button, isHovered) -> menuItem.getSubMenuAction().onHoverState(blockPos, button, isHovered));
        }
        return dropDownItem;
    }

    private void display(List<DropDownItem> items) {
        if (!this.pass) {
            for (DropDownItem item : items) {
                item.setHorizontalAlignment(DrawUtil.HAlign.Right);
            }
            this.pass = true;
            this.setRenderDecorations(false);
            this.setRenderSolidBackground(true);
            this.setItems(items);
            this.setPaneWidth(this.getPaneWidth(items));
            this.setPaneHeight(this.getPaneHeight(items));
            this.setPointsInScreenBounds();
            super.display();
        }
    }

    private void setPointsInScreenBounds() {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        boolean inBoundsX = this.mouseX + this.getPaneWidth() < screenWidth;
        boolean inBoundsY = this.mouseY + this.getPaneHeight() < screenHeight;
        int x;
        if (this.parent instanceof PopupMenu) {
            int width = this.scrollPane != null ? this.getPaneWidth() : this.getPaneWidth() - 6;
            x = inBoundsX ? this.mouseX : ((PopupMenu) this.parent).getPaneX() - width;
        } else {
            x = inBoundsX ? this.mouseX : screenWidth - this.getPaneWidth() - 4;
        }
        int y = inBoundsY ? this.mouseY : screenHeight - this.getPaneHeight() - 2;
        this.mouseX = x;
        this.mouseY = y;
        this.setPaneX(x);
        this.setPaneY(y);
    }

    public void resetPass() {
        this.pass = false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.mouseOver = this.mouseOverPane((double) mouseX, (double) mouseY);
        int x = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
        int y = (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight());
        super.render(graphics, x, y, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (!clicked && !this.mouseOverPane(mouseX, mouseY)) {
            this.resetPass();
            return this.parent.m_6375_(mouseX, mouseY, button);
        } else {
            return clicked;
        }
    }

    public boolean isMouseOver() {
        return this.mouseOver;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (!this.mouseOverPane(mouseX, mouseY)) {
            this.parent.m_94757_(mouseX, mouseY);
        }
        super.mouseMoved(mouseX, mouseY);
    }

    private int getPaneHeight(List<DropDownItem> items) {
        int size = Math.min(items.size(), 6);
        return size * (((DropDownItem) items.get(0)).m_93694_() + (size == 1 ? 7 : 5));
    }

    private int getPaneWidth(List<DropDownItem> items) {
        int width = 0;
        if (items != null) {
            Font fontRenderer = Minecraft.getInstance().font;
            for (DropDownItem item : items) {
                width = Math.max(width, fontRenderer.width(item.getLabel()));
            }
            this.f_96543_ = width + 40;
        }
        return this.f_96543_;
    }

    public void setClickLoc(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void setSelected(DropDownItem button) {
        this.selected = button;
    }

    public void closeStack() {
        if (this.isSub) {
            ((PopupMenu) this.parent).closeStack();
            this.onClose();
        }
    }

    @Override
    public void onClick(DropDownItem pressed) {
        if (pressed.isAutoClose()) {
            this.onClose();
        }
        super.parent.onRemove();
    }

    @Override
    public void onClose() {
        this.visible = false;
        GuiHooks.popGuiLayer();
    }

    @Override
    public void onRemove() {
        if (this.selected != null) {
            this.selected.press();
            this.selected = null;
        }
    }
}