package icyllis.modernui.widget;

import icyllis.modernui.util.DataSetObservable;
import icyllis.modernui.util.DataSetObserver;

public abstract class BaseExpandableListAdapter implements ExpandableListAdapter, HeterogeneousExpandableList {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetInvalidated() {
        this.mDataSetObservable.notifyInvalidated();
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return Long.MIN_VALUE | (groupId & 2147483647L) << 32 | childId & 4294967295L;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return (groupId & 2147483647L) << 32;
    }

    @Override
    public boolean isEmpty() {
        return this.getGroupCount() == 0;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildTypeCount() {
        return 1;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return 0;
    }

    @Override
    public int getGroupTypeCount() {
        return 1;
    }
}