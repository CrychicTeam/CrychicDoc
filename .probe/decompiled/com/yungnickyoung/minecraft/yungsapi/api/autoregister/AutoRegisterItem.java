package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public class AutoRegisterItem extends AutoRegisterEntry<Item> {

    public static AutoRegisterItem of(Supplier<Item> itemSupplier) {
        return new AutoRegisterItem(itemSupplier);
    }

    private AutoRegisterItem(Supplier<Item> itemSupplier) {
        super(itemSupplier);
    }
}