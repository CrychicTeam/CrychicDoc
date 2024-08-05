package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.ViewGroup;

public interface MenuPresenter {

    void initForMenu(@NonNull Context var1, @Nullable MenuBuilder var2);

    MenuView getMenuView(ViewGroup var1);

    void updateMenuView(boolean var1);

    void setCallback(MenuPresenter.Callback var1);

    boolean onSubMenuSelected(SubMenuBuilder var1);

    void onCloseMenu(MenuBuilder var1, boolean var2);

    boolean flagActionItems();

    boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2);

    boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2);

    int getId();

    public interface Callback {

        void onCloseMenu(MenuBuilder var1, boolean var2);

        boolean onOpenSubMenu(MenuBuilder var1);
    }
}