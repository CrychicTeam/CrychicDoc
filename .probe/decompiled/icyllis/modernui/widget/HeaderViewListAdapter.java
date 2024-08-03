package icyllis.modernui.widget;

import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeaderViewListAdapter implements WrapperListAdapter, Filterable {

    private final ListAdapter mAdapter;

    ArrayList<ListView.FixedViewInfo> mHeaderViewInfos;

    ArrayList<ListView.FixedViewInfo> mFooterViewInfos;

    static final ArrayList<ListView.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList();

    boolean mAreAllFixedViewsSelectable;

    private final boolean mIsFilterable;

    public HeaderViewListAdapter(@Nullable ArrayList<ListView.FixedViewInfo> headerViewInfos, @Nullable ArrayList<ListView.FixedViewInfo> footerViewInfos, @Nonnull ListAdapter adapter) {
        this.mAdapter = adapter;
        this.mIsFilterable = adapter instanceof Filterable;
        this.mHeaderViewInfos = (ArrayList<ListView.FixedViewInfo>) Objects.requireNonNullElse(headerViewInfos, EMPTY_INFO_LIST);
        this.mFooterViewInfos = (ArrayList<ListView.FixedViewInfo>) Objects.requireNonNullElse(footerViewInfos, EMPTY_INFO_LIST);
        this.mAreAllFixedViewsSelectable = this.areAllListInfosSelectable(this.mHeaderViewInfos) && this.areAllListInfosSelectable(this.mFooterViewInfos);
    }

    public int getHeadersCount() {
        return this.mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return this.mFooterViewInfos.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mAdapter == null || this.mAdapter.isEmpty();
    }

    private boolean areAllListInfosSelectable(ArrayList<ListView.FixedViewInfo> infos) {
        if (infos != null) {
            for (ListView.FixedViewInfo info : infos) {
                if (!info.isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean removeHeader(@Nonnull View v) {
        for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
            ListView.FixedViewInfo info = (ListView.FixedViewInfo) this.mHeaderViewInfos.get(i);
            if (info.view == v) {
                this.mHeaderViewInfos.remove(i);
                this.mAreAllFixedViewsSelectable = this.areAllListInfosSelectable(this.mHeaderViewInfos) && this.areAllListInfosSelectable(this.mFooterViewInfos);
                return true;
            }
        }
        return false;
    }

    public boolean removeFooter(@Nonnull View v) {
        for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
            ListView.FixedViewInfo info = (ListView.FixedViewInfo) this.mFooterViewInfos.get(i);
            if (info.view == v) {
                this.mFooterViewInfos.remove(i);
                this.mAreAllFixedViewsSelectable = this.areAllListInfosSelectable(this.mHeaderViewInfos) && this.areAllListInfosSelectable(this.mFooterViewInfos);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return this.mAdapter != null ? this.getFootersCount() + this.getHeadersCount() + this.mAdapter.getCount() : this.getFootersCount() + this.getHeadersCount();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return this.mAdapter == null ? true : this.mAreAllFixedViewsSelectable && this.mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        int numHeaders = this.getHeadersCount();
        if (position < numHeaders) {
            return ((ListView.FixedViewInfo) this.mHeaderViewInfos.get(position)).isSelectable;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = this.mAdapter.getCount();
                if (adjPosition < adapterCount) {
                    return this.mAdapter.isEnabled(adjPosition);
                }
            }
            return ((ListView.FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).isSelectable;
        }
    }

    @Override
    public Object getItem(int position) {
        int numHeaders = this.getHeadersCount();
        if (position < numHeaders) {
            return ((ListView.FixedViewInfo) this.mHeaderViewInfos.get(position)).data;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = this.mAdapter.getCount();
                if (adjPosition < adapterCount) {
                    return this.mAdapter.getItem(adjPosition);
                }
            }
            return ((ListView.FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).data;
        }
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = this.getHeadersCount();
        if (this.mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = this.mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return this.mAdapter.getItemId(adjPosition);
            }
        }
        return -1L;
    }

    @Override
    public boolean hasStableIds() {
        return this.mAdapter != null ? this.mAdapter.hasStableIds() : false;
    }

    @Nonnull
    @Override
    public View getView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
        int numHeaders = this.getHeadersCount();
        if (position < numHeaders) {
            return ((ListView.FixedViewInfo) this.mHeaderViewInfos.get(position)).view;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = this.mAdapter.getCount();
                if (adjPosition < adapterCount) {
                    return this.mAdapter.getView(adjPosition, convertView, parent);
                }
            }
            return ((ListView.FixedViewInfo) this.mFooterViewInfos.get(adjPosition - adapterCount)).view;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = this.getHeadersCount();
        if (this.mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = this.mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return this.mAdapter.getItemViewType(adjPosition);
            }
        }
        return -2;
    }

    @Override
    public int getViewTypeCount() {
        return this.mAdapter != null ? this.mAdapter.getViewTypeCount() : 1;
    }

    @Override
    public void registerDataSetObserver(@Nonnull DataSetObserver observer) {
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(@Nonnull DataSetObserver observer) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public Filter getFilter() {
        return this.mIsFilterable ? ((Filterable) this.mAdapter).getFilter() : null;
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return this.mAdapter;
    }
}