package dev.xkmc.l2weaponry.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ TargetGoal.class })
public interface TargetGoalAccessor {

    @Accessor
    void setTargetMob(LivingEntity var1);
}