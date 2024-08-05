package fabric.me.thosea.badoptimizations.mixin.tick;

import fabric.me.thosea.badoptimizations.mixin.accessor.GameRendererAccessor;
import fabric.me.thosea.badoptimizations.mixin.accessor.PlayerAccessor;
import fabric.me.thosea.badoptimizations.other.CommonColorFactors;
import fabric.me.thosea.badoptimizations.other.Config;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_310;
import net.minecraft.class_5294;
import net.minecraft.class_757;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_765.class })
public abstract class MixinLightmapManager {

    @Shadow
    @Final
    private class_310 field_4137;

    private CommonColorFactors bo$commonFactors;

    private boolean bo$allowUpdate = false;

    private double bo$lastGamma;

    private class_5294 bo$lastDimension;

    private boolean bo$lastNightVision;

    private boolean bo$lastConduitPower;

    private float bo$previousSkyDarkness;

    private GameRendererAccessor bo$gameRendererAccessor;

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void onInit(class_757 renderer, class_310 client, CallbackInfo ci) {
        this.bo$gameRendererAccessor = (GameRendererAccessor) renderer;
        this.bo$commonFactors = CommonColorFactors.LIGHTMAP;
    }

    private boolean bo$isDirty() {
        boolean result = false;
        class_5294 dimension = this.field_4137.field_1687.method_28103();
        if (this.bo$lastDimension != dimension) {
            this.bo$lastDimension = dimension;
            result = true;
        }
        float skyDarkness = this.bo$gameRendererAccessor.bo$getSkyDarkness();
        if (this.bo$previousSkyDarkness != skyDarkness) {
            this.bo$previousSkyDarkness = skyDarkness;
            result = true;
        }
        double gamma = (Double) this.field_4137.field_1690.method_42473().method_41753();
        if (this.bo$lastGamma != gamma) {
            this.bo$lastGamma = gamma;
            result = true;
        }
        PlayerAccessor accessor = (PlayerAccessor) this.field_4137.field_1724;
        if (this.field_4137.field_1724.method_5869() && accessor.bo$underwaterVisibilityTicks() < 600) {
            result = true;
        }
        class_1293 nightVision = this.field_4137.field_1724.method_6112(class_1294.field_5925);
        boolean hasNightVision = nightVision != null;
        if (this.bo$lastNightVision != hasNightVision) {
            this.bo$lastNightVision = hasNightVision;
            result = true;
        } else if (nightVision != null && nightVision.method_48557(200)) {
            result = true;
        }
        boolean conduitPower = this.field_4137.field_1724.method_6059(class_1294.field_5927);
        if (this.bo$lastConduitPower != conduitPower) {
            this.bo$lastConduitPower = conduitPower;
            result = true;
        }
        if (this.bo$commonFactors.getTimeDelta() >= (long) Config.lightmap_time_change_needed_for_update) {
            result = true;
        }
        return result;
    }

    @Inject(method = { "enable" }, at = { @At("TAIL") })
    private void onEnable(CallbackInfo ci) {
        if (this.field_4137.field_1724 != null) {
            CommonColorFactors.tick(this.field_4137.method_1488());
            if (this.bo$commonFactors.didTickChange() && this.bo$commonFactors.isDirty() | this.bo$isDirty()) {
                this.bo$commonFactors.updateLastTime();
                this.bo$allowUpdate = true;
                this.method_3314();
                this.bo$allowUpdate = false;
            }
        }
    }

    @Shadow
    public abstract void method_3314();

    @Inject(method = { "tick" }, at = { @At("HEAD") }, cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (!this.bo$allowUpdate) {
            ci.cancel();
        }
    }
}