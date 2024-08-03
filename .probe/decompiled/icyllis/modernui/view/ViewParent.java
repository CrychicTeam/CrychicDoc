package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Point;
import icyllis.modernui.graphics.Rect;

public interface ViewParent {

    @Nullable
    ViewParent getParent();

    void requestLayout();

    boolean isLayoutRequested();

    void requestChildFocus(View var1, View var2);

    void clearChildFocus(View var1);

    boolean getChildVisibleRect(View var1, Rect var2, @Nullable Point var3);

    View focusSearch(View var1, int var2);

    View keyboardNavigationClusterSearch(View var1, int var2);

    void bringChildToFront(View var1);

    void focusableViewAvailable(View var1);

    boolean showContextMenuForChild(View var1, float var2, float var3);

    void createContextMenu(ContextMenu var1);

    ActionMode startActionModeForChild(View var1, ActionMode.Callback var2, int var3);

    void childDrawableStateChanged(View var1);

    void requestDisallowInterceptTouchEvent(boolean var1);

    boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3);

    void childHasTransientStateChanged(View var1, boolean var2);

    boolean canResolveLayoutDirection();

    boolean isLayoutDirectionResolved();

    int getLayoutDirection();

    boolean canResolveTextDirection();

    boolean isTextDirectionResolved();

    int getTextDirection();

    boolean canResolveTextAlignment();

    boolean isTextAlignmentResolved();

    int getTextAlignment();

    boolean onStartNestedScroll(@NonNull View var1, @NonNull View var2, int var3, int var4);

    void onNestedScrollAccepted(@NonNull View var1, @NonNull View var2, int var3, int var4);

    void onStopNestedScroll(@NonNull View var1, int var2);

    void onNestedScroll(@NonNull View var1, int var2, int var3, int var4, int var5, int var6, @NonNull int[] var7);

    void onNestedPreScroll(@NonNull View var1, int var2, int var3, @NonNull int[] var4, int var5);

    boolean onNestedFling(@NonNull View var1, float var2, float var3, boolean var4);

    boolean onNestedPreFling(@NonNull View var1, float var2, float var3);

    int getNestedScrollAxes();
}