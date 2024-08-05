package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.ModernUIClient;
import net.minecraft.client.telemetry.ClientTelemetryManager;
import net.minecraft.client.telemetry.TelemetryEventSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ClientTelemetryManager.class })
public class MixinClientTelemetryManager {

    @Inject(method = { "createEventSender" }, at = { @At("HEAD") }, cancellable = true)
    private void onCreateTelemetrySession(CallbackInfoReturnable<TelemetryEventSender> cir) {
        if (ModernUIClient.sRemoveTelemetrySession) {
            cir.setReturnValue(TelemetryEventSender.DISABLED);
        }
    }
}