package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.View;

public class ContextMenuBuilder extends MenuBuilder implements ContextMenu {

    public ContextMenuBuilder(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ContextMenu setHeaderIcon(Drawable icon) {
        super.setHeaderIconInt(icon);
        return this;
    }

    @NonNull
    @Override
    public ContextMenu setHeaderTitle(CharSequence title) {
        super.setHeaderTitleInt(title);
        return this;
    }

    @NonNull
    @Override
    public ContextMenu setHeaderView(View view) {
        super.setHeaderViewInt(view);
        return this;
    }

    public MenuPopupHelper showPopup(Context context, View originalView, float x, float y) {
        if (originalView != null) {
            originalView.createContextMenu(this);
        }
        if (this.getVisibleItems().size() > 0) {
            int[] location = new int[2];
            assert originalView != null;
            originalView.getLocationInWindow(location);
            MenuPopupHelper helper = new MenuPopupHelper(context, this, originalView, false);
            helper.show(Math.round(x), Math.round(y));
            return helper;
        } else {
            return null;
        }
    }
}