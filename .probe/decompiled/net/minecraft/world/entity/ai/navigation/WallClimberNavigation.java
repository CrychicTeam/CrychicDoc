package net.minecraft.world.entity.ai.navigation;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class WallClimberNavigation extends GroundPathNavigation {

    @Nullable
    private BlockPos pathToPosition;

    public WallClimberNavigation(Mob mob0, Level level1) {
        super(mob0, level1);
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
        Path $$2 = this.createPath(entity0, 0);
        if ($$2 != null) {
            return this.m_26536_($$2, double1);
        } else {
            this.pathToPosition = entity0.blockPosition();
            this.f_26497_ = double1;
            return true;
        }
    }

    @Override
    public void tick() {
        if (!this.m_26571_()) {
            super.m_7638_();
        } else {
            if (this.pathToPosition != null) {
                if (!this.pathToPosition.m_203195_(this.f_26494_.m_20182_(), (double) this.f_26494_.m_20205_()) && (!(this.f_26494_.m_20186_() > (double) this.pathToPosition.m_123342_()) || !BlockPos.containing((double) this.pathToPosition.m_123341_(), this.f_26494_.m_20186_(), (double) this.pathToPosition.m_123343_()).m_203195_(this.f_26494_.m_20182_(), (double) this.f_26494_.m_20205_()))) {
                    this.f_26494_.getMoveControl().setWantedPosition((double) this.pathToPosition.m_123341_(), (double) this.pathToPosition.m_123342_(), (double) this.pathToPosition.m_123343_(), this.f_26497_);
                } else {
                    this.pathToPosition = null;
                }
            }
        }
    }
}