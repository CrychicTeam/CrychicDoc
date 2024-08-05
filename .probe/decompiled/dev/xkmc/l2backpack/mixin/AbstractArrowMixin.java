package dev.xkmc.l2backpack.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2backpack.events.CapabilityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ AbstractArrow.class })
public class AbstractArrowMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z") }, method = { "tryPickup" })
    public boolean l2backpack$playerTouch$tryArrowPickup(Inventory inv, ItemStack stack, Operation<Boolean> op) {
        int old = stack.getCount();
        if (!stack.isDamageableItem() && inv.player instanceof ServerPlayer sp) {
            CapabilityEvents.tryInsertItem(sp, stack);
        }
        if (stack.isEmpty()) {
            return true;
        } else {
            boolean success = stack.getCount() < old;
            boolean ans = (Boolean) op.call(new Object[] { inv, stack });
            return ans || success;
        }
    }
}