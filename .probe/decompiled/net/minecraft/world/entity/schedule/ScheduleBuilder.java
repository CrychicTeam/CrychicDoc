package net.minecraft.world.entity.schedule;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduleBuilder {

    private final Schedule schedule;

    private final List<ScheduleBuilder.ActivityTransition> transitions = Lists.newArrayList();

    public ScheduleBuilder(Schedule schedule0) {
        this.schedule = schedule0;
    }

    public ScheduleBuilder changeActivityAt(int int0, Activity activity1) {
        this.transitions.add(new ScheduleBuilder.ActivityTransition(int0, activity1));
        return this;
    }

    public Schedule build() {
        ((Set) this.transitions.stream().map(ScheduleBuilder.ActivityTransition::m_38054_).collect(Collectors.toSet())).forEach(this.schedule::m_38024_);
        this.transitions.forEach(p_38044_ -> {
            Activity $$1 = p_38044_.getActivity();
            this.schedule.getAllTimelinesExceptFor($$1).forEach(p_150245_ -> p_150245_.addKeyframe(p_38044_.getTime(), 0.0F));
            this.schedule.getTimelineFor($$1).addKeyframe(p_38044_.getTime(), 1.0F);
        });
        return this.schedule;
    }

    static class ActivityTransition {

        private final int time;

        private final Activity activity;

        public ActivityTransition(int int0, Activity activity1) {
            this.time = int0;
            this.activity = activity1;
        }

        public int getTime() {
            return this.time;
        }

        public Activity getActivity() {
            return this.activity;
        }
    }
}