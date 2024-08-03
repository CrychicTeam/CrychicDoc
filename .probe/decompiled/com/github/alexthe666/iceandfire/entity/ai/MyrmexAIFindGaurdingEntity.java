package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

public class MyrmexAIFindGaurdingEntity<T extends EntityMyrmexBase> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super EntityMyrmexBase> targetEntitySelector;

    public EntityMyrmexSoldier myrmex;

    protected EntityMyrmexBase targetEntity;

    public MyrmexAIFindGaurdingEntity(EntityMyrmexSoldier myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<EntityMyrmexBase>() {

            public boolean test(EntityMyrmexBase myrmex) {
                return !(myrmex instanceof EntityMyrmexSoldier) && myrmex.getGrowthStage() > 1 && EntityMyrmexBase.haveSameHive(MyrmexAIFindGaurdingEntity.this.myrmex, myrmex) && !myrmex.isBeingGuarded && myrmex.needsGaurding();
            }
        };
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.guardingEntity == null) {
            List<EntityMyrmexBase> list = this.f_26135_.m_9236_().m_6443_(EntityMyrmexBase.class, this.getTargetableArea(this.m_7623_()), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            } else {
                list.sort(this.theNearestAttackableTargetSorter);
                this.myrmex.guardingEntity = (EntityMyrmexBase) list.get(0);
                return true;
            }
        } else {
            return false;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.f_26135_.m_20191_().inflate(targetDistance, 4.0, targetDistance);
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