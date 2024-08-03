package me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking.goals;

import java.util.List;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.entity.nearby_tracker.NearbyEntityListenerProvider;
import me.jellysquid.mods.lithium.common.entity.nearby_tracker.NearbyEntityTracker;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AvoidEntityGoal.class })
public class FleeEntityGoalMixin<T extends LivingEntity> {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Shadow
    @Final
    protected float maxDist;

    private NearbyEntityTracker<T> tracker;

    @Inject(method = { "<init>(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V" }, at = { @At("RETURN") })
    private void init(PathfinderMob mob, Class<T> fleeFromType, Predicate<LivingEntity> predicate, float distance, double slowSpeed, double fastSpeed, Predicate<LivingEntity> predicate2, CallbackInfo ci) {
        EntityDimensions dimensions = this.mob.m_6095_().getDimensions();
        double adjustedRange = (double) dimensions.width * 0.5 + (double) this.maxDist + 2.0;
        int horizontalRange = Mth.ceil(adjustedRange);
        this.tracker = new NearbyEntityTracker<>(fleeFromType, mob, new Vec3i(horizontalRange, Mth.ceil(dimensions.height + 3.0F + 2.0F), horizontalRange));
        ((NearbyEntityListenerProvider) mob).addListener(this.tracker);
    }

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getClosestEntity(Ljava/util/List;Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;DDD)Lnet/minecraft/entity/LivingEntity;"))
    private T redirectGetNearestEntity(Level world, List<? extends T> entityList, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return this.tracker.getClosestEntity(this.mob.m_20191_().inflate((double) this.maxDist, 3.0, (double) this.maxDist), targetPredicate, x, y, z);
    }

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private <R extends Entity> List<R> redirectGetEntities(Level world, Class<T> entityClass, AABB box, Predicate<? super R> predicate) {
        return null;
    }
}