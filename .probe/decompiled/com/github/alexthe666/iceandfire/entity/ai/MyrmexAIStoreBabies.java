package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

public class MyrmexAIStoreBabies extends Goal {

    private final EntityMyrmexWorker myrmex;

    private BlockPos nextRoom = BlockPos.ZERO;

    public MyrmexAIStoreBabies(EntityMyrmexWorker entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.canMove() && this.myrmex.holdingBaby() && (this.myrmex.shouldEnterHive() || this.myrmex.m_21573_().isDone()) && !this.myrmex.canSeeSky()) {
            MyrmexHive village = this.myrmex.getHive();
            if (village == null) {
                return false;
            } else {
                this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.myrmex.m_217043_(), this.myrmex.m_20183_())).above();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.myrmex.holdingBaby() && !this.myrmex.m_21573_().isDone() && this.myrmex.m_20275_((double) this.nextRoom.m_123341_() + 0.5, (double) this.nextRoom.m_123342_() + 0.5, (double) this.nextRoom.m_123343_() + 0.5) > 3.0 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.m_21573_().moveTo((double) this.nextRoom.m_123341_(), (double) this.nextRoom.m_123342_(), (double) this.nextRoom.m_123343_(), 1.5);
    }

    @Override
    public void tick() {
        if (this.nextRoom != null && this.myrmex.m_20275_((double) this.nextRoom.m_123341_() + 0.5, (double) this.nextRoom.m_123342_() + 0.5, (double) this.nextRoom.m_123343_() + 0.5) < 4.0 && this.myrmex.holdingBaby() && !this.myrmex.m_20197_().isEmpty()) {
            for (Entity entity : this.myrmex.m_20197_()) {
                entity.stopRiding();
                this.stop();
                entity.copyPosition(this.myrmex);
            }
        }
    }

    @Override
    public void stop() {
        this.nextRoom = BlockPos.ZERO;
        this.myrmex.m_21573_().stop();
    }
}