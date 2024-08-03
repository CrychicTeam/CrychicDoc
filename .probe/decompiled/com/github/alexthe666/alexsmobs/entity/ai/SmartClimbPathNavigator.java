package com.github.alexthe666.alexsmobs.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class SmartClimbPathNavigator extends GroundPathNavigation {

    @Nullable
    private BlockPos pathToPosition;

    public SmartClimbPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    public Path createPath(BlockPos blockPos0, int int1) {
        this.pathToPosition = blockPos0;
        return super.createPath(blockPos0, int1);
    }

    @Override
    public Path createPath(Entity entity0, int int1) {
        this.pathToPosition = entity0.blockPosition();
        return super.createPath(entity0, int1);
    }

    @Override
    public boolean moveTo(Entity entity0, double double1) {
        Path path = this.createPath(entity0, 0);
        if (path != null) {
            return this.m_26536_(path, double1);
        } else {
            this.pathToPosition = entity0.blockPosition();
            this.f_26497_ = double1;
            return true;
        }
    }

    @Override
    public void tick() {
        super.m_7638_();
        if (!this.m_26571_()) {
            super.m_7638_();
        } else if (this.pathToPosition != null) {
            Vec3 xzOff = new Vec3((double) ((float) this.pathToPosition.m_123341_() + 0.5F) - this.f_26494_.m_20185_(), 0.0, (double) ((float) this.pathToPosition.m_123343_() + 0.5F) - this.f_26494_.m_20189_());
            double dist = xzOff.length();
            if (!(dist < (double) this.f_26494_.m_20205_()) && !(this.f_26494_.m_20186_() > (double) this.pathToPosition.m_123342_())) {
                this.f_26494_.getMoveControl().setWantedPosition((double) this.pathToPosition.m_123341_(), this.f_26494_.m_20186_(), (double) this.pathToPosition.m_123343_(), this.f_26497_);
            } else {
                this.pathToPosition = null;
            }
        }
    }

    @Override
    protected void doStuckDetection(Vec3 vec) {
        if (this.f_26498_ - this.f_26499_ > 40) {
            if (vec.distanceToSqr(new Vec3(this.f_26500_.x, vec.y, this.f_26500_.z)) < 2.25) {
                this.m_26573_();
            }
            this.f_26499_ = this.f_26498_;
            this.f_26500_ = vec;
        }
    }
}