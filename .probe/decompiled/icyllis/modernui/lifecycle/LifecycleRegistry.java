package icyllis.modernui.lifecycle;

import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Core;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LifecycleRegistry extends Lifecycle {

    private final SafeLinkedHashMap<LifecycleObserver, LifecycleRegistry.ObserverWithState> mObserverMap = new SafeLinkedHashMap<>();

    private Lifecycle.State mState;

    private final WeakReference<LifecycleOwner> mLifecycleOwner;

    private int mAddingObserverCounter = 0;

    private boolean mHandlingEvent = false;

    private boolean mNewEventOccurred = false;

    private final ArrayList<Lifecycle.State> mParentStates = new ArrayList();

    public LifecycleRegistry(@Nonnull LifecycleOwner provider) {
        this.mLifecycleOwner = new WeakReference(provider);
        this.mState = Lifecycle.State.INITIALIZED;
    }

    @UiThread
    public void setCurrentState(@Nonnull Lifecycle.State state) {
        Core.checkUiThread();
        this.moveToState(state);
    }

    public void handleLifecycleEvent(@Nonnull Lifecycle.Event event) {
        Core.checkUiThread();
        this.moveToState(event.getTargetState());
    }

    private void moveToState(Lifecycle.State next) {
        if (this.mState != next) {
            this.mState = next;
            if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
                this.mHandlingEvent = true;
                this.sync();
                this.mHandlingEvent = false;
            } else {
                this.mNewEventOccurred = true;
            }
        }
    }

    private boolean isSynced() {
        if (this.mObserverMap.size() == 0) {
            return true;
        } else {
            Lifecycle.State eldestObserverState = ((LifecycleRegistry.ObserverWithState) this.mObserverMap.head()).mState;
            Lifecycle.State newestObserverState = ((LifecycleRegistry.ObserverWithState) this.mObserverMap.tail()).mState;
            return eldestObserverState == newestObserverState && this.mState == newestObserverState;
        }
    }

    private Lifecycle.State calculateTargetState(LifecycleObserver observer) {
        LifecycleRegistry.ObserverWithState previous = (LifecycleRegistry.ObserverWithState) this.mObserverMap.ceil(observer);
        Lifecycle.State siblingState = previous != null ? previous.mState : null;
        Lifecycle.State parentState = !this.mParentStates.isEmpty() ? (Lifecycle.State) this.mParentStates.get(this.mParentStates.size() - 1) : null;
        return min(min(this.mState, siblingState), parentState);
    }

    @Override
    public void addObserver(@Nonnull LifecycleObserver observer) {
        Core.checkUiThread();
        Lifecycle.State initialState = this.mState == Lifecycle.State.DESTROYED ? Lifecycle.State.DESTROYED : Lifecycle.State.INITIALIZED;
        LifecycleRegistry.ObserverWithState statefulObserver = new LifecycleRegistry.ObserverWithState(observer, initialState);
        LifecycleRegistry.ObserverWithState previous = (LifecycleRegistry.ObserverWithState) this.mObserverMap.putIfAbsent(statefulObserver);
        if (previous == null) {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) this.mLifecycleOwner.get();
            if (lifecycleOwner != null) {
                boolean reentry = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
                Lifecycle.State targetState = this.calculateTargetState(observer);
                this.mAddingObserverCounter++;
                while (statefulObserver.mState.compareTo(targetState) < 0 && this.mObserverMap.contains(observer)) {
                    this.pushParentState(statefulObserver.mState);
                    Lifecycle.Event event = Lifecycle.Event.upFrom(statefulObserver.mState);
                    if (event == null) {
                        throw new IllegalStateException("no event up from " + statefulObserver.mState);
                    }
                    statefulObserver.dispatchEvent(lifecycleOwner, event);
                    this.popParentState();
                    targetState = this.calculateTargetState(observer);
                }
                if (!reentry) {
                    this.sync();
                }
                this.mAddingObserverCounter--;
            }
        }
    }

    private void popParentState() {
        this.mParentStates.remove(this.mParentStates.size() - 1);
    }

    private void pushParentState(Lifecycle.State state) {
        this.mParentStates.add(state);
    }

    @Override
    public void removeObserver(@Nonnull LifecycleObserver observer) {
        Core.checkUiThread();
        this.mObserverMap.remove(observer);
    }

    public int getObserverCount() {
        Core.checkUiThread();
        return this.mObserverMap.size();
    }

    @Nonnull
    @Override
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    private void forwardPass(LifecycleOwner lifecycleOwner) {
        Iterator<LifecycleRegistry.ObserverWithState> ascendingIterator = this.mObserverMap.iteratorWithAdditions();
        while (ascendingIterator.hasNext() && !this.mNewEventOccurred) {
            LifecycleRegistry.ObserverWithState observer = (LifecycleRegistry.ObserverWithState) ascendingIterator.next();
            while (observer.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(observer.mLifecycleObserver)) {
                this.pushParentState(observer.mState);
                Lifecycle.Event event = Lifecycle.Event.upFrom(observer.mState);
                if (event == null) {
                    throw new IllegalStateException("no event up from " + observer.mState);
                }
                observer.dispatchEvent(lifecycleOwner, event);
                this.popParentState();
            }
        }
    }

    private void backwardPass(LifecycleOwner lifecycleOwner) {
        Iterator<LifecycleRegistry.ObserverWithState> descendingIterator = this.mObserverMap.descendingIterator();
        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
            LifecycleRegistry.ObserverWithState observer = (LifecycleRegistry.ObserverWithState) descendingIterator.next();
            while (observer.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(observer.mLifecycleObserver)) {
                Lifecycle.Event event = Lifecycle.Event.downFrom(observer.mState);
                if (event == null) {
                    throw new IllegalStateException("no event down from " + observer.mState);
                }
                this.pushParentState(event.getTargetState());
                observer.dispatchEvent(lifecycleOwner, event);
                this.popParentState();
            }
        }
    }

    private void sync() {
        LifecycleOwner lifecycleOwner = (LifecycleOwner) this.mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
        } else {
            while (!this.isSynced()) {
                this.mNewEventOccurred = false;
                if (this.mState.compareTo(((LifecycleRegistry.ObserverWithState) this.mObserverMap.head()).mState) < 0) {
                    this.backwardPass(lifecycleOwner);
                }
                LifecycleRegistry.ObserverWithState newest = (LifecycleRegistry.ObserverWithState) this.mObserverMap.tail();
                if (!this.mNewEventOccurred && newest != null && this.mState.compareTo(newest.mState) > 0) {
                    this.forwardPass(lifecycleOwner);
                }
            }
            this.mNewEventOccurred = false;
        }
    }

    static Lifecycle.State min(@Nonnull Lifecycle.State state1, @Nullable Lifecycle.State state2) {
        return state2 != null && state2.compareTo(state1) < 0 ? state2 : state1;
    }

    static class ObserverWithState implements Supplier<LifecycleObserver> {

        Lifecycle.State mState;

        LifecycleObserver mLifecycleObserver;

        ObserverWithState(LifecycleObserver observer, Lifecycle.State initialState) {
            this.mLifecycleObserver = observer;
            this.mState = initialState;
        }

        public LifecycleObserver get() {
            return this.mLifecycleObserver;
        }

        void dispatchEvent(LifecycleOwner owner, @Nonnull Lifecycle.Event event) {
            Lifecycle.State newState = event.getTargetState();
            this.mState = LifecycleRegistry.min(this.mState, newState);
            switch(event) {
                case ON_CREATE:
                    this.mLifecycleObserver.onCreate(owner);
                    break;
                case ON_START:
                    this.mLifecycleObserver.onStart(owner);
                    break;
                case ON_RESUME:
                    this.mLifecycleObserver.onResume(owner);
                    break;
                case ON_PAUSE:
                    this.mLifecycleObserver.onPause(owner);
                    break;
                case ON_STOP:
                    this.mLifecycleObserver.onStop(owner);
                    break;
                case ON_DESTROY:
                    this.mLifecycleObserver.onDestroy(owner);
            }
            this.mLifecycleObserver.onStateChanged(owner, event);
            this.mState = newState;
        }
    }
}