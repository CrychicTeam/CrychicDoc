package dev.shadowsoffire.placebo.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ AbstractContainerMenu.class })
public interface AbstractContainerMenuInvoker {

    @Invoker("moveItemStackTo")
    boolean _moveItemStackTo(ItemStack var1, int var2, int var3, boolean var4);
}