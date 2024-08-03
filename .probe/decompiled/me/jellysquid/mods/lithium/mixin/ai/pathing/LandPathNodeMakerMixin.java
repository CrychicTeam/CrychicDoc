package me.jellysquid.mods.lithium.mixin.ai.pathing;

import me.jellysquid.mods.lithium.api.pathing.BlockPathing;
import me.jellysquid.mods.lithium.common.ai.pathing.PathNodeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = { WalkNodeEvaluator.class }, priority = 990)
public abstract class LandPathNodeMakerMixin {

    @Inject(method = { "getCommonNodeType" }, at = { @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", shift = Shift.AFTER) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void getLithiumCachedCommonNodeType(BlockGetter world, BlockPos pos, CallbackInfoReturnable<BlockPathTypes> cir, BlockState blockState) {
        BlockPathTypes type;
        if (((BlockPathing) blockState.m_60734_()).needsDynamicNodeTypeCheck()) {
            type = blockState.getBlockPathType(world, pos, null);
            if (type == null) {
                type = PathNodeCache.getPathNodeType(blockState);
            }
        } else {
            type = PathNodeCache.getPathNodeType(blockState);
            if (type != BlockPathTypes.LAVA && type != BlockPathTypes.DANGER_FIRE && ((BlockPathing) blockState.m_60734_()).needsDynamicBurningCheck() && blockState.isBurning(world, pos)) {
                type = BlockPathTypes.DANGER_FIRE;
            }
        }
        if (type != null) {
            cir.setReturnValue(type);
        }
    }

    @Inject(method = { "getNodeTypeFromNeighbors" }, locals = LocalCapture.CAPTURE_FAILHARD, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(III)Lnet/minecraft/util/math/BlockPos$Mutable;") }, cancellable = true)
    private static void doNotChangePositionIfLithiumSinglePosCall(BlockGetter world, BlockPos.MutableBlockPos pos, BlockPathTypes nodeType, CallbackInfoReturnable<BlockPathTypes> cir, int posX, int posY, int posZ, int dX, int dY, int dZ) {
        if (nodeType == null) {
            if (dX == -1 && dY == -1 && dZ == -1) {
                pos.set(posX, posY, posZ);
            } else {
                cir.setReturnValue(null);
            }
        }
    }

    @Redirect(method = { "getLandNodeType" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/LandPathNodeMaker;getNodeTypeFromNeighbors(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/entity/ai/pathing/PathNodeType;)Lnet/minecraft/entity/ai/pathing/PathNodeType;"))
    private static BlockPathTypes getNodeTypeFromNeighbors(BlockGetter world, BlockPos.MutableBlockPos pos, BlockPathTypes type) {
        return PathNodeCache.getNodeTypeFromNeighbors(world, pos, type);
    }
}