package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;

public class TriggerGate {

    public static <E extends LivingEntity> OneShot<E> triggerOneShuffled(List<Pair<? extends Trigger<? super E>, Integer>> listPairExtendsTriggerSuperEInteger0) {
        return triggerGate(listPairExtendsTriggerSuperEInteger0, GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE);
    }

    public static <E extends LivingEntity> OneShot<E> triggerGate(List<Pair<? extends Trigger<? super E>, Integer>> listPairExtendsTriggerSuperEInteger0, GateBehavior.OrderPolicy gateBehaviorOrderPolicy1, GateBehavior.RunningPolicy gateBehaviorRunningPolicy2) {
        ShufflingList<Trigger<? super E>> $$3 = new ShufflingList<>();
        listPairExtendsTriggerSuperEInteger0.forEach(p_260333_ -> $$3.add((Trigger<? super E>) p_260333_.getFirst(), (Integer) p_260333_.getSecond()));
        return BehaviorBuilder.create(p_259457_ -> p_259457_.point((p_260107_, p_259505_, p_259999_) -> {
            if (gateBehaviorOrderPolicy1 == GateBehavior.OrderPolicy.SHUFFLED) {
                $$3.shuffle();
            }
            for (Trigger<? super E> $$6 : $$3) {
                if ($$6.trigger(p_260107_, (E) p_259505_, p_259999_) && gateBehaviorRunningPolicy2 == GateBehavior.RunningPolicy.RUN_ONE) {
                    break;
                }
            }
            return true;
        }));
    }
}