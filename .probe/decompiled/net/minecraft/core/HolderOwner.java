package net.minecraft.core;

public interface HolderOwner<T> {

    default boolean canSerializeIn(HolderOwner<T> holderOwnerT0) {
        return holderOwnerT0 == this;
    }
}