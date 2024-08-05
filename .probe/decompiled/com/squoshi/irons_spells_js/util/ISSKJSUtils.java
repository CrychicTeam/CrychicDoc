package com.squoshi.irons_spells_js.util;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ISSKJSUtils {

    public static <T> boolean safeCallback(Consumer<T> consumer, T value, String errorMessage) {
        try {
            consumer.accept(value);
            return true;
        } catch (Throwable var4) {
            ConsoleJS.STARTUP.error(errorMessage, var4);
            return false;
        }
    }

    public static record AttributeHolder(ResourceLocation getLocation) implements ISSKJSUtils.ResourceHolder<ISSKJSUtils.AttributeHolder> {

        public static ISSKJSUtils.AttributeHolder of(Object o) {
            return ISSKJSUtils.ResourceHolder.of(o, ISSKJSUtils.AttributeHolder::new);
        }
    }

    public static record DamageTypeHolder(ResourceLocation getLocation) implements ISSKJSUtils.ResourceHolder<ISSKJSUtils.DamageTypeHolder> {

        public static ISSKJSUtils.DamageTypeHolder of(Object o) {
            return ISSKJSUtils.ResourceHolder.of(o, ISSKJSUtils.DamageTypeHolder::new);
        }
    }

    public interface ResourceHolder<T extends ISSKJSUtils.ResourceHolder<T>> {

        static <T extends ISSKJSUtils.ResourceHolder<T>> T of(Object o, Function<ResourceLocation, T> constructor) {
            if (o instanceof String str) {
                return (T) constructor.apply(new ResourceLocation(str));
            } else if (o instanceof ResourceLocation rl) {
                return (T) constructor.apply(rl);
            } else if (o instanceof RegistryObject reg) {
                return (T) constructor.apply(reg.getId());
            } else if (o instanceof BuilderBase builder) {
                return (T) constructor.apply(builder.id);
            } else {
                throw new IllegalArgumentException("Object " + o + " of class " + o.getClass().getName() + " is not valid, should be a String or ResourceLocation.");
            }
        }
    }

    public static record SchoolHolder(ResourceLocation getLocation) implements ISSKJSUtils.ResourceHolder<ISSKJSUtils.SchoolHolder> {

        public static ISSKJSUtils.SchoolHolder of(Object o) {
            return ISSKJSUtils.ResourceHolder.of(o, ISSKJSUtils.SchoolHolder::new);
        }
    }

    public static record SoundEventHolder(ResourceLocation getLocation) implements ISSKJSUtils.ResourceHolder<ISSKJSUtils.SoundEventHolder> {

        public static ISSKJSUtils.SoundEventHolder of(Object o) {
            return ISSKJSUtils.ResourceHolder.of(o, ISSKJSUtils.SoundEventHolder::new);
        }
    }

    public static record SpellHolder(ResourceLocation getLocation) implements ISSKJSUtils.ResourceHolder<ISSKJSUtils.SpellHolder> {

        public static ISSKJSUtils.SpellHolder of(Object o) {
            return ISSKJSUtils.ResourceHolder.of(o, ISSKJSUtils.SpellHolder::new);
        }
    }
}