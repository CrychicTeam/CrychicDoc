package icyllis.modernui.animation;

import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Looper;
import icyllis.modernui.util.ArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class AnimatorSet extends Animator implements AnimationHandler.FrameCallback {

    private ArrayList<AnimatorSet.Node> mPlayingSet = new ArrayList();

    private ArrayMap<Animator, AnimatorSet.Node> mNodeMap = new ArrayMap<>();

    private ArrayList<AnimatorSet.AnimationEvent> mEvents = new ArrayList();

    private ArrayList<AnimatorSet.Node> mNodes = new ArrayList();

    private boolean mDependencyDirty = false;

    private boolean mStarted = false;

    private long mStartDelay = 0L;

    private ValueAnimator mDelayAnim = ValueAnimator.ofFloat(0.0F, 1.0F).setDuration(0L);

    private AnimatorSet.Node mRootNode = new AnimatorSet.Node(this.mDelayAnim);

    private long mDuration = -1L;

    private TimeInterpolator mInterpolator = null;

    private long mTotalDuration = 0L;

    private final boolean mShouldIgnoreEndWithoutStart;

    private final boolean mShouldResetValuesAtStart;

    private final boolean mEndCanBeCalled;

    private long mLastFrameTime = -1L;

    private long mFirstFrame = -1L;

    private int mLastEventId = -1;

    private boolean mReversing = false;

    private boolean mSelfPulse = true;

    private AnimatorSet.SeekState mSeekState = new AnimatorSet.SeekState();

    private boolean mChildrenInitialized = false;

    private long mPauseTime = -1L;

    private AnimatorListener mAnimationEndListener = new AnimatorListener() {

        @Override
        public void onAnimationEnd(@Nonnull Animator animation) {
            if (AnimatorSet.this.mNodeMap.get(animation) == null) {
                throw new RuntimeException("Error: animation ended is not in the node map");
            } else {
                AnimatorSet.this.mNodeMap.get(animation).mEnded = true;
            }
        }
    };

    public AnimatorSet() {
        this.mNodeMap.put(this.mDelayAnim, this.mRootNode);
        this.mNodes.add(this.mRootNode);
        this.mShouldIgnoreEndWithoutStart = false;
        this.mShouldResetValuesAtStart = true;
        this.mEndCanBeCalled = true;
    }

    public void playTogether(Animator... items) {
        if (items != null) {
            AnimatorSet.Builder builder = this.play(items[0]);
            for (int i = 1; i < items.length; i++) {
                builder.with(items[i]);
            }
        }
    }

    public void playTogether(Collection<Animator> items) {
        if (items != null && items.size() > 0) {
            AnimatorSet.Builder builder = null;
            for (Animator anim : items) {
                if (builder == null) {
                    builder = this.play(anim);
                } else {
                    builder.with(anim);
                }
            }
        }
    }

    public void playSequentially(Animator... items) {
        if (items != null) {
            if (items.length == 1) {
                this.play(items[0]);
            } else {
                for (int i = 0; i < items.length - 1; i++) {
                    this.play(items[i]).before(items[i + 1]);
                }
            }
        }
    }

    public void playSequentially(List<Animator> items) {
        if (items != null && items.size() > 0) {
            if (items.size() == 1) {
                this.play((Animator) items.get(0));
            } else {
                for (int i = 0; i < items.size() - 1; i++) {
                    this.play((Animator) items.get(i)).before((Animator) items.get(i + 1));
                }
            }
        }
    }

    @Nonnull
    public ArrayList<Animator> getChildAnimations() {
        ArrayList<Animator> childList = new ArrayList();
        for (AnimatorSet.Node node : this.mNodes) {
            if (node != this.mRootNode) {
                childList.add(node.mAnimation);
            }
        }
        return childList;
    }

    @Override
    public void setTarget(@Nullable Object target) {
        for (AnimatorSet.Node node : this.mNodes) {
            Animator animation = node.mAnimation;
            if (animation instanceof AnimatorSet) {
                animation.setTarget(target);
            } else if (animation instanceof ObjectAnimator) {
                animation.setTarget(target);
            }
        }
    }

    @Override
    public void setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    @Nullable
    @Override
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public AnimatorSet.Builder play(Animator anim) {
        return anim != null ? new AnimatorSet.Builder(anim) : null;
    }

    @Override
    public void cancel() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            if (this.isStarted()) {
                if (this.mListeners != null) {
                    for (AnimatorListener l : this.mListeners) {
                        l.onAnimationCancel(this);
                    }
                }
                for (AnimatorSet.Node node : new ArrayList(this.mPlayingSet)) {
                    node.mAnimation.cancel();
                }
                this.mPlayingSet.clear();
                this.endAnimation();
            }
        }
    }

    private void forceToEnd() {
        if (this.mEndCanBeCalled) {
            this.end();
        } else {
            if (this.mReversing) {
                this.handleAnimationEvents(this.mLastEventId, 0, this.getTotalDuration());
            } else {
                long zeroScalePlayTime = this.getTotalDuration();
                if (zeroScalePlayTime == -1L) {
                    zeroScalePlayTime = 2147483647L;
                }
                this.handleAnimationEvents(this.mLastEventId, this.mEvents.size() - 1, zeroScalePlayTime);
            }
            this.mPlayingSet.clear();
            this.endAnimation();
        }
    }

    @Override
    public void end() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else if (!this.mShouldIgnoreEndWithoutStart || this.isStarted()) {
            if (this.isStarted()) {
                if (this.mReversing) {
                    this.mLastEventId = this.mLastEventId == -1 ? this.mEvents.size() : this.mLastEventId;
                    while (this.mLastEventId > 0) {
                        this.mLastEventId--;
                        AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(this.mLastEventId);
                        Animator anim = event.mNode.mAnimation;
                        if (!this.mNodeMap.get(anim).mEnded) {
                            if (event.mEvent == 2) {
                                anim.reverse();
                            } else if (event.mEvent == 1 && anim.isStarted()) {
                                anim.end();
                            }
                        }
                    }
                } else {
                    while (this.mLastEventId < this.mEvents.size() - 1) {
                        this.mLastEventId++;
                        AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(this.mLastEventId);
                        Animator anim = event.mNode.mAnimation;
                        if (!this.mNodeMap.get(anim).mEnded) {
                            if (event.mEvent == 0) {
                                anim.start();
                            } else if (event.mEvent == 2 && anim.isStarted()) {
                                anim.end();
                            }
                        }
                    }
                }
                this.mPlayingSet.clear();
            }
            this.endAnimation();
        }
    }

    @Override
    public boolean isRunning() {
        return this.mStartDelay == 0L ? this.mStarted : this.mLastFrameTime > 0L;
    }

    @Override
    public boolean isStarted() {
        return this.mStarted;
    }

    @Override
    public long getStartDelay() {
        return this.mStartDelay;
    }

    @Override
    public void setStartDelay(long startDelay) {
        if (startDelay < 0L) {
            ModernUI.LOGGER.warn(MARKER, "Start delay should always be non-negative");
            startDelay = 0L;
        }
        long delta = startDelay - this.mStartDelay;
        if (delta != 0L) {
            this.mStartDelay = startDelay;
            if (!this.mDependencyDirty) {
                for (AnimatorSet.Node node : this.mNodes) {
                    if (node == this.mRootNode) {
                        node.mEndTime = this.mStartDelay;
                    } else {
                        node.mStartTime = node.mStartTime == -1L ? -1L : node.mStartTime + delta;
                        node.mEndTime = node.mEndTime == -1L ? -1L : node.mEndTime + delta;
                    }
                }
                if (this.mTotalDuration != -1L) {
                    this.mTotalDuration += delta;
                }
            }
        }
    }

    @Override
    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet setDuration(long duration) {
        if (duration < 0L) {
            throw new IllegalArgumentException("duration must be a value of zero or greater");
        } else {
            this.mDependencyDirty = true;
            this.mDuration = duration;
            return this;
        }
    }

    @Override
    public void setupStartValues() {
        for (AnimatorSet.Node node : this.mNodes) {
            if (node != this.mRootNode) {
                node.mAnimation.setupStartValues();
            }
        }
    }

    @Override
    public void setupEndValues() {
        for (AnimatorSet.Node node : this.mNodes) {
            if (node != this.mRootNode) {
                node.mAnimation.setupEndValues();
            }
        }
    }

    @Override
    public void pause() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            boolean previouslyPaused = this.mPaused;
            super.pause();
            if (!previouslyPaused && this.mPaused) {
                this.mPauseTime = -1L;
            }
        }
    }

    @Override
    public void resume() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            boolean previouslyPaused = this.mPaused;
            super.resume();
            if (previouslyPaused && !this.mPaused && this.mPauseTime >= 0L) {
                this.addAnimationCallback();
            }
        }
    }

    @Override
    public void start() {
        this.start(false, true);
    }

    @Override
    void startWithoutPulsing(boolean inReverse) {
        this.start(inReverse, false);
    }

    private void initAnimation() {
        if (this.mInterpolator != null) {
            for (AnimatorSet.Node node : this.mNodes) {
                node.mAnimation.setInterpolator(this.mInterpolator);
            }
        }
        this.updateAnimatorsDuration();
        this.createDependencyGraph();
    }

    private void start(boolean inReverse, boolean selfPulse) {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            this.mStarted = true;
            this.mSelfPulse = selfPulse;
            this.mPaused = false;
            this.mPauseTime = -1L;
            for (AnimatorSet.Node node : this.mNodes) {
                node.mEnded = false;
            }
            this.initAnimation();
            if (inReverse && !this.canReverse()) {
                throw new UnsupportedOperationException("Cannot reverse infinite AnimatorSet");
            } else {
                this.mReversing = inReverse;
                boolean isEmptySet = isEmptySet(this);
                if (!isEmptySet) {
                    this.startAnimation();
                }
                if (this.mListeners != null) {
                    for (AnimatorListener l : this.mListeners) {
                        l.onAnimationStart(this, inReverse);
                    }
                }
                if (isEmptySet) {
                    this.end();
                }
            }
        }
    }

    private static boolean isEmptySet(@Nonnull AnimatorSet set) {
        if (set.getStartDelay() > 0L) {
            return false;
        } else {
            for (int i = 0; i < set.getChildAnimations().size(); i++) {
                Animator anim = (Animator) set.getChildAnimations().get(i);
                if (!(anim instanceof AnimatorSet)) {
                    return false;
                }
                if (!isEmptySet((AnimatorSet) anim)) {
                    return false;
                }
            }
            return true;
        }
    }

    private void updateAnimatorsDuration() {
        if (this.mDuration >= 0L) {
            for (AnimatorSet.Node node : this.mNodes) {
                node.mAnimation.setDuration(this.mDuration);
            }
        }
        this.mDelayAnim.setDuration(this.mStartDelay);
    }

    @Override
    void skipToEndValue(boolean inReverse) {
        if (!this.isInitialized()) {
            throw new UnsupportedOperationException("Children must be initialized.");
        } else {
            this.initAnimation();
            if (inReverse) {
                for (int i = this.mEvents.size() - 1; i >= 0; i--) {
                    if (((AnimatorSet.AnimationEvent) this.mEvents.get(i)).mEvent == 1) {
                        ((AnimatorSet.AnimationEvent) this.mEvents.get(i)).mNode.mAnimation.skipToEndValue(true);
                    }
                }
            } else {
                for (AnimatorSet.AnimationEvent event : this.mEvents) {
                    if (event.mEvent == 2) {
                        event.mNode.mAnimation.skipToEndValue(false);
                    }
                }
            }
        }
    }

    @Override
    void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
        if (currentPlayTime >= 0L && lastPlayTime >= 0L) {
            if (inReverse) {
                if (this.getTotalDuration() == -1L) {
                    throw new UnsupportedOperationException("Cannot reverse AnimatorSet with infinite duration");
                }
                long duration = this.getTotalDuration() - this.mStartDelay;
                currentPlayTime = Math.min(currentPlayTime, duration);
                currentPlayTime = duration - currentPlayTime;
                lastPlayTime = duration - lastPlayTime;
            }
            ArrayList<AnimatorSet.Node> unfinishedNodes = new ArrayList();
            for (AnimatorSet.AnimationEvent event : this.mEvents) {
                if (event.getTime() > currentPlayTime || event.getTime() == -1L) {
                    break;
                }
                if (event.mEvent == 1 && (event.mNode.mEndTime == -1L || event.mNode.mEndTime > currentPlayTime)) {
                    unfinishedNodes.add(event.mNode);
                }
                if (event.mEvent == 2) {
                    event.mNode.mAnimation.skipToEndValue(false);
                }
            }
            for (AnimatorSet.Node node : unfinishedNodes) {
                long playTime = this.getPlayTimeForNode(currentPlayTime, node, false);
                playTime -= node.mAnimation.getStartDelay();
                node.mAnimation.animateBasedOnPlayTime(playTime, lastPlayTime, false);
            }
            for (AnimatorSet.AnimationEvent event : this.mEvents) {
                if (event.getTime() > currentPlayTime && event.mEvent == 1) {
                    event.mNode.mAnimation.skipToEndValue(true);
                }
            }
        } else {
            throw new UnsupportedOperationException("Error: Play time should never be negative.");
        }
    }

    @Override
    boolean isInitialized() {
        if (this.mChildrenInitialized) {
            return true;
        } else {
            boolean allInitialized = true;
            for (AnimatorSet.Node node : this.mNodes) {
                if (!node.mAnimation.isInitialized()) {
                    allInitialized = false;
                    break;
                }
            }
            this.mChildrenInitialized = allInitialized;
            return this.mChildrenInitialized;
        }
    }

    private void skipToStartValue(boolean inReverse) {
        this.skipToEndValue(!inReverse);
    }

    public void setCurrentPlayTime(long playTime) {
        if (this.mReversing && this.getTotalDuration() == -1L) {
            throw new UnsupportedOperationException("Error: Cannot seek in reverse in an infinite AnimatorSet");
        } else if ((this.getTotalDuration() == -1L || playTime <= this.getTotalDuration() - this.mStartDelay) && playTime >= 0L) {
            this.initAnimation();
            if (this.isStarted() && !this.isPaused()) {
                this.mSeekState.setPlayTime(playTime, this.mReversing);
            } else {
                if (this.mReversing) {
                    throw new UnsupportedOperationException("Error: Something went wrong. mReversing should not be set when AnimatorSet is not started.");
                }
                if (!this.mSeekState.isActive()) {
                    this.findLatestEventIdForTime(0L);
                    this.initChildren();
                    this.mSeekState.setPlayTime(0L, this.mReversing);
                }
                this.animateBasedOnPlayTime(playTime, 0L, this.mReversing);
                this.mSeekState.setPlayTime(playTime, this.mReversing);
            }
        } else {
            throw new UnsupportedOperationException("Error: Play time should always be in between0 and duration.");
        }
    }

    public long getCurrentPlayTime() {
        if (this.mSeekState.isActive()) {
            return this.mSeekState.getPlayTime();
        } else if (this.mLastFrameTime == -1L) {
            return 0L;
        } else {
            float durationScale = ValueAnimator.sDurationScale;
            durationScale = durationScale == 0.0F ? 1.0F : durationScale;
            return this.mReversing ? (long) ((float) (this.mLastFrameTime - this.mFirstFrame) / durationScale) : (long) ((float) (this.mLastFrameTime - this.mFirstFrame - this.mStartDelay) / durationScale);
        }
    }

    private void initChildren() {
        if (!this.isInitialized()) {
            this.mChildrenInitialized = true;
            this.skipToEndValue(false);
        }
    }

    @Internal
    @Override
    public boolean doAnimationFrame(long frameTime) {
        float durationScale = ValueAnimator.sDurationScale;
        if (durationScale == 0.0F) {
            this.forceToEnd();
            return true;
        } else {
            if (this.mFirstFrame < 0L) {
                this.mFirstFrame = frameTime;
            }
            if (this.mPaused) {
                this.mPauseTime = frameTime;
                this.removeAnimationCallback();
                return false;
            } else {
                if (this.mPauseTime > 0L) {
                    this.mFirstFrame = this.mFirstFrame + (frameTime - this.mPauseTime);
                    this.mPauseTime = -1L;
                }
                if (this.mSeekState.isActive()) {
                    this.mSeekState.updateSeekDirection(this.mReversing);
                    if (this.mReversing) {
                        this.mFirstFrame = (long) ((float) frameTime - (float) this.mSeekState.getPlayTime() * durationScale);
                    } else {
                        this.mFirstFrame = (long) ((float) frameTime - (float) (this.mSeekState.getPlayTime() + this.mStartDelay) * durationScale);
                    }
                    this.mSeekState.reset();
                }
                if (!this.mReversing && (float) frameTime < (float) this.mFirstFrame + (float) this.mStartDelay * durationScale) {
                    return false;
                } else {
                    long unscaledPlayTime = (long) ((float) (frameTime - this.mFirstFrame) / durationScale);
                    this.mLastFrameTime = frameTime;
                    int latestId = this.findLatestEventIdForTime(unscaledPlayTime);
                    int startId = this.mLastEventId;
                    this.handleAnimationEvents(startId, latestId, unscaledPlayTime);
                    this.mLastEventId = latestId;
                    for (AnimatorSet.Node node : this.mPlayingSet) {
                        if (!node.mEnded) {
                            this.pulseFrame(node, this.getPlayTimeForNode(unscaledPlayTime, node));
                        }
                    }
                    for (int i = this.mPlayingSet.size() - 1; i >= 0; i--) {
                        if (((AnimatorSet.Node) this.mPlayingSet.get(i)).mEnded) {
                            this.mPlayingSet.remove(i);
                        }
                    }
                    boolean finished = false;
                    if (this.mReversing) {
                        if (this.mPlayingSet.size() == 1 && this.mPlayingSet.get(0) == this.mRootNode) {
                            finished = true;
                        } else if (this.mPlayingSet.isEmpty() && this.mLastEventId < 3) {
                            finished = true;
                        }
                    } else {
                        finished = this.mPlayingSet.isEmpty() && this.mLastEventId == this.mEvents.size() - 1;
                    }
                    if (finished) {
                        this.endAnimation();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    @Internal
    public void commitAnimationFrame(long frameTime) {
    }

    @Override
    boolean pulseAnimationFrame(long frameTime) {
        return this.doAnimationFrame(frameTime);
    }

    private void handleAnimationEvents(int startId, int latestId, long playTime) {
        if (this.mReversing) {
            startId = startId == -1 ? this.mEvents.size() : startId;
            for (int i = startId - 1; i >= latestId; i--) {
                AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(i);
                AnimatorSet.Node node = event.mNode;
                if (event.mEvent == 2) {
                    if (node.mAnimation.isStarted()) {
                        node.mAnimation.cancel();
                    }
                    node.mEnded = false;
                    this.mPlayingSet.add(event.mNode);
                    node.mAnimation.startWithoutPulsing(true);
                    this.pulseFrame(node, 0L);
                } else if (event.mEvent == 1 && !node.mEnded) {
                    this.pulseFrame(node, this.getPlayTimeForNode(playTime, node));
                }
            }
        } else {
            for (int ix = startId + 1; ix <= latestId; ix++) {
                AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(ix);
                AnimatorSet.Node node = event.mNode;
                if (event.mEvent == 0) {
                    this.mPlayingSet.add(event.mNode);
                    if (node.mAnimation.isStarted()) {
                        node.mAnimation.cancel();
                    }
                    node.mEnded = false;
                    node.mAnimation.startWithoutPulsing(false);
                    this.pulseFrame(node, 0L);
                } else if (event.mEvent == 2 && !node.mEnded) {
                    this.pulseFrame(node, this.getPlayTimeForNode(playTime, node));
                }
            }
        }
    }

    private void pulseFrame(@Nonnull AnimatorSet.Node node, long animPlayTime) {
        if (!node.mEnded) {
            float durationScale = ValueAnimator.sDurationScale;
            durationScale = durationScale == 0.0F ? 1.0F : durationScale;
            node.mEnded = node.mAnimation.pulseAnimationFrame((long) ((float) animPlayTime * durationScale));
        }
    }

    private long getPlayTimeForNode(long overallPlayTime, AnimatorSet.Node node) {
        return this.getPlayTimeForNode(overallPlayTime, node, this.mReversing);
    }

    private long getPlayTimeForNode(long overallPlayTime, AnimatorSet.Node node, boolean inReverse) {
        if (inReverse) {
            overallPlayTime = this.getTotalDuration() - overallPlayTime;
            return node.mEndTime - overallPlayTime;
        } else {
            return overallPlayTime - node.mStartTime;
        }
    }

    private void startAnimation() {
        this.addAnimationEndListener();
        this.addAnimationCallback();
        if (this.mSeekState.getPlayTimeNormalized() == 0L && this.mReversing) {
            this.mSeekState.reset();
        }
        if (this.mShouldResetValuesAtStart) {
            if (this.isInitialized()) {
                this.skipToEndValue(!this.mReversing);
            } else if (this.mReversing) {
                this.initChildren();
                this.skipToEndValue(!this.mReversing);
            } else {
                for (int i = this.mEvents.size() - 1; i >= 0; i--) {
                    if (((AnimatorSet.AnimationEvent) this.mEvents.get(i)).mEvent == 1) {
                        Animator anim = ((AnimatorSet.AnimationEvent) this.mEvents.get(i)).mNode.mAnimation;
                        if (anim.isInitialized()) {
                            anim.skipToEndValue(true);
                        }
                    }
                }
            }
        }
        if (this.mReversing || this.mStartDelay == 0L || this.mSeekState.isActive()) {
            long playTime;
            if (this.mSeekState.isActive()) {
                this.mSeekState.updateSeekDirection(this.mReversing);
                playTime = this.mSeekState.getPlayTime();
            } else {
                playTime = 0L;
            }
            int toId = this.findLatestEventIdForTime(playTime);
            this.handleAnimationEvents(-1, toId, playTime);
            for (int ix = this.mPlayingSet.size() - 1; ix >= 0; ix--) {
                if (((AnimatorSet.Node) this.mPlayingSet.get(ix)).mEnded) {
                    this.mPlayingSet.remove(ix);
                }
            }
            this.mLastEventId = toId;
        }
    }

    private void addAnimationEndListener() {
        for (int i = 1; i < this.mNodes.size(); i++) {
            ((AnimatorSet.Node) this.mNodes.get(i)).mAnimation.addListener(this.mAnimationEndListener);
        }
    }

    private void removeAnimationEndListener() {
        for (int i = 1; i < this.mNodes.size(); i++) {
            ((AnimatorSet.Node) this.mNodes.get(i)).mAnimation.removeListener(this.mAnimationEndListener);
        }
    }

    private int findLatestEventIdForTime(long currentPlayTime) {
        int size = this.mEvents.size();
        int latestId = this.mLastEventId;
        if (this.mReversing) {
            currentPlayTime = this.getTotalDuration() - currentPlayTime;
            this.mLastEventId = this.mLastEventId == -1 ? size : this.mLastEventId;
            for (int j = this.mLastEventId - 1; j >= 0; j--) {
                AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(j);
                if (event.getTime() >= currentPlayTime) {
                    latestId = j;
                }
            }
        } else {
            for (int i = this.mLastEventId + 1; i < size; i++) {
                AnimatorSet.AnimationEvent event = (AnimatorSet.AnimationEvent) this.mEvents.get(i);
                if (event.getTime() != -1L && event.getTime() <= currentPlayTime) {
                    latestId = i;
                }
            }
        }
        return latestId;
    }

    private void endAnimation() {
        this.mStarted = false;
        this.mLastFrameTime = -1L;
        this.mFirstFrame = -1L;
        this.mLastEventId = -1;
        this.mPaused = false;
        this.mPauseTime = -1L;
        this.mSeekState.reset();
        this.mPlayingSet.clear();
        this.removeAnimationCallback();
        if (this.mListeners != null) {
            for (AnimatorListener l : this.mListeners) {
                l.onAnimationEnd(this, this.mReversing);
            }
        }
        this.removeAnimationEndListener();
        this.mSelfPulse = true;
        this.mReversing = false;
    }

    private void removeAnimationCallback() {
        if (this.mSelfPulse) {
            AnimationHandler handler = AnimationHandler.getInstance();
            handler.removeCallback(this);
        }
    }

    private void addAnimationCallback() {
        if (this.mSelfPulse) {
            AnimationHandler handler = AnimationHandler.getInstance();
            handler.addFrameCallback(this, 0L);
        }
    }

    @Nonnull
    public AnimatorSet clone() {
        final AnimatorSet anim = (AnimatorSet) super.clone();
        int nodeCount = this.mNodes.size();
        anim.mStarted = false;
        anim.mLastFrameTime = -1L;
        anim.mFirstFrame = -1L;
        anim.mLastEventId = -1;
        anim.mPaused = false;
        anim.mPauseTime = -1L;
        anim.mSeekState = new AnimatorSet.SeekState();
        anim.mSelfPulse = true;
        anim.mPlayingSet = new ArrayList();
        anim.mNodeMap = new ArrayMap<>();
        anim.mNodes = new ArrayList(nodeCount);
        anim.mEvents = new ArrayList();
        anim.mAnimationEndListener = new AnimatorListener() {

            @Override
            public void onAnimationEnd(@Nonnull Animator animation) {
                AnimatorSet.Node node = anim.mNodeMap.get(animation);
                if (node == null) {
                    throw new RuntimeException("Error: animation ended is not in the node map");
                } else {
                    node.mEnded = true;
                }
            }
        };
        anim.mReversing = false;
        anim.mDependencyDirty = true;
        HashMap<AnimatorSet.Node, AnimatorSet.Node> clonesMap = new HashMap(nodeCount);
        for (AnimatorSet.Node node : this.mNodes) {
            AnimatorSet.Node nodeClone = node.clone();
            nodeClone.mAnimation.removeListener(this.mAnimationEndListener);
            clonesMap.put(node, nodeClone);
            anim.mNodes.add(nodeClone);
            anim.mNodeMap.put(nodeClone.mAnimation, nodeClone);
        }
        anim.mRootNode = (AnimatorSet.Node) clonesMap.get(this.mRootNode);
        anim.mDelayAnim = (ValueAnimator) anim.mRootNode.mAnimation;
        for (AnimatorSet.Node node : this.mNodes) {
            AnimatorSet.Node nodeClone = (AnimatorSet.Node) clonesMap.get(node);
            nodeClone.mLatestParent = node.mLatestParent == null ? null : (AnimatorSet.Node) clonesMap.get(node.mLatestParent);
            int size = node.mChildNodes == null ? 0 : node.mChildNodes.size();
            for (int j = 0; j < size; j++) {
                nodeClone.mChildNodes.set(j, (AnimatorSet.Node) clonesMap.get(node.mChildNodes.get(j)));
            }
            size = node.mSiblings == null ? 0 : node.mSiblings.size();
            for (int j = 0; j < size; j++) {
                nodeClone.mSiblings.set(j, (AnimatorSet.Node) clonesMap.get(node.mSiblings.get(j)));
            }
            size = node.mParents == null ? 0 : node.mParents.size();
            for (int j = 0; j < size; j++) {
                nodeClone.mParents.set(j, (AnimatorSet.Node) clonesMap.get(node.mParents.get(j)));
            }
        }
        return anim;
    }

    @Internal
    @Override
    public boolean canReverse() {
        return this.getTotalDuration() != -1L;
    }

    @Override
    public void reverse() {
        this.start(true, true);
    }

    @Nonnull
    public String toString() {
        StringBuilder returnVal = new StringBuilder("AnimatorSet@" + Integer.toHexString(this.hashCode()) + "{");
        for (AnimatorSet.Node node : this.mNodes) {
            returnVal.append("\n    ").append(node.mAnimation.toString());
        }
        return returnVal + "\n}";
    }

    private void printChildCount() {
        ArrayList<AnimatorSet.Node> list = new ArrayList(this.mNodes.size());
        list.add(this.mRootNode);
        ModernUI.LOGGER.debug(MARKER, "Current tree: ");
        int index = 0;
        while (index < list.size()) {
            int listSize = list.size();
            StringBuilder builder;
            for (builder = new StringBuilder(); index < listSize; index++) {
                AnimatorSet.Node node = (AnimatorSet.Node) list.get(index);
                int num = 0;
                if (node.mChildNodes != null) {
                    for (int i = 0; i < node.mChildNodes.size(); i++) {
                        AnimatorSet.Node child = (AnimatorSet.Node) node.mChildNodes.get(i);
                        if (child.mLatestParent == node) {
                            num++;
                            list.add(child);
                        }
                    }
                }
                builder.append(" ");
                builder.append(num);
            }
            ModernUI.LOGGER.debug(MARKER, builder.toString());
        }
    }

    private void createDependencyGraph() {
        if (!this.mDependencyDirty) {
            boolean durationChanged = false;
            for (AnimatorSet.Node node : this.mNodes) {
                Animator anim = node.mAnimation;
                if (node.mTotalDuration != anim.getTotalDuration()) {
                    durationChanged = true;
                    break;
                }
            }
            if (!durationChanged) {
                return;
            }
        }
        this.mDependencyDirty = false;
        for (AnimatorSet.Node value : this.mNodes) {
            value.mParentsAdded = false;
        }
        for (AnimatorSet.Node nodex : this.mNodes) {
            if (!nodex.mParentsAdded) {
                nodex.mParentsAdded = true;
                if (nodex.mSiblings != null) {
                    this.findSiblings(nodex, nodex.mSiblings);
                    nodex.mSiblings.remove(nodex);
                    int siblingSize = nodex.mSiblings.size();
                    for (int j = 0; j < siblingSize; j++) {
                        nodex.addParents(((AnimatorSet.Node) nodex.mSiblings.get(j)).mParents);
                    }
                    for (int j = 0; j < siblingSize; j++) {
                        AnimatorSet.Node sibling = (AnimatorSet.Node) nodex.mSiblings.get(j);
                        sibling.addParents(nodex.mParents);
                        sibling.mParentsAdded = true;
                    }
                }
            }
        }
        for (AnimatorSet.Node nodexx : this.mNodes) {
            if (nodexx != this.mRootNode && nodexx.mParents == null) {
                nodexx.addParent(this.mRootNode);
            }
        }
        ArrayList<AnimatorSet.Node> visited = new ArrayList(this.mNodes.size());
        this.mRootNode.mStartTime = 0L;
        this.mRootNode.mEndTime = this.mDelayAnim.getDuration();
        this.updatePlayTime(this.mRootNode, visited);
        this.sortAnimationEvents();
        this.mTotalDuration = ((AnimatorSet.AnimationEvent) this.mEvents.get(this.mEvents.size() - 1)).getTime();
    }

    private void sortAnimationEvents() {
        this.mEvents.clear();
        for (int i = 1; i < this.mNodes.size(); i++) {
            AnimatorSet.Node node = (AnimatorSet.Node) this.mNodes.get(i);
            this.mEvents.add(new AnimatorSet.AnimationEvent(node, 0));
            this.mEvents.add(new AnimatorSet.AnimationEvent(node, 1));
            this.mEvents.add(new AnimatorSet.AnimationEvent(node, 2));
        }
        this.mEvents.sort((e1, e2) -> {
            long t1 = e1.getTime();
            long t2 = e2.getTime();
            if (t1 == t2) {
                return e2.mEvent + e1.mEvent == 1 ? e1.mEvent - e2.mEvent : e2.mEvent - e1.mEvent;
            } else if (t2 == -1L) {
                return -1;
            } else {
                return t1 == -1L ? 1 : (int) (t1 - t2);
            }
        });
        int eventSize = this.mEvents.size();
        int i = 0;
        while (true) {
            AnimatorSet.AnimationEvent event;
            boolean needToSwapStart;
            while (true) {
                if (i >= eventSize) {
                    if (!this.mEvents.isEmpty() && ((AnimatorSet.AnimationEvent) this.mEvents.get(0)).mEvent != 0) {
                        throw new UnsupportedOperationException("Sorting went bad, the start event should always be at index 0");
                    }
                    this.mEvents.add(0, new AnimatorSet.AnimationEvent(this.mRootNode, 0));
                    this.mEvents.add(1, new AnimatorSet.AnimationEvent(this.mRootNode, 1));
                    this.mEvents.add(2, new AnimatorSet.AnimationEvent(this.mRootNode, 2));
                    if (((AnimatorSet.AnimationEvent) this.mEvents.get(this.mEvents.size() - 1)).mEvent != 0 && ((AnimatorSet.AnimationEvent) this.mEvents.get(this.mEvents.size() - 1)).mEvent != 1) {
                        return;
                    }
                    throw new UnsupportedOperationException("Something went wrong, the last event is not an end event");
                }
                event = (AnimatorSet.AnimationEvent) this.mEvents.get(i);
                if (event.mEvent == 2) {
                    if (event.mNode.mStartTime == event.mNode.mEndTime) {
                        needToSwapStart = true;
                        break;
                    }
                    if (event.mNode.mEndTime == event.mNode.mStartTime + event.mNode.mAnimation.getStartDelay()) {
                        needToSwapStart = false;
                        break;
                    }
                    i++;
                } else {
                    i++;
                }
            }
            int startEventId = eventSize;
            int startDelayEndId = eventSize;
            for (int j = i + 1; j < eventSize && (startEventId >= eventSize || startDelayEndId >= eventSize); j++) {
                if (((AnimatorSet.AnimationEvent) this.mEvents.get(j)).mNode == event.mNode) {
                    if (((AnimatorSet.AnimationEvent) this.mEvents.get(j)).mEvent == 0) {
                        startEventId = j;
                    } else if (((AnimatorSet.AnimationEvent) this.mEvents.get(j)).mEvent == 1) {
                        startDelayEndId = j;
                    }
                }
            }
            if (needToSwapStart && startEventId == this.mEvents.size()) {
                throw new UnsupportedOperationException("Something went wrong, no start isfound after stop for an animation that has the same start and endtime.");
            }
            if (startDelayEndId == this.mEvents.size()) {
                throw new UnsupportedOperationException("Something went wrong, no startdelay end is found after stop for an animation");
            }
            if (needToSwapStart) {
                AnimatorSet.AnimationEvent startEvent = (AnimatorSet.AnimationEvent) this.mEvents.remove(startEventId);
                this.mEvents.add(i, startEvent);
                i++;
            }
            AnimatorSet.AnimationEvent startDelayEndEvent = (AnimatorSet.AnimationEvent) this.mEvents.remove(startDelayEndId);
            this.mEvents.add(i, startDelayEndEvent);
            i += 2;
        }
    }

    private void updatePlayTime(@Nonnull AnimatorSet.Node parent, @Nonnull ArrayList<AnimatorSet.Node> visited) {
        if (parent.mChildNodes == null) {
            if (parent == this.mRootNode) {
                for (AnimatorSet.Node node : this.mNodes) {
                    if (node != this.mRootNode) {
                        node.mStartTime = -1L;
                        node.mEndTime = -1L;
                    }
                }
            }
        } else {
            visited.add(parent);
            int childrenSize = parent.mChildNodes.size();
            for (int i = 0; i < childrenSize; i++) {
                AnimatorSet.Node child = (AnimatorSet.Node) parent.mChildNodes.get(i);
                child.mTotalDuration = child.mAnimation.getTotalDuration();
                int index = visited.indexOf(child);
                if (index < 0) {
                    if (child.mStartTime != -1L) {
                        if (parent.mEndTime == -1L) {
                            child.mLatestParent = parent;
                            child.mStartTime = -1L;
                            child.mEndTime = -1L;
                        } else {
                            if (parent.mEndTime >= child.mStartTime) {
                                child.mLatestParent = parent;
                                child.mStartTime = parent.mEndTime;
                            }
                            child.mEndTime = child.mTotalDuration == -1L ? -1L : child.mStartTime + child.mTotalDuration;
                        }
                    }
                    this.updatePlayTime(child, visited);
                } else {
                    for (int j = index; j < visited.size(); j++) {
                        ((AnimatorSet.Node) visited.get(j)).mLatestParent = null;
                        ((AnimatorSet.Node) visited.get(j)).mStartTime = -1L;
                        ((AnimatorSet.Node) visited.get(j)).mEndTime = -1L;
                    }
                    child.mStartTime = -1L;
                    child.mEndTime = -1L;
                    child.mLatestParent = null;
                    ModernUI.LOGGER.warn(MARKER, "Cycle found in AnimatorSet: " + this);
                }
            }
            visited.remove(parent);
        }
    }

    private void findSiblings(AnimatorSet.Node node, @Nonnull ArrayList<AnimatorSet.Node> siblings) {
        if (!siblings.contains(node)) {
            siblings.add(node);
            if (node.mSiblings == null) {
                return;
            }
            for (int i = 0; i < node.mSiblings.size(); i++) {
                this.findSiblings((AnimatorSet.Node) node.mSiblings.get(i), siblings);
            }
        }
    }

    public boolean shouldPlayTogether() {
        this.updateAnimatorsDuration();
        this.createDependencyGraph();
        return this.mRootNode.mChildNodes == null || this.mRootNode.mChildNodes.size() == this.mNodes.size() - 1;
    }

    @Override
    public long getTotalDuration() {
        this.updateAnimatorsDuration();
        this.createDependencyGraph();
        return this.mTotalDuration;
    }

    @Nonnull
    private AnimatorSet.Node getNodeForAnimation(@Nonnull Animator anim) {
        AnimatorSet.Node node = this.mNodeMap.get(anim);
        if (node == null) {
            node = new AnimatorSet.Node(anim);
            this.mNodeMap.put(anim, node);
            this.mNodes.add(node);
        }
        return node;
    }

    private static class AnimationEvent {

        static final int ANIMATION_START = 0;

        static final int ANIMATION_DELAY_ENDED = 1;

        static final int ANIMATION_END = 2;

        final AnimatorSet.Node mNode;

        final int mEvent;

        AnimationEvent(AnimatorSet.Node node, int event) {
            this.mNode = node;
            this.mEvent = event;
        }

        long getTime() {
            if (this.mEvent == 0) {
                return this.mNode.mStartTime;
            } else if (this.mEvent == 1) {
                return this.mNode.mStartTime == -1L ? -1L : this.mNode.mStartTime + this.mNode.mAnimation.getStartDelay();
            } else {
                return this.mNode.mEndTime;
            }
        }

        public String toString() {
            String eventStr = this.mEvent == 0 ? "start" : (this.mEvent == 1 ? "delay ended" : "end");
            return eventStr + " " + this.mNode.mAnimation.toString();
        }
    }

    public class Builder {

        private final AnimatorSet.Node mCurrentNode;

        Builder(@Nonnull Animator anim) {
            AnimatorSet.this.mDependencyDirty = true;
            this.mCurrentNode = AnimatorSet.this.getNodeForAnimation(anim);
        }

        public AnimatorSet.Builder with(@Nonnull Animator anim) {
            AnimatorSet.Node node = AnimatorSet.this.getNodeForAnimation(anim);
            this.mCurrentNode.addSibling(node);
            return this;
        }

        public AnimatorSet.Builder before(@Nonnull Animator anim) {
            AnimatorSet.Node node = AnimatorSet.this.getNodeForAnimation(anim);
            this.mCurrentNode.addChild(node);
            return this;
        }

        public AnimatorSet.Builder after(@Nonnull Animator anim) {
            AnimatorSet.Node node = AnimatorSet.this.getNodeForAnimation(anim);
            this.mCurrentNode.addParent(node);
            return this;
        }

        public AnimatorSet.Builder after(long delay) {
            ValueAnimator anim = ValueAnimator.ofFloat(0.0F, 1.0F);
            anim.setDuration(delay);
            this.after(anim);
            return this;
        }
    }

    private static class Node implements Cloneable {

        Animator mAnimation;

        ArrayList<AnimatorSet.Node> mChildNodes = null;

        boolean mEnded = false;

        ArrayList<AnimatorSet.Node> mSiblings;

        ArrayList<AnimatorSet.Node> mParents;

        AnimatorSet.Node mLatestParent = null;

        boolean mParentsAdded = false;

        long mStartTime = 0L;

        long mEndTime = 0L;

        long mTotalDuration = 0L;

        public Node(Animator animation) {
            this.mAnimation = animation;
        }

        @Nonnull
        public AnimatorSet.Node clone() {
            try {
                AnimatorSet.Node node = (AnimatorSet.Node) super.clone();
                node.mAnimation = this.mAnimation.clone();
                if (this.mChildNodes != null) {
                    node.mChildNodes = new ArrayList(this.mChildNodes);
                }
                if (this.mSiblings != null) {
                    node.mSiblings = new ArrayList(this.mSiblings);
                }
                if (this.mParents != null) {
                    node.mParents = new ArrayList(this.mParents);
                }
                node.mEnded = false;
                return node;
            } catch (CloneNotSupportedException var2) {
                throw new AssertionError();
            }
        }

        void addChild(AnimatorSet.Node node) {
            if (this.mChildNodes == null) {
                this.mChildNodes = new ArrayList();
            }
            if (!this.mChildNodes.contains(node)) {
                this.mChildNodes.add(node);
                node.addParent(this);
            }
        }

        public void addSibling(AnimatorSet.Node node) {
            if (this.mSiblings == null) {
                this.mSiblings = new ArrayList();
            }
            if (!this.mSiblings.contains(node)) {
                this.mSiblings.add(node);
                node.addSibling(this);
            }
        }

        public void addParent(AnimatorSet.Node node) {
            if (this.mParents == null) {
                this.mParents = new ArrayList();
            }
            if (!this.mParents.contains(node)) {
                this.mParents.add(node);
                node.addChild(this);
            }
        }

        public void addParents(ArrayList<AnimatorSet.Node> parents) {
            if (parents != null) {
                for (AnimatorSet.Node parent : parents) {
                    this.addParent(parent);
                }
            }
        }
    }

    private class SeekState {

        private long mPlayTime = -1L;

        private boolean mSeekingInReverse = false;

        void reset() {
            this.mPlayTime = -1L;
            this.mSeekingInReverse = false;
        }

        void setPlayTime(long playTime, boolean inReverse) {
            if (AnimatorSet.this.getTotalDuration() != -1L) {
                this.mPlayTime = Math.min(playTime, AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay);
            }
            this.mPlayTime = Math.max(0L, this.mPlayTime);
            this.mSeekingInReverse = inReverse;
        }

        void updateSeekDirection(boolean inReverse) {
            if (inReverse && AnimatorSet.this.getTotalDuration() == -1L) {
                throw new UnsupportedOperationException("Error: Cannot reverse infinite animator set");
            } else {
                if (this.mPlayTime >= 0L && inReverse != this.mSeekingInReverse) {
                    this.mPlayTime = AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay - this.mPlayTime;
                    this.mSeekingInReverse = inReverse;
                }
            }
        }

        long getPlayTime() {
            return this.mPlayTime;
        }

        long getPlayTimeNormalized() {
            return AnimatorSet.this.mReversing ? AnimatorSet.this.getTotalDuration() - AnimatorSet.this.mStartDelay - this.mPlayTime : this.mPlayTime;
        }

        boolean isActive() {
            return this.mPlayTime != -1L;
        }
    }
}