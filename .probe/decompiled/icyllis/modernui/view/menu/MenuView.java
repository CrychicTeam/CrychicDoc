package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.drawable.Drawable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface MenuView {

    void initialize(MenuBuilder var1);

    int getWindowAnimations();

    public interface ItemView {

        void initialize(@NonNull MenuItemImpl var1, int var2);

        MenuItemImpl getItemData();

        void setTitle(CharSequence var1);

        void setEnabled(boolean var1);

        void setCheckable(boolean var1);

        void setChecked(boolean var1);

        void setShortcut(boolean var1, char var2);

        void setIcon(Drawable var1);

        boolean prefersCondensedTitle();

        boolean showsIcon();
    }
}