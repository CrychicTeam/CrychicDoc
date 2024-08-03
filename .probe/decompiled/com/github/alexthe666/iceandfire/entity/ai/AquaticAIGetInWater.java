package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AquaticAIGetInWater extends Goal {

    private final Mob creature;

    private final double movementSpeed;

    private final Level world;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    public AquaticAIGetInWater(Mob theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    protected boolean isAttackerInWater() {
        return this.creature.getTarget() != null && !this.creature.getTarget().m_20069_();
    }

    @Override
    public boolean canUse() {
        if (!this.creature.m_20160_() && (!(this.creature instanceof TamableAnimal) || !((TamableAnimal) this.creature).isTame()) && !this.creature.m_20069_() && !this.isAttackerInWater() && (!(this.creature instanceof EntitySiren) || !((EntitySiren) this.creature).isSinging() && !((EntitySiren) this.creature).wantsToSing())) {
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
    public Vec3 findPossibleShelter() {
        return this.findPossibleShelter(10, 3);
    }

    @Nullable
    protected Vec3 findPossibleShelter(int xz, int y) {
        RandomSource random = this.creature.m_217043_();
        BlockPos blockpos = BlockPos.containing((double) this.creature.m_146903_(), this.creature.m_20191_().minY, (double) this.creature.m_146907_());
        for (int i = 0; i < 10; i++) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(xz * 2) - xz, random.nextInt(y * 2) - y, random.nextInt(xz * 2) - xz);
            if (this.world.getBlockState(blockpos1).m_60713_(Blocks.WATER)) {
                return new Vec3((double) blockpos1.m_123341_(), (double) blockpos1.m_123342_(), (double) blockpos1.m_123343_());
            }
        }
        return null;
    }
}