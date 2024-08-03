package com.craisinlord.integrated_api.mixins.blocks;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { StructureBlockEditScreen.class }, priority = 995)
public class StructureBlockScreenMixin {

    @Shadow
    private EditBox nameEdit;

    @Inject(method = { "init" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setMaxLength(I)V", ordinal = 0, shift = Shift.AFTER) })
    private void integrated_api_makeFileNameLonger(CallbackInfo ci) {
        this.nameEdit.setMaxLength(256);
    }
}