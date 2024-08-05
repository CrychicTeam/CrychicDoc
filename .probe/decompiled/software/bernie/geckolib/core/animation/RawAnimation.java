package software.bernie.geckolib.core.animation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;

public final class RawAnimation {

    private final List<RawAnimation.Stage> animationList = new ObjectArrayList();

    private RawAnimation() {
    }

    public static RawAnimation begin() {
        return new RawAnimation();
    }

    public RawAnimation thenPlay(String animationName) {
        return this.then(animationName, Animation.LoopType.DEFAULT);
    }

    public RawAnimation thenLoop(String animationName) {
        return this.then(animationName, Animation.LoopType.LOOP);
    }

    public RawAnimation thenWait(int ticks) {
        this.animationList.add(new RawAnimation.Stage("internal.wait", Animation.LoopType.PLAY_ONCE, ticks));
        return this;
    }

    public RawAnimation thenPlayAndHold(String animation) {
        return this.then(animation, Animation.LoopType.HOLD_ON_LAST_FRAME);
    }

    public RawAnimation thenPlayXTimes(String animationName, int playCount) {
        for (int i = 0; i < playCount; i++) {
            this.then(animationName, i == playCount - 1 ? Animation.LoopType.DEFAULT : Animation.LoopType.PLAY_ONCE);
        }
        return this;
    }

    public RawAnimation then(String animationName, Animation.LoopType loopType) {
        this.animationList.add(new RawAnimation.Stage(animationName, loopType));
        return this;
    }

    public List<RawAnimation.Stage> getAnimationStages() {
        return this.animationList;
    }

    public static RawAnimation copyOf(RawAnimation other) {
        RawAnimation newInstance = begin();
        newInstance.animationList.addAll(other.animationList);
        return newInstance;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj != null && this.getClass() == obj.getClass() ? this.hashCode() == obj.hashCode() : false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.animationList });
    }

    public static record Stage(String animationName, Animation.LoopType loopType, int additionalTicks) {

        static final String WAIT = "internal.wait";

        public Stage(String animationName, Animation.LoopType loopType) {
            this(animationName, loopType, 0);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else {
                return obj != null && this.getClass() == obj.getClass() ? this.hashCode() == obj.hashCode() : false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.animationName, this.loopType });
        }
    }
}