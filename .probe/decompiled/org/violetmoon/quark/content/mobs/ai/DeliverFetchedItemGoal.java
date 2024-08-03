package org.violetmoon.quark.content.mobs.ai;

import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import org.violetmoon.quark.content.mobs.entity.Shiba;

public class DeliverFetchedItemGoal extends FollowOwnerGoal {

    private final Shiba shiba;

    private int timeTilNextJump = 20;

    public DeliverFetchedItemGoal(Shiba shiba, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        super(shiba, speed, minDist, maxDist, teleportToLeaves);
        this.shiba = shiba;
    }

    @Override
    public void tick() {
        super.tick();
        this.timeTilNextJump--;
        if (this.timeTilNextJump <= 0) {
            this.timeTilNextJump = this.shiba.m_9236_().random.nextInt(5) + 10;
            if (this.shiba.m_20096_()) {
                this.shiba.m_5997_(0.0, 0.3, 0.0);
                this.shiba.m_6862_(true);
            }
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.shiba.getMouthItem().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.shiba.getMouthItem().isEmpty();
    }
}