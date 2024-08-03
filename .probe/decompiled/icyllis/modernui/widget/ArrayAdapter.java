package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrayAdapter<T> extends BaseAdapter implements Filterable {

    private final Object mLock = new Object();

    private final Context mContext;

    private List<T> mObjects;

    private boolean mNotifyOnChange = true;

    private ArrayList<T> mOriginalValues;

    private ArrayAdapter<T>.ArrayFilter mFilter;

    public ArrayAdapter(Context context, @Nonnull T[] objects) {
        this(context, Arrays.asList(objects));
    }

    public ArrayAdapter(Context context, @Nonnull List<T> objects) {
        this.mContext = context;
        this.mObjects = objects;
    }

    public void add(@Nullable T object) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.add(object);
            } else {
                this.mObjects.add(object);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void addAll(@Nonnull Collection<? extends T> collection) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.addAll(collection);
            } else {
                this.mObjects.addAll(collection);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void addAll(@Nonnull T[] items) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                Collections.addAll(this.mOriginalValues, items);
            } else {
                Collections.addAll(this.mObjects, items);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void insert(@Nullable T object, int index) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.add(index, object);
            } else {
                this.mObjects.add(index, object);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void remove(@Nullable T object) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.remove(object);
            } else {
                this.mObjects.remove(object);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.clear();
            } else {
                this.mObjects.clear();
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void sort(@Nonnull Comparator<? super T> comparator) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.sort(comparator);
            } else {
                this.mObjects.sort(comparator);
            }
        }
        if (this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.mNotifyOnChange = notifyOnChange;
    }

    @Override
    public int getCount() {
        return this.mObjects.size();
    }

    @Override
    public T getItem(int position) {
        return (T) this.mObjects.get(position);
    }

    public int getPosition(@Nullable T item) {
        return this.mObjects.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Nonnull
    @Override
    public View getView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
        return this.createViewInner(position, convertView);
    }

    @Nonnull
    private View createViewInner(int position, @Nullable View convertView) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(this.mContext);
        } else {
            tv = (TextView) convertView;
        }
        T item = this.getItem(position);
        if (item instanceof CharSequence) {
            tv.setText((CharSequence) item);
        } else {
            tv.setText(String.valueOf(item));
        }
        tv.setTextSize(14.0F);
        tv.setTextAlignment(5);
        int dp4 = tv.dp(4.0F);
        tv.setPadding(dp4, dp4, dp4, dp4);
        return tv;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
        return this.createViewInner(position, convertView);
    }

    @Nonnull
    @Override
    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayAdapter.ArrayFilter();
        }
        return this.mFilter;
    }

    private class ArrayFilter extends Filter {

        @Nonnull
        @Override
        protected Filter.FilterResults performFiltering(CharSequence prefix) {
            Filter.FilterResults results = new Filter.FilterResults();
            if (ArrayAdapter.this.mOriginalValues == null) {
                synchronized (ArrayAdapter.this.mLock) {
                    ArrayAdapter.this.mOriginalValues = new ArrayList(ArrayAdapter.this.mObjects);
                }
            }
            if (prefix != null && prefix.length() != 0) {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<T> values;
                synchronized (ArrayAdapter.this.mLock) {
                    values = new ArrayList(ArrayAdapter.this.mOriginalValues);
                }
                ArrayList<T> newValues = new ArrayList();
                for (T value : values) {
                    String valueText = value.toString().toLowerCase();
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            } else {
                ArrayList<T> list;
                synchronized (ArrayAdapter.this.mLock) {
                    list = new ArrayList(ArrayAdapter.this.mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @Nonnull Filter.FilterResults results) {
            ArrayAdapter.this.mObjects = (List<T>) results.values;
            if (results.count > 0) {
                ArrayAdapter.this.notifyDataSetChanged();
            } else {
                ArrayAdapter.this.notifyDataSetInvalidated();
            }
        }
    }
}