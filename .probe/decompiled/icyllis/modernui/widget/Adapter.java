package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nullable;

public interface Adapter {

    int IGNORE_ITEM_VIEW_TYPE = -1;

    int NO_SELECTION = Integer.MIN_VALUE;

    void registerDataSetObserver(@NonNull DataSetObserver var1);

    void unregisterDataSetObserver(@NonNull DataSetObserver var1);

    int getCount();

    Object getItem(int var1);

    long getItemId(int var1);

    boolean hasStableIds();

    View getView(int var1, @Nullable View var2, @NonNull ViewGroup var3);

    int getItemViewType(int var1);

    int getViewTypeCount();

    boolean isEmpty();
}