package icyllis.modernui.widget;

import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.menu.MenuBuilder;
import javax.annotation.Nonnull;

public interface MenuItemHoverListener {

    void onItemHoverExit(@Nonnull MenuBuilder var1, @Nonnull MenuItem var2);

    void onItemHoverEnter(@Nonnull MenuBuilder var1, @Nonnull MenuItem var2);
}