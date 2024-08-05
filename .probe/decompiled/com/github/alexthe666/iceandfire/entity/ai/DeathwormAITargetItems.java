package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

public class DeathwormAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super ItemEntity> targetEntitySelector;

    protected final int targetChance;

    private final EntityDeathWorm worm;

    protected ItemEntity targetEntity;

    private final List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.worm = creature;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && item.getItem().getItem() == Blocks.TNT.asItem() && item.m_9236_().getBlockState(item.m_20183_().below()).m_204336_(BlockTags.SAND);
            }
        };
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.f_26135_.m_217043_().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            List<ItemEntity> list = this.f_26135_.m_9236_().m_6443_(ItemEntity.class, this.getTargetableArea(this.m_7623_()), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            } else {
                list.sort(this.theNearestAttackableTargetSorter);
                this.targetEntity = (ItemEntity) list.get(0);
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
    public boolean canContinueToUse() {
        Entity itemTarget = this.targetEntity;
        if (itemTarget == null) {
            return false;
        } else if (!itemTarget.isAlive()) {
            return false;
        } else {
            Team team = this.f_26135_.m_5647_();
            Team team1 = itemTarget.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.m_7623_();
                return !(this.f_26135_.m_20280_(itemTarget) > d0 * d0);
            }
        }
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.targetEntity != null && this.targetEntity.m_6084_()) {
            if (this.f_26135_.m_20280_(this.targetEntity) < 1.0) {
                EntityDeathWorm deathWorm = (EntityDeathWorm) this.f_26135_;
                this.targetEntity.getItem().shrink(1);
                this.f_26135_.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                deathWorm.setAnimation(EntityDeathWorm.ANIMATION_BITE);
                Player thrower = null;
                if (this.targetEntity.getOwner() != null) {
                    thrower = this.targetEntity.m_9236_().m_46003_(this.targetEntity.getOwner().getUUID());
                }
                deathWorm.setExplosive(true, thrower);
                this.m_8041_();
            }
        } else {
            this.m_8041_();
        }
        if (this.worm.m_21573_().isDone()) {
            this.worm.m_21573_().moveTo(this.targetEntity, 1.0);
        }
    }
}