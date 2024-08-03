package net.liopyu.entityjs.mixin;

import dev.latvian.mods.kubejs.command.KubeJSCommands;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ KubeJSCommands.class })
public abstract class KubeJSCommandsMixin {

    @Inject(method = { "reloadStartup" }, at = { @At(value = "RETURN", ordinal = 0) }, remap = false)
    private static void entityjs$onReloadStartup(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        EntityJSHelperClass.errorMessagesLogged.clear();
        EntityJSHelperClass.warningMessagesLogged.clear();
    }
}