package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class InteractGoal extends LookAtPlayerGoal {

    public InteractGoal(Mob mob0, Class<? extends LivingEntity> classExtendsLivingEntity1, float float2) {
        super(mob0, classExtendsLivingEntity1, float2);
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    public InteractGoal(Mob mob0, Class<? extends LivingEntity> classExtendsLivingEntity1, float float2, float float3) {
        super(mob0, classExtendsLivingEntity1, float2, float3);
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }
}