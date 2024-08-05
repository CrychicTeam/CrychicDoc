package icyllis.modernui.animation;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Choreographer;
import icyllis.modernui.core.Core;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.ArrayList;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class AnimationHandler {

    private static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal();

    private final ArrayList<AnimationHandler.FrameCallback> mAnimationCallbacks = new ArrayList();

    private final Object2LongOpenHashMap<AnimationHandler.FrameCallback> mDelayedStartTime = new Object2LongOpenHashMap();

    private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {

        @Override
        public void doFrame(@NonNull Choreographer choreographer, long frameTimeNanos) {
            AnimationHandler.this.doAnimationFrame(frameTimeNanos / 1000000L);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                choreographer.postFrameCallback(this);
            }
        }
    };

    private boolean mListDirty = false;

    private AnimationHandler() {
    }

    @NonNull
    public static AnimationHandler getInstance() {
        if (sAnimatorHandler.get() == null) {
            sAnimatorHandler.set(new AnimationHandler());
        }
        return (AnimationHandler) sAnimatorHandler.get();
    }

    public static int getAnimationCount() {
        AnimationHandler handler = (AnimationHandler) sAnimatorHandler.get();
        return handler == null ? 0 : handler.getCallbackSize();
    }

    public void addFrameCallback(@NonNull AnimationHandler.FrameCallback callback, long delay) {
        if (this.mAnimationCallbacks.isEmpty()) {
            Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
        }
        boolean newlyAdded;
        if (!this.mAnimationCallbacks.contains(callback)) {
            this.mAnimationCallbacks.add(callback);
            newlyAdded = true;
        } else {
            newlyAdded = false;
        }
        if (delay > 0L) {
            this.mDelayedStartTime.put(callback, Core.timeMillis() + delay);
        } else if (!newlyAdded) {
            this.mDelayedStartTime.removeLong(callback);
        }
    }

    public void removeCallback(@NonNull AnimationHandler.FrameCallback callback) {
        int id = this.mAnimationCallbacks.indexOf(callback);
        if (id >= 0) {
            this.mAnimationCallbacks.set(id, null);
            this.mDelayedStartTime.removeLong(callback);
            this.mListDirty = true;
        }
    }

    private void doAnimationFrame(long frameTime) {
        long currentTime = Core.timeMillis();
        int size = this.mAnimationCallbacks.size();
        for (int i = 0; i < size; i++) {
            AnimationHandler.FrameCallback callback = (AnimationHandler.FrameCallback) this.mAnimationCallbacks.get(i);
            if (callback != null && this.isCallbackDue(callback, currentTime)) {
                callback.doAnimationFrame(frameTime);
            }
        }
        this.cleanUpList();
    }

    private boolean isCallbackDue(@NonNull AnimationHandler.FrameCallback callback, long currentTime) {
        long startTime = this.mDelayedStartTime.getLong(callback);
        if (startTime == 0L) {
            return true;
        } else if (currentTime >= startTime) {
            this.mDelayedStartTime.removeLong(callback);
            return true;
        } else {
            return false;
        }
    }

    void autoCancelBasedOn(@NonNull ObjectAnimator animator) {
        for (int i = this.mAnimationCallbacks.size() - 1; i >= 0; i--) {
            AnimationHandler.FrameCallback cb = (AnimationHandler.FrameCallback) this.mAnimationCallbacks.get(i);
            if (cb != null && animator.shouldAutoCancel(cb)) {
                ((Animator) cb).cancel();
            }
        }
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int i = this.mAnimationCallbacks.size() - 1; i >= 0; i--) {
                if (this.mAnimationCallbacks.get(i) == null) {
                    this.mAnimationCallbacks.remove(i);
                }
            }
            this.mListDirty = false;
        }
    }

    private int getCallbackSize() {
        int count = 0;
        for (int i = this.mAnimationCallbacks.size() - 1; i >= 0; i--) {
            if (this.mAnimationCallbacks.get(i) != null) {
                count++;
            }
        }
        return count;
    }

    public interface FrameCallback {

        boolean doAnimationFrame(long var1);
    }
}