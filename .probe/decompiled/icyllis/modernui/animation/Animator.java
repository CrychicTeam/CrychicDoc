package icyllis.modernui.animation;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Animator implements Cloneable {

    public static final Marker MARKER = MarkerManager.getMarker("Animator");

    public static final long DURATION_INFINITE = -1L;

    boolean mPaused = false;

    @Nullable
    CopyOnWriteArrayList<AnimatorListener> mListeners;

    public abstract void start();

    public abstract void cancel();

    public abstract void end();

    public void pause() {
        if (this.isStarted() && !this.mPaused) {
            this.mPaused = true;
            if (this.mListeners != null) {
                for (AnimatorListener l : this.mListeners) {
                    l.onAnimationPause(this);
                }
            }
        }
    }

    public void resume() {
        if (this.mPaused) {
            this.mPaused = false;
            if (this.mListeners != null) {
                for (AnimatorListener l : this.mListeners) {
                    l.onAnimationResume(this);
                }
            }
        }
    }

    public final boolean isPaused() {
        return this.mPaused;
    }

    public abstract void setStartDelay(long var1);

    public abstract long getStartDelay();

    public abstract Animator setDuration(long var1);

    public abstract long getDuration();

    public abstract long getTotalDuration();

    public abstract void setInterpolator(TimeInterpolator var1);

    public abstract TimeInterpolator getInterpolator();

    public abstract boolean isRunning();

    public abstract boolean isStarted();

    public final void addListener(@NonNull AnimatorListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new CopyOnWriteArrayList();
        }
        this.mListeners.addIfAbsent(listener);
    }

    public final void removeListener(@NonNull AnimatorListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
            if (this.mListeners.isEmpty()) {
                this.mListeners = null;
            }
        }
    }

    public final void removeAllListeners() {
        if (this.mListeners != null) {
            this.mListeners.clear();
            this.mListeners = null;
        }
    }

    @Nullable
    public final List<AnimatorListener> getListeners() {
        return this.mListeners;
    }

    public Animator clone() {
        try {
            Animator anim = (Animator) super.clone();
            if (this.mListeners != null) {
                anim.mListeners = new CopyOnWriteArrayList(this.mListeners);
            }
            return anim;
        } catch (CloneNotSupportedException var2) {
            throw new AssertionError();
        }
    }

    public void setupStartValues() {
    }

    public void setupEndValues() {
    }

    public void setTarget(@Nullable Object target) {
    }

    @Internal
    public boolean canReverse() {
        return false;
    }

    @Internal
    public void reverse() {
        throw new IllegalStateException("Reverse is not supported");
    }

    boolean pulseAnimationFrame(long frameTime) {
        return false;
    }

    void startWithoutPulsing(boolean inReverse) {
        if (inReverse) {
            this.reverse();
        } else {
            this.start();
        }
    }

    void skipToEndValue(boolean inReverse) {
    }

    boolean isInitialized() {
        return true;
    }

    void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
    }
}