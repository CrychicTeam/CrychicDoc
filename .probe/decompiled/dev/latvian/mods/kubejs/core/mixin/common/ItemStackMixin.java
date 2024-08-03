package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.ItemStackKJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("kjs$")
@Mixin({ ItemStack.class })
public abstract class ItemStackMixin implements ItemStackKJS {

    @Shadow
    @RemapForJS("enchantStack")
    public abstract void enchant(Enchantment var1, int var2);

    @Shadow
    @RemapForJS("getNbt")
    public abstract CompoundTag getTag();

    @Shadow
    @RemapForJS("setNbt")
    public abstract void setTag(CompoundTag var1);

    @Shadow
    @RemapForJS("hasNBT")
    public abstract boolean hasTag();
}