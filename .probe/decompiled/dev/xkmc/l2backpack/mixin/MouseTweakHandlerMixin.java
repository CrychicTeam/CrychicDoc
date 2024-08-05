package dev.xkmc.l2backpack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.xkmc.l2backpack.compat.MouseTweakCompat;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yalter.mousetweaks.MouseButton;

@Pseudo
@Mixin(targets = { "yalter.mousetweaks.handlers.GuiContainerHandler" }, remap = false)
public class MouseTweakHandlerMixin {

    @Shadow
    @Final
    private AbstractContainerScreen<?> screen;

    @Inject(at = { @At("HEAD") }, method = { "clickSlot" }, cancellable = true)
    private void l2backpack$clickSlot$clickDrawer(Slot slot, MouseButton btn, boolean shift, CallbackInfo ci, @Local(argsOnly = true) LocalRef<MouseButton> btnRef) {
        if (MouseTweakCompat.delegateSlotClick(this.screen, slot, btn, shift, btnRef::set)) {
            ci.cancel();
        }
    }
}