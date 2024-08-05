package net.minecraft.world.entity.ai.goal.target;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raider;

public class NearestAttackableWitchTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private boolean canAttack = true;

    public NearestAttackableWitchTargetGoal(Raider raider0, Class<T> classT1, int int2, boolean boolean3, boolean boolean4, @Nullable Predicate<LivingEntity> predicateLivingEntity5) {
        super(raider0, classT1, int2, boolean3, boolean4, predicateLivingEntity5);
    }

    public void setCanAttack(boolean boolean0) {
        this.canAttack = boolean0;
    }

    @Override
    public boolean canUse() {
        return this.canAttack && super.canUse();
    }
}