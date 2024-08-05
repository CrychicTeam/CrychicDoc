package committee.nova.oworld2create.fabric.mixin;

import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_528;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ class_528.class })
public abstract class MixinWorldSelectionList {

    @Redirect(method = { "loadLevels" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;openFresh(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void redirect$loadLevels(class_310 minecraft, class_437 screen) {
    }
}