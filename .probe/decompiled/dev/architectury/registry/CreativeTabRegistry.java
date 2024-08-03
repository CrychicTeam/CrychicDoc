package dev.architectury.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.forge.CreativeTabRegistryImpl;
import dev.architectury.registry.registries.DeferredSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus.Experimental;

public final class CreativeTabRegistry {

    private CreativeTabRegistry() {
    }

    public static CreativeModeTab create(Component title, Supplier<ItemStack> icon) {
        return create(builder -> {
            builder.title(title);
            builder.icon(icon);
        });
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static CreativeModeTab create(Consumer<CreativeModeTab.Builder> callback) {
        return CreativeTabRegistryImpl.create(callback);
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static DeferredSupplier<CreativeModeTab> ofBuiltin(CreativeModeTab tab) {
        return CreativeTabRegistryImpl.ofBuiltin(tab);
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static DeferredSupplier<CreativeModeTab> defer(ResourceLocation name) {
        return CreativeTabRegistryImpl.defer(name);
    }

    @Experimental
    public static DeferredSupplier<CreativeModeTab> defer(ResourceKey<CreativeModeTab> name) {
        return defer(name.location());
    }

    @Experimental
    public static void modifyBuiltin(CreativeModeTab tab, CreativeTabRegistry.ModifyTabCallback filler) {
        modify(ofBuiltin(tab), filler);
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static void modify(DeferredSupplier<CreativeModeTab> tab, CreativeTabRegistry.ModifyTabCallback filler) {
        CreativeTabRegistryImpl.modify(tab, filler);
    }

    @Experimental
    public static void appendBuiltin(CreativeModeTab tab, ItemLike... items) {
        append(ofBuiltin(tab), items);
    }

    @Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void appendBuiltin(CreativeModeTab tab, T... items) {
        appendStack(ofBuiltin(tab), Stream.of(items).map(supplier -> () -> new ItemStack((ItemLike) supplier.get())));
    }

    @Experimental
    public static void appendBuiltinStack(CreativeModeTab tab, ItemStack... items) {
        appendStack(ofBuiltin(tab), items);
    }

    @Experimental
    public static void appendBuiltinStack(CreativeModeTab tab, Supplier<ItemStack>... items) {
        appendStack(ofBuiltin(tab), items);
    }

    @Experimental
    public static void append(DeferredSupplier<CreativeModeTab> tab, ItemLike... items) {
        appendStack(tab, Stream.of(items).map(item -> () -> new ItemStack(item)));
    }

    @Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(DeferredSupplier<CreativeModeTab> tab, T... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> new ItemStack((ItemLike) supplier.get())));
    }

    @Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, ItemStack... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> supplier));
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack> item) {
        CreativeTabRegistryImpl.appendStack(tab, item);
    }

    @Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack>... items) {
        for (Supplier<ItemStack> item : items) {
            appendStack(tab, item);
        }
    }

    @Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Stream<Supplier<ItemStack>> items) {
        items.forEach(item -> appendStack(tab, item));
    }

    @Experimental
    public static void append(ResourceKey<CreativeModeTab> tab, ItemLike... items) {
        appendStack(defer(tab), Stream.of(items).map(item -> () -> new ItemStack(item)));
    }

    @Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(ResourceKey<CreativeModeTab> tab, T... items) {
        appendStack(defer(tab), Stream.of(items).map(supplier -> () -> new ItemStack((ItemLike) supplier.get())));
    }

    @Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, ItemStack... items) {
        appendStack(defer(tab), Stream.of(items).map(supplier -> () -> supplier));
    }

    @Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack> item) {
        appendStack(defer(tab), item);
    }

    @Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack>... items) {
        appendStack(defer(tab), items);
    }

    @Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Stream<Supplier<ItemStack>> items) {
        appendStack(defer(tab), items);
    }

    @FunctionalInterface
    public interface ModifyTabCallback {

        void accept(FeatureFlagSet var1, CreativeTabOutput var2, boolean var3);
    }
}