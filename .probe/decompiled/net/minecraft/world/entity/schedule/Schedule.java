package net.minecraft.world.entity.schedule;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class Schedule {

    public static final int WORK_START_TIME = 2000;

    public static final int TOTAL_WORK_TIME = 7000;

    public static final Schedule EMPTY = register("empty").changeActivityAt(0, Activity.IDLE).build();

    public static final Schedule SIMPLE = register("simple").changeActivityAt(5000, Activity.WORK).changeActivityAt(11000, Activity.REST).build();

    public static final Schedule VILLAGER_BABY = register("villager_baby").changeActivityAt(10, Activity.IDLE).changeActivityAt(3000, Activity.PLAY).changeActivityAt(6000, Activity.IDLE).changeActivityAt(10000, Activity.PLAY).changeActivityAt(12000, Activity.REST).build();

    public static final Schedule VILLAGER_DEFAULT = register("villager_default").changeActivityAt(10, Activity.IDLE).changeActivityAt(2000, Activity.WORK).changeActivityAt(9000, Activity.MEET).changeActivityAt(11000, Activity.IDLE).changeActivityAt(12000, Activity.REST).build();

    private final Map<Activity, Timeline> timelines = Maps.newHashMap();

    protected static ScheduleBuilder register(String string0) {
        Schedule $$1 = Registry.register(BuiltInRegistries.SCHEDULE, string0, new Schedule());
        return new ScheduleBuilder($$1);
    }

    protected void ensureTimelineExistsFor(Activity activity0) {
        if (!this.timelines.containsKey(activity0)) {
            this.timelines.put(activity0, new Timeline());
        }
    }

    protected Timeline getTimelineFor(Activity activity0) {
        return (Timeline) this.timelines.get(activity0);
    }

    protected List<Timeline> getAllTimelinesExceptFor(Activity activity0) {
        return (List<Timeline>) this.timelines.entrySet().stream().filter(p_38028_ -> p_38028_.getKey() != activity0).map(Entry::getValue).collect(Collectors.toList());
    }

    public Activity getActivityAt(int int0) {
        return (Activity) this.timelines.entrySet().stream().max(Comparator.comparingDouble(p_38023_ -> (double) ((Timeline) p_38023_.getValue()).getValueAt(int0))).map(Entry::getKey).orElse(Activity.IDLE);
    }
}