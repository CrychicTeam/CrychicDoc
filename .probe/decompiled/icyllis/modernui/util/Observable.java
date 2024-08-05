package icyllis.modernui.util;

import java.util.ArrayList;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class Observable<T> {

    protected final ArrayList<T> mObservers = new ArrayList();

    public void registerObserver(T observer) {
        synchronized (this.mObservers) {
            if (this.mObservers.contains(observer)) {
                throw new IllegalStateException("Observer " + observer + " is already registered.");
            } else {
                this.mObservers.add(observer);
            }
        }
    }

    public void unregisterObserver(T observer) {
        synchronized (this.mObservers) {
            if (!this.mObservers.remove(observer)) {
                throw new IllegalStateException("Observer " + observer + " was not registered.");
            }
        }
    }

    public void unregisterAll() {
        synchronized (this.mObservers) {
            this.mObservers.clear();
        }
    }
}