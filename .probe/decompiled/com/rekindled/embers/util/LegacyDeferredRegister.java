package com.rekindled.embers.util;

import java.util.function.Supplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LegacyDeferredRegister<T> {

    DeferredRegister<T> current;

    DeferredRegister<T> old;

    public LegacyDeferredRegister(DeferredRegister<T> current, DeferredRegister<T> old) {
        this.current = current;
        this.old = old;
    }

    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> sup) {
        RegistryObject<I> reg = this.current.register(name, sup);
        this.old.register(name, sup);
        return reg;
    }
}