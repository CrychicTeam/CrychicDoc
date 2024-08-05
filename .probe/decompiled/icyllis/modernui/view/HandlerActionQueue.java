package icyllis.modernui.view;

import icyllis.modernui.core.Handler;
import icyllis.modernui.util.GrowingArrayUtils;

public class HandlerActionQueue {

    private HandlerActionQueue.HandlerAction[] mActions;

    private int mCount;

    public void post(Runnable action) {
        this.postDelayed(action, 0L);
    }

    public void postDelayed(Runnable action, long delayMillis) {
        HandlerActionQueue.HandlerAction handlerAction = new HandlerActionQueue.HandlerAction(action, delayMillis);
        synchronized (this) {
            if (this.mActions == null) {
                this.mActions = new HandlerActionQueue.HandlerAction[4];
            }
            this.mActions = GrowingArrayUtils.append(this.mActions, this.mCount, handlerAction);
            this.mCount++;
        }
    }

    public void removeCallbacks(Runnable action) {
        synchronized (this) {
            int count = this.mCount;
            int j = 0;
            HandlerActionQueue.HandlerAction[] actions = this.mActions;
            for (int i = 0; i < count; i++) {
                if (!actions[i].matches(action)) {
                    if (j != i) {
                        actions[j] = actions[i];
                    }
                    j++;
                }
            }
            for (this.mCount = j; j < count; j++) {
                actions[j] = null;
            }
        }
    }

    public void executeActions(Handler handler) {
        synchronized (this) {
            HandlerActionQueue.HandlerAction[] actions = this.mActions;
            int i = 0;
            for (int count = this.mCount; i < count; i++) {
                HandlerActionQueue.HandlerAction handlerAction = actions[i];
                handler.postDelayed(handlerAction.action, handlerAction.delay);
            }
            this.mActions = null;
            this.mCount = 0;
        }
    }

    public int size() {
        return this.mCount;
    }

    public Runnable getRunnable(int index) {
        if (index >= this.mCount) {
            throw new IndexOutOfBoundsException();
        } else {
            return this.mActions[index].action;
        }
    }

    public long getDelay(int index) {
        if (index >= this.mCount) {
            throw new IndexOutOfBoundsException();
        } else {
            return this.mActions[index].delay;
        }
    }

    private static record HandlerAction(Runnable action, long delay) {

        public boolean matches(Runnable otherAction) {
            return otherAction == null && this.action == null || this.action != null && this.action.equals(otherAction);
        }
    }
}