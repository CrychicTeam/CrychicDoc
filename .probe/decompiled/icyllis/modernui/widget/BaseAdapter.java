package icyllis.modernui.widget;

import icyllis.modernui.util.DataSetObservable;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseAdapter implements ListAdapter, SpinnerAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void registerDataSetObserver(@Nonnull DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(@Nonnull DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        this.mDataSetObservable.notifyInvalidated();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Nullable
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getCount() == 0;
    }
}