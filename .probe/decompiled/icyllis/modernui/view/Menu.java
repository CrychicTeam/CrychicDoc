package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public interface Menu {

    int USER_MASK = 65535;

    int USER_SHIFT = 0;

    int CATEGORY_MASK = -65536;

    int CATEGORY_SHIFT = 16;

    int SUPPORTED_MODIFIERS_MASK = KeyEvent.META_CTRL_ON | 4 | 1;

    int NONE = 0;

    int FIRST = 1;

    int CATEGORY_CONTAINER = 65536;

    int CATEGORY_SYSTEM = 131072;

    int CATEGORY_SECONDARY = 196608;

    int CATEGORY_ALTERNATIVE = 262144;

    int FLAG_PERFORM_NO_CLOSE = 1;

    int FLAG_ALWAYS_PERFORM_CLOSE = 2;

    @NonNull
    MenuItem add(CharSequence var1);

    @NonNull
    MenuItem add(int var1, int var2, int var3, CharSequence var4);

    @NonNull
    SubMenu addSubMenu(CharSequence var1);

    @NonNull
    SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4);

    void removeItem(int var1);

    void removeGroup(int var1);

    void clear();

    void setGroupCheckable(int var1, boolean var2, boolean var3);

    void setGroupVisible(int var1, boolean var2);

    void setGroupEnabled(int var1, boolean var2);

    boolean hasVisibleItems();

    @Nullable
    MenuItem findItem(int var1);

    int size();

    @NonNull
    MenuItem getItem(int var1);

    void close();

    boolean performShortcut(int var1, @NonNull KeyEvent var2, int var3);

    boolean isShortcutKey(int var1, @NonNull KeyEvent var2);

    boolean performIdentifierAction(int var1, int var2);

    void setQwertyMode(boolean var1);

    void setGroupDividerEnabled(boolean var1);
}