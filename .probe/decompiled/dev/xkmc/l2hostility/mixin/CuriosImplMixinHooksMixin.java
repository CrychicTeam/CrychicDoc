package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.events.MiscHandlers;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

@Mixin({ CuriosImplMixinHooks.class })
public class CuriosImplMixinHooksMixin {

    @Inject(at = { @At("HEAD") }, method = { "isStackValid" }, cancellable = true, remap = false)
    private static void l2hostility$isStackValid$makeValid(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(LHItems.SEAL.m_5456_())) {
            cir.setReturnValue(MiscHandlers.predicateSlotValid(slotContext, stack));
        }
    }
}