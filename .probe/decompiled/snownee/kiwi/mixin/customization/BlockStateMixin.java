package snownee.kiwi.mixin.customization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.placement.PlacementSystem;

@Mixin({ BlockBehaviour.BlockStateBase.class })
public abstract class BlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    protected abstract BlockState asState();

    @Inject(method = { "canSurvive" }, at = { @At("HEAD") }, cancellable = true)
    private void kiwi$canSurvive(LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        KBlockSettings settings = KBlockSettings.of(this.getBlock());
        if (settings != null && settings.canSurviveHandler != null) {
            cir.setReturnValue(settings.canSurviveHandler.canSurvive(this.asState(), pLevel, pPos));
        }
    }

    @Inject(method = { "updateShape" }, at = { @At("HEAD") }, cancellable = true)
    private void kiwi$checkCanSurvive(Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos, CallbackInfoReturnable<BlockState> cir) {
        KBlockSettings settings = KBlockSettings.of(this.getBlock());
        if (settings != null && settings.canSurviveHandler != null && settings.canSurviveHandler.isSensitiveSide(this.asState(), pDirection) && !settings.canSurviveHandler.canSurvive(this.asState(), pLevel, pPos)) {
            cir.setReturnValue(Blocks.AIR.defaultBlockState());
        }
    }

    @Inject(method = { "updateShape" }, at = { @At("RETURN") }, cancellable = true)
    private void kiwi$updateShape(Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (((BlockState) cir.getReturnValue()).m_60713_(this.getBlock())) {
            KBlockSettings settings = KBlockSettings.of(this.getBlock());
            if (settings != null) {
                cir.setReturnValue(settings.updateShape((BlockState) cir.getReturnValue(), pDirection, pNeighborState, pLevel, pPos, pNeighborPos));
            }
        }
    }

    @Inject(method = { "canBeReplaced(Lnet/minecraft/world/item/context/BlockPlaceContext;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void kiwi$canBeReplaced(BlockPlaceContext pUseContext, CallbackInfoReturnable<Boolean> cir) {
        KBlockSettings settings = KBlockSettings.of(this.getBlock());
        if (settings != null) {
            Boolean triState = settings.canBeReplaced(this.asState(), pUseContext);
            if (triState != null) {
                cir.setReturnValue(triState);
            }
        }
    }

    @WrapOperation(method = { "onRemove" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;onRemove(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V") })
    private void kiwi$onRemove(Block block, BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean pMovedByPiston, Operation<Void> original) {
        original.call(new Object[] { block, oldState, level, pos, newState, pMovedByPiston });
        try {
            PlacementSystem.onBlockRemoved(level, pos, oldState, newState);
        } catch (Throwable var9) {
            Kiwi.LOGGER.error("Failed to handle placement for %s".formatted(oldState), var9);
        }
    }
}