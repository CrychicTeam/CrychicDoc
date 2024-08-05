package icyllis.modernui.animation;

import icyllis.modernui.core.Core;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class AnimationUtils {

    private static final ThreadLocal<AnimationUtils.AnimationState> sAnimationState = ThreadLocal.withInitial(AnimationUtils.AnimationState::new);

    @Internal
    public static void lockAnimationClock(long vsyncMillis) {
        AnimationUtils.AnimationState state = (AnimationUtils.AnimationState) sAnimationState.get();
        state.animationClockLocked = true;
        state.currentVsyncTimeMillis = vsyncMillis;
    }

    @Internal
    public static void unlockAnimationClock() {
        ((AnimationUtils.AnimationState) sAnimationState.get()).animationClockLocked = false;
    }

    public static long currentAnimationTimeMillis() {
        AnimationUtils.AnimationState state = (AnimationUtils.AnimationState) sAnimationState.get();
        if (state.animationClockLocked) {
            return Math.max(state.currentVsyncTimeMillis, state.lastReportedTimeMillis);
        } else {
            state.lastReportedTimeMillis = Core.timeMillis();
            return state.lastReportedTimeMillis;
        }
    }

    private AnimationUtils() {
    }

    private static class AnimationState {

        boolean animationClockLocked;

        long currentVsyncTimeMillis;

        long lastReportedTimeMillis;
    }
}