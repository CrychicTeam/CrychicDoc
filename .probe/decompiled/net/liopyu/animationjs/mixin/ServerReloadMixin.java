package net.liopyu.animationjs.mixin;

import dev.latvian.mods.kubejs.command.KubeJSCommands;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ KubeJSCommands.class })
public abstract class ServerReloadMixin {

    @Inject(method = { "reloadServer" }, at = { @At(value = "RETURN", ordinal = 0) }, remap = false)
    private static void animationJS$onReloadServer(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        AnimationJSHelperClass.serverErrorMessagesLogged.clear();
        AnimationJSHelperClass.serverWarningMessagesLogged.clear();
    }
}