package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.world_border;

import me.jellysquid.mods.lithium.common.world.listeners.WorldBorderListenerOnce;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.border.BorderStatus;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = { "net.minecraft.world.chunk.WorldChunk$DirectBlockEntityTickInvoker" })
public abstract class DirectBlockEntityTickInvokerMixin implements WorldBorderListenerOnce {

    @Shadow
    @Final
    LevelChunk f_156427_;

    private byte worldBorderState = 0;

    @Shadow
    public abstract BlockPos getPos();

    @Redirect(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;canTickBlockEntity(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean cachedCanTickBlockEntity(LevelChunk instance, BlockPos pos) {
        if (this.isInsideWorldBorder()) {
            return !(this.f_156427_.getLevel() instanceof ServerLevel serverWorld) ? true : this.f_156427_.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING) && serverWorld.areEntitiesLoaded(ChunkPos.asLong(pos));
        } else {
            return false;
        }
    }

    private boolean isInsideWorldBorder() {
        if (this.worldBorderState == 0) {
            this.startWorldBorderCaching();
        }
        int worldBorderState = this.worldBorderState;
        return (worldBorderState & 3) == 3 ? (worldBorderState & 4) != 0 : this.f_156427_.getLevel().getWorldBorder().isWithinBounds(this.getPos());
    }

    private void startWorldBorderCaching() {
        this.worldBorderState = 1;
        WorldBorder worldBorder = this.f_156427_.getLevel().getWorldBorder();
        worldBorder.addListener(this);
        boolean isStationary = worldBorder.getStatus() == BorderStatus.STATIONARY;
        if (worldBorder.isWithinBounds(this.getPos())) {
            if (isStationary || worldBorder.getStatus() == BorderStatus.GROWING) {
                this.worldBorderState = (byte) (this.worldBorderState | 6);
            }
        } else if (isStationary || worldBorder.getStatus() == BorderStatus.SHRINKING) {
            this.worldBorderState = (byte) (this.worldBorderState | 2);
        }
    }

    @Override
    public void onWorldBorderShapeChange(WorldBorder worldBorder) {
        this.worldBorderState = 0;
    }
}