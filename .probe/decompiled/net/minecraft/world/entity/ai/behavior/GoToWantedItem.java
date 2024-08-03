package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.kinds.K1;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;

public class GoToWantedItem {

    public static BehaviorControl<LivingEntity> create(float float0, boolean boolean1, int int2) {
        return create(p_23158_ -> true, float0, boolean1, int2);
    }

    public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> predicateE0, float float1, boolean boolean2, int int3) {
        return BehaviorBuilder.create(p_258371_ -> {
            BehaviorBuilder<E, ? extends MemoryAccessor<? extends K1, WalkTarget>> $$5 = boolean2 ? p_258371_.registered(MemoryModuleType.WALK_TARGET) : p_258371_.absent(MemoryModuleType.WALK_TARGET);
            return p_258371_.group(p_258371_.registered(MemoryModuleType.LOOK_TARGET), $$5, p_258371_.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), p_258371_.registered(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)).apply(p_258371_, (p_258387_, p_258388_, p_258389_, p_258390_) -> (p_258380_, p_258381_, p_258382_) -> {
                ItemEntity $$11 = p_258371_.get(p_258389_);
                if (p_258371_.tryGet(p_258390_).isEmpty() && predicateE0.test(p_258381_) && $$11.m_19950_(p_258381_, (double) int3) && p_258381_.m_9236_().getWorldBorder().isWithinBounds($$11.m_20183_())) {
                    WalkTarget $$12 = new WalkTarget(new EntityTracker($$11, false), float1, 0);
                    p_258387_.set(new EntityTracker($$11, true));
                    p_258388_.set($$12);
                    return true;
                } else {
                    return false;
                }
            });
        });
    }
}