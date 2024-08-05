package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class StymphalianBirdAITarget extends NearestAttackableTargetGoal<LivingEntity> {

    private final EntityStymphalianBird bird;

    public StymphalianBirdAITarget(EntityStymphalianBird entityIn, Class<LivingEntity> classTarget, boolean checkSight) {
        super(entityIn, classTarget, 0, checkSight, false, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                return !EntityGorgon.isStoneMob(entity) && (entity instanceof Player && !((Player) entity).isCreative() || entity instanceof AbstractVillager || entity instanceof AbstractGolem || entity instanceof Animal && IafConfig.stympahlianBirdAttackAnimals);
            }
        });
        this.bird = entityIn;
    }

    @Override
    public boolean canUse() {
        boolean supe = super.canUse();
        return this.f_26050_ != null && this.bird.getVictor() != null && this.bird.getVictor().m_20148_().equals(this.f_26050_.m_20148_()) ? false : supe && this.f_26050_ != null && !this.f_26050_.getClass().equals(this.bird.getClass());
    }

    @NotNull
    @Override
    protected AABB getTargetSearchArea(double targetDistance) {
        return this.bird.m_20191_().inflate(targetDistance, targetDistance, targetDistance);
    }
}