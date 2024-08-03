package dev.lambdaurora.lambdynlights.mixin;

import dev.lambdaurora.lambdynlights.LambDynLights;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ DebugScreenOverlay.class })
public class DebugHudMixin {

    @Inject(method = { "getGameInformation" }, at = { @At("RETURN") })
    private void onGetLeftText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = (List<String>) cir.getReturnValue();
        LambDynLights ldl = LambDynLights.get();
        StringBuilder builder = new StringBuilder("Dynamic Light Sources: ");
        builder.append(ldl.getLightSourcesCount()).append(" (U: ").append(ldl.getLastUpdateCount());
        if (!LambDynLights.isEnabled()) {
            builder.append(" ; ");
            builder.append(ChatFormatting.RED);
            builder.append("Disabled");
            builder.append(ChatFormatting.RESET);
        }
        builder.append(')');
        list.add(builder.toString());
    }
}