package dev.xkmc.l2backpack.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.tool.IBagTool;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ AbstractContainerScreen.class })
public class AbstractContainerScreenMixin {

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z") }, method = { "renderTooltip" })
    public boolean l2backpack$renderTooltips$isEmpty(ItemStack stack, Operation<Boolean> bool) {
        if (stack.getItem() instanceof IBagTool tool && this.hoveredSlot != null && this.hoveredSlot.getItem().getItem() instanceof PickupBagItem) {
            return true;
        }
        return (Boolean) bool.call(new Object[] { stack });
    }
}