package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

public class AmphithereAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    protected ItemEntity targetEntity;

    protected final int targetChance;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public AmphithereAITargetItems(Mob creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public AmphithereAITargetItems(Mob creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public AmphithereAITargetItems(Mob creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = item -> item != null && !item.getItem().isEmpty() && item.getItem().is(IafItemTags.HEAL_AMPITHERE);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.f_26135_.m_217043_().nextInt(this.targetChance) != 0) {
            return false;
        } else if (!((EntityAmphithere) this.f_26135_).canMove()) {
            this.list = IAFMath.emptyItemEntityList;
            return false;
        } else if (this.targetEntitySelector.test(this.targetEntity)) {
            return true;
        } else {
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
    public void stop() {
        this.targetEntity = null;
        super.stop();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity == null || !this.targetEntity.m_6084_()) {
            this.stop();
        }
        if (this.targetEntity != null && this.targetEntity.m_6084_() && this.f_26135_.m_20280_(this.targetEntity) < 1.0) {
            EntityAmphithere hippo = (EntityAmphithere) this.f_26135_;
            this.targetEntity.getItem().shrink(1);
            this.f_26135_.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            hippo.m_5634_(5.0F);
            this.stop();
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