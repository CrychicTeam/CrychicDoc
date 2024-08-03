package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class GloomothFleeMothBallsGoal extends Goal {

    private GloomothEntity gloomoth;

    private BlockPos blockPos;

    private Vec3 retreatTo = null;

    public GloomothFleeMothBallsGoal(GloomothEntity gloomoth) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.gloomoth = gloomoth;
    }

    @Override
    public boolean canUse() {
        long worldTime = this.gloomoth.m_9236_().getGameTime() % 10L;
        if (this.gloomoth.m_217043_().nextInt(20) != 0 && worldTime != 0L) {
            return false;
        } else {
            if (this.gloomoth.m_9236_() instanceof ServerLevel serverLevel) {
                BlockPos pos = this.gloomoth.getNearestMothBall(serverLevel, this.gloomoth.m_20183_(), 20);
                if (pos != null) {
                    this.blockPos = pos;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.blockPos != null && this.gloomoth.m_20238_(this.blockPos.getCenter()) < 32.0;
    }

    @Override
    public void start() {
        this.gloomoth.setFlying(true);
    }

    @Override
    public void stop() {
        this.retreatTo = null;
        this.blockPos = null;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.retreatTo != null && !(this.gloomoth.m_20238_(this.retreatTo) < 4.0)) {
            this.gloomoth.m_21573_().moveTo(this.retreatTo.x, this.retreatTo.y, this.retreatTo.z, 1.0);
        } else {
            for (int i = 0; i < 15; i++) {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.gloomoth, 32, 15, this.blockPos.getCenter());
                if (vec3 != null) {
                    this.retreatTo = vec3;
                    break;
                }
            }
        }
    }
}