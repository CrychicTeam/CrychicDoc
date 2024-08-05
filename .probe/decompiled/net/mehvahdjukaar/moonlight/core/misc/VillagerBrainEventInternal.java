package net.mehvahdjukaar.moonlight.core.misc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.mixins.accessor.BrainAccessor;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;

public class VillagerBrainEventInternal {

    private TreeMap<Integer, Activity> scheduleBuilder = null;

    private final Brain<Villager> brain;

    private final Villager villager;

    public VillagerBrainEventInternal(Brain<Villager> brain, Villager villager) {
        this.brain = brain;
        this.villager = villager;
    }

    public Villager getVillager() {
        return this.villager;
    }

    public Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories() {
        return this.brain.getMemories();
    }

    public void addOrReplaceActivity(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super Villager>>> activityPackage) {
        this.brain.addActivity(activity, activityPackage);
    }

    public void scheduleActivity(Activity activity, int startTime, int endTime) {
        if (this.scheduleBuilder == null) {
            this.scheduleBuilder = this.makeDefaultSchedule(this.villager);
        }
        TreeMap<Integer, Activity> newSchedule = new TreeMap();
        newSchedule.put(startTime, activity);
        Activity previousActivity = (Activity) this.scheduleBuilder.lastEntry().getValue();
        for (Entry<Integer, Activity> e : this.scheduleBuilder.entrySet()) {
            int key = (Integer) e.getKey();
            if (key < endTime) {
                previousActivity = (Activity) e.getValue();
            }
            if (startTime < endTime) {
                if (key < startTime || key > endTime) {
                    newSchedule.put(key, (Activity) e.getValue());
                }
            } else if (key > endTime && key < startTime) {
                newSchedule.put(key, (Activity) e.getValue());
            }
        }
        newSchedule.put(endTime, previousActivity);
        this.scheduleBuilder = newSchedule;
    }

    public void addSensor(SensorType<? extends Sensor<Villager>> newSensor) {
        try {
            Map<SensorType<? extends Sensor<Villager>>, Sensor<Villager>> sensors = ((BrainAccessor) this.brain).getSensors();
            Sensor<Villager> sensorInstance = (Sensor<Villager>) newSensor.create();
            sensors.put(newSensor, sensorInstance);
            Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories = this.brain.getMemories();
            for (MemoryModuleType<?> memoryModuleType : sensorInstance.requires()) {
                memories.put(memoryModuleType, Optional.empty());
            }
        } catch (Exception var7) {
            Moonlight.LOGGER.warn("failed to register pumpkin sensor type for villagers: " + var7);
        }
    }

    public <P extends Pair<Integer, ? extends Behavior<Villager>>> boolean addTaskToActivity(Activity activity, P task) {
        try {
            Map<Integer, Map<Activity, Set<Behavior<Villager>>>> map = ((BrainAccessor) this.brain).getAvailableBehaviorsByPriority();
            Map<Activity, Set<Behavior<Villager>>> tasksWithSamePriority = (Map<Activity, Set<Behavior<Villager>>>) map.computeIfAbsent((Integer) task.getFirst(), m -> Maps.newHashMap());
            Set<Behavior<Villager>> activityTaskSet = (Set<Behavior<Villager>>) tasksWithSamePriority.computeIfAbsent(activity, a -> Sets.newLinkedHashSet());
            activityTaskSet.add((Behavior) task.getSecond());
            return true;
        } catch (Exception var6) {
            Moonlight.LOGGER.warn("failed to add task for activity {} for villagers: {}", activity, var6);
            return false;
        }
    }

    private TreeMap<Integer, Activity> makeDefaultSchedule(Villager villager) {
        TreeMap<Integer, Activity> map = new TreeMap();
        if (villager.m_6162_()) {
            map.put(10, Activity.IDLE);
            map.put(3000, Activity.PLAY);
            map.put(6000, Activity.IDLE);
            map.put(10000, Activity.PLAY);
            map.put(12000, Activity.REST);
        } else {
            map.put(10, Activity.IDLE);
            map.put(2000, Activity.WORK);
            map.put(9000, Activity.MEET);
            map.put(11000, Activity.IDLE);
            map.put(12000, Activity.REST);
        }
        return map;
    }

    Schedule buildFinalizedSchedule() {
        ScheduleBuilder builder = new ScheduleBuilder((Schedule) VillagerAIInternal.CUSTOM_VILLAGER_SCHEDULE.get());
        for (Entry<Integer, Activity> e : this.scheduleBuilder.entrySet()) {
            builder.changeActivityAt((Integer) e.getKey(), (Activity) e.getValue());
        }
        return builder.build();
    }

    boolean hasCustomSchedule() {
        return this.scheduleBuilder != null;
    }
}