package net.minecraft.world.entity.ai;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

public class Brain<E extends LivingEntity> {

    static final Logger LOGGER = LogUtils.getLogger();

    private final Supplier<Codec<Brain<E>>> codec;

    private static final int SCHEDULE_UPDATE_DELAY = 20;

    private final Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories = Maps.newHashMap();

    private final Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors = Maps.newLinkedHashMap();

    private final Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority = Maps.newTreeMap();

    private Schedule schedule = Schedule.EMPTY;

    private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements = Maps.newHashMap();

    private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped = Maps.newHashMap();

    private Set<Activity> coreActivities = Sets.newHashSet();

    private final Set<Activity> activeActivities = Sets.newHashSet();

    private Activity defaultActivity = Activity.IDLE;

    private long lastScheduleUpdate = -9999L;

    public static <E extends LivingEntity> Brain.Provider<E> provider(Collection<? extends MemoryModuleType<?>> collectionExtendsMemoryModuleType0, Collection<? extends SensorType<? extends Sensor<? super E>>> collectionExtendsSensorTypeExtendsSensorSuperE1) {
        return new Brain.Provider<>(collectionExtendsMemoryModuleType0, collectionExtendsSensorTypeExtendsSensorSuperE1);
    }

    public static <E extends LivingEntity> Codec<Brain<E>> codec(final Collection<? extends MemoryModuleType<?>> collectionExtendsMemoryModuleType0, final Collection<? extends SensorType<? extends Sensor<? super E>>> collectionExtendsSensorTypeExtendsSensorSuperE1) {
        final MutableObject<Codec<Brain<E>>> $$2 = new MutableObject();
        $$2.setValue((new MapCodec<Brain<E>>() {

            public <T> Stream<T> keys(DynamicOps<T> p_22029_) {
                return collectionExtendsMemoryModuleType0.stream().flatMap(p_22020_ -> p_22020_.getCodec().map(p_258254_ -> BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(p_22020_)).stream()).map(p_22018_ -> p_22029_.createString(p_22018_.toString()));
            }

            public <T> DataResult<Brain<E>> decode(DynamicOps<T> p_22022_, MapLike<T> p_22023_) {
                MutableObject<DataResult<Builder<Brain.MemoryValue<?>>>> $$2 = new MutableObject(DataResult.success(ImmutableList.builder()));
                p_22023_.entries().forEach(p_258252_ -> {
                    DataResult<MemoryModuleType<?>> $$3x = BuiltInRegistries.MEMORY_MODULE_TYPE.m_194605_().parse(p_22022_, p_258252_.getFirst());
                    DataResult<? extends Brain.MemoryValue<?>> $$4 = $$3x.flatMap(p_147350_ -> this.captureRead(p_147350_, p_22022_, (T) p_258252_.getSecond()));
                    $$2.setValue(((DataResult) $$2.getValue()).apply2(Builder::add, $$4));
                });
                ImmutableList<Brain.MemoryValue<?>> $$3 = (ImmutableList<Brain.MemoryValue<?>>) ((DataResult) $$2.getValue()).resultOrPartial(Brain.LOGGER::error).map(Builder::build).orElseGet(ImmutableList::of);
                return DataResult.success(new Brain(collectionExtendsMemoryModuleType0, collectionExtendsSensorTypeExtendsSensorSuperE1, $$3, $$2::getValue));
            }

            private <T, U> DataResult<Brain.MemoryValue<U>> captureRead(MemoryModuleType<U> p_21997_, DynamicOps<T> p_21998_, T p_21999_) {
                return ((DataResult) p_21997_.getCodec().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "No codec for memory: " + p_21997_))).flatMap(p_22011_ -> p_22011_.parse(p_21998_, p_21999_)).map(p_21992_ -> new Brain.MemoryValue<>(p_21997_, Optional.of(p_21992_)));
            }

            public <T> RecordBuilder<T> encode(Brain<E> p_21985_, DynamicOps<T> p_21986_, RecordBuilder<T> p_21987_) {
                p_21985_.memories().forEach(p_22007_ -> p_22007_.serialize(p_21986_, p_21987_));
                return p_21987_;
            }
        }).fieldOf("memories").codec());
        return (Codec<Brain<E>>) $$2.getValue();
    }

    public Brain(Collection<? extends MemoryModuleType<?>> collectionExtendsMemoryModuleType0, Collection<? extends SensorType<? extends Sensor<? super E>>> collectionExtendsSensorTypeExtendsSensorSuperE1, ImmutableList<Brain.MemoryValue<?>> immutableListBrainMemoryValue2, Supplier<Codec<Brain<E>>> supplierCodecBrainE3) {
        this.codec = supplierCodecBrainE3;
        for (MemoryModuleType<?> $$4 : collectionExtendsMemoryModuleType0) {
            this.memories.put($$4, Optional.empty());
        }
        for (SensorType<? extends Sensor<? super E>> $$5 : collectionExtendsSensorTypeExtendsSensorSuperE1) {
            this.sensors.put($$5, $$5.create());
        }
        for (Sensor<? super E> $$6 : this.sensors.values()) {
            for (MemoryModuleType<?> $$7 : $$6.requires()) {
                this.memories.put($$7, Optional.empty());
            }
        }
        UnmodifiableIterator var11 = immutableListBrainMemoryValue2.iterator();
        while (var11.hasNext()) {
            Brain.MemoryValue<?> $$8 = (Brain.MemoryValue<?>) var11.next();
            $$8.setMemoryInternal(this);
        }
    }

    public <T> DataResult<T> serializeStart(DynamicOps<T> dynamicOpsT0) {
        return ((Codec) this.codec.get()).encodeStart(dynamicOpsT0, this);
    }

    Stream<Brain.MemoryValue<?>> memories() {
        return this.memories.entrySet().stream().map(p_21929_ -> Brain.MemoryValue.createUnchecked((MemoryModuleType) p_21929_.getKey(), (Optional<? extends ExpirableValue<?>>) p_21929_.getValue()));
    }

    public boolean hasMemoryValue(MemoryModuleType<?> memoryModuleType0) {
        return this.checkMemory(memoryModuleType0, MemoryStatus.VALUE_PRESENT);
    }

    public void clearMemories() {
        this.memories.keySet().forEach(p_276103_ -> this.memories.put(p_276103_, Optional.empty()));
    }

    public <U> void eraseMemory(MemoryModuleType<U> memoryModuleTypeU0) {
        this.setMemory(memoryModuleTypeU0, Optional.empty());
    }

    public <U> void setMemory(MemoryModuleType<U> memoryModuleTypeU0, @Nullable U u1) {
        this.setMemory(memoryModuleTypeU0, Optional.ofNullable(u1));
    }

    public <U> void setMemoryWithExpiry(MemoryModuleType<U> memoryModuleTypeU0, U u1, long long2) {
        this.setMemoryInternal(memoryModuleTypeU0, Optional.of(ExpirableValue.of(u1, long2)));
    }

    public <U> void setMemory(MemoryModuleType<U> memoryModuleTypeU0, Optional<? extends U> optionalExtendsU1) {
        this.setMemoryInternal(memoryModuleTypeU0, optionalExtendsU1.map(ExpirableValue::m_26309_));
    }

    <U> void setMemoryInternal(MemoryModuleType<U> memoryModuleTypeU0, Optional<? extends ExpirableValue<?>> optionalExtendsExpirableValue1) {
        if (this.memories.containsKey(memoryModuleTypeU0)) {
            if (optionalExtendsExpirableValue1.isPresent() && this.isEmptyCollection(((ExpirableValue) optionalExtendsExpirableValue1.get()).getValue())) {
                this.eraseMemory(memoryModuleTypeU0);
            } else {
                this.memories.put(memoryModuleTypeU0, optionalExtendsExpirableValue1);
            }
        }
    }

    public <U> Optional<U> getMemory(MemoryModuleType<U> memoryModuleTypeU0) {
        Optional<? extends ExpirableValue<?>> $$1 = (Optional<? extends ExpirableValue<?>>) this.memories.get(memoryModuleTypeU0);
        if ($$1 == null) {
            throw new IllegalStateException("Unregistered memory fetched: " + memoryModuleTypeU0);
        } else {
            return $$1.map(ExpirableValue::m_26319_);
        }
    }

    @Nullable
    public <U> Optional<U> getMemoryInternal(MemoryModuleType<U> memoryModuleTypeU0) {
        Optional<? extends ExpirableValue<?>> $$1 = (Optional<? extends ExpirableValue<?>>) this.memories.get(memoryModuleTypeU0);
        return $$1 == null ? null : $$1.map(ExpirableValue::m_26319_);
    }

    public <U> long getTimeUntilExpiry(MemoryModuleType<U> memoryModuleTypeU0) {
        Optional<? extends ExpirableValue<?>> $$1 = (Optional<? extends ExpirableValue<?>>) this.memories.get(memoryModuleTypeU0);
        return (Long) $$1.map(ExpirableValue::m_148191_).orElse(0L);
    }

    @Deprecated
    @VisibleForDebug
    public Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories() {
        return this.memories;
    }

    public <U> boolean isMemoryValue(MemoryModuleType<U> memoryModuleTypeU0, U u1) {
        return !this.hasMemoryValue(memoryModuleTypeU0) ? false : this.getMemory(memoryModuleTypeU0).filter(p_21922_ -> p_21922_.equals(u1)).isPresent();
    }

    public boolean checkMemory(MemoryModuleType<?> memoryModuleType0, MemoryStatus memoryStatus1) {
        Optional<? extends ExpirableValue<?>> $$2 = (Optional<? extends ExpirableValue<?>>) this.memories.get(memoryModuleType0);
        return $$2 == null ? false : memoryStatus1 == MemoryStatus.REGISTERED || memoryStatus1 == MemoryStatus.VALUE_PRESENT && $$2.isPresent() || memoryStatus1 == MemoryStatus.VALUE_ABSENT && !$$2.isPresent();
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule0) {
        this.schedule = schedule0;
    }

    public void setCoreActivities(Set<Activity> setActivity0) {
        this.coreActivities = setActivity0;
    }

    @Deprecated
    @VisibleForDebug
    public Set<Activity> getActiveActivities() {
        return this.activeActivities;
    }

    @Deprecated
    @VisibleForDebug
    public List<BehaviorControl<? super E>> getRunningBehaviors() {
        List<BehaviorControl<? super E>> $$0 = new ObjectArrayList();
        for (Map<Activity, Set<BehaviorControl<? super E>>> $$1 : this.availableBehaviorsByPriority.values()) {
            for (Set<BehaviorControl<? super E>> $$2 : $$1.values()) {
                for (BehaviorControl<? super E> $$3 : $$2) {
                    if ($$3.getStatus() == Behavior.Status.RUNNING) {
                        $$0.add($$3);
                    }
                }
            }
        }
        return $$0;
    }

    public void useDefaultActivity() {
        this.setActiveActivity(this.defaultActivity);
    }

    public Optional<Activity> getActiveNonCoreActivity() {
        for (Activity $$0 : this.activeActivities) {
            if (!this.coreActivities.contains($$0)) {
                return Optional.of($$0);
            }
        }
        return Optional.empty();
    }

    public void setActiveActivityIfPossible(Activity activity0) {
        if (this.activityRequirementsAreMet(activity0)) {
            this.setActiveActivity(activity0);
        } else {
            this.useDefaultActivity();
        }
    }

    private void setActiveActivity(Activity activity0) {
        if (!this.isActive(activity0)) {
            this.eraseMemoriesForOtherActivitesThan(activity0);
            this.activeActivities.clear();
            this.activeActivities.addAll(this.coreActivities);
            this.activeActivities.add(activity0);
        }
    }

    private void eraseMemoriesForOtherActivitesThan(Activity activity0) {
        for (Activity $$1 : this.activeActivities) {
            if ($$1 != activity0) {
                Set<MemoryModuleType<?>> $$2 = (Set<MemoryModuleType<?>>) this.activityMemoriesToEraseWhenStopped.get($$1);
                if ($$2 != null) {
                    for (MemoryModuleType<?> $$3 : $$2) {
                        this.eraseMemory($$3);
                    }
                }
            }
        }
    }

    public void updateActivityFromSchedule(long long0, long long1) {
        if (long1 - this.lastScheduleUpdate > 20L) {
            this.lastScheduleUpdate = long1;
            Activity $$2 = this.getSchedule().getActivityAt((int) (long0 % 24000L));
            if (!this.activeActivities.contains($$2)) {
                this.setActiveActivityIfPossible($$2);
            }
        }
    }

    public void setActiveActivityToFirstValid(List<Activity> listActivity0) {
        for (Activity $$1 : listActivity0) {
            if (this.activityRequirementsAreMet($$1)) {
                this.setActiveActivity($$1);
                break;
            }
        }
    }

    public void setDefaultActivity(Activity activity0) {
        this.defaultActivity = activity0;
    }

    public void addActivity(Activity activity0, int int1, ImmutableList<? extends BehaviorControl<? super E>> immutableListExtendsBehaviorControlSuperE2) {
        this.addActivity(activity0, this.createPriorityPairs(int1, immutableListExtendsBehaviorControlSuperE2));
    }

    public void addActivityAndRemoveMemoryWhenStopped(Activity activity0, int int1, ImmutableList<? extends BehaviorControl<? super E>> immutableListExtendsBehaviorControlSuperE2, MemoryModuleType<?> memoryModuleType3) {
        Set<Pair<MemoryModuleType<?>, MemoryStatus>> $$4 = ImmutableSet.of(Pair.of(memoryModuleType3, MemoryStatus.VALUE_PRESENT));
        Set<MemoryModuleType<?>> $$5 = ImmutableSet.of(memoryModuleType3);
        this.addActivityAndRemoveMemoriesWhenStopped(activity0, this.createPriorityPairs(int1, immutableListExtendsBehaviorControlSuperE2), $$4, $$5);
    }

    public void addActivity(Activity activity0, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1) {
        this.addActivityAndRemoveMemoriesWhenStopped(activity0, immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1, ImmutableSet.of(), Sets.newHashSet());
    }

    public void addActivityWithConditions(Activity activity0, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1, Set<Pair<MemoryModuleType<?>, MemoryStatus>> setPairMemoryModuleTypeMemoryStatus2) {
        this.addActivityAndRemoveMemoriesWhenStopped(activity0, immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1, setPairMemoryModuleTypeMemoryStatus2, Sets.newHashSet());
    }

    public void addActivityAndRemoveMemoriesWhenStopped(Activity activity0, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1, Set<Pair<MemoryModuleType<?>, MemoryStatus>> setPairMemoryModuleTypeMemoryStatus2, Set<MemoryModuleType<?>> setMemoryModuleType3) {
        this.activityRequirements.put(activity0, setPairMemoryModuleTypeMemoryStatus2);
        if (!setMemoryModuleType3.isEmpty()) {
            this.activityMemoriesToEraseWhenStopped.put(activity0, setMemoryModuleType3);
        }
        UnmodifiableIterator var5 = immutableListExtendsPairIntegerExtendsBehaviorControlSuperE1.iterator();
        while (var5.hasNext()) {
            Pair<Integer, ? extends BehaviorControl<? super E>> $$4 = (Pair<Integer, ? extends BehaviorControl<? super E>>) var5.next();
            ((Set) ((Map) this.availableBehaviorsByPriority.computeIfAbsent((Integer) $$4.getFirst(), p_21917_ -> Maps.newHashMap())).computeIfAbsent(activity0, p_21972_ -> Sets.newLinkedHashSet())).add((BehaviorControl) $$4.getSecond());
        }
    }

    @VisibleForTesting
    public void removeAllBehaviors() {
        this.availableBehaviorsByPriority.clear();
    }

    public boolean isActive(Activity activity0) {
        return this.activeActivities.contains(activity0);
    }

    public Brain<E> copyWithoutBehaviors() {
        Brain<E> $$0 = new Brain<>(this.memories.keySet(), this.sensors.keySet(), ImmutableList.of(), this.codec);
        for (Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> $$1 : this.memories.entrySet()) {
            MemoryModuleType<?> $$2 = (MemoryModuleType<?>) $$1.getKey();
            if (((Optional) $$1.getValue()).isPresent()) {
                $$0.memories.put($$2, (Optional) $$1.getValue());
            }
        }
        return $$0;
    }

    public void tick(ServerLevel serverLevel0, E e1) {
        this.forgetOutdatedMemories();
        this.tickSensors(serverLevel0, e1);
        this.startEachNonRunningBehavior(serverLevel0, e1);
        this.tickEachRunningBehavior(serverLevel0, e1);
    }

    private void tickSensors(ServerLevel serverLevel0, E e1) {
        for (Sensor<? super E> $$2 : this.sensors.values()) {
            $$2.tick(serverLevel0, e1);
        }
    }

    private void forgetOutdatedMemories() {
        for (Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> $$0 : this.memories.entrySet()) {
            if (((Optional) $$0.getValue()).isPresent()) {
                ExpirableValue<?> $$1 = (ExpirableValue<?>) ((Optional) $$0.getValue()).get();
                if ($$1.hasExpired()) {
                    this.eraseMemory((MemoryModuleType) $$0.getKey());
                }
                $$1.tick();
            }
        }
    }

    public void stopAll(ServerLevel serverLevel0, E e1) {
        long $$2 = e1.m_9236_().getGameTime();
        for (BehaviorControl<? super E> $$3 : this.getRunningBehaviors()) {
            $$3.doStop(serverLevel0, e1, $$2);
        }
    }

    private void startEachNonRunningBehavior(ServerLevel serverLevel0, E e1) {
        long $$2 = serverLevel0.m_46467_();
        for (Map<Activity, Set<BehaviorControl<? super E>>> $$3 : this.availableBehaviorsByPriority.values()) {
            for (Entry<Activity, Set<BehaviorControl<? super E>>> $$4 : $$3.entrySet()) {
                Activity $$5 = (Activity) $$4.getKey();
                if (this.activeActivities.contains($$5)) {
                    for (BehaviorControl<? super E> $$7 : (Set) $$4.getValue()) {
                        if ($$7.getStatus() == Behavior.Status.STOPPED) {
                            $$7.tryStart(serverLevel0, e1, $$2);
                        }
                    }
                }
            }
        }
    }

    private void tickEachRunningBehavior(ServerLevel serverLevel0, E e1) {
        long $$2 = serverLevel0.m_46467_();
        for (BehaviorControl<? super E> $$3 : this.getRunningBehaviors()) {
            $$3.tickOrStop(serverLevel0, e1, $$2);
        }
    }

    private boolean activityRequirementsAreMet(Activity activity0) {
        if (!this.activityRequirements.containsKey(activity0)) {
            return false;
        } else {
            for (Pair<MemoryModuleType<?>, MemoryStatus> $$1 : (Set) this.activityRequirements.get(activity0)) {
                MemoryModuleType<?> $$2 = (MemoryModuleType<?>) $$1.getFirst();
                MemoryStatus $$3 = (MemoryStatus) $$1.getSecond();
                if (!this.checkMemory($$2, $$3)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isEmptyCollection(Object object0) {
        return object0 instanceof Collection && ((Collection) object0).isEmpty();
    }

    ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> createPriorityPairs(int int0, ImmutableList<? extends BehaviorControl<? super E>> immutableListExtendsBehaviorControlSuperE1) {
        int $$2 = int0;
        Builder<Pair<Integer, ? extends BehaviorControl<? super E>>> $$3 = ImmutableList.builder();
        UnmodifiableIterator var5 = immutableListExtendsBehaviorControlSuperE1.iterator();
        while (var5.hasNext()) {
            BehaviorControl<? super E> $$4 = (BehaviorControl<? super E>) var5.next();
            $$3.add(Pair.of($$2++, $$4));
        }
        return $$3.build();
    }

    static final class MemoryValue<U> {

        private final MemoryModuleType<U> type;

        private final Optional<? extends ExpirableValue<U>> value;

        static <U> Brain.MemoryValue<U> createUnchecked(MemoryModuleType<U> memoryModuleTypeU0, Optional<? extends ExpirableValue<?>> optionalExtendsExpirableValue1) {
            return new Brain.MemoryValue<>(memoryModuleTypeU0, (Optional<? extends ExpirableValue<U>>) optionalExtendsExpirableValue1);
        }

        MemoryValue(MemoryModuleType<U> memoryModuleTypeU0, Optional<? extends ExpirableValue<U>> optionalExtendsExpirableValueU1) {
            this.type = memoryModuleTypeU0;
            this.value = optionalExtendsExpirableValueU1;
        }

        void setMemoryInternal(Brain<?> brain0) {
            brain0.setMemoryInternal(this.type, this.value);
        }

        public <T> void serialize(DynamicOps<T> dynamicOpsT0, RecordBuilder<T> recordBuilderT1) {
            this.type.getCodec().ifPresent(p_22053_ -> this.value.ifPresent(p_258258_ -> recordBuilderT1.add(BuiltInRegistries.MEMORY_MODULE_TYPE.m_194605_().encodeStart(dynamicOpsT0, this.type), p_22053_.encodeStart(dynamicOpsT0, p_258258_))));
        }
    }

    public static final class Provider<E extends LivingEntity> {

        private final Collection<? extends MemoryModuleType<?>> memoryTypes;

        private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;

        private final Codec<Brain<E>> codec;

        Provider(Collection<? extends MemoryModuleType<?>> collectionExtendsMemoryModuleType0, Collection<? extends SensorType<? extends Sensor<? super E>>> collectionExtendsSensorTypeExtendsSensorSuperE1) {
            this.memoryTypes = collectionExtendsMemoryModuleType0;
            this.sensorTypes = collectionExtendsSensorTypeExtendsSensorSuperE1;
            this.codec = Brain.codec(collectionExtendsMemoryModuleType0, collectionExtendsSensorTypeExtendsSensorSuperE1);
        }

        public Brain<E> makeBrain(Dynamic<?> dynamic0) {
            return (Brain<E>) this.codec.parse(dynamic0).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain(this.memoryTypes, this.sensorTypes, ImmutableList.of(), () -> this.codec));
        }
    }
}