package com.prunoideae.powerfuljs.events.forge;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Predicate;
import net.minecraftforge.common.capabilities.CapabilityProvider;

public abstract class DynamicAttachEventJS<T extends CapabilityProvider<T>> extends EventJS {

    public abstract DynamicAttachEventJS<T> add(Predicate<T> var1, CapabilityBuilderForge<T, ?> var2);
}