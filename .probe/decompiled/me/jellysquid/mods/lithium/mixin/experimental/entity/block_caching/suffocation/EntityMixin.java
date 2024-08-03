package me.jellysquid.mods.lithium.mixin.experimental.entity.block_caching.suffocation;

import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCache;
import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCacheProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Entity.class })
public abstract class EntityMixin implements BlockCacheProvider {

    @Shadow
    public Level level;

    @Shadow
    private EntityDimensions dimensions;

    protected EntityMixin(EntityDimensions dimensions) {
        this.dimensions = dimensions;
    }

    @Inject(method = { "isInsideWall" }, cancellable = true, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;stream(Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;", shift = Shift.BEFORE) }, locals = LocalCapture.CAPTURE_FAILHARD)
    public void isInsideWall(CallbackInfoReturnable<Boolean> cir, float f, AABB box) {
        int minX = Mth.floor(box.minX);
        int minY = Mth.floor(box.minY);
        int minZ = Mth.floor(box.minZ);
        int maxX = Mth.floor(box.maxX);
        int maxY = Mth.floor(box.maxY);
        int maxZ = Mth.floor(box.maxZ);
        BlockCache bc = this.getUpdatedBlockCache((Entity) this);
        byte cachedSuffocation = bc.getIsSuffocating();
        if (cachedSuffocation == 0) {
            cir.setReturnValue(false);
        } else if (cachedSuffocation == 1) {
            cir.setReturnValue(true);
        } else {
            Level world = this.level;
            if (world.m_141937_() <= maxY && world.m_151558_() >= minY) {
                BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
                VoxelShape suffocationShape = null;
                boolean shouldCache = true;
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        for (int x = minX; x <= maxX; x++) {
                            blockPos.set(x, y, z);
                            BlockState blockState = world.getBlockState(blockPos);
                            if (!blockState.m_60795_() && blockState.m_60828_(this.level, blockPos)) {
                                if (shouldCache && blockState.m_204336_(BlockTags.SHULKER_BOXES)) {
                                    shouldCache = false;
                                }
                                if (suffocationShape == null) {
                                    suffocationShape = Shapes.create(new AABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
                                }
                                if (Shapes.joinIsNotEmpty(blockState.m_60812_(this.level, blockPos).move((double) blockPos.m_123341_(), (double) blockPos.m_123342_(), (double) blockPos.m_123343_()), suffocationShape, BooleanOp.AND)) {
                                    if (shouldCache) {
                                        bc.setCachedIsSuffocating(true);
                                    }
                                    cir.setReturnValue(true);
                                    return;
                                }
                            }
                        }
                    }
                }
                if (shouldCache) {
                    bc.setCachedIsSuffocating(false);
                }
                cir.setReturnValue(false);
            } else {
                bc.setCachedIsSuffocating(false);
                cir.setReturnValue(false);
            }
        }
    }
}