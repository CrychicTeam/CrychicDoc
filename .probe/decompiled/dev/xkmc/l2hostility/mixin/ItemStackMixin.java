package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.events.MiscHandlers;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ItemStack.class })
public abstract class ItemStackMixin {

    @Inject(at = { @At("HEAD") }, method = { "inventoryTick" })
    public void l2hostility_stackTick(Level level, Entity user, int slot, boolean selected, CallbackInfo ci) {
        EnchantmentDisabler.tickStack(level, user, (ItemStack) Wrappers.cast(this));
    }

    @Inject(at = { @At("HEAD") }, method = { "useOn" }, cancellable = true)
    public void l2hostility_noBuild(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        if (MiscHandlers.useOnSkip(ctx, (ItemStack) Wrappers.cast(this))) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}