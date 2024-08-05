package pm.meh.icterine.mixin;

import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.icterine.util.ItemStackUtil;
import pm.meh.icterine.util.LogHelper;

@Mixin({ AbstractContainerMenu.class })
abstract class AbstractContainerMenuMixin {

    @Redirect(method = { "addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 1))
    protected boolean addSlot(NonNullList<ItemStack> lastSlots, Object emptyStack, Slot slot) {
        if (slot.container instanceof Inventory && slot.hasItem()) {
            LogHelper.debug((Supplier<String>) (() -> "Adding %s to lastSlots".formatted(slot.getItem())));
            lastSlots.add(slot.getItem());
        } else {
            lastSlots.add(ItemStack.EMPTY);
        }
        return true;
    }

    @Inject(method = { "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack, Supplier<ItemStack> newStackSupplier, CallbackInfo ci, ItemStack oldStack, ItemStack newStack1) {
        ItemStackUtil.processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }
}