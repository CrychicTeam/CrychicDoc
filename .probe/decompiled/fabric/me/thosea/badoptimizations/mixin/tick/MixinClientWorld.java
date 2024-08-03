package fabric.me.thosea.badoptimizations.mixin.tick;

import fabric.me.thosea.badoptimizations.interfaces.BiomeSkyColorGetter;
import fabric.me.thosea.badoptimizations.other.CommonColorFactors;
import fabric.me.thosea.badoptimizations.other.Config;
import java.util.function.Supplier;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2874;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3695;
import net.minecraft.class_5269;
import net.minecraft.class_5321;
import net.minecraft.class_5455;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ class_638.class })
public abstract class MixinClientWorld extends class_1937 {

    @Shadow
    @Final
    private class_310 field_3729;

    private BiomeSkyColorGetter bo$biomeColors;

    private CommonColorFactors bo$commonFactors;

    private class_243 bo$skyColorCache;

    private int bo$lastBiomeColor;

    private class_243 bo$biomeColorVector;

    @Inject(method = { "getSkyColor" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> cir) {
        if (this.bo$skyColorCache != null && this.field_3729.field_1724 != null) {
            CommonColorFactors.tick(tickDelta);
            if (this.bo$commonFactors.didTickChange()) {
                if (this.bo$isBiomeDirty(cameraPos.method_1023(2.0, 2.0, 2.0).method_1021(0.25))) {
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

    private boolean bo$isBiomeDirty(class_243 pos) {
        int x = class_3532.method_15357(pos.field_1352);
        int y = class_3532.method_15357(pos.field_1351);
        int z = class_3532.method_15357(pos.field_1350);
        int color = this.bo$biomeColors.get(x - 2, y - 2, z - 2);
        if (this.bo$lastBiomeColor != color) {
            this.bo$lastBiomeColor = color;
            this.bo$biomeColorVector = class_243.method_24457(color);
            return true;
        } else {
            return this.bo$biomeColors.get(x + 3, y + 3, z + 3) != color;
        }
    }

    @Shadow
    public abstract int method_23789();

    @Shadow
    public abstract class_243 method_23777(class_243 var1, float var2);

    private class_243 bo$calcSkyColor(float delta) {
        float angle = class_3532.method_15362(this.method_30274(1.0F) * (float) (Math.PI * 2)) * 2.0F + 0.5F;
        angle = class_3532.method_15363(angle, 0.0F, 1.0F);
        double x = this.bo$biomeColorVector.field_1352 * (double) angle;
        double y = this.bo$biomeColorVector.field_1351 * (double) angle;
        double z = this.bo$biomeColorVector.field_1350 * (double) angle;
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
        return new class_243(x, y, z);
    }

    @Inject(method = { "getSkyColor" }, at = { @At("RETURN") })
    private void afterGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> cir) {
        this.bo$skyColorCache = (class_243) cir.getReturnValue();
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void afterInit(CallbackInfo ci) {
        this.bo$commonFactors = CommonColorFactors.SKY_COLOR;
        this.bo$lastBiomeColor = Integer.MIN_VALUE;
        this.bo$biomeColorVector = class_243.field_1353;
        this.bo$biomeColors = BiomeSkyColorGetter.of(this.method_22385());
    }

    protected MixinClientWorld(class_5269 properties, class_5321<class_1937> registryRef, class_5455 registryManager, class_6880<class_2874> dimensionEntry, Supplier<class_3695> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        throw new AssertionError("nuh uh");
    }
}