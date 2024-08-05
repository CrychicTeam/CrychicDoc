package net.minecraftforge.registries.holdersets;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
public interface HolderSetType {

    <T> Codec<? extends ICustomHolderSet<T>> makeCodec(ResourceKey<? extends Registry<T>> var1, Codec<Holder<T>> var2, boolean var3);
}