package net.minecraft.world.entity.ai.goal.target;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class NonTameRandomTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final TamableAnimal tamableMob;

    public NonTameRandomTargetGoal(TamableAnimal tamableAnimal0, Class<T> classT1, boolean boolean2, @Nullable Predicate<LivingEntity> predicateLivingEntity3) {
        super(tamableAnimal0, classT1, 10, boolean2, false, predicateLivingEntity3);
        this.tamableMob = tamableAnimal0;
    }

    @Override
    public boolean canUse() {
        return !this.tamableMob.isTame() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_26051_ != null ? this.f_26051_.test(this.f_26135_, this.f_26050_) : super.m_8045_();
    }
}