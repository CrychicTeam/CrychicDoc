package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AquaticAIGetOutOfWater extends Goal {

    private final Mob creature;

    private final double movementSpeed;

    private final Level world;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    public AquaticAIGetOutOfWater(Mob theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.creature.m_20069_() && ((EntitySiren) this.creature).wantsToSing()) {
            Vec3 Vector3d = this.findPossibleShelter();
            if (Vector3d == null) {
                return false;
            } else {
                this.shelterX = Vector3d.x;
                this.shelterY = Vector3d.y;
                this.shelterZ = Vector3d.z;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3 findPossibleShelter() {
        RandomSource random = this.creature.m_217043_();
        BlockPos blockpos = BlockPos.containing((double) this.creature.m_146903_(), this.creature.m_20191_().minY, (double) this.creature.m_146907_());
        for (int i = 0; i < 10; i++) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (this.world.getBlockState(blockpos1).m_60804_(this.world, blockpos1)) {
                return new Vec3((double) blockpos1.m_123341_(), (double) blockpos1.m_123342_(), (double) blockpos1.m_123343_());
            }
        }
        return null;
    }
}