package com.mna.api.entities.construct.ai;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.phys.Vec3;

public class ConstructFleeTarget extends AvoidEntityGoal<LivingEntity> {

    final IConstruct<?> construct;

    public ConstructFleeTarget(IConstruct<?> construct, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
        super(construct.asEntity(), LivingEntity.class, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier);
        this.construct = construct;
    }

    @Override
    public boolean canUse() {
        if (this.construct.getIntelligence() >= 16 && !this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.BLOCK) && this.construct.getConstructData().isAnyCapabilityEnabled(ConstructCapability.CAST_SPELL, ConstructCapability.RANGED_ATTACK, ConstructCapability.FLUID_DISPENSE)) {
            AbstractGolem constructEntity = this.construct.asEntity();
            this.f_25016_ = constructEntity.m_5448_();
            if (this.f_25016_ == null) {
                return false;
            } else if (!(constructEntity.m_20270_(this.f_25016_) > 4.0F) && !this.construct.isRangedAttacking()) {
                Vec3 vec3 = getPosAway(this.construct, 10, 7, this.f_25016_.m_20182_());
                if (vec3 == null) {
                    return false;
                } else if (this.f_25016_.m_20275_(vec3.x, vec3.y, vec3.z) < this.f_25016_.m_20280_(this.f_25015_)) {
                    return false;
                } else {
                    if (this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.FLY)) {
                        for (int count = 0; count < 5 && constructEntity.m_9236_().m_46859_(BlockPos.containing(vec3.x, vec3.y, vec3.z)); vec3 = vec3.add(0.0, 1.0, 0.0)) {
                            count++;
                        }
                        vec3 = vec3.subtract(0.0, 1.0, 0.0);
                    }
                    this.f_25018_ = this.f_25019_.createPath(vec3.x, vec3.y, vec3.z, 0);
                    return this.f_25018_ != null;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Nullable
    private static Vec3 getPosAway(IConstruct<?> pMob, int pRadius, int pYRange, Vec3 pVectorPosition) {
        AbstractGolem constructEntity = pMob.asEntity();
        Vec3 vec3 = constructEntity.m_20182_().subtract(pVectorPosition);
        return RandomPos.generateRandomPos(constructEntity, () -> {
            BlockPos blockpos = RandomPos.generateRandomDirectionWithinRadians(constructEntity.m_217043_(), pRadius, pYRange, 0, vec3.x, vec3.z, (float) (Math.PI / 2));
            return blockpos == null ? null : generateRandomPosTowardDirection(pMob, pRadius, false, blockpos);
        });
    }

    @Nullable
    private static BlockPos generateRandomPosTowardDirection(IConstruct<?> pMob, int pRadius, boolean pShortCircuit, BlockPos pPos) {
        AbstractGolem constructEntity = pMob.asEntity();
        BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(constructEntity, pRadius, constructEntity.m_217043_(), pPos);
        if (!pMob.getConstructData().isCapabilityEnabled(ConstructCapability.FLY) && GoalUtils.isNotStable(constructEntity.m_21573_(), blockpos)) {
            return null;
        } else {
            return !GoalUtils.isOutsideLimits(blockpos, constructEntity) && !GoalUtils.hasMalus(constructEntity, blockpos) ? blockpos : null;
        }
    }
}