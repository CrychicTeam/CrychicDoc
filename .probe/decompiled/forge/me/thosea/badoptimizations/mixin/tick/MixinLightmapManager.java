package forge.me.thosea.badoptimizations.mixin.tick;

import forge.me.thosea.badoptimizations.mixin.accessor.GameRendererAccessor;
import forge.me.thosea.badoptimizations.mixin.accessor.PlayerAccessor;
import forge.me.thosea.badoptimizations.other.CommonColorFactors;
import forge.me.thosea.badoptimizations.other.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LightTexture.class })
public abstract class MixinLightmapManager {

    @Shadow
    @Final
    private Minecraft minecraft;

    private CommonColorFactors bo$commonFactors;

    private boolean bo$allowUpdate = false;

    private double bo$lastGamma;

    private DimensionSpecialEffects bo$lastDimension;

    private boolean bo$lastNightVision;

    private boolean bo$lastConduitPower;

    private float bo$previousSkyDarkness;

    private GameRendererAccessor bo$gameRendererAccessor;

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void onInit(GameRenderer renderer, Minecraft client, CallbackInfo ci) {
        this.bo$gameRendererAccessor = (GameRendererAccessor) renderer;
        this.bo$commonFactors = CommonColorFactors.LIGHTMAP;
    }

    private boolean bo$isDirty() {
        boolean result = false;
        DimensionSpecialEffects dimension = this.minecraft.level.effects();
        if (this.bo$lastDimension != dimension) {
            this.bo$lastDimension = dimension;
            result = true;
        }
        float skyDarkness = this.bo$gameRendererAccessor.bo$getSkyDarkness();
        if (this.bo$previousSkyDarkness != skyDarkness) {
            this.bo$previousSkyDarkness = skyDarkness;
            result = true;
        }
        double gamma = this.minecraft.options.gamma().get();
        if (this.bo$lastGamma != gamma) {
            this.bo$lastGamma = gamma;
            result = true;
        }
        PlayerAccessor accessor = (PlayerAccessor) this.minecraft.player;
        if (this.minecraft.player.isUnderWater() && accessor.bo$underwaterVisibilityTicks() < 600) {
            result = true;
        }
        MobEffectInstance nightVision = this.minecraft.player.m_21124_(MobEffects.NIGHT_VISION);
        boolean hasNightVision = nightVision != null;
        if (this.bo$lastNightVision != hasNightVision) {
            this.bo$lastNightVision = hasNightVision;
            result = true;
        } else if (nightVision != null && nightVision.endsWithin(200)) {
            result = true;
        }
        boolean conduitPower = this.minecraft.player.m_21023_(MobEffects.CONDUIT_POWER);
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
        if (this.minecraft.player != null) {
            CommonColorFactors.tick(this.minecraft.getFrameTime());
            if (this.bo$commonFactors.didTickChange() && this.bo$commonFactors.isDirty() | this.bo$isDirty()) {
                this.bo$commonFactors.updateLastTime();
                this.bo$allowUpdate = true;
                this.tick();
                this.bo$allowUpdate = false;
            }
        }
    }

    @Shadow
    public abstract void tick();

    @Inject(method = { "tick" }, at = { @At("HEAD") }, cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (!this.bo$allowUpdate) {
            ci.cancel();
        }
    }
}