package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;

public class VesperTargetUnderneathEntities<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private VesperEntity entity;

    private final float flyingRange;

    public VesperTargetUnderneathEntities(VesperEntity mob, float flyingRange, Class<T> clzz) {
        super(mob, clzz, 10, true, true, null);
        this.entity = mob;
        this.flyingRange = flyingRange;
    }

    @Override
    protected AABB getTargetSearchArea(double distance) {
        if (this.entity.isHanging()) {
            AABB aabb = this.entity.m_20191_();
            double newDistance = 2.0;
            return new AABB(aabb.minX - newDistance, (double) (this.entity.m_9236_().m_141937_() - 5), aabb.minZ - newDistance, aabb.maxX + newDistance, aabb.maxY + 1.0, aabb.maxZ + newDistance);
        } else {
            return this.entity.m_20191_().inflate((double) this.flyingRange, (double) this.flyingRange, (double) this.flyingRange);
        }
    }
}