package portb.biggerstacks.mixin.vanilla;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ AnvilMenu.class })
public class AnvilMenuMixin {

    @Redirect(method = { "onTake" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 3))
    void fixStackedEnchantedBooksBeingDeleted(Container inputSlots, int i, ItemStack itemStack) {
        inputSlots.getItem(1).shrink(1);
    }
}