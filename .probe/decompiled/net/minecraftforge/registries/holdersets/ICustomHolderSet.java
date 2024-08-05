package net.minecraftforge.registries.holdersets;

import net.minecraft.core.HolderSet;
import net.minecraftforge.common.extensions.IForgeHolderSet;

public interface ICustomHolderSet<T> extends HolderSet<T> {

    HolderSetType type();

    default IForgeHolderSet.SerializationType serializationType() {
        return IForgeHolderSet.SerializationType.OBJECT;
    }
}