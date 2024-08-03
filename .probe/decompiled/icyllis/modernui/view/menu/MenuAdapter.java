package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.BaseAdapter;
import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {

    Context mContext;

    MenuBuilder mAdapterMenu;

    private int mExpandedIndex = -1;

    private boolean mForceShowIcon;

    private final boolean mOverflowOnly;

    public MenuAdapter(Context context, MenuBuilder menu, boolean overflowOnly) {
        this.mContext = context;
        this.mAdapterMenu = menu;
        this.mOverflowOnly = overflowOnly;
        this.findExpandedIndex();
    }

    public boolean getForceShowIcon() {
        return this.mForceShowIcon;
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
    }

    @Override
    public int getCount() {
        ArrayList<MenuItemImpl> items = this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
        return this.mExpandedIndex < 0 ? items.size() : items.size() - 1;
    }

    public MenuBuilder getAdapterMenu() {
        return this.mAdapterMenu;
    }

    @NonNull
    public MenuItemImpl getItem(int position) {
        ArrayList<MenuItemImpl> items = this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
        if (this.mExpandedIndex >= 0 && position >= this.mExpandedIndex) {
            position++;
        }
        return (MenuItemImpl) items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListMenuItemView view;
        if (convertView == null) {
            view = new ListMenuItemView(this.mContext);
        } else {
            view = (ListMenuItemView) convertView;
        }
        int currGroupId = this.getItem(position).getGroupId();
        int prevGroupId = position - 1 >= 0 ? this.getItem(position - 1).getGroupId() : currGroupId;
        view.setGroupDividerEnabled(this.mAdapterMenu.isGroupDividerEnabled() && currGroupId != prevGroupId);
        if (this.mForceShowIcon) {
            view.setForceShowIcon(true);
        }
        view.initialize(this.getItem(position), 0);
        return view;
    }

    void findExpandedIndex() {
        MenuItemImpl expandedItem = this.mAdapterMenu.getExpandedItem();
        if (expandedItem != null) {
            ArrayList<MenuItemImpl> items = this.mAdapterMenu.getNonActionItems();
            int count = items.size();
            for (int i = 0; i < count; i++) {
                MenuItemImpl item = (MenuItemImpl) items.get(i);
                if (item == expandedItem) {
                    this.mExpandedIndex = i;
                    return;
                }
            }
        }
        this.mExpandedIndex = -1;
    }

    @Override
    public void notifyDataSetChanged() {
        this.findExpandedIndex();
        super.notifyDataSetChanged();
    }
}