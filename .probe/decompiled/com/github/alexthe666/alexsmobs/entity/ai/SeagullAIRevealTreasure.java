package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySeagull;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class SeagullAIRevealTreasure extends Goal {

    private final EntitySeagull seagull;

    private BlockPos sitPos;

    public SeagullAIRevealTreasure(EntitySeagull entitySeagull) {
        this.seagull = entitySeagull;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return this.seagull.getTreasurePos() != null && this.seagull.treasureSitTime > 0;
    }

    @Override
    public void start() {
        this.seagull.aiItemFlag = true;
        this.sitPos = this.seagull.getSeagullGround(this.seagull.getTreasurePos());
    }

    @Override
    public void stop() {
        this.sitPos = null;
        this.seagull.setSitting(false);
        this.seagull.aiItemFlag = false;
    }

    @Override
    public void tick() {
        if (this.sitPos != null) {
            if (this.seagull.m_20238_(new Vec3((double) ((float) this.sitPos.m_123341_() + 0.5F), this.seagull.m_20186_(), (double) ((float) this.sitPos.m_123343_() + 0.5F))) > 2.5) {
                this.seagull.m_21566_().setWantedPosition((double) ((float) this.sitPos.m_123341_() + 0.5F), (double) (this.sitPos.m_123342_() + 2), (double) ((float) this.sitPos.m_123343_() + 0.5F), 1.0);
                if (!this.seagull.m_20096_()) {
                    this.seagull.setFlying(true);
                }
            } else {
                Vec3 vec = Vec3.upFromBottomCenterOf(this.sitPos, 1.0);
                if (vec.subtract(this.seagull.m_20182_()).length() > 0.04F) {
                    this.seagull.m_20256_(vec.subtract(this.seagull.m_20182_()).scale(0.2F));
                }
                this.seagull.eatItem();
                this.seagull.treasureSitTime = Math.min(this.seagull.treasureSitTime, 100);
                this.seagull.setFlying(false);
                this.seagull.setSitting(true);
            }
        }
    }
}