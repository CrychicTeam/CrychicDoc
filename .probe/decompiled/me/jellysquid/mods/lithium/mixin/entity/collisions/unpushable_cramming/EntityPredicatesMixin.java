package me.jellysquid.mods.lithium.mixin.entity.collisions.unpushable_cramming;

import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.entity.pushable.EntityPushablePredicate;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ EntitySelector.class })
public class EntityPredicatesMixin {

    @Redirect(method = { "canBePushedBy(Lnet/minecraft/entity/Entity;)Ljava/util/function/Predicate;" }, at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;and(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;"))
    private static <T> Predicate<T> getEntityPushablePredicate(Predicate<T> first, Predicate<? super T> second) {
        return EntityPushablePredicate.and(first, second);
    }
}