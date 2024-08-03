package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.UiThread;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Transformations {

    private Transformations() {
    }

    @Nonnull
    @UiThread
    public static <X, Y> LiveData<Y> map(@Nonnull LiveData<X> source, @Nonnull Function<? super X, ? extends Y> mapFunction) {
        MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, x -> result.setValue((Y) mapFunction.apply(x)));
        return result;
    }

    @Nonnull
    @UiThread
    public static <X, Y> LiveData<Y> switchMap(@Nonnull LiveData<X> source, @Nonnull Function<? super X, ? extends LiveData<? extends Y>> switchMapFunction) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {

            private LiveData<? extends Y> mSource;

            @Override
            public void onChanged(@Nullable X x) {
                LiveData<? extends Y> newLiveData = (LiveData<? extends Y>) switchMapFunction.apply(x);
                if (this.mSource != newLiveData) {
                    if (this.mSource != null) {
                        result.removeSource(this.mSource);
                    }
                    this.mSource = newLiveData;
                    if (this.mSource != null) {
                        result.addSource(this.mSource, result::setValue);
                    }
                }
            }
        });
        return result;
    }

    @Nonnull
    @UiThread
    public static <X> LiveData<X> distinctUntilChanged(@Nonnull LiveData<X> source) {
        final MediatorLiveData<X> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {

            private boolean mFirstChanged;

            @Override
            public void onChanged(X currentValue) {
                X previousValue = result.getValue();
                if (!this.mFirstChanged || !Objects.equals(previousValue, currentValue)) {
                    this.mFirstChanged = true;
                    result.setValue(currentValue);
                }
            }
        });
        return result;
    }
}