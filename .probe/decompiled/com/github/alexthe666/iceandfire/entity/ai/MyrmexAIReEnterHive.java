package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

public class MyrmexAIReEnterHive extends Goal {

    private final EntityMyrmexBase myrmex;

    private final double movementSpeed;

    private PathResult path;

    private BlockPos currentTarget = BlockPos.ZERO;

    private MyrmexAIReEnterHive.Phases currentPhase = MyrmexAIReEnterHive.Phases.GOTOENTRANCE;

    private MyrmexHive hive;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.canMove() && !this.myrmex.shouldLeaveHive() && this.myrmex.shouldEnterHive() && this.currentPhase == MyrmexAIReEnterHive.Phases.GOTOENTRANCE) {
            MyrmexHive village = this.myrmex.getHive();
            if (village == null) {
                village = MyrmexWorldData.get(this.myrmex.m_9236_()).getNearestHive(this.myrmex.m_20183_(), 500);
            }
            if (!(this.myrmex.m_21573_() instanceof AdvancedPathNavigate) || this.myrmex.m_20159_()) {
                return false;
            } else if (village != null && !this.myrmex.isInHive()) {
                this.hive = village;
                this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), this.hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.m_217043_(), false));
                this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.currentTarget.m_123341_(), (double) this.currentTarget.m_123342_(), (double) this.currentTarget.m_123343_(), 1.0);
                this.currentPhase = MyrmexAIReEnterHive.Phases.GOTOENTRANCE;
                return this.path != null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.currentPhase == MyrmexAIReEnterHive.Phases.GOTOENTRANCE && !this.myrmex.pathReachesTarget(this.path, this.currentTarget, 12.0)) {
            this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), this.hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.m_217043_(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.currentTarget.m_123341_(), (double) this.currentTarget.m_123342_(), (double) this.currentTarget.m_123343_(), this.movementSpeed);
        }
        if (this.currentPhase == MyrmexAIReEnterHive.Phases.GOTOENTRANCE && this.myrmex.isCloseEnoughToTarget(this.currentTarget, 12.0) && this.hive != null) {
            this.currentTarget = this.hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.m_217043_());
            this.currentPhase = MyrmexAIReEnterHive.Phases.GOTOEXIT;
            this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.currentTarget.m_123341_(), (double) this.currentTarget.m_123342_(), (double) this.currentTarget.m_123343_(), 1.0);
        }
        if (this.currentPhase == MyrmexAIReEnterHive.Phases.GOTOEXIT && this.myrmex.isCloseEnoughToTarget(this.currentTarget, 12.0) && this.hive != null) {
            this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), this.hive.getCenter());
            this.currentPhase = MyrmexAIReEnterHive.Phases.GOTOCENTER;
            this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.currentTarget.m_123341_(), (double) this.currentTarget.m_123342_(), (double) this.currentTarget.m_123343_(), 1.0);
        }
        this.myrmex.isEnteringHive = !this.myrmex.isCloseEnoughToTarget(this.currentTarget, 14.0) && this.currentPhase != MyrmexAIReEnterHive.Phases.GOTOCENTER;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.isCloseEnoughToTarget(this.currentTarget, 9.0) || this.currentPhase == MyrmexAIReEnterHive.Phases.GOTOCENTER;
    }

    @Override
    public void stop() {
        this.currentTarget = BlockPos.ZERO;
        this.currentPhase = MyrmexAIReEnterHive.Phases.GOTOENTRANCE;
    }

    private static enum Phases {

        GOTOENTRANCE, GOTOEXIT, GOTOCENTER
    }
}