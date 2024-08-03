package dev.latvian.mods.kubejs.event;

import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class Extra {

    public static final Extra STRING = new Extra().transformer(Extra::toString);

    public static final Extra REQUIRES_STRING = STRING.copy().required();

    public static final Extra ID = new Extra().transformer(Extra::toResourceLocation);

    public static final Extra REQUIRES_ID = ID.copy().required();

    public static final Extra REGISTRY = new Extra().transformer(Extra::toRegistryKey).identity();

    public static final Extra REQUIRES_REGISTRY = REGISTRY.copy().required();

    public Extra.Transformer transformer = Extra.Transformer.IDENTITY;

    public boolean identity = false;

    public boolean required = false;

    public Predicate<Object> validator = UtilsJS.ALWAYS_TRUE;

    public Extra.Transformer toString = Extra.Transformer.IDENTITY;

    public Function<DescriptionContext, TypeDescJS> describeType = context -> TypeDescJS.STRING;

    private static String toString(Object object) {
        if (object == null) {
            return null;
        } else {
            String s = object.toString();
            return s.isBlank() ? null : s;
        }
    }

    private static ResourceLocation toResourceLocation(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof ResourceLocation) {
            return (ResourceLocation) object;
        } else {
            String s = object.toString();
            return s.isBlank() ? null : ResourceLocation.tryParse(s);
        }
    }

    private static ResourceKey<? extends Registry<?>> toRegistryKey(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof ResourceKey) {
            return (ResourceKey<? extends Registry<?>>) object;
        } else if (object instanceof ResourceLocation rl) {
            return ResourceKey.createRegistryKey(rl);
        } else {
            String s = object.toString();
            return s.isBlank() ? null : ResourceKey.createRegistryKey(new ResourceLocation(s));
        }
    }

    public Extra copy() {
        Extra t = new Extra();
        t.transformer = this.transformer;
        t.identity = this.identity;
        t.required = this.required;
        t.validator = this.validator;
        t.toString = this.toString;
        return t;
    }

    public Extra transformer(Extra.Transformer factory) {
        this.transformer = factory;
        return this;
    }

    public Extra identity() {
        this.identity = true;
        return this;
    }

    public Extra required() {
        this.required = true;
        return this;
    }

    public Extra validator(Predicate<Object> validator) {
        this.validator = validator;
        return this;
    }

    public Extra describeType(Function<DescriptionContext, TypeDescJS> describeType) {
        this.describeType = describeType;
        return this;
    }

    public Extra toString(Extra.Transformer factory) {
        this.toString = factory;
        return this;
    }

    @FunctionalInterface
    public interface Transformer {

        Extra.Transformer IDENTITY = o -> o;

        @Nullable
        Object transform(Object var1);
    }
}