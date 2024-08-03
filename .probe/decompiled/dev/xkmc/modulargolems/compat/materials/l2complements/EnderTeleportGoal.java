package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class EnderTeleportGoal extends Goal {

    private static final int INIT_CD = 20;

    private static final int SUCCESS_CD = 100;

    private static final int FAIL_CD = 20;

    private static final int DIST = 6;

    private final AbstractGolemEntity<?, ?> entity;

    private int coolDown = 0;

    public EnderTeleportGoal(AbstractGolemEntity<?, ?> entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && !this.entity.m_21573_().isDone() && (this.entity.m_21573_().isStuck() || this.entity.m_20280_(this.entity.m_5448_()) > 36.0);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.coolDown = 20;
    }

    @Override
    public void stop() {
        this.coolDown = 0;
    }

    @Override
    public void tick() {
        if (this.coolDown > 0) {
            this.coolDown--;
        }
        LivingEntity target = this.entity.m_5448_();
        if (target != null) {
            if (EnderTeleportModifier.mayTeleport(this.entity)) {
                if (this.coolDown <= 0) {
                    if (EnderTeleportModifier.teleportTowards(this.entity, target)) {
                        EnderTeleportModifier.resetCooldown(this.entity);
                        this.coolDown = 100;
                    } else {
                        this.coolDown = 20;
                    }
                }
            }
        }
    }
}