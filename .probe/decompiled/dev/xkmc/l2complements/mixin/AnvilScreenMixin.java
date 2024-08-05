package dev.xkmc.l2complements.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AnvilScreen.class })
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    public AnvilScreenMixin(AnvilMenu anvilMenu0, Inventory inventory1, Component component2, ResourceLocation resourceLocation3) {
        super(anvilMenu0, inventory1, component2, resourceLocation3);
    }

    @Inject(at = { @At("HEAD") }, method = { "keyPressed" }, cancellable = true)
    public void l2complements$removed$preventEarlyRemoval(int int0, int int1, int int2, CallbackInfoReturnable<Boolean> cir) {
        if (int0 == 256 && super.m_7933_(int0, int1, int2)) {
            cir.setReturnValue(true);
        }
    }
}