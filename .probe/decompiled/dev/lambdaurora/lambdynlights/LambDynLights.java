package dev.lambdaurora.lambdynlights;

import dev.lambdaurora.lambdynlights.accessor.WorldRendererAccessor;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSources;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod("dynamiclightsreforged")
public class LambDynLights {

    public static final String MODID = "dynamiclightsreforged";

    private static final double MAX_RADIUS = 7.75;

    private static final double MAX_RADIUS_SQUARED = 60.0625;

    private static LambDynLights INSTANCE;

    public final Logger logger = LogManager.getLogger("dynamiclightsreforged");

    private final Set<DynamicLightSource> dynamicLightSources = new HashSet();

    private final ReentrantReadWriteLock lightSourcesLock = new ReentrantReadWriteLock();

    private long lastUpdate = System.currentTimeMillis();

    private int lastUpdateCount = 0;

    public static boolean isEnabled() {
        return !Objects.equals(DynamicLightsConfig.Quality.get(), "OFF");
    }

    public LambDynLights() {
        INSTANCE = this;
        this.log("Initializing Dynamic Lights Reforged...");
        DynamicLightsConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("dynamic_lights_reforged.toml"));
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ExecutorHelper::onInitializeClient);
    }

    public void updateAll(@NotNull LevelRenderer renderer) {
        if (isEnabled()) {
            long now = System.currentTimeMillis();
            if (now >= this.lastUpdate + 50L) {
                this.lastUpdate = now;
                this.lastUpdateCount = 0;
                this.lightSourcesLock.readLock().lock();
                for (DynamicLightSource lightSource : this.dynamicLightSources) {
                    if (lightSource.tdv$lambdynlights$updateDynamicLight(renderer)) {
                        this.lastUpdateCount++;
                    }
                }
                this.lightSourcesLock.readLock().unlock();
            }
        }
    }

    public int getLastUpdateCount() {
        return this.lastUpdateCount;
    }

    public int getLightmapWithDynamicLight(@NotNull BlockPos pos, int lightmap) {
        return this.getLightmapWithDynamicLight(this.getDynamicLightLevel(pos), lightmap);
    }

    public int getLightmapWithDynamicLight(@NotNull Entity entity, int lightmap) {
        int posLightLevel = (int) this.getDynamicLightLevel(entity.blockPosition());
        int entityLuminance = ((DynamicLightSource) entity).tdv$getLuminance();
        return this.getLightmapWithDynamicLight((double) Math.max(posLightLevel, entityLuminance), lightmap);
    }

    public int getLightmapWithDynamicLight(double dynamicLightLevel, int lightmap) {
        if (dynamicLightLevel > 0.0) {
            int blockLevel = LightTexture.block(lightmap);
            if (dynamicLightLevel > (double) blockLevel) {
                int luminance = (int) (dynamicLightLevel * 16.0);
                lightmap &= -1048576;
                lightmap |= luminance & 1048575;
            }
        }
        return lightmap;
    }

    public double getDynamicLightLevel(@NotNull BlockPos pos) {
        double result = 0.0;
        this.lightSourcesLock.readLock().lock();
        for (DynamicLightSource lightSource : this.dynamicLightSources) {
            result = maxDynamicLightLevel(pos, lightSource, result);
        }
        this.lightSourcesLock.readLock().unlock();
        return Mth.clamp(result, 0.0, 15.0);
    }

    public static double maxDynamicLightLevel(@NotNull BlockPos pos, @NotNull DynamicLightSource lightSource, double currentLightLevel) {
        int luminance = lightSource.tdv$getLuminance();
        if (luminance > 0) {
            double dx = (double) pos.m_123341_() - lightSource.tdv$getDynamicLightX() + 0.5;
            double dy = (double) pos.m_123342_() - lightSource.tdv$getDynamicLightY() + 0.5;
            double dz = (double) pos.m_123343_() - lightSource.tdv$getDynamicLightZ() + 0.5;
            double distanceSquared = dx * dx + dy * dy + dz * dz;
            if (distanceSquared <= 60.0625) {
                double multiplier = 1.0 - Math.sqrt(distanceSquared) / 7.75;
                double lightLevel = multiplier * (double) luminance;
                if (lightLevel > currentLightLevel) {
                    return lightLevel;
                }
            }
        }
        return currentLightLevel;
    }

    public void addLightSource(@NotNull DynamicLightSource lightSource) {
        if (lightSource.tdv$getDynamicLightWorld().isClientSide()) {
            if (isEnabled()) {
                if (!this.containsLightSource(lightSource)) {
                    this.lightSourcesLock.writeLock().lock();
                    this.dynamicLightSources.add(lightSource);
                    this.lightSourcesLock.writeLock().unlock();
                }
            }
        }
    }

    public boolean containsLightSource(@NotNull DynamicLightSource lightSource) {
        if (!lightSource.tdv$getDynamicLightWorld().isClientSide()) {
            return false;
        } else {
            this.lightSourcesLock.readLock().lock();
            boolean result = this.dynamicLightSources.contains(lightSource);
            this.lightSourcesLock.readLock().unlock();
            return result;
        }
    }

    public int getLightSourcesCount() {
        this.lightSourcesLock.readLock().lock();
        int result = this.dynamicLightSources.size();
        this.lightSourcesLock.readLock().unlock();
        return result;
    }

    public void removeLightSource(@NotNull DynamicLightSource lightSource) {
        this.lightSourcesLock.writeLock().lock();
        Iterator<DynamicLightSource> dynamicLightSources = this.dynamicLightSources.iterator();
        while (dynamicLightSources.hasNext()) {
            DynamicLightSource it = (DynamicLightSource) dynamicLightSources.next();
            if (it.equals(lightSource)) {
                dynamicLightSources.remove();
                if (Minecraft.getInstance().levelRenderer != null) {
                    lightSource.tdv$lambdynlights$scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
                }
                break;
            }
        }
        this.lightSourcesLock.writeLock().unlock();
    }

    public void clearLightSources() {
        this.lightSourcesLock.writeLock().lock();
        Iterator<DynamicLightSource> dynamicLightSources = this.dynamicLightSources.iterator();
        while (dynamicLightSources.hasNext()) {
            DynamicLightSource it = (DynamicLightSource) dynamicLightSources.next();
            dynamicLightSources.remove();
            if (Minecraft.getInstance().levelRenderer != null) {
                if (it.tdv$getLuminance() > 0) {
                    it.tdv$resetDynamicLight();
                }
                it.tdv$lambdynlights$scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
            }
        }
        this.lightSourcesLock.writeLock().unlock();
    }

    public void removeLightSources(@NotNull Predicate<DynamicLightSource> filter) {
        this.lightSourcesLock.writeLock().lock();
        Iterator<DynamicLightSource> dynamicLightSources = this.dynamicLightSources.iterator();
        while (dynamicLightSources.hasNext()) {
            DynamicLightSource it = (DynamicLightSource) dynamicLightSources.next();
            if (filter.test(it)) {
                dynamicLightSources.remove();
                if (Minecraft.getInstance().levelRenderer != null) {
                    if (it.tdv$getLuminance() > 0) {
                        it.tdv$resetDynamicLight();
                    }
                    it.tdv$lambdynlights$scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
                }
                break;
            }
        }
        this.lightSourcesLock.writeLock().unlock();
    }

    public void removeEntitiesLightSource() {
        this.removeLightSources(lightSource -> lightSource instanceof Entity && !(lightSource instanceof Player));
    }

    public void removeCreeperLightSources() {
        this.removeLightSources(entity -> entity instanceof Creeper);
    }

    public void removeTntLightSources() {
        this.removeLightSources(entity -> entity instanceof PrimedTnt);
    }

    public void removeBlockEntitiesLightSource() {
        this.removeLightSources(lightSource -> lightSource instanceof BlockEntity);
    }

    public void log(String info) {
        this.logger.info("[LambDynLights] " + info);
    }

    public void warn(String info) {
        this.logger.warn("[LambDynLights] " + info);
    }

    public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, @NotNull BlockPos chunkPos) {
        scheduleChunkRebuild(renderer, chunkPos.m_123341_(), chunkPos.m_123342_(), chunkPos.m_123343_());
    }

    public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, long chunkPos) {
        scheduleChunkRebuild(renderer, BlockPos.getX(chunkPos), BlockPos.getY(chunkPos), BlockPos.getZ(chunkPos));
    }

    public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, int x, int y, int z) {
        if (Minecraft.getInstance().level != null) {
            ((WorldRendererAccessor) renderer).dynlights_setSectionDirty(x, y, z, false);
        }
    }

    public static void updateTrackedChunks(@NotNull BlockPos chunkPos, @Nullable LongOpenHashSet old, @Nullable LongOpenHashSet newPos) {
        if (old != null || newPos != null) {
            long pos = chunkPos.asLong();
            if (old != null) {
                old.remove(pos);
            }
            if (newPos != null) {
                newPos.add(pos);
            }
        }
    }

    public static void updateTracking(@NotNull DynamicLightSource lightSource) {
        boolean enabled = lightSource.tdv$isDynamicLightEnabled();
        int luminance = lightSource.tdv$getLuminance();
        if (!enabled && luminance > 0) {
            lightSource.tdv$setDynamicLightEnabled(true);
        } else if (enabled && luminance < 1) {
            lightSource.tdv$setDynamicLightEnabled(false);
        }
    }

    public static int getLuminanceFromItemStack(@NotNull ItemStack stack, boolean submergedInWater) {
        return ItemLightSources.getLuminance(stack, submergedInWater);
    }

    public static LambDynLights get() {
        return INSTANCE;
    }
}