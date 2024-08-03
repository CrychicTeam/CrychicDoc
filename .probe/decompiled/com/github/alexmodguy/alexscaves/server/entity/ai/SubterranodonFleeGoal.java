package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

public class SubterranodonFleeGoal extends Goal {

    private SubterranodonEntity subterranodon;

    public SubterranodonFleeGoal(SubterranodonEntity subterranodon) {
        this.subterranodon = subterranodon;
    }

    @Override
    public boolean canUse() {
        if (!this.subterranodon.isFlying() && !this.subterranodon.isDancing() && !this.subterranodon.m_20160_() && !this.subterranodon.m_21825_()) {
            long worldTime = this.subterranodon.m_9236_().getGameTime() % 10L;
            if (this.subterranodon.m_217043_().nextInt(10) != 0 && worldTime != 0L) {
                return false;
            } else {
                AABB aabb = this.subterranodon.m_20191_().inflate(7.0);
                List<Entity> list = this.subterranodon.m_9236_().m_6443_(Entity.class, aabb, entity -> entity.getType().is(ACTagRegistry.SUBTERRANODON_FLEES));
                return !list.isEmpty();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        this.subterranodon.setFlying(true);
        this.subterranodon.setHovering(true);
    }
}