package net.minecraftforge.common.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import org.jetbrains.annotations.ApiStatus.Internal;

public class BrainBuilder<E extends LivingEntity> {

    private final Collection<MemoryModuleType<?>> memoryTypes = new HashSet();

    private final Collection<SensorType<? extends Sensor<? super E>>> sensorTypes = new HashSet();

    private final Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority = Maps.newTreeMap();

    private Schedule schedule = Schedule.EMPTY;

    private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements = Maps.newHashMap();

    private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped = Maps.newHashMap();

    private final Set<Activity> coreActivities = Sets.newHashSet();

    private final Set<Activity> activeActivites = Sets.newHashSet();

    private Activity defaultActivity = Activity.IDLE;

    public BrainBuilder(Brain<E> ignoredBrain) {
    }

    public Brain.Provider<E> provider() {
        return Brain.provider(this.memoryTypes, this.sensorTypes);
    }

    public Collection<MemoryModuleType<?>> getMemoryTypes() {
        return this.memoryTypes;
    }

    public Collection<SensorType<? extends Sensor<? super E>>> getSensorTypes() {
        return this.sensorTypes;
    }

    public Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> getAvailableBehaviorsByPriority() {
        return this.availableBehaviorsByPriority;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> getActivityRequirements() {
        return this.activityRequirements;
    }

    public Map<Activity, Set<MemoryModuleType<?>>> getActivityMemoriesToEraseWhenStopped() {
        return this.activityMemoriesToEraseWhenStopped;
    }

    public Set<Activity> getCoreActivities() {
        return this.coreActivities;
    }

    public Activity getDefaultActivity() {
        return this.defaultActivity;
    }

    public void setDefaultActivity(Activity defaultActivity) {
        this.defaultActivity = defaultActivity;
    }

    public Set<Activity> getActiveActivites() {
        return this.activeActivites;
    }

    public void setActiveActivites(Set<Activity> value) {
        this.activeActivites.clear();
        this.activeActivites.addAll(value);
    }

    public void addBehaviorToActivityByPriority(Integer priority, Activity activity, BehaviorControl<? super E> behaviorControl) {
        ((Set) ((Map) this.availableBehaviorsByPriority.computeIfAbsent(priority, i -> Maps.newHashMap())).computeIfAbsent(activity, a -> Sets.newLinkedHashSet())).add(behaviorControl);
    }

    public void addRequirementsToActivity(Activity activity, Collection<Pair<MemoryModuleType<?>, MemoryStatus>> requirements) {
        addRequirementsToActivityInternal(this.activityRequirements, activity, requirements);
    }

    public void addMemoriesToEraseWhenActivityStopped(Activity activity, Collection<MemoryModuleType<?>> memories) {
        addMemoriesToEraseWhenActivityStoppedInternal(this.activityMemoriesToEraseWhenStopped, activity, memories);
    }

    @Internal
    public void addAvailableBehaviorsByPriorityFrom(Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> addFrom) {
        addFrom.forEach((priority, activitySetMap) -> activitySetMap.forEach((activity, behaviorControls) -> ((Set) ((Map) this.availableBehaviorsByPriority.computeIfAbsent(priority, p -> Maps.newHashMap())).computeIfAbsent(activity, a -> Sets.newLinkedHashSet())).addAll(behaviorControls)));
    }

    @Internal
    public void addAvailableBehaviorsByPriorityTo(Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> addTo) {
        this.availableBehaviorsByPriority.forEach((priority, activitySetMap) -> activitySetMap.forEach((activity, behaviorControls) -> ((Set) ((Map) addTo.computeIfAbsent(priority, p -> Maps.newHashMap())).computeIfAbsent(activity, a -> Sets.newLinkedHashSet())).addAll(behaviorControls)));
    }

    @Internal
    public void addActivityRequirementsFrom(Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> addFrom) {
        addFrom.forEach(this::addRequirementsToActivity);
    }

    @Internal
    public void addActivityRequirementsTo(Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> addTo) {
        this.activityRequirements.forEach((activity, requirements) -> addRequirementsToActivityInternal(addTo, activity, requirements));
    }

    @Internal
    public void addActivityMemoriesToEraseWhenStoppedFrom(Map<Activity, Set<MemoryModuleType<?>>> addFrom) {
        addFrom.forEach(this::addMemoriesToEraseWhenActivityStopped);
    }

    @Internal
    public void addActivityMemoriesToEraseWhenStoppedTo(Map<Activity, Set<MemoryModuleType<?>>> addTo) {
        this.activityMemoriesToEraseWhenStopped.forEach((activity, memories) -> addMemoriesToEraseWhenActivityStoppedInternal(addTo, activity, memories));
    }

    private static void addMemoriesToEraseWhenActivityStoppedInternal(Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped, Activity activity, Collection<MemoryModuleType<?>> memories) {
        ((Set) activityMemoriesToEraseWhenStopped.computeIfAbsent(activity, a -> Sets.newHashSet())).addAll(memories);
    }

    private static void addRequirementsToActivityInternal(Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements, Activity activity, Collection<Pair<MemoryModuleType<?>, MemoryStatus>> requirements) {
        ((Set) activityRequirements.computeIfAbsent(activity, a -> Sets.newHashSet())).addAll(requirements);
    }

    @Internal
    public Brain<E> makeBrain(Dynamic<?> dynamic) {
        Brain<E> brain = Brain.<E>provider(this.memoryTypes, this.sensorTypes).makeBrain(dynamic);
        brain.copyFromBuilder(this);
        return brain;
    }
}