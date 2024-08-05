package snownee.loquat.mixin.canary;

import com.abdelaziz.canary.common.entity.CanaryEntityCollisions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.Hooks;

@Mixin(value = { CanaryEntityCollisions.class }, remap = false)
public class CanaryEntityCollisionsMixin {

    @Inject(method = { "getEntityWorldBorderCollisions" }, at = { @At(value = "INVOKE", target = "Lcom/abdelaziz/canary/common/entity/CanaryEntityCollisions;getEntityWorldBorderCollisionIterable(Lnet/minecraft/world/level/EntityGetter;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Z)Ljava/lang/Iterable;") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void loquat$getEntityWorldBorderCollisions(Level world, Entity entity, AABB box, boolean includeWorldBorder, CallbackInfoReturnable<List<VoxelShape>> cir, ArrayList<VoxelShape> shapes) {
        Hooks.collideWithLoquatAreas(entity, box, shapes::add);
    }
}