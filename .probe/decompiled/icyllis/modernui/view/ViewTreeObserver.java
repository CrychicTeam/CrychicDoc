package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ViewTreeObserver {

    private CopyOnWriteArrayList<ViewTreeObserver.OnGlobalFocusChangeListener> mOnGlobalFocusListeners;

    private ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListeners;

    private ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnPreDrawListener> mOnPreDrawListeners;

    private ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnScrollChangedListener> mOnScrollChangedListeners;

    private boolean mAlive = true;

    ViewTreeObserver() {
    }

    void merge(@NonNull ViewTreeObserver observer) {
        if (observer.mOnGlobalFocusListeners != null) {
            if (this.mOnGlobalFocusListeners != null) {
                this.mOnGlobalFocusListeners.addAll(observer.mOnGlobalFocusListeners);
            } else {
                this.mOnGlobalFocusListeners = observer.mOnGlobalFocusListeners;
            }
        }
        if (observer.mOnGlobalLayoutListeners != null) {
            if (this.mOnGlobalLayoutListeners != null) {
                this.mOnGlobalLayoutListeners.addAll(observer.mOnGlobalLayoutListeners);
            } else {
                this.mOnGlobalLayoutListeners = observer.mOnGlobalLayoutListeners;
            }
        }
        if (observer.mOnPreDrawListeners != null) {
            if (this.mOnPreDrawListeners != null) {
                this.mOnPreDrawListeners.addAll(observer.mOnPreDrawListeners);
            } else {
                this.mOnPreDrawListeners = observer.mOnPreDrawListeners;
            }
        }
        if (observer.mOnScrollChangedListeners != null) {
            if (this.mOnScrollChangedListeners != null) {
                this.mOnScrollChangedListeners.addAll(observer.mOnScrollChangedListeners);
            } else {
                this.mOnScrollChangedListeners = observer.mOnScrollChangedListeners;
            }
        }
        observer.kill();
    }

    public void addOnGlobalFocusChangeListener(@NonNull ViewTreeObserver.OnGlobalFocusChangeListener listener) {
        this.checkIsAlive();
        if (this.mOnGlobalFocusListeners == null) {
            this.mOnGlobalFocusListeners = new CopyOnWriteArrayList();
        }
        this.mOnGlobalFocusListeners.add(listener);
    }

    public void removeOnGlobalFocusChangeListener(@NonNull ViewTreeObserver.OnGlobalFocusChangeListener victim) {
        this.checkIsAlive();
        if (this.mOnGlobalFocusListeners != null) {
            this.mOnGlobalFocusListeners.remove(victim);
        }
    }

    public void addOnGlobalLayoutListener(@NonNull ViewTreeObserver.OnGlobalLayoutListener listener) {
        this.checkIsAlive();
        if (this.mOnGlobalLayoutListeners == null) {
            this.mOnGlobalLayoutListeners = new ViewTreeObserver.CopyOnWriteArray<>();
        }
        this.mOnGlobalLayoutListeners.add(listener);
    }

    public void removeOnGlobalLayoutListener(@NonNull ViewTreeObserver.OnGlobalLayoutListener victim) {
        this.checkIsAlive();
        if (this.mOnGlobalLayoutListeners != null) {
            this.mOnGlobalLayoutListeners.remove(victim);
        }
    }

    public void addOnPreDrawListener(@NonNull ViewTreeObserver.OnPreDrawListener listener) {
        this.checkIsAlive();
        if (this.mOnPreDrawListeners == null) {
            this.mOnPreDrawListeners = new ViewTreeObserver.CopyOnWriteArray<>();
        }
        this.mOnPreDrawListeners.add(listener);
    }

    public void removeOnPreDrawListener(@NonNull ViewTreeObserver.OnPreDrawListener victim) {
        this.checkIsAlive();
        if (this.mOnPreDrawListeners != null) {
            this.mOnPreDrawListeners.remove(victim);
        }
    }

    public void addOnScrollChangedListener(@NonNull ViewTreeObserver.OnScrollChangedListener listener) {
        this.checkIsAlive();
        if (this.mOnScrollChangedListeners == null) {
            this.mOnScrollChangedListeners = new ViewTreeObserver.CopyOnWriteArray<>();
        }
        this.mOnScrollChangedListeners.add(listener);
    }

    public void removeOnScrollChangedListener(@NonNull ViewTreeObserver.OnScrollChangedListener victim) {
        this.checkIsAlive();
        if (this.mOnScrollChangedListeners != null) {
            this.mOnScrollChangedListeners.remove(victim);
        }
    }

    private void checkIsAlive() {
        if (!this.mAlive) {
            throw new IllegalStateException("This ViewTreeObserver is not alive, call getViewTreeObserver() again");
        }
    }

    public boolean isAlive() {
        return this.mAlive;
    }

    private void kill() {
        this.mAlive = false;
    }

    void dispatchOnGlobalFocusChange(@Nullable View oldFocus, @Nullable View newFocus) {
        CopyOnWriteArrayList<ViewTreeObserver.OnGlobalFocusChangeListener> listeners = this.mOnGlobalFocusListeners;
        if (listeners != null && listeners.size() > 0) {
            for (ViewTreeObserver.OnGlobalFocusChangeListener listener : listeners) {
                listener.onGlobalFocusChanged(oldFocus, newFocus);
            }
        }
    }

    public void dispatchOnGlobalLayout() {
        ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnGlobalLayoutListener> listeners = this.mOnGlobalLayoutListeners;
        if (listeners != null && listeners.size() > 0) {
            ViewTreeObserver.CopyOnWriteArray.Access<ViewTreeObserver.OnGlobalLayoutListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onGlobalLayout();
                }
            } finally {
                listeners.end();
            }
        }
    }

    public boolean dispatchOnPreDraw() {
        boolean cancelDraw = false;
        ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnPreDrawListener> listeners = this.mOnPreDrawListeners;
        if (listeners != null && listeners.size() > 0) {
            ViewTreeObserver.CopyOnWriteArray.Access<ViewTreeObserver.OnPreDrawListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    cancelDraw |= !access.get(i).onPreDraw();
                }
            } finally {
                listeners.end();
            }
        }
        return cancelDraw;
    }

    public void dispatchOnScrollChanged() {
        ViewTreeObserver.CopyOnWriteArray<ViewTreeObserver.OnScrollChangedListener> listeners = this.mOnScrollChangedListeners;
        if (listeners != null && listeners.size() > 0) {
            ViewTreeObserver.CopyOnWriteArray.Access<ViewTreeObserver.OnScrollChangedListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onScrollChanged();
                }
            } finally {
                listeners.end();
            }
        }
    }

    static class CopyOnWriteArray<T> {

        private ArrayList<T> mData = new ArrayList();

        private ArrayList<T> mDataCopy;

        private final ViewTreeObserver.CopyOnWriteArray.Access<T> mAccess = new ViewTreeObserver.CopyOnWriteArray.Access<>();

        private boolean mStart;

        private ArrayList<T> getArray() {
            if (this.mStart) {
                if (this.mDataCopy == null) {
                    this.mDataCopy = new ArrayList(this.mData);
                }
                return this.mDataCopy;
            } else {
                return this.mData;
            }
        }

        ViewTreeObserver.CopyOnWriteArray.Access<T> start() {
            if (this.mStart) {
                throw new IllegalStateException("Iteration already started");
            } else {
                this.mStart = true;
                this.mDataCopy = null;
                this.mAccess.mData = this.mData;
                this.mAccess.mSize = this.mData.size();
                return this.mAccess;
            }
        }

        void end() {
            if (!this.mStart) {
                throw new IllegalStateException("Iteration not started");
            } else {
                this.mStart = false;
                if (this.mDataCopy != null) {
                    this.mData = this.mDataCopy;
                    this.mAccess.mData.clear();
                    this.mAccess.mSize = 0;
                }
                this.mDataCopy = null;
            }
        }

        int size() {
            return this.getArray().size();
        }

        void add(T item) {
            this.getArray().add(item);
        }

        void addAll(@NonNull ViewTreeObserver.CopyOnWriteArray<T> array) {
            this.getArray().addAll(array.mData);
        }

        void remove(T item) {
            this.getArray().remove(item);
        }

        void clear() {
            this.getArray().clear();
        }

        static class Access<T> {

            private ArrayList<T> mData;

            private int mSize;

            T get(int index) {
                return (T) this.mData.get(index);
            }

            int size() {
                return this.mSize;
            }
        }
    }

    @FunctionalInterface
    public interface OnGlobalFocusChangeListener {

        void onGlobalFocusChanged(@Nullable View var1, @Nullable View var2);
    }

    @FunctionalInterface
    public interface OnGlobalLayoutListener {

        void onGlobalLayout();
    }

    @FunctionalInterface
    public interface OnPreDrawListener {

        boolean onPreDraw();
    }

    @FunctionalInterface
    public interface OnScrollChangedListener {

        void onScrollChanged();
    }
}