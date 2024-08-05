package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

public class DragonAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    private final int targetChance;

    private final boolean prioritizeItems;

    private final boolean isIce;

    protected ItemEntity targetEntity;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public DragonAITargetItems(EntityDragonBase creature, boolean checkSight) {
        this(creature, 20, checkSight, false, false);
    }

    public DragonAITargetItems(EntityDragonBase creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, false);
    }

    public DragonAITargetItems(EntityDragonBase creature, int chance, boolean checkSight, boolean onlyNearby) {
        this(creature, chance, checkSight, onlyNearby, false);
    }

    public DragonAITargetItems(EntityDragonBase creature, int chance, boolean checkSight, boolean onlyNearby, boolean prioritizeItems) {
        super(creature, checkSight, onlyNearby);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        this.isIce = creature instanceof EntityIceDragon;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.targetEntitySelector = item -> item != null && !item.getItem().isEmpty() && FoodUtils.getFoodPoints(item.getItem(), true, this.isIce) > 0;
        this.prioritizeItems = prioritizeItems;
    }

    @Override
    public boolean canUse() {
        EntityDragonBase dragon = (EntityDragonBase) this.f_26135_;
        if (this.prioritizeItems && dragon.getHunger() >= 60) {
            return false;
        } else if (dragon.getHunger() < 100 && dragon.canMove() && (this.targetChance <= 0 || this.f_26135_.m_217043_().nextInt(10) == 0)) {
            return this.updateList();
        } else {
            this.list = IAFMath.emptyItemEntityList;
            return false;
        }
    }

    private boolean updateList() {
        if (this.f_26135_.m_9236_().getGameTime() % 4L == 0L) {
            this.list = this.f_26135_.m_9236_().m_6443_(ItemEntity.class, this.getTargetableArea(this.m_7623_()), this.targetEntitySelector);
        }
        if (this.list.isEmpty()) {
            return false;
        } else {
            this.list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = (ItemEntity) this.list.get(0);
            return true;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.f_26135_.m_20191_().inflate(targetDistance, 4.0, targetDistance);
    }

    @Override
    public void start() {
        this.f_26135_.getNavigation().moveTo(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.0);
        super.start();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && this.targetEntity.m_6084_()) {
            if (this.f_26135_.m_20280_(this.targetEntity) < (double) (this.f_26135_.m_20205_() * 2.0F + this.f_26135_.m_20206_() / 2.0F) || this.f_26135_ instanceof EntityDragonBase dragon && dragon.getHeadPosition().distanceToSqr(this.targetEntity.m_20182_()) < (double) this.f_26135_.m_20206_()) {
                this.f_26135_.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                int hunger = FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, this.isIce);
                EntityDragonBase dragonx = (EntityDragonBase) this.f_26135_;
                dragonx.setHunger(Math.min(100, dragonx.getHunger() + hunger));
                dragonx.eatFoodBonus(this.targetEntity.getItem());
                this.f_26135_.m_21153_(Math.min(this.f_26135_.m_21233_(), (float) ((int) (this.f_26135_.m_21223_() + (float) FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, this.isIce)))));
                if (EntityDragonBase.ANIMATION_EAT != null) {
                    dragonx.setAnimation(EntityDragonBase.ANIMATION_EAT);
                }
                for (int i = 0; i < 4; i++) {
                    dragonx.spawnItemCrackParticles(this.targetEntity.getItem().getItem());
                }
                this.targetEntity.getItem().shrink(1);
                this.m_8041_();
            } else {
                this.updateList();
            }
        } else {
            this.m_8041_();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.f_26135_.getNavigation().isDone();
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