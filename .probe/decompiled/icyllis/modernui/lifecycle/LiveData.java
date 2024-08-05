package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Core;
import java.util.Iterator;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class LiveData<T> {

    final Object mDataLock = new Object();

    static final int START_VERSION = -1;

    static final Object NOT_SET = new Object();

    private final SafeLinkedList<Observer<? super T>, LiveData<T>.ObserverWrapper> mObservers = new SafeLinkedList<>();

    int mActiveCount = 0;

    private volatile Object mData;

    volatile Object mPendingData = NOT_SET;

    private int mVersion;

    private boolean mDispatchingValue;

    private boolean mDispatchInvalidated;

    private final Runnable mPostValueRunnable = () -> {
        Object newValue;
        synchronized (this.mDataLock) {
            newValue = this.mPendingData;
            this.mPendingData = NOT_SET;
        }
        this.setValue((T) newValue);
    };

    public LiveData(T value) {
        this.mData = value;
        this.mVersion = 0;
    }

    public LiveData() {
        this.mData = NOT_SET;
        this.mVersion = -1;
    }

    private void considerNotify(@Nonnull LiveData<T>.ObserverWrapper observer) {
        if (observer.mActive) {
            if (!observer.shouldBeActive()) {
                observer.activeStateChanged(false);
            } else if (observer.mLastVersion < this.mVersion) {
                observer.mLastVersion = this.mVersion;
                observer.mObserver.onChanged((T) this.mData);
            }
        }
    }

    void dispatchingValue(@Nullable LiveData<T>.ObserverWrapper initiator) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
        } else {
            this.mDispatchingValue = true;
            do {
                this.mDispatchInvalidated = false;
                if (initiator != null) {
                    this.considerNotify(initiator);
                    initiator = null;
                } else {
                    Iterator<LiveData<T>.ObserverWrapper> iterator = this.mObservers.iteratorWithAdditions();
                    while (iterator.hasNext()) {
                        this.considerNotify((LiveData.ObserverWrapper) iterator.next());
                        if (this.mDispatchInvalidated) {
                            break;
                        }
                    }
                }
            } while (this.mDispatchInvalidated);
            this.mDispatchingValue = false;
        }
    }

    @UiThread
    public void observe(@Nonnull LifecycleOwner owner, @Nonnull Observer<? super T> observer) {
        Core.checkUiThread();
        if (owner.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {
            LiveData<T>.LifecycleBoundObserver wrapper = new LiveData.LifecycleBoundObserver(owner, observer);
            LiveData<T>.ObserverWrapper existing = (LiveData.ObserverWrapper) this.mObservers.putIfAbsent(wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
            } else if (existing == null) {
                owner.getLifecycle().addObserver(wrapper);
            }
        }
    }

    @UiThread
    public void observeForever(@Nonnull Observer<? super T> observer) {
        Core.checkUiThread();
        LiveData<T>.AlwaysActiveObserver wrapper = new LiveData.AlwaysActiveObserver(observer);
        LiveData<T>.ObserverWrapper existing = (LiveData.ObserverWrapper) this.mObservers.putIfAbsent(wrapper);
        if (existing instanceof LiveData.LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        } else if (existing == null) {
            wrapper.activeStateChanged(true);
        }
    }

    @UiThread
    public void removeObserver(@Nonnull Observer<? super T> observer) {
        Core.checkUiThread();
        LiveData<T>.ObserverWrapper removed = (LiveData.ObserverWrapper) this.mObservers.remove(observer);
        if (removed != null) {
            removed.detachObserver();
            removed.activeStateChanged(false);
        }
    }

    @UiThread
    public void removeObservers(@Nonnull LifecycleOwner owner) {
        Core.checkUiThread();
        for (LiveData<T>.ObserverWrapper entry : this.mObservers) {
            if (entry.isAttachedTo(owner)) {
                this.removeObserver(entry.mObserver);
            }
        }
    }

    protected void postValue(T value) {
        boolean postTask;
        synchronized (this.mDataLock) {
            postTask = this.mPendingData == NOT_SET;
            this.mPendingData = value;
        }
        if (postTask) {
            Core.getUiHandlerAsync().post(this.mPostValueRunnable);
        }
    }

    @UiThread
    protected void setValue(T value) {
        Core.checkUiThread();
        this.mVersion++;
        this.mData = value;
        this.dispatchingValue(null);
    }

    @Nullable
    public T getValue() {
        Object data = this.mData;
        return (T) (data != NOT_SET ? data : null);
    }

    int getVersion() {
        return this.mVersion;
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }

    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }

    private class AlwaysActiveObserver extends LiveData<T>.ObserverWrapper {

        AlwaysActiveObserver(Observer<? super T> observer) {
            super(observer);
        }

        @Override
        boolean shouldBeActive() {
            return true;
        }
    }

    class LifecycleBoundObserver extends LiveData<T>.ObserverWrapper implements LifecycleObserver {

        @Nonnull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@Nonnull LifecycleOwner owner, Observer<? super T> observer) {
            super(observer);
            this.mOwner = owner;
        }

        @Override
        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }

        @Override
        public void onStateChanged(@Nonnull LifecycleOwner source, @Nonnull Lifecycle.Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
            } else {
                this.activeStateChanged(this.shouldBeActive());
            }
        }

        @Override
        boolean isAttachedTo(LifecycleOwner owner) {
            return this.mOwner == owner;
        }

        @Override
        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }
    }

    private abstract class ObserverWrapper implements Supplier<Observer<? super T>> {

        final Observer<? super T> mObserver;

        boolean mActive;

        int mLastVersion = -1;

        ObserverWrapper(Observer<? super T> observer) {
            this.mObserver = observer;
        }

        public final Observer<? super T> get() {
            return this.mObserver;
        }

        abstract boolean shouldBeActive();

        boolean isAttachedTo(LifecycleOwner owner) {
            return false;
        }

        void detachObserver() {
        }

        void activeStateChanged(boolean newActive) {
            if (newActive != this.mActive) {
                this.mActive = newActive;
                boolean wasInactive = LiveData.this.mActiveCount == 0;
                LiveData.this.mActiveCount = LiveData.this.mActiveCount + (this.mActive ? 1 : -1);
                if (wasInactive && this.mActive) {
                    LiveData.this.onActive();
                }
                if (LiveData.this.mActiveCount == 0 && !this.mActive) {
                    LiveData.this.onInactive();
                }
                if (this.mActive) {
                    LiveData.this.dispatchingValue(this);
                }
            }
        }
    }
}