package net.minecraft.world.entity.ai.goal;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;

public class FollowBoatGoal extends Goal {

    private int timeToRecalcPath;

    private final PathfinderMob mob;

    @Nullable
    private Player following;

    private BoatGoals currentGoal;

    public FollowBoatGoal(PathfinderMob pathfinderMob0) {
        this.mob = pathfinderMob0;
    }

    @Override
    public boolean canUse() {
        List<Boat> $$0 = this.mob.m_9236_().m_45976_(Boat.class, this.mob.m_20191_().inflate(5.0));
        boolean $$1 = false;
        for (Boat $$2 : $$0) {
            Entity $$3 = $$2.getControllingPassenger();
            if ($$3 instanceof Player && (Mth.abs(((Player) $$3).f_20900_) > 0.0F || Mth.abs(((Player) $$3).f_20902_) > 0.0F)) {
                $$1 = true;
                break;
            }
        }
        return this.following != null && (Mth.abs(this.following.f_20900_) > 0.0F || Mth.abs(this.following.f_20902_) > 0.0F) || $$1;
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.following != null && this.following.m_20159_() && (Mth.abs(this.following.f_20900_) > 0.0F || Mth.abs(this.following.f_20902_) > 0.0F);
    }

    @Override
    public void start() {
        for (Boat $$1 : this.mob.m_9236_().m_45976_(Boat.class, this.mob.m_20191_().inflate(5.0))) {
            if ($$1.getControllingPassenger() != null && $$1.getControllingPassenger() instanceof Player) {
                this.following = (Player) $$1.getControllingPassenger();
                break;
            }
        }
        this.timeToRecalcPath = 0;
        this.currentGoal = BoatGoals.GO_TO_BOAT;
    }

    @Override
    public void stop() {
        this.following = null;
    }

    @Override
    public void tick() {
        boolean $$0 = Mth.abs(this.following.f_20900_) > 0.0F || Mth.abs(this.following.f_20902_) > 0.0F;
        float $$1 = this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION ? ($$0 ? 0.01F : 0.0F) : 0.015F;
        this.mob.m_19920_($$1, new Vec3((double) this.mob.f_20900_, (double) this.mob.f_20901_, (double) this.mob.f_20902_));
        this.mob.m_6478_(MoverType.SELF, this.mob.m_20184_());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.m_183277_(10);
            if (this.currentGoal == BoatGoals.GO_TO_BOAT) {
                BlockPos $$2 = this.following.m_20183_().relative(this.following.m_6350_().getOpposite());
                $$2 = $$2.offset(0, -1, 0);
                this.mob.m_21573_().moveTo((double) $$2.m_123341_(), (double) $$2.m_123342_(), (double) $$2.m_123343_(), 1.0);
                if (this.mob.m_20270_(this.following) < 4.0F) {
                    this.timeToRecalcPath = 0;
                    this.currentGoal = BoatGoals.GO_IN_BOAT_DIRECTION;
                }
            } else if (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) {
                Direction $$3 = this.following.m_6374_();
                BlockPos $$4 = this.following.m_20183_().relative($$3, 10);
                this.mob.m_21573_().moveTo((double) $$4.m_123341_(), (double) ($$4.m_123342_() - 1), (double) $$4.m_123343_(), 1.0);
                if (this.mob.m_20270_(this.following) > 12.0F) {
                    this.timeToRecalcPath = 0;
                    this.currentGoal = BoatGoals.GO_TO_BOAT;
                }
            }
        }
    }
}