package icyllis.modernui.fragment;

import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.View;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class FragmentLifecycleCallbacksDispatcher {

    @Nonnull
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList();

    @Nonnull
    private final FragmentManager mFragmentManager;

    FragmentLifecycleCallbacksDispatcher(@Nonnull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public void registerFragmentLifecycleCallbacks(@Nonnull FragmentLifecycleCallbacks cb, boolean recursive) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder(cb, recursive));
    }

    public void unregisterFragmentLifecycleCallbacks(@Nonnull FragmentLifecycleCallbacks cb) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            for (int count = this.mLifecycleCallbacks.size(); i < count; i++) {
                if (((FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) this.mLifecycleCallbacks.get(i)).mCallback == cb) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                }
            }
        }
    }

    void dispatchOnFragmentPreAttached(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentPreAttached(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPreAttached(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentAttached(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentAttached(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentAttached(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentPreCreated(@Nonnull Fragment f, @Nullable DataSet savedInstanceState, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentPreCreated(f, savedInstanceState, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPreCreated(this.mFragmentManager, f, savedInstanceState);
            }
        }
    }

    void dispatchOnFragmentCreated(@Nonnull Fragment f, @Nullable DataSet savedInstanceState, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentCreated(f, savedInstanceState, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentCreated(this.mFragmentManager, f, savedInstanceState);
            }
        }
    }

    void dispatchOnFragmentViewCreated(@Nonnull Fragment f, @Nonnull View v, @Nullable DataSet savedInstanceState, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentViewCreated(this.mFragmentManager, f, v, savedInstanceState);
            }
        }
    }

    void dispatchOnFragmentStarted(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentStarted(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentStarted(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentResumed(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentResumed(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentResumed(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentPaused(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentPaused(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentPaused(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentStopped(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentStopped(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentStopped(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentSaveInstanceState(@Nonnull Fragment f, @Nonnull DataSet outState, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentSaveInstanceState(f, outState, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentSaveInstanceState(this.mFragmentManager, f, outState);
            }
        }
    }

    void dispatchOnFragmentViewDestroyed(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentViewDestroyed(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentViewDestroyed(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentDestroyed(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentDestroyed(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentDestroyed(this.mFragmentManager, f);
            }
        }
    }

    void dispatchOnFragmentDetached(@Nonnull Fragment f, boolean onlyRecursive) {
        Fragment parent = this.mFragmentManager.getParent();
        if (parent != null) {
            FragmentManager parentManager = parent.getParentFragmentManager();
            parentManager.getLifecycleCallbacksDispatcher().dispatchOnFragmentDetached(f, true);
        }
        for (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder holder : this.mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentDetached(this.mFragmentManager, f);
            }
        }
    }

    private static final class FragmentLifecycleCallbacksHolder {

        @Nonnull
        final FragmentLifecycleCallbacks mCallback;

        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(@Nonnull FragmentLifecycleCallbacks callback, boolean recursive) {
            this.mCallback = callback;
            this.mRecursive = recursive;
        }
    }
}