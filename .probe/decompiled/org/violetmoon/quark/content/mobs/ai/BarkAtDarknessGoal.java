package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.content.mobs.entity.Shiba;

public class BarkAtDarknessGoal extends Goal {

    private final Shiba shiba;

    private final PathNavigation navigator;

    public BarkAtDarknessGoal(Shiba shiba) {
        this.shiba = shiba;
        this.navigator = shiba.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void tick() {
        if (this.shiba.currentHyperfocus != null) {
            this.navigator.moveTo(this.navigator.createPath(this.shiba.currentHyperfocus, 1), 1.1);
            if (this.shiba.m_9236_() instanceof ServerLevel slevel && this.shiba.f_19797_ % 10 == 0) {
                Vec3 pos = this.shiba.m_20182_();
                slevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y + 0.5, pos.z, 1, 0.25, 0.1F, 0.25, 0.0);
                this.shiba.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.shiba.currentHyperfocus.m_123341_() + 0.5, (double) this.shiba.currentHyperfocus.m_123342_(), (double) this.shiba.currentHyperfocus.m_123343_() + 0.5));
                this.shiba.m_8032_();
            }
        }
    }

    @Override
    public boolean canUse() {
        return this.shiba.currentHyperfocus != null;
    }
}