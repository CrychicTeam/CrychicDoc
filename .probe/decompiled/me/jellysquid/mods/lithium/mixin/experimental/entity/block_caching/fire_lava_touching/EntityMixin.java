package me.jellysquid.mods.lithium.mixin.experimental.entity.block_caching.fire_lava_touching;

import java.util.function.Predicate;
import java.util.stream.Stream;
import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCache;
import me.jellysquid.mods.lithium.common.entity.block_tracking.BlockCacheProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Entity.class })
public abstract class EntityMixin implements BlockCacheProvider {

    private static final Stream<BlockState> EMPTY_BLOCKSTATE_STREAM = Stream.empty();

    @Shadow
    private int remainingFireTicks;

    @Shadow
    public boolean wasOnFire;

    @Shadow
    public boolean isInPowderSnow;

    @Shadow
    protected abstract int getFireImmuneTicks();

    @Shadow
    public abstract boolean isInWaterRainOrBubble();

    @Redirect(method = { "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getStatesInBoxIfLoaded(Lnet/minecraft/util/math/Box;)Ljava/util/stream/Stream;"))
    private Stream<BlockState> skipFireTestIfResultDoesNotMatterOrIsCached(Level world, AABB box) {
        if (this.remainingFireTicks <= 0 && this.remainingFireTicks != -this.getFireImmuneTicks() || this.wasOnFire && (this.isInPowderSnow || this.isInWaterRainOrBubble())) {
            BlockCache bc = this.getUpdatedBlockCache((Entity) this);
            byte cachedTouchingFireLava = bc.getIsTouchingFireLava();
            if (cachedTouchingFireLava == 0) {
                return null;
            } else if (cachedTouchingFireLava == 1) {
                return EMPTY_BLOCKSTATE_STREAM;
            } else {
                int minX = Mth.floor(box.minX);
                int maxX = Mth.floor(box.maxX);
                int minY = Mth.floor(box.minY);
                int maxY = Mth.floor(box.maxY);
                int minZ = Mth.floor(box.minZ);
                int maxZ = Mth.floor(box.maxZ);
                if (maxY >= world.m_141937_() && minY < world.m_151558_() && world.m_151572_(minX, minZ, maxX, maxZ)) {
                    BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            for (int x = minX; x <= maxX; x++) {
                                blockPos.set(x, y, z);
                                BlockState state = world.getBlockState(blockPos);
                                if (state.m_204336_(BlockTags.FIRE) || state.m_60713_(Blocks.LAVA)) {
                                    bc.setCachedTouchingFireLava(true);
                                    return EMPTY_BLOCKSTATE_STREAM;
                                }
                            }
                        }
                    }
                }
                bc.setCachedTouchingFireLava(false);
                return null;
            }
        } else {
            return null;
        }
    }

    @Redirect(method = { "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V" }, at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"))
    private boolean skipNullStream(Stream<BlockState> stream, Predicate<BlockState> predicate) {
        return stream == null;
    }
}