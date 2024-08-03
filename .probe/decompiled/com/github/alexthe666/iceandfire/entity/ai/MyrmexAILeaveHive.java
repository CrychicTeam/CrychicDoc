package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;

public class MyrmexAILeaveHive extends Goal {

    private final EntityMyrmexBase myrmex;

    private final double movementSpeed;

    private PathResult path;

    private BlockPos nextEntrance = BlockPos.ZERO;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex instanceof EntityMyrmexQueen) {
            return false;
        } else if (!(this.myrmex.m_21573_() instanceof AdvancedPathNavigate) || this.myrmex.m_20159_()) {
            return false;
        } else if (this.myrmex.isBaby()) {
            return false;
        } else if (this.myrmex.canMove() && this.myrmex.shouldLeaveHive() && !this.myrmex.shouldEnterHive() && this.myrmex.isInHive() && (!(this.myrmex instanceof EntityMyrmexWorker) || !((EntityMyrmexWorker) this.myrmex).holdingSomething() && this.myrmex.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) && !this.myrmex.isEnteringHive) {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.m_9236_()).getNearestHive(this.myrmex.m_20183_(), 1000);
            if (village == null) {
                return false;
            } else {
                this.nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.m_217043_(), true));
                this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.nextEntrance.m_123341_(), (double) this.nextEntrance.m_123342_(), (double) this.nextEntrance.m_123343_(), this.movementSpeed);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.isCloseEnoughToTarget(this.nextEntrance, 12.0) && !this.myrmex.shouldEnterHive() ? this.myrmex.shouldLeaveHive() : false;
    }

    @Override
    public void tick() {
        if (!this.myrmex.pathReachesTarget(this.path, this.nextEntrance, 12.0)) {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.m_9236_()).getNearestHive(this.myrmex.m_20183_(), 1000);
            this.nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.m_217043_(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.nextEntrance.m_123341_(), (double) (this.nextEntrance.m_123342_() + 1), (double) this.nextEntrance.m_123343_(), this.movementSpeed);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        this.nextEntrance = BlockPos.ZERO;
        this.myrmex.m_21573_().stop();
    }
}