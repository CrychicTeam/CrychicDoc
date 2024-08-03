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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LookAtPlayerGoal.class })
public class LookAtEntityGoalMixin {

    @Shadow
    @Final
    protected Mob mob;

    @Shadow
    @Final
    protected float lookDistance;

    private NearbyEntityTracker<? extends LivingEntity> tracker;

    @Inject(method = { "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;FFZ)V" }, at = { @At("RETURN") })
    private void init(Mob mob, Class<? extends LivingEntity> targetType, float range, float chance, boolean b, CallbackInfo ci) {
        EntityDimensions dimensions = this.mob.m_6095_().getDimensions();
        double adjustedRange = (double) dimensions.width * 0.5 + (double) this.lookDistance + 2.0;
        int horizontalRange = Mth.ceil(adjustedRange);
        this.tracker = new NearbyEntityTracker<>(targetType, mob, new Vec3i(horizontalRange, Mth.ceil(dimensions.height + 3.0F + 2.0F), horizontalRange));
        ((NearbyEntityListenerProvider) mob).addListener(this.tracker);
    }

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getClosestEntity(Ljava/util/List;Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;DDD)Lnet/minecraft/entity/LivingEntity;"))
    private LivingEntity redirectGetNearestEntity(Level world, List<LivingEntity> entityList, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return this.tracker.getClosestEntity(this.mob.m_20191_().inflate((double) this.lookDistance, 3.0, (double) this.lookDistance), targetPredicate, x, y, z);
    }

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private <R extends Entity> List<R> redirectGetEntities(Level world, Class<LivingEntity> entityClass, AABB box, Predicate<? super R> predicate) {
        return null;
    }

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;DDD)Lnet/minecraft/entity/player/PlayerEntity;"))
    private Player redirectGetClosestPlayer(Level world, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return (Player) this.tracker.getClosestEntity(null, targetPredicate, x, y, z);
    }
}