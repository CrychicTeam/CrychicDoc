package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FleeSunGoal extends Goal {

    protected final PathfinderMob mob;

    private double wantedX;

    private double wantedY;

    private double wantedZ;

    private final double speedModifier;

    private final Level level;

    public FleeSunGoal(PathfinderMob pathfinderMob0, double double1) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.level = pathfinderMob0.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_5448_() != null) {
            return false;
        } else if (!this.level.isDay()) {
            return false;
        } else if (!this.mob.m_6060_()) {
            return false;
        } else if (!this.level.m_45527_(this.mob.m_20183_())) {
            return false;
        } else {
            return !this.mob.m_6844_(EquipmentSlot.HEAD).isEmpty() ? false : this.setWantedPos();
        }
    }

    protected boolean setWantedPos() {
        Vec3 $$0 = this.getHidePos();
        if ($$0 == null) {
            return false;
        } else {
            this.wantedX = $$0.x;
            this.wantedY = $$0.y;
            this.wantedZ = $$0.z;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Nullable
    protected Vec3 getHidePos() {
        RandomSource $$0 = this.mob.m_217043_();
        BlockPos $$1 = this.mob.m_20183_();
        for (int $$2 = 0; $$2 < 10; $$2++) {
            BlockPos $$3 = $$1.offset($$0.nextInt(20) - 10, $$0.nextInt(6) - 3, $$0.nextInt(20) - 10);
            if (!this.level.m_45527_($$3) && this.mob.getWalkTargetValue($$3) < 0.0F) {
                return Vec3.atBottomCenterOf($$3);
            }
        }
        return null;
    }
}