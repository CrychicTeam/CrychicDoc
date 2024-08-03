package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

public class MyrmexAIWanderHiveCenter extends Goal {

    private final EntityMyrmexBase myrmex;

    private final double movementSpeed;

    private Path path;

    private BlockPos target = BlockPos.ZERO;

    public MyrmexAIWanderHiveCenter(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.canMove() && (this.myrmex.shouldEnterHive() || this.myrmex.m_21573_().isDone()) && !this.myrmex.canSeeSky()) {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.m_9236_()).getNearestHive(this.myrmex.m_20183_(), 300);
            if (village == null) {
                village = this.myrmex.getHive();
            }
            if (village == null) {
                return false;
            } else {
                this.target = this.getNearPos(MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getCenter()));
                this.path = this.myrmex.m_21573_().createPath(this.target, 0);
                return this.path != null;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.m_21573_().isDone() && this.myrmex.m_20275_((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5) > 3.0 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.m_21573_().moveTo(this.path, this.movementSpeed);
    }

    @Override
    public void stop() {
        this.target = BlockPos.ZERO;
        this.myrmex.m_21573_().moveTo((Path) null, this.movementSpeed);
    }

    public BlockPos getNearPos(BlockPos pos) {
        return pos.offset(this.myrmex.m_217043_().nextInt(8) - 4, 0, this.myrmex.m_217043_().nextInt(8) - 4);
    }
}