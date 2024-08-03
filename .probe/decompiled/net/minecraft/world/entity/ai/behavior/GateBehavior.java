package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class GateBehavior<E extends LivingEntity> implements BehaviorControl<E> {

    private final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;

    private final Set<MemoryModuleType<?>> exitErasedMemories;

    private final GateBehavior.OrderPolicy orderPolicy;

    private final GateBehavior.RunningPolicy runningPolicy;

    private final ShufflingList<BehaviorControl<? super E>> behaviors = new ShufflingList<>();

    private Behavior.Status status = Behavior.Status.STOPPED;

    public GateBehavior(Map<MemoryModuleType<?>, MemoryStatus> mapMemoryModuleTypeMemoryStatus0, Set<MemoryModuleType<?>> setMemoryModuleType1, GateBehavior.OrderPolicy gateBehaviorOrderPolicy2, GateBehavior.RunningPolicy gateBehaviorRunningPolicy3, List<Pair<? extends BehaviorControl<? super E>, Integer>> listPairExtendsBehaviorControlSuperEInteger4) {
        this.entryCondition = mapMemoryModuleTypeMemoryStatus0;
        this.exitErasedMemories = setMemoryModuleType1;
        this.orderPolicy = gateBehaviorOrderPolicy2;
        this.runningPolicy = gateBehaviorRunningPolicy3;
        listPairExtendsBehaviorControlSuperEInteger4.forEach(p_258332_ -> this.behaviors.add((BehaviorControl<? super E>) p_258332_.getFirst(), (Integer) p_258332_.getSecond()));
    }

    @Override
    public Behavior.Status getStatus() {
        return this.status;
    }

    private boolean hasRequiredMemories(E e0) {
        for (Entry<MemoryModuleType<?>, MemoryStatus> $$1 : this.entryCondition.entrySet()) {
            MemoryModuleType<?> $$2 = (MemoryModuleType<?>) $$1.getKey();
            MemoryStatus $$3 = (MemoryStatus) $$1.getValue();
            if (!e0.getBrain().checkMemory($$2, $$3)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean tryStart(ServerLevel serverLevel0, E e1, long long2) {
        if (this.hasRequiredMemories(e1)) {
            this.status = Behavior.Status.RUNNING;
            this.orderPolicy.apply(this.behaviors);
            this.runningPolicy.apply(this.behaviors.stream(), serverLevel0, e1, long2);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void tickOrStop(ServerLevel serverLevel0, E e1, long long2) {
        this.behaviors.stream().filter(p_258342_ -> p_258342_.getStatus() == Behavior.Status.RUNNING).forEach(p_258336_ -> p_258336_.tickOrStop(serverLevel0, e1, long2));
        if (this.behaviors.stream().noneMatch(p_258344_ -> p_258344_.getStatus() == Behavior.Status.RUNNING)) {
            this.doStop(serverLevel0, e1, long2);
        }
    }

    @Override
    public final void doStop(ServerLevel serverLevel0, E e1, long long2) {
        this.status = Behavior.Status.STOPPED;
        this.behaviors.stream().filter(p_258337_ -> p_258337_.getStatus() == Behavior.Status.RUNNING).forEach(p_258341_ -> p_258341_.doStop(serverLevel0, e1, long2));
        this.exitErasedMemories.forEach(e1.getBrain()::m_21936_);
    }

    @Override
    public String debugString() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        Set<? extends BehaviorControl<? super E>> $$0 = (Set<? extends BehaviorControl<? super E>>) this.behaviors.stream().filter(p_258343_ -> p_258343_.getStatus() == Behavior.Status.RUNNING).collect(Collectors.toSet());
        return "(" + this.getClass().getSimpleName() + "): " + $$0;
    }

    public static enum OrderPolicy {

        ORDERED(p_147530_ -> {
        }), SHUFFLED(ShufflingList::m_147922_);

        private final Consumer<ShufflingList<?>> consumer;

        private OrderPolicy(Consumer<ShufflingList<?>> p_22930_) {
            this.consumer = p_22930_;
        }

        public void apply(ShufflingList<?> p_147528_) {
            this.consumer.accept(p_147528_);
        }
    }

    public static enum RunningPolicy {

        RUN_ONE {

            @Override
            public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> p_147537_, ServerLevel p_147538_, E p_147539_, long p_147540_) {
                p_147537_.filter(p_258349_ -> p_258349_.getStatus() == Behavior.Status.STOPPED).filter(p_258348_ -> p_258348_.tryStart(p_147538_, p_147539_, p_147540_)).findFirst();
            }
        }
        , TRY_ALL {

            @Override
            public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> p_147542_, ServerLevel p_147543_, E p_147544_, long p_147545_) {
                p_147542_.filter(p_258350_ -> p_258350_.getStatus() == Behavior.Status.STOPPED).forEach(p_258354_ -> p_258354_.tryStart(p_147543_, p_147544_, p_147545_));
            }
        }
        ;

        public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> var1, ServerLevel var2, E var3, long var4);
    }
}