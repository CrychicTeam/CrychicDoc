package net.minecraftforge.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public interface IForgeHolderSet<T> {

    default void addInvalidationListener(Runnable runnable) {
    }

    default IForgeHolderSet.SerializationType serializationType() {
        return this instanceof HolderSet.ListBacked<T> listBacked ? (IForgeHolderSet.SerializationType) listBacked.m_203440_().map(tag -> IForgeHolderSet.SerializationType.STRING, list -> list.size() == 1 ? (IForgeHolderSet.SerializationType) ((Holder) list.get(0)).unwrap().map(key -> key == null ? IForgeHolderSet.SerializationType.OBJECT : IForgeHolderSet.SerializationType.STRING, value -> IForgeHolderSet.SerializationType.OBJECT) : IForgeHolderSet.SerializationType.LIST) : IForgeHolderSet.SerializationType.UNKNOWN;
    }

    public static enum SerializationType {

        UNKNOWN, STRING, LIST, OBJECT
    }
}