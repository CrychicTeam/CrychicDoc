package journeymap.client.api.impl;

import com.google.common.collect.Lists;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.ui.GuiHooks;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.fullscreen.menu.PopupMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;

public class ModPopupMenuImpl implements ModPopupMenu {

    private final List<ModPopupMenuImpl.MenuItem> menuItemList;

    private final PopupMenu popupMenu;

    private List<ModPopupMenuImpl> childMenus;

    private ModPopupMenuImpl lastClicked;

    private boolean opened = false;

    private boolean sub = false;

    private boolean clicked = false;

    public ModPopupMenuImpl(PopupMenu popupMenu) {
        this.menuItemList = Lists.newArrayList();
        this.childMenus = Lists.newArrayList();
        this.popupMenu = popupMenu;
    }

    public ModPopupMenuImpl(PopupMenu popupMenu, boolean sub) {
        this(popupMenu);
        this.sub = sub;
    }

    @Override
    public ModPopupMenu addMenuItem(String label, ModPopupMenu.Action action) {
        this.menuItemList.add(new ModPopupMenuImpl.MenuItem(label, action));
        return this;
    }

    @Override
    public ModPopupMenu addMenuItemScreen(String label, Screen screen) {
        this.menuItemList.add(new ModPopupMenuImpl.MenuItem(label, blockPos -> GuiHooks.pushGuiLayer(screen)));
        return this;
    }

    @Override
    public ModPopupMenu createSubItemList(String label) {
        PopupMenu menu = new PopupMenu(this.popupMenu);
        ModPopupMenuImpl modPopupMenu = new ModPopupMenuImpl(menu, true);
        this.childMenus.add(modPopupMenu);
        this.menuItemList.add(new ModPopupMenuImpl.MenuItem(label, this.createSubPopup(modPopupMenu, menu), false));
        return modPopupMenu;
    }

    public List<ModPopupMenuImpl.MenuItem> getMenuItemList() {
        return this.menuItemList;
    }

    public boolean isSub() {
        return this.sub;
    }

    private ModPopupMenuImpl.SubMenuAction createSubPopup(final ModPopupMenuImpl modPopupMenu, final PopupMenu menu) {
        return new ModPopupMenuImpl.SubMenuAction() {

            boolean displayed = false;

            @Override
            public void doAction(BlockPos blockPos, Button button) {
                ModPopupMenuImpl.this.clicked = true;
                ModPopupMenuImpl.this.lastClicked = modPopupMenu;
                this.displayed = ModPopupMenuImpl.this.hoverStateChange(modPopupMenu, menu, blockPos, button, true, false);
            }

            @Override
            public void onHoverState(BlockPos blockPos, Button button, boolean isHovered) {
                if (ModPopupMenuImpl.this.lastClicked != modPopupMenu) {
                    ModPopupMenuImpl.this.clicked = false;
                }
                this.displayed = ModPopupMenuImpl.this.hoverStateChange(modPopupMenu, menu, blockPos, button, isHovered, this.displayed);
            }
        };
    }

    private boolean hoverStateChange(ModPopupMenuImpl modPopupMenu, PopupMenu menu, BlockPos blockPos, Button button, boolean isHovered, boolean displayed) {
        if (isHovered && (!displayed || !modPopupMenu.opened)) {
            this.closeChildren(modPopupMenu);
            int x = this.popupMenu.getPaneX() + this.popupMenu.getPaneWidth() - (this.popupMenu.getScrollPane().isScrollVisible() ? 12 : 6);
            int y = button.m_252907_() - 5;
            menu.setClickLoc(x, y);
            menu.displayOptions(blockPos, modPopupMenu);
            modPopupMenu.opened = true;
        } else if (!this.clicked && displayed && !modPopupMenu.popupMenu.isMouseOver()) {
            this.resetChildren(modPopupMenu);
        }
        return modPopupMenu.opened;
    }

    private void resetChildren(ModPopupMenuImpl modPopupMenu) {
        if (this.hasChildren(modPopupMenu)) {
            modPopupMenu.closeChildren(null);
        }
        modPopupMenu.opened = false;
        modPopupMenu.popupMenu.onClose();
        modPopupMenu.popupMenu.resetPass();
        modPopupMenu.clicked = false;
    }

    private boolean hasChildren(ModPopupMenuImpl menu) {
        return menu.childMenus != null || !menu.childMenus.isEmpty();
    }

    private void closeChildren(ModPopupMenuImpl modPopupMenu) {
        for (ModPopupMenuImpl child : this.childMenus) {
            if (child.opened && !child.equals(modPopupMenu)) {
                this.resetChildren(child);
            }
        }
    }

    public static class MenuItem {

        private final ModPopupMenu.Action action;

        private final String label;

        private boolean autoClose = true;

        public MenuItem(String label, ModPopupMenu.Action action) {
            this.action = action;
            this.label = Constants.getString(label);
        }

        public MenuItem(String label, ModPopupMenuImpl.SubMenuAction subMenu, boolean autoClose) {
            this.autoClose = autoClose;
            this.action = subMenu;
            this.label = Constants.getString(label);
        }

        public ModPopupMenu.Action getAction() {
            return this.action;
        }

        public String getLabel() {
            return this.label;
        }

        public boolean isAutoCloseable() {
            return this.autoClose;
        }

        public ModPopupMenuImpl.SubMenuAction getSubMenuAction() {
            return (ModPopupMenuImpl.SubMenuAction) this.action;
        }
    }

    public interface SubMenuAction extends ModPopupMenu.Action {

        @Override
        default void doAction(BlockPos blockPos) {
        }

        void doAction(BlockPos var1, Button var2);

        void onHoverState(BlockPos var1, Button var2, boolean var3);
    }
}