package snownee.loquat.mixin;

import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.Hooks;

@Mixin(value = { Entity.class }, priority = 1500)
public class EntityMixin {

    @Inject(method = { "collideBoundingBox" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;") }, locals = LocalCapture.CAPTURE_FAILHARD, require = 0)
    private static void loquat$collideBoundingBox(@Nullable Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits, CallbackInfoReturnable<Vec3> cir, Builder<VoxelShape> builder) {
        Hooks.collideWithLoquatAreas(entity, collisionBox.expandTowards(vec), builder::add);
    }
}