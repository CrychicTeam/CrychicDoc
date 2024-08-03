package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.drawable.Drawable;

public interface ContextMenu extends Menu {

    @NonNull
    ContextMenu setHeaderTitle(CharSequence var1);

    @NonNull
    ContextMenu setHeaderIcon(Drawable var1);

    @NonNull
    ContextMenu setHeaderView(View var1);

    void clearHeader();

    public interface ContextMenuInfo {
    }
}