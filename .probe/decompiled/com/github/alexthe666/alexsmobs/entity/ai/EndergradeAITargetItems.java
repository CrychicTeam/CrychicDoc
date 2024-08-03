package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityEndergrade;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EndergradeAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final EndergradeAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    protected int executionChance;

    protected boolean mustUpdate;

    protected ItemEntity targetEntity;

    private EntityEndergrade endergrade;

    public EndergradeAITargetItems(EntityEndergrade creature, boolean checkSight) {
        this(creature, checkSight, false);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    public EndergradeAITargetItems(EntityEndergrade creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public EndergradeAITargetItems(EntityEndergrade creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.executionChance = chance;
        this.endergrade = creature;
        this.theNearestAttackableTargetSorter = new EndergradeAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            public boolean apply(@Nullable ItemEntity item) {
                ItemStack stack = item.getItem();
                return !stack.isEmpty() && EndergradeAITargetItems.this.endergrade.canTargetItem(stack);
            }
        };
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.f_26135_.m_20159_() && (!this.f_26135_.m_20160_() || this.f_26135_.getControllingPassenger() == null)) {
            if (!this.f_26135_.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
                return false;
            } else {
                if (!this.mustUpdate) {
                    long worldTime = this.f_26135_.m_9236_().getGameTime() % 10L;
                    if (this.f_26135_.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (this.f_26135_.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<ItemEntity> list = this.f_26135_.m_9236_().m_6443_(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (ItemEntity) list.get(0);
                    this.endergrade.stopWandering = true;
                    this.endergrade.hasItemTarget = true;
                    this.mustUpdate = false;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    protected double getFollowDistance() {
        return 16.0;
    }

    protected AABB getTargetableArea(double targetDistance) {
        Vec3 renderCenter = new Vec3(this.f_26135_.m_20185_() + 0.5, this.f_26135_.m_20186_() + 0.5, this.f_26135_.m_20189_() + 0.5);
        double renderRadius = 9.0;
        AABB aabb = new AABB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
        return aabb.move(renderCenter);
    }

    @Override
    public void start() {
        this.f_26135_.getMoveControl().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
        super.start();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && (this.targetEntity == null || this.targetEntity.m_6084_())) {
            this.f_26135_.getMoveControl().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
        } else {
            this.stop();
        }
        if (this.targetEntity != null && this.targetEntity.m_6084_() && this.f_26135_.m_20280_(this.targetEntity) < 2.0 && this.f_26135_.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            ItemStack duplicate = this.targetEntity.getItem().copy();
            this.endergrade.bite();
            duplicate.setCount(1);
            if (!this.f_26135_.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.f_26135_.m_9236_().isClientSide) {
                this.f_26135_.m_5552_(this.f_26135_.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            }
            this.f_26135_.m_21008_(InteractionHand.MAIN_HAND, duplicate);
            this.endergrade.onGetItem(this.targetEntity);
            this.targetEntity.getItem().shrink(1);
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.targetEntity = null;
        this.endergrade.hasItemTarget = false;
        this.endergrade.stopWandering = false;
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_26135_.getMoveControl().hasWanted();
    }

    public static record Sorter(Entity theEntity) implements Comparator<Entity> {

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}