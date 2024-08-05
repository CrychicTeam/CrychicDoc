package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrollAIFleeSun extends Goal {

    private final EntityTroll troll;

    private final double movementSpeed;

    private final Level world;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    public TrollAIFleeSun(EntityTroll theCreatureIn, double movementSpeedIn) {
        this.troll = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.world.isDay()) {
            return false;
        } else if (!this.world.m_45527_(BlockPos.containing((double) this.troll.m_146903_(), this.troll.m_20191_().minY, (double) this.troll.m_146907_()))) {
            return false;
        } else {
            Vec3 Vector3d = this.findPossibleShelter();
            if (Vector3d == null) {
                return false;
            } else {
                this.shelterX = Vector3d.x;
                this.shelterY = Vector3d.y;
                this.shelterZ = Vector3d.z;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.troll.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.troll.m_21573_().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3 findPossibleShelter() {
        RandomSource random = this.troll.m_217043_();
        BlockPos blockpos = BlockPos.containing((double) this.troll.m_146903_(), this.troll.m_20191_().minY, (double) this.troll.m_146907_());
        for (int i = 0; i < 10; i++) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.world.m_45527_(blockpos1) && this.troll.m_21692_(blockpos1) < 0.0F) {
                return new Vec3((double) blockpos1.m_123341_(), (double) blockpos1.m_123342_(), (double) blockpos1.m_123343_());
            }
        }
        return null;
    }
}