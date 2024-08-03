package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

@Deprecated
public class SetEntityLookTargetSometimes {

    public static BehaviorControl<LivingEntity> create(float float0, UniformInt uniformInt1) {
        return create(float0, uniformInt1, p_259715_ -> true);
    }

    public static BehaviorControl<LivingEntity> create(EntityType<?> entityType0, float float1, UniformInt uniformInt2) {
        return create(float1, uniformInt2, p_289379_ -> entityType0.equals(p_289379_.m_6095_()));
    }

    private static BehaviorControl<LivingEntity> create(float float0, UniformInt uniformInt1, Predicate<LivingEntity> predicateLivingEntity2) {
        float $$3 = float0 * float0;
        SetEntityLookTargetSometimes.Ticker $$4 = new SetEntityLookTargetSometimes.Ticker(uniformInt1);
        return BehaviorBuilder.create(p_259288_ -> p_259288_.group(p_259288_.absent(MemoryModuleType.LOOK_TARGET), p_259288_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_259288_, (p_259350_, p_260134_) -> (p_264952_, p_264953_, p_264954_) -> {
            Optional<LivingEntity> $$9 = p_259288_.<NearestVisibleLivingEntities>get(p_260134_).findClosest(predicateLivingEntity2.and(p_259358_ -> p_259358_.m_20280_(p_264953_) <= (double) $$3));
            if ($$9.isEmpty()) {
                return false;
            } else if (!$$4.tickDownAndCheck(p_264952_.f_46441_)) {
                return false;
            } else {
                p_259350_.set(new EntityTracker((Entity) $$9.get(), true));
                return true;
            }
        }));
    }

    public static final class Ticker {

        private final UniformInt interval;

        private int ticksUntilNextStart;

        public Ticker(UniformInt uniformInt0) {
            if (uniformInt0.getMinValue() <= 1) {
                throw new IllegalArgumentException();
            } else {
                this.interval = uniformInt0;
            }
        }

        public boolean tickDownAndCheck(RandomSource randomSource0) {
            if (this.ticksUntilNextStart == 0) {
                this.ticksUntilNextStart = this.interval.sample(randomSource0) - 1;
                return false;
            } else {
                return --this.ticksUntilNextStart == 0;
            }
        }
    }
}