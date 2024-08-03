package me.jellysquid.mods.lithium.mixin.experimental.entity.block_caching.block_touching;

import me.jellysquid.mods.lithium.common.block.BlockStateFlagHolder;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCache;
import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCacheProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Entity.class })
public abstract class EntityMixin implements BlockCacheProvider {

    @Inject(method = { "checkBlockCollision()V" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelIfSkippable(CallbackInfo ci) {
        if (!(this instanceof ServerPlayer)) {
            BlockCache bc = this.getUpdatedBlockCache((Entity) this);
            if (bc.canSkipBlockTouching()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = { "checkBlockCollision()V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;getX()I", ordinal = 0) })
    private void assumeNoTouchableBlock(CallbackInfo ci) {
        BlockCache bc = this.getBlockCache();
        if (bc.isTracking()) {
            bc.setCanSkipBlockTouching(true);
        }
    }

    @Inject(method = { "checkBlockCollision()V" }, locals = LocalCapture.CAPTURE_FAILHARD, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V") })
    private void checkTouchableBlock(CallbackInfo ci, AABB box, BlockPos blockPos, BlockPos blockPos2, BlockPos.MutableBlockPos mutable, int i, int j, int k, BlockState blockState) {
        BlockCache bc = this.getBlockCache();
        if (bc.canSkipBlockTouching() && 0 != (((BlockStateFlagHolder) blockState).getAllFlags() & 1 << BlockStateFlags.ENTITY_TOUCHABLE.getIndex())) {
            bc.setCanSkipBlockTouching(false);
        }
    }
}