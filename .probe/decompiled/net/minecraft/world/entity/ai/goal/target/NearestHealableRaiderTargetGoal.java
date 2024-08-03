package net.minecraft.world.entity.ai.goal.target;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raider;

public class NearestHealableRaiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private static final int DEFAULT_COOLDOWN = 200;

    private int cooldown = 0;

    public NearestHealableRaiderTargetGoal(Raider raider0, Class<T> classT1, boolean boolean2, @Nullable Predicate<LivingEntity> predicateLivingEntity3) {
        super(raider0, classT1, 500, boolean2, false, predicateLivingEntity3);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void decrementCooldown() {
        this.cooldown--;
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0 || !this.f_26135_.m_217043_().nextBoolean()) {
            return false;
        } else if (!((Raider) this.f_26135_).hasActiveRaid()) {
            return false;
        } else {
            this.m_26073_();
            return this.f_26050_ != null;
        }
    }

    @Override
    public void start() {
        this.cooldown = m_186073_(200);
        super.start();
    }
}