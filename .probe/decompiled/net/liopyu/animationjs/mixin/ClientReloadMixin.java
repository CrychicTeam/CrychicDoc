package net.liopyu.animationjs.mixin;

import dev.latvian.mods.kubejs.command.KubeJSCommands;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ KubeJSCommands.class })
public abstract class ClientReloadMixin {

    @Inject(method = { "reloadClient" }, at = { @At(value = "RETURN", ordinal = 0) }, remap = false)
    private static void animationJS$onReloadClient(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        AnimationJSHelperClass.clientErrorMessagesLogged.clear();
        AnimationJSHelperClass.clientWarningMessagesLogged.clear();
    }
}