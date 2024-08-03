package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DeathWormAIGetInSand extends Goal {

    private final EntityDeathWorm creature;

    private final double movementSpeed;

    private final Level world;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    public DeathWormAIGetInSand(EntityDeathWorm theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.creature.m_20160_() && !this.creature.isInSand() && (this.creature.m_5448_() == null || this.creature.m_5448_().m_20069_()) && this.creature.targetItemsGoal.targetEntity == null) {
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
        return !this.creature.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.creature.m_21573_().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3 findPossibleShelter() {
        RandomSource random = this.creature.m_217043_();
        BlockPos blockpos = BlockPos.containing((double) this.creature.m_146903_(), this.creature.m_20191_().minY, (double) this.creature.m_146907_());
        for (int i = 0; i < 10; i++) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (this.world.getBlockState(blockpos1).m_204336_(BlockTags.SAND)) {
                return new Vec3((double) blockpos1.m_123341_(), (double) blockpos1.m_123342_(), (double) blockpos1.m_123343_());
            }
        }
        return null;
    }
}