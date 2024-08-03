package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.drawable.Drawable;

public interface SubMenu extends Menu {

    @NonNull
    SubMenu setHeaderTitle(CharSequence var1);

    @NonNull
    SubMenu setHeaderIcon(Drawable var1);

    @NonNull
    SubMenu setHeaderView(View var1);

    void clearHeader();

    @NonNull
    SubMenu setIcon(Drawable var1);

    @NonNull
    MenuItem getItem();
}