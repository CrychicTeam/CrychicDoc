package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class CorrodentFearLightGoal extends Goal {

    private CorrodentEntity entity;

    private Vec3 retreatTo = null;

    private int tryDigTime = 0;

    private BlockPos tryDigPos = null;

    public CorrodentFearLightGoal(CorrodentEntity entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.entity.m_9236_().m_45517_(LightLayer.BLOCK, this.entity.m_20183_()) > 7 && !this.entity.isDigging();
    }

    @Override
    public void tick() {
        this.entity.fleeLightFor = 50;
        int light = this.entity.m_9236_().m_45517_(LightLayer.BLOCK, this.entity.m_20183_());
        if (this.retreatTo != null && !(this.entity.m_20238_(this.retreatTo) < 6.0)) {
            this.entity.setAfraid(true);
            this.entity.m_21573_().stop();
            Vec3 flip = this.retreatTo.subtract(this.entity.m_20182_()).yRot((float) (Math.PI / 2)).add(this.entity.m_20182_());
            this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, flip);
            this.entity.m_21566_().strafe(-1.0F, 0.0F);
            if (this.entity.m_20096_() && this.tryDigTime++ > 20) {
                this.tryDigTime = 0;
                if (this.tryDigPos != null && this.tryDigPos.m_123331_(this.entity.m_20183_()) < 2.25) {
                    this.entity.setDigging(true);
                }
                this.tryDigPos = this.entity.m_20183_();
            }
        } else {
            for (int i = 0; i < 15; i++) {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.entity, 30, 15, this.entity.m_20182_());
                if (vec3 != null && this.entity.m_9236_().m_45517_(LightLayer.BLOCK, BlockPos.containing(vec3)) < 7) {
                    this.retreatTo = vec3;
                    break;
                }
            }
        }
    }

    @Override
    public void stop() {
        this.entity.setAfraid(false);
        if (this.entity.m_20096_()) {
            this.entity.fleeLightFor = 50;
            this.entity.setDigging(true);
        }
        this.entity.m_21566_().strafe(0.0F, 0.0F);
        this.tryDigPos = null;
        this.tryDigTime = 0;
    }
}