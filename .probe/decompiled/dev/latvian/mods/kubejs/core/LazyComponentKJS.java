package dev.latvian.mods.kubejs.core;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface LazyComponentKJS extends Supplier<Component> {

    Component get();
}