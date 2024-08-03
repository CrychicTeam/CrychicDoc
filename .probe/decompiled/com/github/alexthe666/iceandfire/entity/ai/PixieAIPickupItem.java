package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

public class PixieAIPickupItem<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    protected ItemEntity targetEntity;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public PixieAIPickupItem(EntityPixie creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public PixieAIPickupItem(EntityPixie creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public PixieAIPickupItem(final EntityPixie creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && (item.getItem().getItem() == Items.CAKE && !creature.m_21824_() || item.getItem().getItem() == Items.SUGAR && creature.m_21824_() && creature.m_21223_() < creature.m_21233_());
            }
        };
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        EntityPixie pixie = (EntityPixie) this.f_26135_;
        if (pixie.isPixieSitting()) {
            return false;
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
        this.f_26135_.getMoveControl().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 0.25);
        LivingEntity attackTarget = this.f_26135_.getTarget();
        if (attackTarget == null) {
            this.f_26135_.getLookControl().setLookAt(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 180.0F, 20.0F);
        }
        super.start();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && this.targetEntity.m_6084_()) {
            if (this.f_26135_.m_20280_(this.targetEntity) < 1.0) {
                EntityPixie pixie = (EntityPixie) this.f_26135_;
                if (this.targetEntity.getItem() != null && this.targetEntity.getItem().getItem() != null) {
                    if (this.targetEntity.getItem().is(IafItemTags.HEAL_PIXIE)) {
                        pixie.m_5634_(5.0F);
                    } else if (this.targetEntity.getItem().is(IafItemTags.TAME_PIXIE) && !pixie.m_21824_() && this.targetEntity.getOwner() instanceof Player player) {
                        pixie.m_21828_(player);
                        pixie.setPixieSitting(true);
                        pixie.m_6853_(true);
                    }
                }
                pixie.m_21008_(InteractionHand.MAIN_HAND, this.targetEntity.getItem());
                this.targetEntity.getItem().shrink(1);
                pixie.m_5496_(IafSoundRegistry.PIXIE_TAUNT, 1.0F, 1.0F);
                this.m_8041_();
            }
        } else {
            this.m_8041_();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return true;
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