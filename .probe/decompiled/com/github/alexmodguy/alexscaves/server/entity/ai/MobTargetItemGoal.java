package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MobTargetItemGoal<T extends ItemEntity> extends TargetGoal {

    protected final MobTargetItemGoal.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    protected int executionChance;

    protected boolean mustUpdate;

    protected ItemEntity targetEntity;

    protected TargetsDroppedItems hunter;

    private int tickThreshold;

    private float radius = 9.0F;

    private int walkCooldown = 0;

    public MobTargetItemGoal(PathfinderMob creature, boolean checkSight) {
        this(creature, checkSight, false);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    public MobTargetItemGoal(PathfinderMob creature, boolean checkSight, int tickThreshold) {
        this(creature, checkSight, false, tickThreshold, 9);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    public MobTargetItemGoal(PathfinderMob creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null, 0);
    }

    public MobTargetItemGoal(PathfinderMob creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
        this(creature, 10, checkSight, onlyNearby, null, tickThreshold);
        this.radius = (float) radius;
    }

    public MobTargetItemGoal(PathfinderMob creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector, int ticksExisted) {
        super(creature, checkSight, onlyNearby);
        this.executionChance = chance;
        this.tickThreshold = ticksExisted;
        this.hunter = (TargetsDroppedItems) creature;
        this.theNearestAttackableTargetSorter = new MobTargetItemGoal.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            public boolean apply(@Nullable ItemEntity item) {
                ItemStack stack = item.getItem();
                return !stack.isEmpty() && MobTargetItemGoal.this.hunter.canTargetItem(stack) && item.f_19797_ > MobTargetItemGoal.this.tickThreshold;
            }
        };
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.f_26135_.m_20159_() || this.f_26135_.m_20160_() && this.f_26135_.getControllingPassenger() != null) {
            return false;
        } else {
            if (this.f_26135_ instanceof TamableAnimal tamableAnimal && tamableAnimal.isOrderedToSit()) {
                return false;
            }
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
                    this.mustUpdate = false;
                    this.hunter.onFindTarget(this.targetEntity);
                    return true;
                }
            }
        }
    }

    @Override
    protected double getFollowDistance() {
        return 32.0;
    }

    protected AABB getTargetableArea(double targetDistance) {
        Vec3 renderCenter = new Vec3(this.f_26135_.m_20185_() + 0.5, this.f_26135_.m_20186_() + 0.5, this.f_26135_.m_20189_() + 0.5);
        AABB aabb = new AABB((double) (-this.radius), (double) (-this.radius), (double) (-this.radius), (double) this.radius, (double) this.radius, (double) this.radius);
        return aabb.move(renderCenter);
    }

    @Override
    public void start() {
        this.moveTo();
        super.start();
    }

    protected void moveTo() {
        if (this.walkCooldown > 0) {
            this.walkCooldown--;
        } else {
            this.f_26135_.getNavigation().moveTo(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
            this.f_26135_.m_7618_(EntityAnchorArgument.Anchor.EYES, this.targetEntity.m_20182_());
            this.walkCooldown = 30 + this.f_26135_.m_217043_().nextInt(40);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.f_26135_.getNavigation().stop();
        this.targetEntity = null;
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && (this.targetEntity == null || this.targetEntity.m_6084_())) {
            this.moveTo();
        } else {
            this.stop();
            this.f_26135_.getNavigation().stop();
        }
        if (this.targetEntity != null && this.f_26135_.m_142582_(this.targetEntity) && (double) this.f_26135_.m_20205_() > 2.0 && this.f_26135_.m_20096_()) {
            this.f_26135_.getMoveControl().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
        }
        if (this.targetEntity != null && this.targetEntity.m_6084_() && this.f_26135_.m_20280_(this.targetEntity) < this.hunter.getMaxDistToItem() && this.f_26135_.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.hunter.onGetItem(this.targetEntity);
            this.stop();
        }
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    @Override
    public boolean canContinueToUse() {
        boolean path = (double) this.f_26135_.m_20205_() > 2.0 || !this.f_26135_.getNavigation().isDone();
        return path && this.targetEntity != null && this.targetEntity.m_6084_();
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}