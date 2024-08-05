package forge.me.thosea.badoptimizations.mixin.tick;

import forge.me.thosea.badoptimizations.interfaces.BiomeSkyColorGetter;
import forge.me.thosea.badoptimizations.other.CommonColorFactors;
import forge.me.thosea.badoptimizations.other.Config;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ClientLevel.class })
public abstract class MixinClientWorld extends Level {

    @Shadow
    @Final
    private Minecraft minecraft;

    private BiomeSkyColorGetter bo$biomeColors;

    private CommonColorFactors bo$commonFactors;

    private Vec3 bo$skyColorCache;

    private int bo$lastBiomeColor;

    private Vec3 bo$biomeColorVector;

    @Inject(method = { "getSkyColor" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetSkyColor(Vec3 cameraPos, float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        if (this.bo$skyColorCache != null && this.minecraft.player != null) {
            CommonColorFactors.tick(tickDelta);
            if (this.bo$commonFactors.didTickChange()) {
                if (this.bo$isBiomeDirty(cameraPos.subtract(2.0, 2.0, 2.0).scale(0.25))) {
                    this.bo$commonFactors.updateLastTime();
                    return;
                }
                if (this.bo$commonFactors.isDirty() || this.bo$commonFactors.getTimeDelta() >= (long) Config.skycolor_time_change_needed_for_update) {
                    this.bo$skyColorCache = this.bo$calcSkyColor(tickDelta);
                    this.bo$commonFactors.updateLastTime();
                }
            }
            cir.setReturnValue(this.bo$skyColorCache);
        }
    }

    private boolean bo$isBiomeDirty(Vec3 pos) {
        int x = Mth.floor(pos.x);
        int y = Mth.floor(pos.y);
        int z = Mth.floor(pos.z);
        int color = this.bo$biomeColors.get(x - 2, y - 2, z - 2);
        if (this.bo$lastBiomeColor != color) {
            this.bo$lastBiomeColor = color;
            this.bo$biomeColorVector = Vec3.fromRGB24(color);
            return true;
        } else {
            return this.bo$biomeColors.get(x + 3, y + 3, z + 3) != color;
        }
    }

    @Shadow
    public abstract int getSkyFlashTime();

    @Shadow
    public abstract Vec3 getSkyColor(Vec3 var1, float var2);

    private Vec3 bo$calcSkyColor(float delta) {
        float angle = Mth.cos(this.m_46942_(1.0F) * (float) (Math.PI * 2)) * 2.0F + 0.5F;
        angle = Mth.clamp(angle, 0.0F, 1.0F);
        double x = this.bo$biomeColorVector.x * (double) angle;
        double y = this.bo$biomeColorVector.y * (double) angle;
        double z = this.bo$biomeColorVector.z * (double) angle;
        if (CommonColorFactors.rainGradientMultiplier > 0.0F) {
            double color = (x * 0.3F + y * 0.59F + z * 0.11F) * 0.6F;
            x = x * (double) CommonColorFactors.rainGradientMultiplier + color * (1.0 - (double) CommonColorFactors.rainGradientMultiplier);
            y = y * (double) CommonColorFactors.rainGradientMultiplier + color * (1.0 - (double) CommonColorFactors.rainGradientMultiplier);
            z = z * (double) CommonColorFactors.rainGradientMultiplier + color * (1.0 - (double) CommonColorFactors.rainGradientMultiplier);
        }
        if (CommonColorFactors.thunderGradientMultiplier > 0.0F) {
            double color = (x * 0.3F + y * 0.59F + z * 0.11F) * 0.2F;
            x = x * (double) CommonColorFactors.thunderGradientMultiplier + color * (1.0 - (double) CommonColorFactors.thunderGradientMultiplier);
            y = y * (double) CommonColorFactors.thunderGradientMultiplier + color * (1.0 - (double) CommonColorFactors.thunderGradientMultiplier);
            z = z * (double) CommonColorFactors.thunderGradientMultiplier + color * (1.0 - (double) CommonColorFactors.thunderGradientMultiplier);
        }
        if (CommonColorFactors.lastLightningTicks > 0) {
            float lightningMultiplier = (float) CommonColorFactors.lastLightningTicks - delta;
            if (lightningMultiplier > 1.0F) {
                lightningMultiplier = 1.0F;
            }
            lightningMultiplier *= 0.45F;
            x = x * (double) (1.0F - lightningMultiplier) + (double) (0.8F * lightningMultiplier);
            y = y * (double) (1.0F - lightningMultiplier) + (double) (0.8F * lightningMultiplier);
            z = z * (double) (1.0F - lightningMultiplier) + (double) lightningMultiplier;
        }
        return new Vec3(x, y, z);
    }

    @Inject(method = { "getSkyColor" }, at = { @At("RETURN") })
    private void afterGetSkyColor(Vec3 cameraPos, float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        this.bo$skyColorCache = (Vec3) cir.getReturnValue();
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void afterInit(CallbackInfo ci) {
        this.bo$commonFactors = CommonColorFactors.SKY_COLOR;
        this.bo$lastBiomeColor = Integer.MIN_VALUE;
        this.bo$biomeColorVector = Vec3.ZERO;
        this.bo$biomeColors = BiomeSkyColorGetter.of(this.m_7062_());
    }

    protected MixinClientWorld(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        throw new AssertionError("nuh uh");
    }
}