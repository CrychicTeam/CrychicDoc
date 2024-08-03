package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;

public interface ExpandableListAdapter {

    void registerDataSetObserver(DataSetObserver var1);

    void unregisterDataSetObserver(DataSetObserver var1);

    int getGroupCount();

    int getChildrenCount(int var1);

    Object getGroup(int var1);

    Object getChild(int var1, int var2);

    long getGroupId(int var1);

    long getChildId(int var1, int var2);

    boolean hasStableIds();

    View getGroupView(int var1, boolean var2, @Nullable View var3, @NonNull ViewGroup var4);

    View getChildView(int var1, int var2, boolean var3, @Nullable View var4, @NonNull ViewGroup var5);

    boolean isChildSelectable(int var1, int var2);

    boolean areAllItemsEnabled();

    boolean isEmpty();

    void onGroupExpanded(int var1);

    void onGroupCollapsed(int var1);

    long getCombinedChildId(long var1, long var3);

    long getCombinedGroupId(long var1);
}