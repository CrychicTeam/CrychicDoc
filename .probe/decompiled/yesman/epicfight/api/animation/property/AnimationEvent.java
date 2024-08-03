package yesman.epicfight.api.animation.property;

import java.util.function.Predicate;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class AnimationEvent {

    final AnimationEvent.Side executionSide;

    final AnimationEvent.AnimationEventConsumer event;

    Object[] params;

    private AnimationEvent(AnimationEvent.Side executionSide, AnimationEvent.AnimationEventConsumer event) {
        this.executionSide = executionSide;
        this.event = event;
    }

    public void executeIfRightSide(LivingEntityPatch<?> entitypatch, StaticAnimation animation) {
        if (this.executionSide.predicate.test(entitypatch.isLogicalClient())) {
            this.event.fire(entitypatch, animation, this.params);
        }
    }

    public AnimationEvent params(Object... params) {
        this.params = params;
        return this;
    }

    public static AnimationEvent create(AnimationEvent.AnimationEventConsumer event, AnimationEvent.Side isRemote) {
        return new AnimationEvent(isRemote, event);
    }

    @FunctionalInterface
    public interface AnimationEventConsumer {

        void fire(LivingEntityPatch<?> var1, StaticAnimation var2, Object... var3);
    }

    public static enum Side {

        CLIENT(isLogicalClient -> isLogicalClient), SERVER(isLogicalClient -> !isLogicalClient), BOTH(isLogicalClient -> true);

        Predicate<Boolean> predicate;

        private Side(Predicate<Boolean> predicate) {
            this.predicate = predicate;
        }
    }

    public static class TimePeriodEvent extends AnimationEvent implements Comparable<AnimationEvent.TimePeriodEvent> {

        final float start;

        final float end;

        private TimePeriodEvent(float start, float end, AnimationEvent.Side executionSide, AnimationEvent.AnimationEventConsumer event) {
            super(executionSide, event);
            this.start = start;
            this.end = end;
        }

        public void executeIfRightSide(LivingEntityPatch<?> entitypatch, StaticAnimation animation, float prevElapsed, float elapsed) {
            if (this.start <= elapsed && this.end > elapsed) {
                super.executeIfRightSide(entitypatch, animation);
            }
        }

        public static AnimationEvent.TimePeriodEvent create(float start, float end, AnimationEvent.AnimationEventConsumer event, AnimationEvent.Side isRemote) {
            return new AnimationEvent.TimePeriodEvent(start, end, isRemote, event);
        }

        public AnimationEvent.TimePeriodEvent withParams(Object... params) {
            AnimationEvent.TimePeriodEvent event = new AnimationEvent.TimePeriodEvent(this.start, this.end, this.executionSide, this.event);
            event.params = params;
            return event;
        }

        public int compareTo(AnimationEvent.TimePeriodEvent arg0) {
            if (this.start == arg0.start) {
                return 0;
            } else {
                return this.start > arg0.start ? 1 : -1;
            }
        }

        public AnimationEvent.TimePeriodEvent params(Object... params) {
            this.params = params;
            return this;
        }
    }

    public static class TimeStampedEvent extends AnimationEvent implements Comparable<AnimationEvent.TimeStampedEvent> {

        final float time;

        private TimeStampedEvent(float time, AnimationEvent.Side executionSide, AnimationEvent.AnimationEventConsumer event) {
            super(executionSide, event);
            this.time = time;
        }

        public void executeIfRightSide(LivingEntityPatch<?> entitypatch, StaticAnimation animation, float prevElapsed, float elapsed) {
            if (this.time >= prevElapsed && this.time < elapsed) {
                super.executeIfRightSide(entitypatch, animation);
            }
        }

        public static AnimationEvent.TimeStampedEvent create(float time, AnimationEvent.AnimationEventConsumer event, AnimationEvent.Side isRemote) {
            return new AnimationEvent.TimeStampedEvent(time, isRemote, event);
        }

        public AnimationEvent.TimeStampedEvent withParams(Object... params) {
            AnimationEvent.TimeStampedEvent event = new AnimationEvent.TimeStampedEvent(this.time, this.executionSide, this.event);
            event.params = params;
            return event;
        }

        public int compareTo(AnimationEvent.TimeStampedEvent arg0) {
            if (this.time == arg0.time) {
                return 0;
            } else {
                return this.time > arg0.time ? 1 : -1;
            }
        }

        public AnimationEvent.TimeStampedEvent params(Object... params) {
            this.params = params;
            return this;
        }
    }
}