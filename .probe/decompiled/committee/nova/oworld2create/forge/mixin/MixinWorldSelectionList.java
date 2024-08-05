package committee.nova.oworld2create.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ WorldSelectionList.class })
public abstract class MixinWorldSelectionList {

    @Redirect(method = { "loadLevels" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;openFresh(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void redirect$loadLevels(Minecraft mc, Screen screen) {
    }
}