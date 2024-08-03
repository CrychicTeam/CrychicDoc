package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

public class MyrmexAIForageForItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    public EntityMyrmexWorker myrmex;

    protected ItemEntity targetEntity;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public MyrmexAIForageForItems(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && !item.m_20069_();
            }
        };
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.canMove() && !this.myrmex.holdingSomething() && !this.myrmex.shouldEnterHive() && this.myrmex.keepSearching && this.myrmex.m_5448_() == null) {
            if (this.myrmex.m_9236_().getGameTime() % 4L == 0L) {
                this.list = this.f_26135_.m_9236_().m_6443_(ItemEntity.class, this.getTargetableArea(32.0), this.targetEntitySelector);
            }
            if (this.list.isEmpty()) {
                return false;
            } else {
                this.list.sort(this.theNearestAttackableTargetSorter);
                this.targetEntity = (ItemEntity) this.list.get(0);
                return true;
            }
        } else {
            this.list = IAFMath.emptyItemEntityList;
            return false;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.f_26135_.m_20191_().inflate(targetDistance, 5.0, targetDistance);
    }

    @Override
    public void start() {
        this.f_26135_.getNavigation().moveTo(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
        super.start();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && this.targetEntity.m_6084_() && !this.targetEntity.m_20069_()) {
            if (this.f_26135_.m_20280_(this.targetEntity) < 8.0) {
                this.myrmex.onPickupItem(this.targetEntity);
                this.myrmex.m_21008_(InteractionHand.MAIN_HAND, this.targetEntity.getItem());
                this.targetEntity.m_142687_(Entity.RemovalReason.DISCARDED);
                this.stop();
            }
        } else {
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.myrmex.m_21573_().stop();
        super.stop();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.f_26135_.getNavigation().isDone() && this.myrmex.m_5448_() == null;
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}