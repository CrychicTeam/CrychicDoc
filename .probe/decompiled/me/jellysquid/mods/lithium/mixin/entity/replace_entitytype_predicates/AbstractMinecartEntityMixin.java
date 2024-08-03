package me.jellysquid.mods.lithium.mixin.entity.replace_entitytype_predicates;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ AbstractMinecart.class })
public class AbstractMinecartEntityMixin {

    @Redirect(method = { "tick()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private List<AbstractMinecart> getOtherAbstractMinecarts(Level world, Entity except, AABB box) {
        return world.m_6443_(AbstractMinecart.class, box, entity -> entity != except);
    }
}