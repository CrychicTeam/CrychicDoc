package aurelienribon.tweenengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TweenManager {

    private final ArrayList<BaseTween<?>> objects = new ArrayList(20);

    private boolean isPaused = false;

    public static void setAutoRemove(BaseTween<?> object, boolean value) {
        object.isAutoRemoveEnabled = value;
    }

    public static void setAutoStart(BaseTween<?> object, boolean value) {
        object.isAutoStartEnabled = value;
    }

    public TweenManager add(BaseTween<?> object) {
        if (!this.objects.contains(object)) {
            this.objects.add(object);
        }
        if (object.isAutoStartEnabled) {
            object.start();
        }
        return this;
    }

    public boolean containsTarget(Object target) {
        for (BaseTween<?> obj : this.objects) {
            if (obj.containsTarget(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTarget(Object target, int tweenType) {
        for (BaseTween<?> obj : this.objects) {
            if (obj.containsTarget(target, tweenType)) {
                return true;
            }
        }
        return false;
    }

    public void killAll() {
        for (BaseTween<?> obj : this.objects) {
            obj.kill();
        }
    }

    public void killTarget(Object target) {
        for (BaseTween<?> obj : this.objects) {
            obj.killTarget(target);
        }
    }

    public void killTarget(Object target, int tweenType) {
        for (BaseTween<?> obj : this.objects) {
            obj.killTarget(target, tweenType);
        }
    }

    public void ensureCapacity(int minCapacity) {
        this.objects.ensureCapacity(minCapacity);
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    public void update(float delta) {
        for (int i = this.objects.size() - 1; i >= 0; i--) {
            BaseTween<?> obj = (BaseTween<?>) this.objects.get(i);
            if (obj.isFinished() && obj.isAutoRemoveEnabled) {
                this.objects.remove(i);
                obj.free();
            }
        }
        if (!this.isPaused) {
            if (delta >= 0.0F) {
                for (BaseTween<?> object : this.objects) {
                    object.update(delta);
                }
            } else {
                for (int ix = this.objects.size() - 1; ix >= 0; ix--) {
                    ((BaseTween) this.objects.get(ix)).update(delta);
                }
            }
        }
    }

    public int size() {
        return this.objects.size();
    }

    public int getRunningTweensCount() {
        return getTweensCount(this.objects);
    }

    public int getRunningTimelinesCount() {
        return getTimelinesCount(this.objects);
    }

    public List<BaseTween<?>> getObjects() {
        return Collections.unmodifiableList(this.objects);
    }

    private static int getTweensCount(List<BaseTween<?>> objects) {
        int cnt = 0;
        for (BaseTween<?> obj : objects) {
            if (obj instanceof Tween) {
                cnt++;
            } else {
                cnt += getTweensCount(((Timeline) obj).getChildren());
            }
        }
        return cnt;
    }

    private static int getTimelinesCount(List<BaseTween<?>> objects) {
        int cnt = 0;
        for (BaseTween<?> obj : objects) {
            if (obj instanceof Timeline) {
                cnt += 1 + getTimelinesCount(((Timeline) obj).getChildren());
            }
        }
        return cnt;
    }
}