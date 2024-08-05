package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.config.QualityMode;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BlockEntity.class })
public abstract class BlockEntityMixin implements DynamicLightSource {

    @Final
    @Shadow
    protected BlockPos worldPosition;

    @Shadow
    @Nullable
    protected Level level;

    @Shadow
    protected boolean remove;

    @Unique
    private int luminance = 0;

    @Unique
    private int lastLuminance = 0;

    @Unique
    private long lastUpdate = 0L;

    @Unique
    private final LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

    private static long lambdynlights_lastUpdate = 0L;

    @Override
    public double tdv$getDynamicLightX() {
        return (double) this.worldPosition.m_123341_() + 0.5;
    }

    @Override
    public double tdv$getDynamicLightY() {
        return (double) this.worldPosition.m_123342_() + 0.5;
    }

    @Override
    public double tdv$getDynamicLightZ() {
        return (double) this.worldPosition.m_123343_() + 0.5;
    }

    @Override
    public Level tdv$getDynamicLightWorld() {
        return this.level;
    }

    @Inject(method = { "setRemoved" }, at = { @At("TAIL") })
    private void onRemoved(CallbackInfo ci) {
        this.tdv$setDynamicLightEnabled(false);
    }

    @Override
    public void tdv$resetDynamicLight() {
        this.lastLuminance = 0;
    }

    @Override
    public void tdv$dynamicLightTick() {
        if (this.level != null && this.level.isClientSide()) {
            if (!this.remove) {
                this.luminance = DynamicLightHandlers.getLuminanceFrom((BlockEntity) this);
                LambDynLights.updateTracking(this);
                if (!this.tdv$isDynamicLightEnabled()) {
                    this.lastLuminance = 0;
                }
            }
        }
    }

    @Override
    public int tdv$getLuminance() {
        return this.luminance;
    }

    @Override
    public boolean tdv$shouldUpdateDynamicLight() {
        QualityMode mode = (QualityMode) DynamicLightsConfig.Quality.get();
        if (Objects.equals(mode, QualityMode.OFF)) {
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            if (Objects.equals(mode, QualityMode.SLOW) && currentTime < lambdynlights_lastUpdate + 500L) {
                return false;
            } else if (Objects.equals(mode, QualityMode.FAST) && currentTime < lambdynlights_lastUpdate + 200L) {
                return false;
            } else {
                lambdynlights_lastUpdate = currentTime;
                return true;
            }
        }
    }

    @Override
    public boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer) {
        if (!this.tdv$shouldUpdateDynamicLight()) {
            return false;
        } else {
            int luminance = this.tdv$getLuminance();
            if (luminance == this.lastLuminance) {
                return false;
            } else {
                this.lastLuminance = luminance;
                if (this.lambdynlights$trackedLitChunkPos.isEmpty()) {
                    BlockPos.MutableBlockPos chunkPos = new BlockPos.MutableBlockPos(Mth.floorDiv(this.worldPosition.m_123341_(), 16), Mth.floorDiv(this.worldPosition.m_123342_(), 16), Mth.floorDiv(this.worldPosition.m_123343_(), 16));
                    LambDynLights.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);
                    Direction directionX = (this.worldPosition.m_123341_() & 15) >= 8 ? Direction.EAST : Direction.WEST;
                    Direction directionY = (this.worldPosition.m_123342_() & 15) >= 8 ? Direction.UP : Direction.DOWN;
                    Direction directionZ = (this.worldPosition.m_123343_() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;
                    for (int i = 0; i < 7; i++) {
                        if (i % 4 == 0) {
                            chunkPos.move(directionX);
                        } else if (i % 4 == 1) {
                            chunkPos.move(directionZ);
                        } else if (i % 4 == 2) {
                            chunkPos.move(directionX.getOpposite());
                        } else {
                            chunkPos.move(directionZ.getOpposite());
                            chunkPos.move(directionY);
                        }
                        LambDynLights.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);
                    }
                }
                this.tdv$lambdynlights$scheduleTrackedChunksRebuild(renderer);
                return true;
            }
        }
    }

    @Override
    public void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
        if (this.level == Minecraft.getInstance().level) {
            LongIterator var2 = this.lambdynlights$trackedLitChunkPos.iterator();
            while (var2.hasNext()) {
                long pos = (Long) var2.next();
                LambDynLights.scheduleChunkRebuild(renderer, pos);
            }
        }
    }
}