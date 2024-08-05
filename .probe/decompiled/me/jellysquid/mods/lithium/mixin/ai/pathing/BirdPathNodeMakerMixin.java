package me.jellysquid.mods.lithium.mixin.ai.pathing;

import me.jellysquid.mods.lithium.common.ai.pathing.PathNodeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FlyNodeEvaluator.class })
public class BirdPathNodeMakerMixin {

    @Redirect(method = { "getDefaultNodeType" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/BirdPathNodeMaker;getNodeTypeFromNeighbors(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/entity/ai/pathing/PathNodeType;)Lnet/minecraft/entity/ai/pathing/PathNodeType;"))
    private BlockPathTypes getNodeTypeFromNeighbors(BlockGetter world, BlockPos.MutableBlockPos pos, BlockPathTypes type) {
        return PathNodeCache.getNodeTypeFromNeighbors(world, pos, type);
    }
}