package me.jellysquid.mods.lithium.mixin.entity.collisions.unpushable_cramming;

import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.entity.pushable.EntityPushablePredicate;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ AbstractMinecart.class })
public class AbstractMinecartEntityMixin {

    @Redirect(method = { "tick()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"), require = 0)
    private List<Entity> getOtherPushableEntities(Level world, @Nullable Entity except, AABB box, Predicate<? super Entity> predicate) {
        if (predicate == Predicates.alwaysFalse()) {
            return Collections.emptyList();
        } else {
            if (predicate instanceof EntityPushablePredicate<?> entityPushablePredicate) {
                EntitySectionStorage<Entity> cache = WorldHelper.getEntityCacheOrNull(world);
                if (cache != null) {
                    return WorldHelper.getPushableEntities(world, cache, except, box, (EntityPushablePredicate<? super Entity>) entityPushablePredicate);
                }
            }
            return world.getEntities(except, box, predicate);
        }
    }
}