package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ItemFrameEntityKJS extends EntityKJS {

    default ItemFrame kjs$self() {
        return (ItemFrame) this;
    }

    @Override
    default boolean kjs$isFrame() {
        return true;
    }

    @Nullable
    @Override
    default ItemStack kjs$getItem() {
        ItemStack stack = this.kjs$self().getItem();
        return stack.isEmpty() ? null : stack;
    }
}