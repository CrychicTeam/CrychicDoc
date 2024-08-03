package noppes.npcs.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ AbstractContainerMenu.class })
public interface AbstractContainerMenuMixin {

    @Accessor("lastSlots")
    NonNullList<ItemStack> lastSlots();

    @Accessor("remoteSlots")
    NonNullList<ItemStack> remoteSlots();
}