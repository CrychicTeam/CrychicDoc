package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class DeepOneAttackGoal extends Goal {

    private DeepOneBaseEntity deepOne;

    public DeepOneAttackGoal(DeepOneBaseEntity deepOne) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.deepOne = deepOne;
    }

    @Override
    public boolean canUse() {
        return this.deepOne.m_5448_() != null && this.deepOne.m_5448_().isAlive() && !this.deepOne.isTradingLocked();
    }

    @Override
    public void stop() {
        super.stop();
        this.deepOne.setSoundsAngry(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.deepOne.m_5448_();
        if (target != null) {
            this.deepOne.m_21563_().setLookAt(target.m_20185_(), target.m_20188_(), target.m_20189_(), 20.0F, (float) this.deepOne.m_8132_());
            this.deepOne.startAttackBehavior(target);
            this.deepOne.setSoundsAngry(true);
            if (this.deepOne.m_20270_(target) <= 16.0F) {
                SubmarineEntity.alertSubmarineMountOf(target);
            }
        }
    }
}