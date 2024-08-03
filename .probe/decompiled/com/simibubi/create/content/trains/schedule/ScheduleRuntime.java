package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.trains.display.GlobalTrainDisplayData;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import com.simibubi.create.content.trains.schedule.condition.ScheduledDelay;
import com.simibubi.create.content.trains.schedule.destination.ChangeThrottleInstruction;
import com.simibubi.create.content.trains.schedule.destination.ChangeTitleInstruction;
import com.simibubi.create.content.trains.schedule.destination.DestinationInstruction;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScheduleRuntime {

    private static final int TBD = -1;

    private static final int INVALID = -2;

    Train train;

    Schedule schedule;

    public boolean isAutoSchedule;

    public boolean paused;

    public boolean completed;

    public int currentEntry;

    public ScheduleRuntime.State state;

    static final int INTERVAL = 40;

    int cooldown;

    List<Integer> conditionProgress;

    List<CompoundTag> conditionContext;

    String currentTitle;

    int ticksInTransit;

    List<Integer> predictionTicks;

    public boolean displayLinkUpdateRequested;

    public ScheduleRuntime(Train train) {
        this.train = train;
        this.reset();
    }

    public void destinationReached() {
        if (this.state == ScheduleRuntime.State.IN_TRANSIT) {
            this.state = ScheduleRuntime.State.POST_TRANSIT;
            this.conditionProgress.clear();
            this.displayLinkUpdateRequested = true;
            for (Carriage carriage : this.train.carriages) {
                carriage.storage.resetIdleCargoTracker();
            }
            if (this.ticksInTransit > 0) {
                int current = (Integer) this.predictionTicks.get(this.currentEntry);
                if (current > 0) {
                    this.ticksInTransit = (current + this.ticksInTransit) / 2;
                }
                this.predictionTicks.set(this.currentEntry, this.ticksInTransit);
            }
            if (this.currentEntry < this.schedule.entries.size()) {
                List<List<ScheduleWaitCondition>> conditions = ((ScheduleEntry) this.schedule.entries.get(this.currentEntry)).conditions;
                for (int i = 0; i < conditions.size(); i++) {
                    this.conditionProgress.add(0);
                    this.conditionContext.add(new CompoundTag());
                }
            }
        }
    }

    public void transitInterrupted() {
        if (this.schedule != null && this.state == ScheduleRuntime.State.IN_TRANSIT) {
            this.state = ScheduleRuntime.State.PRE_TRANSIT;
            this.cooldown = 0;
        }
    }

    public void tick(Level level) {
        if (this.schedule != null) {
            if (!this.paused) {
                if (!this.train.derailed) {
                    if (this.train.navigation.destination != null) {
                        this.ticksInTransit++;
                    } else if (this.currentEntry >= this.schedule.entries.size()) {
                        this.currentEntry = 0;
                        if (!this.schedule.cyclic) {
                            this.paused = true;
                            this.completed = true;
                        }
                    } else if (this.cooldown-- <= 0) {
                        if (this.state != ScheduleRuntime.State.IN_TRANSIT) {
                            if (this.state == ScheduleRuntime.State.POST_TRANSIT) {
                                this.tickConditions(level);
                            } else {
                                DiscoveredPath nextPath = this.startCurrentInstruction();
                                if (nextPath != null) {
                                    this.train.status.successfulNavigation();
                                    if (nextPath.destination == this.train.getCurrentStation()) {
                                        this.state = ScheduleRuntime.State.IN_TRANSIT;
                                        this.destinationReached();
                                    } else {
                                        if (this.train.navigation.startNavigation(nextPath) != -1.0) {
                                            this.state = ScheduleRuntime.State.IN_TRANSIT;
                                            this.ticksInTransit = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void tickConditions(Level level) {
        List<List<ScheduleWaitCondition>> conditions = ((ScheduleEntry) this.schedule.entries.get(this.currentEntry)).conditions;
        for (int i = 0; i < conditions.size(); i++) {
            List<ScheduleWaitCondition> list = (List<ScheduleWaitCondition>) conditions.get(i);
            int progress = (Integer) this.conditionProgress.get(i);
            if (progress >= list.size()) {
                this.state = ScheduleRuntime.State.PRE_TRANSIT;
                this.currentEntry++;
                return;
            }
            CompoundTag tag = (CompoundTag) this.conditionContext.get(i);
            ScheduleWaitCondition condition = (ScheduleWaitCondition) list.get(progress);
            int prevVersion = tag.getInt("StatusVersion");
            if (condition.tickCompletion(level, this.train, tag)) {
                this.conditionContext.set(i, new CompoundTag());
                this.conditionProgress.set(i, progress + 1);
                this.displayLinkUpdateRequested |= i == 0;
            }
            this.displayLinkUpdateRequested = this.displayLinkUpdateRequested | (i == 0 && prevVersion != tag.getInt("StatusVersion"));
        }
        for (Carriage carriage : this.train.carriages) {
            carriage.storage.tickIdleCargoTracker();
        }
    }

    public DiscoveredPath startCurrentInstruction() {
        ScheduleEntry entry = (ScheduleEntry) this.schedule.entries.get(this.currentEntry);
        ScheduleInstruction instruction = entry.instruction;
        if (!(instruction instanceof DestinationInstruction destination)) {
            if (instruction instanceof ChangeTitleInstruction title) {
                this.currentTitle = title.getScheduleTitle();
                this.state = ScheduleRuntime.State.PRE_TRANSIT;
                this.currentEntry++;
                return null;
            } else if (instruction instanceof ChangeThrottleInstruction throttle) {
                this.train.throttle = (double) throttle.getThrottle();
                this.state = ScheduleRuntime.State.PRE_TRANSIT;
                this.currentEntry++;
                return null;
            } else {
                return null;
            }
        } else {
            String regex = destination.getFilterForRegex();
            boolean anyMatch = false;
            ArrayList<GlobalStation> validStations = new ArrayList();
            if (!this.train.hasForwardConductor() && !this.train.hasBackwardConductor()) {
                this.train.status.missingConductor();
                this.cooldown = 40;
                return null;
            } else {
                try {
                    for (GlobalStation globalStation : this.train.graph.getPoints(EdgePointType.STATION)) {
                        if (globalStation.name.matches(regex)) {
                            anyMatch = true;
                            validStations.add(globalStation);
                        }
                    }
                } catch (PatternSyntaxException var9) {
                }
                DiscoveredPath best = this.train.navigation.findPathTo(validStations, Double.MAX_VALUE);
                if (best == null) {
                    if (anyMatch) {
                        this.train.status.failedNavigation();
                    } else {
                        this.train.status.failedNavigationNoTarget(destination.getFilter());
                    }
                    this.cooldown = 40;
                    return null;
                } else {
                    return best;
                }
            }
        }
    }

    public void setSchedule(Schedule schedule, boolean auto) {
        this.reset();
        this.schedule = schedule;
        this.currentEntry = Mth.clamp(schedule.savedProgress, 0, schedule.entries.size() - 1);
        this.paused = false;
        this.isAutoSchedule = auto;
        this.train.status.newSchedule();
        this.predictionTicks = new ArrayList();
        schedule.entries.forEach($ -> this.predictionTicks.add(-1));
        this.displayLinkUpdateRequested = true;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void discardSchedule() {
        this.train.navigation.cancelNavigation();
        this.reset();
    }

    private void reset() {
        this.paused = true;
        this.completed = false;
        this.isAutoSchedule = false;
        this.currentEntry = 0;
        this.currentTitle = "";
        this.schedule = null;
        this.state = ScheduleRuntime.State.PRE_TRANSIT;
        this.conditionProgress = new ArrayList();
        this.conditionContext = new ArrayList();
        this.predictionTicks = new ArrayList();
    }

    public Collection<GlobalTrainDisplayData.TrainDeparturePrediction> submitPredictions() {
        Collection<GlobalTrainDisplayData.TrainDeparturePrediction> predictions = new ArrayList();
        int entryCount = this.schedule.entries.size();
        int accumulatedTime = 0;
        int current = this.currentEntry;
        if (this.state != ScheduleRuntime.State.POST_TRANSIT && current < entryCount) {
            GlobalStation destination = this.train.navigation.destination;
            if (destination != null) {
                double speed = Math.min(this.train.throttle * (double) this.train.maxSpeed(), (double) ((this.train.maxSpeed() + this.train.maxTurnSpeed()) / 2.0F));
                int timeRemaining = (int) (this.train.navigation.distanceToDestination / speed) * 2;
                if (this.predictionTicks.size() > current && this.train.navigation.distanceStartedAt != 0.0) {
                    float predictedTime = (float) ((Integer) this.predictionTicks.get(current)).intValue();
                    if (predictedTime > 0.0F) {
                        predictedTime = (float) ((double) predictedTime * Mth.clamp(this.train.navigation.distanceToDestination / this.train.navigation.distanceStartedAt, 0.0, 1.0));
                        timeRemaining = (timeRemaining + (int) predictedTime) / 2;
                    }
                }
                accumulatedTime += timeRemaining;
                predictions.add(this.createPrediction(current, destination.name, this.currentTitle, accumulatedTime));
                int departureTime = this.estimateStayDuration(current);
                if (departureTime != -2) {
                    accumulatedTime += departureTime;
                } else {
                    accumulatedTime = -2;
                }
            } else {
                this.predictForEntry(current, this.currentTitle, accumulatedTime, predictions);
            }
        } else {
            GlobalStation currentStation = this.train.getCurrentStation();
            if (currentStation != null) {
                predictions.add(this.createPrediction(current, currentStation.name, this.currentTitle, 0));
            }
            int departureTime = this.estimateStayDuration(current);
            if (departureTime == -2) {
                accumulatedTime = -2;
            } else {
                accumulatedTime += departureTime;
            }
        }
        String currentTitle = this.currentTitle;
        for (int i = 1; i < entryCount; i++) {
            int index = (i + current) % entryCount;
            if (index == 0 && !this.schedule.cyclic) {
                break;
            }
            if (((ScheduleEntry) this.schedule.entries.get(index)).instruction instanceof ChangeTitleInstruction title) {
                currentTitle = title.getScheduleTitle();
            } else {
                accumulatedTime = this.predictForEntry(index, currentTitle, accumulatedTime, predictions);
            }
        }
        predictions.removeIf(Objects::isNull);
        return predictions;
    }

    private int predictForEntry(int index, String currentTitle, int accumulatedTime, Collection<GlobalTrainDisplayData.TrainDeparturePrediction> predictions) {
        ScheduleEntry entry = (ScheduleEntry) this.schedule.entries.get(index);
        if (entry.instruction instanceof DestinationInstruction filter) {
            if (this.predictionTicks.size() <= this.currentEntry) {
                return accumulatedTime;
            } else {
                int departureTime = this.estimateStayDuration(index);
                if (accumulatedTime < 0) {
                    predictions.add(this.createPrediction(index, filter.getFilter(), currentTitle, accumulatedTime));
                    return Math.min(accumulatedTime, departureTime);
                } else {
                    int predictedTime = (Integer) this.predictionTicks.get(index);
                    accumulatedTime += predictedTime;
                    if (predictedTime == -1) {
                        accumulatedTime = -1;
                    }
                    predictions.add(this.createPrediction(index, filter.getFilter(), currentTitle, accumulatedTime));
                    if (accumulatedTime != -1) {
                        accumulatedTime += departureTime;
                    }
                    if (departureTime == -2) {
                        accumulatedTime = -2;
                    }
                    return accumulatedTime;
                }
            }
        } else {
            return accumulatedTime;
        }
    }

    private int estimateStayDuration(int index) {
        if (index >= this.schedule.entries.size()) {
            if (!this.schedule.cyclic) {
                return -2;
            }
            index = 0;
        }
        ScheduleEntry scheduleEntry = (ScheduleEntry) this.schedule.entries.get(index);
        label29: for (List<ScheduleWaitCondition> list : scheduleEntry.conditions) {
            int total = 0;
            for (ScheduleWaitCondition condition : list) {
                if (!(condition instanceof ScheduledDelay wait)) {
                    continue label29;
                }
                total += wait.totalWaitTicks();
            }
            return total;
        }
        return -2;
    }

    private GlobalTrainDisplayData.TrainDeparturePrediction createPrediction(int index, String destination, String currentTitle, int time) {
        if (time == -2) {
            return null;
        } else {
            int size = this.schedule.entries.size();
            if (index >= size) {
                if (!this.schedule.cyclic) {
                    return new GlobalTrainDisplayData.TrainDeparturePrediction(this.train, time, Components.literal(" "), destination);
                }
                index %= size;
            }
            String text = currentTitle;
            if (currentTitle.isBlank()) {
                for (int i = 1; i < size; i++) {
                    int j = (index + i) % size;
                    ScheduleEntry scheduleEntry = (ScheduleEntry) this.schedule.entries.get(j);
                    if (scheduleEntry.instruction instanceof DestinationInstruction instruction) {
                        text = instruction.getFilter().replaceAll("\\*", "").trim();
                        break;
                    }
                }
            }
            return new GlobalTrainDisplayData.TrainDeparturePrediction(this.train, time, Components.literal(text), destination);
        }
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("CurrentEntry", this.currentEntry);
        tag.putBoolean("AutoSchedule", this.isAutoSchedule);
        tag.putBoolean("Paused", this.paused);
        tag.putBoolean("Completed", this.completed);
        if (this.schedule != null) {
            tag.put("Schedule", this.schedule.write());
        }
        NBTHelper.writeEnum(tag, "State", this.state);
        tag.putIntArray("ConditionProgress", this.conditionProgress);
        tag.put("ConditionContext", NBTHelper.writeCompoundList(this.conditionContext, CompoundTag::m_6426_));
        tag.putIntArray("TransitTimes", this.predictionTicks);
        return tag;
    }

    public void read(CompoundTag tag) {
        this.reset();
        this.paused = tag.getBoolean("Paused");
        this.completed = tag.getBoolean("Completed");
        this.isAutoSchedule = tag.getBoolean("AutoSchedule");
        this.currentEntry = tag.getInt("CurrentEntry");
        if (tag.contains("Schedule")) {
            this.schedule = Schedule.fromTag(tag.getCompound("Schedule"));
        }
        this.state = NBTHelper.readEnum(tag, "State", ScheduleRuntime.State.class);
        for (int i : tag.getIntArray("ConditionProgress")) {
            this.conditionProgress.add(i);
        }
        NBTHelper.iterateCompoundList(tag.getList("ConditionContext", 10), this.conditionContext::add);
        int[] readTransits = tag.getIntArray("TransitTimes");
        if (this.schedule != null) {
            this.schedule.entries.forEach($ -> this.predictionTicks.add(-1));
            if (readTransits.length == this.schedule.entries.size()) {
                for (int i = 0; i < readTransits.length; i++) {
                    this.predictionTicks.set(i, readTransits[i]);
                }
            }
        }
    }

    public ItemStack returnSchedule() {
        if (this.schedule == null) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = AllItems.SCHEDULE.asStack();
            CompoundTag nbt = stack.getOrCreateTag();
            this.schedule.savedProgress = this.currentEntry;
            nbt.put("Schedule", this.schedule.write());
            stack = this.isAutoSchedule ? ItemStack.EMPTY : stack;
            this.discardSchedule();
            return stack;
        }
    }

    public void setSchedulePresentClientside(boolean present) {
        this.schedule = present ? new Schedule() : null;
    }

    public MutableComponent getWaitingStatus(Level level) {
        List<List<ScheduleWaitCondition>> conditions = ((ScheduleEntry) this.schedule.entries.get(this.currentEntry)).conditions;
        if (!conditions.isEmpty() && !this.conditionProgress.isEmpty() && !this.conditionContext.isEmpty()) {
            List<ScheduleWaitCondition> list = (List<ScheduleWaitCondition>) conditions.get(0);
            int progress = (Integer) this.conditionProgress.get(0);
            if (progress >= list.size()) {
                return Components.empty();
            } else {
                CompoundTag tag = (CompoundTag) this.conditionContext.get(0);
                ScheduleWaitCondition condition = (ScheduleWaitCondition) list.get(progress);
                return condition.getWaitingStatus(level, this.train, tag);
            }
        } else {
            return Components.empty();
        }
    }

    public static enum State {

        PRE_TRANSIT, IN_TRANSIT, POST_TRANSIT
    }
}