package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AnimalAILeapRandomly extends Goal {

    private final PathfinderMob mob;

    private final int chance;

    private final int maxLeapDistance;

    private Vec3 leapToPos = null;

    public AnimalAILeapRandomly(PathfinderMob mob, int chance, int maxLeapDistance) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.mob = mob;
        this.chance = chance;
        this.maxLeapDistance = maxLeapDistance;
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_217043_().nextInt(this.chance) == 0 && this.mob.m_20096_() && this.mob.m_21573_().isDone()) {
            Vec3 found = LandRandomPos.getPos(this.mob, this.maxLeapDistance, this.maxLeapDistance);
            if (found != null && this.mob.m_20238_(found) < (double) (this.maxLeapDistance * this.maxLeapDistance) && this.hasLineOfSightBlock(found)) {
                this.leapToPos = found;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.leapToPos != null && this.mob.m_20238_(this.leapToPos) < (double) (this.maxLeapDistance * this.maxLeapDistance) && this.hasLineOfSightBlock(this.leapToPos);
    }

    private boolean hasLineOfSightBlock(Vec3 blockVec) {
        Vec3 Vector3d = new Vec3(this.mob.m_20185_(), this.mob.m_20188_(), this.mob.m_20189_());
        BlockHitResult result = this.mob.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
        return blockVec.distanceTo(result.m_82450_()) < 1.2F;
    }

    @Override
    public void stop() {
        super.stop();
        this.leapToPos = null;
    }

    @Override
    public void start() {
        if (this.leapToPos != null) {
            Vec3 vector3d = this.mob.m_20184_();
            Vec3 vector3d1 = new Vec3(this.leapToPos.x - this.mob.m_20185_(), 0.0, this.leapToPos.z - this.mob.m_20189_());
            if (vector3d1.lengthSqr() > 1.0E-7) {
                vector3d1 = vector3d1.normalize().scale(0.9).add(vector3d.scale(0.8));
            }
            if (this.mob instanceof EntityBunfungus) {
                ((EntityBunfungus) this.mob).onJump();
            }
            this.mob.m_20334_(vector3d1.x, 0.6F, vector3d1.z);
            this.mob.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
            this.mob.f_20883_ = this.mob.m_146908_();
            this.mob.f_20885_ = this.mob.m_146908_();
            this.leapToPos = null;
        }
    }
}