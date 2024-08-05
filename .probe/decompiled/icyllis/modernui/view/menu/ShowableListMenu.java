package icyllis.modernui.view.menu;

import icyllis.modernui.widget.ListView;

public interface ShowableListMenu {

    void show();

    void dismiss();

    boolean isShowing();

    ListView getListView();
}