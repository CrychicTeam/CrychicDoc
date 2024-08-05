package net.minecraft.world.entity.ai.goal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

public class GoalSelector {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final WrappedGoal NO_GOAL = new WrappedGoal(Integer.MAX_VALUE, new Goal() {

        @Override
        public boolean canUse() {
            return false;
        }
    }) {

        @Override
        public boolean isRunning() {
            return false;
        }
    };

    private final Map<Goal.Flag, WrappedGoal> lockedFlags = new EnumMap(Goal.Flag.class);

    private final Set<WrappedGoal> availableGoals = Sets.newLinkedHashSet();

    private final Supplier<ProfilerFiller> profiler;

    private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);

    private int tickCount;

    private int newGoalRate = 3;

    public GoalSelector(Supplier<ProfilerFiller> supplierProfilerFiller0) {
        this.profiler = supplierProfilerFiller0;
    }

    public void addGoal(int int0, Goal goal1) {
        this.availableGoals.add(new WrappedGoal(int0, goal1));
    }

    @VisibleForTesting
    public void removeAllGoals(Predicate<Goal> predicateGoal0) {
        this.availableGoals.removeIf(p_262564_ -> predicateGoal0.test(p_262564_.getGoal()));
    }

    public void removeGoal(Goal goal0) {
        this.availableGoals.stream().filter(p_25378_ -> p_25378_.getGoal() == goal0).filter(WrappedGoal::m_7620_).forEach(WrappedGoal::m_8041_);
        this.availableGoals.removeIf(p_25367_ -> p_25367_.getGoal() == goal0);
    }

    private static boolean goalContainsAnyFlags(WrappedGoal wrappedGoal0, EnumSet<Goal.Flag> enumSetGoalFlag1) {
        for (Goal.Flag $$2 : wrappedGoal0.getFlags()) {
            if (enumSetGoalFlag1.contains($$2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean goalCanBeReplacedForAllFlags(WrappedGoal wrappedGoal0, Map<Goal.Flag, WrappedGoal> mapGoalFlagWrappedGoal1) {
        for (Goal.Flag $$2 : wrappedGoal0.getFlags()) {
            if (!((WrappedGoal) mapGoalFlagWrappedGoal1.getOrDefault($$2, NO_GOAL)).canBeReplacedBy(wrappedGoal0)) {
                return false;
            }
        }
        return true;
    }

    public void tick() {
        ProfilerFiller $$0 = (ProfilerFiller) this.profiler.get();
        $$0.push("goalCleanup");
        for (WrappedGoal $$1 : this.availableGoals) {
            if ($$1.isRunning() && (goalContainsAnyFlags($$1, this.disabledFlags) || !$$1.canContinueToUse())) {
                $$1.stop();
            }
        }
        Iterator<Entry<Goal.Flag, WrappedGoal>> $$2 = this.lockedFlags.entrySet().iterator();
        while ($$2.hasNext()) {
            Entry<Goal.Flag, WrappedGoal> $$3 = (Entry<Goal.Flag, WrappedGoal>) $$2.next();
            if (!((WrappedGoal) $$3.getValue()).isRunning()) {
                $$2.remove();
            }
        }
        $$0.pop();
        $$0.push("goalUpdate");
        for (WrappedGoal $$4 : this.availableGoals) {
            if (!$$4.isRunning() && !goalContainsAnyFlags($$4, this.disabledFlags) && goalCanBeReplacedForAllFlags($$4, this.lockedFlags) && $$4.canUse()) {
                for (Goal.Flag $$5 : $$4.getFlags()) {
                    WrappedGoal $$6 = (WrappedGoal) this.lockedFlags.getOrDefault($$5, NO_GOAL);
                    $$6.stop();
                    this.lockedFlags.put($$5, $$4);
                }
                $$4.start();
            }
        }
        $$0.pop();
        this.tickRunningGoals(true);
    }

    public void tickRunningGoals(boolean boolean0) {
        ProfilerFiller $$1 = (ProfilerFiller) this.profiler.get();
        $$1.push("goalTick");
        for (WrappedGoal $$2 : this.availableGoals) {
            if ($$2.isRunning() && (boolean0 || $$2.requiresUpdateEveryTick())) {
                $$2.tick();
            }
        }
        $$1.pop();
    }

    public Set<WrappedGoal> getAvailableGoals() {
        return this.availableGoals;
    }

    public Stream<WrappedGoal> getRunningGoals() {
        return this.availableGoals.stream().filter(WrappedGoal::m_7620_);
    }

    public void setNewGoalRate(int int0) {
        this.newGoalRate = int0;
    }

    public void disableControlFlag(Goal.Flag goalFlag0) {
        this.disabledFlags.add(goalFlag0);
    }

    public void enableControlFlag(Goal.Flag goalFlag0) {
        this.disabledFlags.remove(goalFlag0);
    }

    public void setControlFlag(Goal.Flag goalFlag0, boolean boolean1) {
        if (boolean1) {
            this.enableControlFlag(goalFlag0);
        } else {
            this.disableControlFlag(goalFlag0);
        }
    }
}