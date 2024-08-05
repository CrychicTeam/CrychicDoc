package org.violetmoon.quark.mixin.mixins;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.building.module.ShearVinesModule;

@Mixin({ ShearsItem.class })
public class ShearsItemMixin {

    @Inject(method = { "getDestroySpeed" }, at = { @At("HEAD") }, cancellable = true)
    public void getDestroySpeed(ItemStack pStack, BlockState pState, CallbackInfoReturnable<Float> cbi) {
        if (pState.m_60713_(ShearVinesModule.cut_vine)) {
            cbi.setReturnValue(2.0F);
        }
    }
}