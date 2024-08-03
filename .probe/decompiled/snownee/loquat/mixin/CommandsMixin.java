package snownee.loquat.mixin;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.LoquatConfig;

@Mixin({ Commands.class })
public class CommandsMixin {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = { "performCommand" }, at = { @At(value = "INVOKE", target = "Lorg/slf4j/Logger;isDebugEnabled()Z", remap = false) }, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void loquat$performCommand(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfoReturnable<Integer> cir, CommandSourceStack commandSourceStack, Exception exception, MutableComponent mutableComponent2) {
        if (LoquatConfig.debug && !LOGGER.isDebugEnabled()) {
            LOGGER.error("Command exception: /" + command, exception);
            StackTraceElement[] stackTraceElements = exception.getStackTrace();
            for (int j = 0; j < Math.min(stackTraceElements.length, 3); j++) {
                mutableComponent2.append("\n\n").append(stackTraceElements[j].getMethodName()).append("\n ").append(stackTraceElements[j].getFileName()).append(":").append(String.valueOf(stackTraceElements[j].getLineNumber()));
            }
        }
    }
}