package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.AdapterView;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.HeaderViewListAdapter;
import icyllis.modernui.widget.ListAdapter;
import icyllis.modernui.widget.PopupWindow;

public abstract class MenuPopup implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener {

    private Rect mEpicenterBounds;

    public abstract void setForceShowIcon(boolean var1);

    public abstract void addMenu(MenuBuilder var1);

    public abstract void setGravity(int var1);

    public abstract void setAnchorView(View var1);

    public abstract void setHorizontalOffset(int var1);

    public abstract void setVerticalOffset(int var1);

    public void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds;
    }

    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds;
    }

    public abstract void setShowTitle(boolean var1);

    public abstract void setOnDismissListener(PopupWindow.OnDismissListener var1);

    @Override
    public void initForMenu(@NonNull Context context, @Nullable MenuBuilder menu) {
    }

    @Override
    public MenuView getMenuView(ViewGroup root) {
        throw new UnsupportedOperationException("MenuPopups manage their own views");
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
        ListAdapter outerAdapter = (ListAdapter) parent.getAdapter();
        MenuAdapter wrappedAdapter = toMenuAdapter(outerAdapter);
        wrappedAdapter.mAdapterMenu.performItemAction((MenuItem) outerAdapter.getItem(position), 0);
    }

    protected static int measureIndividualMenuWidth(@NonNull ListAdapter adapter, @Nullable ViewGroup parent, Context context, int maxAllowedWidth) {
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (parent == null) {
                parent = new FrameLayout(context);
            }
            itemView = adapter.getView(i, itemView, parent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            if (itemWidth >= maxAllowedWidth) {
                return maxAllowedWidth;
            }
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth;
    }

    protected static MenuAdapter toMenuAdapter(ListAdapter adapter) {
        return adapter instanceof HeaderViewListAdapter ? (MenuAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter() : (MenuAdapter) adapter;
    }

    protected static boolean shouldPreserveIconSpacing(@NonNull MenuBuilder menu) {
        boolean preserveIconSpacing = false;
        int count = menu.size();
        for (int i = 0; i < count; i++) {
            MenuItem childItem = menu.getItem(i);
            if (childItem.isVisible() && childItem.getIcon() != null) {
                preserveIconSpacing = true;
                break;
            }
        }
        return preserveIconSpacing;
    }
}