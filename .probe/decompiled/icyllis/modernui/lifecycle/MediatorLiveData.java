package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.annotation.UiThread;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MediatorLiveData<T> extends MutableLiveData<T> {

    private final SafeLinkedList<LiveData<?>, MediatorLiveData.Source<?>> mSources = (SafeLinkedList<LiveData<?>, MediatorLiveData.Source<?>>) (new SafeLinkedList<>());

    @UiThread
    public <S> void addSource(@Nonnull LiveData<S> source, @Nonnull Observer<? super S> onChanged) {
        MediatorLiveData.Source<S> e = new MediatorLiveData.Source<>(source, onChanged);
        MediatorLiveData.Source<?> existing = this.mSources.putIfAbsent(e);
        if (existing != null && existing.mObserver != onChanged) {
            throw new IllegalArgumentException("This source was already added with the different observer");
        } else if (existing == null) {
            if (this.hasActiveObservers()) {
                e.plug();
            }
        }
    }

    @UiThread
    public <S> void removeSource(@Nonnull LiveData<S> toRemote) {
        MediatorLiveData.Source<?> source = this.mSources.remove(toRemote);
        if (source != null) {
            source.unplug();
        }
    }

    @CallSuper
    @Override
    protected void onActive() {
        for (MediatorLiveData.Source<?> source : this.mSources) {
            source.plug();
        }
    }

    @CallSuper
    @Override
    protected void onInactive() {
        for (MediatorLiveData.Source<?> source : this.mSources) {
            source.unplug();
        }
    }

    private static class Source<V> implements Observer<V>, Supplier<LiveData<?>> {

        final LiveData<V> mLiveData;

        final Observer<? super V> mObserver;

        int mVersion = -1;

        Source(LiveData<V> liveData, Observer<? super V> observer) {
            this.mLiveData = liveData;
            this.mObserver = observer;
        }

        public LiveData<?> get() {
            return this.mLiveData;
        }

        void plug() {
            this.mLiveData.observeForever(this);
        }

        void unplug() {
            this.mLiveData.removeObserver(this);
        }

        @Override
        public void onChanged(@Nullable V v) {
            if (this.mVersion != this.mLiveData.getVersion()) {
                this.mVersion = this.mLiveData.getVersion();
                this.mObserver.onChanged(v);
            }
        }
    }
}