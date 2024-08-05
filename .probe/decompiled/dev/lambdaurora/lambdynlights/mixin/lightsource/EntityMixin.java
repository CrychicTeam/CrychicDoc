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
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Entity.class })
public abstract class EntityMixin implements DynamicLightSource {

    @Shadow
    public Level level;

    @Shadow
    private ChunkPos chunkPosition;

    @Unique
    protected int lambdynlights$luminance = 0;

    @Unique
    private int lambdynlights$lastLuminance = 0;

    @Unique
    private long lambdynlights$lastUpdate = 0L;

    @Unique
    private double lambdynlights$prevX;

    @Unique
    private double lambdynlights$prevY;

    @Unique
    private double lambdynlights$prevZ;

    @Unique
    private LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

    private static long lambdynlights_lastUpdate = 0L;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getEyeY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract boolean isOnFire();

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract boolean isRemoved();

    @Shadow
    public abstract BlockPos blockPosition();

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    public void onTick(CallbackInfo ci) {
        if (this.level.isClientSide()) {
            if (this.isRemoved()) {
                this.tdv$setDynamicLightEnabled(false);
            } else {
                this.tdv$dynamicLightTick();
                if (!DynamicLightsConfig.TileEntityLighting.get() && this.getType() != EntityType.PLAYER || !DynamicLightHandlers.canLightUp((Entity) this)) {
                    this.lambdynlights$luminance = 0;
                }
                LambDynLights.updateTracking(this);
            }
        }
    }

    @Inject(method = { "remove" }, at = { @At("TAIL") })
    public void onRemove(CallbackInfo ci) {
        if (this.level.isClientSide()) {
            this.tdv$setDynamicLightEnabled(false);
        }
    }

    @Override
    public double tdv$getDynamicLightX() {
        return this.getX();
    }

    @Override
    public double tdv$getDynamicLightY() {
        return this.getEyeY();
    }

    @Override
    public double tdv$getDynamicLightZ() {
        return this.getZ();
    }

    @Override
    public Level tdv$getDynamicLightWorld() {
        return this.level;
    }

    @Override
    public void tdv$resetDynamicLight() {
        this.lambdynlights$lastLuminance = 0;
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
    public void tdv$dynamicLightTick() {
        this.lambdynlights$luminance = this.isOnFire() ? 15 : 0;
        int luminance = DynamicLightHandlers.getLuminanceFrom((Entity) this);
        if (luminance > this.lambdynlights$luminance) {
            this.lambdynlights$luminance = luminance;
        }
    }

    @Override
    public int tdv$getLuminance() {
        return this.lambdynlights$luminance;
    }

    @Override
    public boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer) {
        if (!this.tdv$shouldUpdateDynamicLight()) {
            return false;
        } else {
            double deltaX = this.getX() - this.lambdynlights$prevX;
            double deltaY = this.getY() - this.lambdynlights$prevY;
            double deltaZ = this.getZ() - this.lambdynlights$prevZ;
            int luminance = this.tdv$getLuminance();
            if (!(Math.abs(deltaX) > 0.1) && !(Math.abs(deltaY) > 0.1) && !(Math.abs(deltaZ) > 0.1) && luminance == this.lambdynlights$lastLuminance) {
                return false;
            } else {
                this.lambdynlights$prevX = this.getX();
                this.lambdynlights$prevY = this.getY();
                this.lambdynlights$prevZ = this.getZ();
                this.lambdynlights$lastLuminance = luminance;
                LongOpenHashSet newPos = new LongOpenHashSet();
                if (luminance > 0) {
                    ChunkPos entityChunkPos = this.chunkPosition;
                    BlockPos.MutableBlockPos chunkPos = new BlockPos.MutableBlockPos(entityChunkPos.x, SectionPos.posToSectionCoord(this.getEyeY()), entityChunkPos.z);
                    LambDynLights.scheduleChunkRebuild(renderer, chunkPos);
                    LambDynLights.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);
                    Direction directionX = (this.blockPosition().m_123341_() & 15) >= 8 ? Direction.EAST : Direction.WEST;
                    Direction directionY = (Mth.floor(this.getEyeY()) & 15) >= 8 ? Direction.UP : Direction.DOWN;
                    Direction directionZ = (this.blockPosition().m_123343_() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;
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
                        LambDynLights.scheduleChunkRebuild(renderer, chunkPos);
                        LambDynLights.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);
                    }
                }
                this.tdv$lambdynlights$scheduleTrackedChunksRebuild(renderer);
                this.lambdynlights$trackedLitChunkPos = newPos;
                return true;
            }
        }
    }

    @Override
    public void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
        if (Minecraft.getInstance().level == this.level) {
            LongIterator var2 = this.lambdynlights$trackedLitChunkPos.iterator();
            while (var2.hasNext()) {
                long pos = (Long) var2.next();
                LambDynLights.scheduleChunkRebuild(renderer, pos);
            }
        }
    }
}