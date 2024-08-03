package me.srrapero720.embeddiumplus.mixins.impl.fps;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Locale;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.fps.DebugOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class })
public abstract class GpuUsageMixin {

    @Shadow
    public static int fps;

    @Shadow
    @Final
    public Options options;

    @Shadow
    private MetricsRecorder metricsRecorder;

    @Shadow
    public ClientLevel level;

    @Shadow
    private double gpuUtilization;

    @Unique
    private double embPlus$gpuUsage = 0.0;

    @Redirect(method = { "runTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;isRecording()Z"))
    private boolean redirect$renderDebug(MetricsRecorder instance) {
        return this.level == null ? instance.isRecording() : ((EmbyConfig.FPSDisplaySystemMode) EmbyConfig.fpsDisplaySystemMode.get()).gpu() || instance.isRecording();
    }

    @Redirect(method = { "runTick" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gpuUtilization:D", opcode = 181))
    private void redirect$assign(Minecraft instance, double value) {
        this.embPlus$gpuUsage = value;
    }

    @Inject(method = { "runTick" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gpuUtilization:D", opcode = 180, ordinal = 0, shift = Shift.BEFORE) })
    private void inject$getGPU(boolean pRenderLevel, CallbackInfo ci) {
        this.gpuUtilization = this.embPlus$gpuUsage;
        DebugOverlayEvent.AVERAGE.push(fps);
    }

    @WrapOperation(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;") })
    private String redirect$removeString(Locale l, String format, Object[] args, Operation<String> original) {
        return this.options.renderDebug && !this.metricsRecorder.isRecording() ? (String) original.call(new Object[] { l, format, args }) : "";
    }
}