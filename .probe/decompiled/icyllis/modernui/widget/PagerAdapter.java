package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.util.DataSetObservable;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.util.Parcelable;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;

public abstract class PagerAdapter {

    private final DataSetObservable mObservable = new DataSetObservable();

    private DataSetObserver mViewPagerObserver;

    public static final int POSITION_UNCHANGED = -1;

    public static final int POSITION_NONE = -2;

    public abstract int getCount();

    public void startUpdate(@NonNull ViewGroup container) {
    }

    @NonNull
    public abstract Object instantiateItem(@NonNull ViewGroup var1, int var2);

    public abstract void destroyItem(@NonNull ViewGroup var1, int var2, @NonNull Object var3);

    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    public void finishUpdate(@NonNull ViewGroup container) {
    }

    public abstract boolean isViewFromObject(@NonNull View var1, @NonNull Object var2);

    @Nullable
    public Parcelable saveState() {
        return null;
    }

    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    public int getItemPosition(@NonNull Object object) {
        return -1;
    }

    public void notifyDataSetChanged() {
        synchronized (this) {
            if (this.mViewPagerObserver != null) {
                this.mViewPagerObserver.onChanged();
            }
        }
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }

    void setViewPagerObserver(DataSetObserver observer) {
        synchronized (this) {
            this.mViewPagerObserver = observer;
        }
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public float getPageWidth(int position) {
        return 1.0F;
    }
}