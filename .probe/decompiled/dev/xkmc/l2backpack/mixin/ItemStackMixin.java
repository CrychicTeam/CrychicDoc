package dev.xkmc.l2backpack.mixin;

import dev.xkmc.l2backpack.events.ArrowBagEvents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemStack.class })
public class ItemStackMixin {

    @Inject(at = { @At("HEAD") }, method = { "shrink" })
    public void l2backpack_shrinkListener(int count, CallbackInfo ci) {
        ArrowBagEvents.shrink((ItemStack) this, count);
    }
}