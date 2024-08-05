package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueLabeledSwitchCycleButton;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.worldselection.SwitchGrid;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ SwitchGrid.SwitchBuilder.class })
public class MixinSwitchGrid_SwitchBuilder {

    @Shadow
    @Final
    private Component label;

    @WrapOperation(method = { "build" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;") })
    private CycleButton<Boolean> wrapCreateCycleButtonInBuild_FancyMenu(CycleButton.Builder<Boolean> instance, int int0, int int1, int int2, int int3, Component label, CycleButton.OnValueChange<Boolean> cycleButtonOnValueChangeBoolean4, Operation<CycleButton<Boolean>> original) {
        CycleButton<Boolean> button = (CycleButton<Boolean>) original.call(new Object[] { instance, int0, int1, int2, int3, label, cycleButtonOnValueChangeBoolean4 });
        ((UniqueLabeledSwitchCycleButton) button).setLabeledSwitchComponentLabel_FancyMenu(this.label);
        return button;
    }
}