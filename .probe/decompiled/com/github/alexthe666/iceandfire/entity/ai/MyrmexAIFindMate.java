package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.util.IAFMath;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

public class MyrmexAIFindMate<T extends EntityMyrmexBase> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super Entity> targetEntitySelector;

    public EntityMyrmexRoyal myrmex;

    protected EntityMyrmexBase targetEntity;

    @Nonnull
    private List<Entity> list = IAFMath.emptyEntityList;

    public MyrmexAIFindMate(EntityMyrmexRoyal myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {

            public boolean test(Entity myrmex) {
                return myrmex instanceof EntityMyrmexRoyal && ((EntityMyrmexRoyal) myrmex).getGrowthStage() >= 2;
            }
        };
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.shouldHaveNormalAI()) {
            this.list = IAFMath.emptyEntityList;
            return false;
        } else if (this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.releaseTicks >= 400 && this.myrmex.mate == null) {
            MyrmexHive village = this.myrmex.getHive();
            if (village == null) {
                village = MyrmexWorldData.get(this.myrmex.m_9236_()).getNearestHive(this.myrmex.m_20183_(), 100);
            }
            if (village != null && village.getCenter().m_203198_(this.myrmex.m_20185_(), (double) village.getCenter().m_123342_(), this.myrmex.m_20189_()) < 2000.0) {
                this.list = IAFMath.emptyEntityList;
                return false;
            } else {
                if (this.myrmex.m_9236_().getGameTime() % 4L == 0L) {
                    this.list = this.f_26135_.m_9236_().getEntities(this.myrmex, this.getTargetableArea(100.0), this.targetEntitySelector);
                }
                if (this.list.isEmpty()) {
                    return false;
                } else {
                    this.list.sort(this.theNearestAttackableTargetSorter);
                    for (Entity royal : this.list) {
                        if (this.myrmex.canMate((EntityMyrmexRoyal) royal)) {
                            this.myrmex.mate = (EntityMyrmexRoyal) royal;
                            this.myrmex.m_9236_().broadcastEntityEvent(this.myrmex, (byte) 76);
                            return true;
                        }
                    }
                    return false;
                }
            }
        } else {
            this.list = IAFMath.emptyEntityList;
            return false;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.f_26135_.m_20191_().inflate(targetDistance, targetDistance / 2.0, targetDistance);
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity theEntity;

        public Sorter(EntityMyrmexBase theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}