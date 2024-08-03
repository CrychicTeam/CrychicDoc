package dev.architectury.registry.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredSupplier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@EventBusSubscriber(modid = "architectury", bus = Bus.MOD, value = { Dist.CLIENT })
public class CreativeTabRegistryImpl {

    private static final Logger LOGGER = LogManager.getLogger(CreativeTabRegistryImpl.class);

    private static final List<Consumer<BuildCreativeModeTabContentsEvent>> BUILD_CONTENTS_LISTENERS = new ArrayList();

    private static final Multimap<CreativeTabRegistryImpl.TabKey, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();

    @SubscribeEvent
    public static void event(BuildCreativeModeTabContentsEvent event) {
        for (Consumer<BuildCreativeModeTabContentsEvent> listener : BUILD_CONTENTS_LISTENERS) {
            listener.accept(event);
        }
    }

    @Experimental
    public static CreativeModeTab create(Consumer<CreativeModeTab.Builder> callback) {
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        callback.accept(builder);
        return builder.build();
    }

    @Experimental
    public static DeferredSupplier<CreativeModeTab> ofBuiltin(CreativeModeTab tab) {
        ResourceLocation key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key == null) {
            throw new IllegalArgumentException("Builtin tab %s is not registered!".formatted(tab));
        } else {
            return new DeferredSupplier<CreativeModeTab>() {

                @Override
                public ResourceLocation getRegistryId() {
                    return Registries.CREATIVE_MODE_TAB.location();
                }

                @Override
                public ResourceLocation getId() {
                    return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
                }

                @Override
                public boolean isPresent() {
                    return true;
                }

                public CreativeModeTab get() {
                    return tab;
                }
            };
        }
    }

    @Experimental
    public static DeferredSupplier<CreativeModeTab> defer(ResourceLocation name) {
        return new DeferredSupplier<CreativeModeTab>() {

            @Nullable
            private CreativeModeTab tab;

            @Override
            public ResourceLocation getRegistryId() {
                return Registries.CREATIVE_MODE_TAB.location();
            }

            @Override
            public ResourceLocation getId() {
                return name;
            }

            public CreativeModeTab get() {
                this.resolve();
                if (this.tab == null) {
                    throw new IllegalStateException("Creative tab %s was not registered yet!".formatted(name));
                } else {
                    return this.tab;
                }
            }

            @Override
            public boolean isPresent() {
                this.resolve();
                return this.tab != null;
            }

            private void resolve() {
                if (this.tab == null) {
                    this.tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(name);
                }
            }
        };
    }

    public static void modify(DeferredSupplier<CreativeModeTab> tab, CreativeTabRegistry.ModifyTabCallback filler) {
        BUILD_CONTENTS_LISTENERS.add((Consumer) event -> {
            if (tab.isPresent()) {
                if (event.getTab().equals(tab.get())) {
                    filler.accept(event.getFlags(), wrapTabOutput(event.getEntries()), event.hasPermissions());
                }
            } else if (Objects.equals(CreativeModeTabRegistry.getName(event.getTab()), tab.getId())) {
                filler.accept(event.getFlags(), wrapTabOutput(event.getEntries()), event.hasPermissions());
            }
        });
    }

    private static CreativeTabOutput wrapTabOutput(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
        return new CreativeTabOutput() {

            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (after.isEmpty()) {
                    entries.put(stack, visibility);
                } else {
                    entries.putAfter(after, stack, visibility);
                }
            }

            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (before.isEmpty()) {
                    entries.put(stack, visibility);
                } else {
                    entries.putBefore(before, stack, visibility);
                }
            }
        };
    }

    @Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack> item) {
        APPENDS.put(new CreativeTabRegistryImpl.TabKey.SupplierTabKey(tab), item);
    }

    static {
        BUILD_CONTENTS_LISTENERS.add((Consumer) event -> {
            for (Entry<CreativeTabRegistryImpl.TabKey, Collection<Supplier<ItemStack>>> keyEntry : APPENDS.asMap().entrySet()) {
                Supplier<List<ItemStack>> stacks = Suppliers.memoize(() -> ((Collection) keyEntry.getValue()).stream().map(Supplier::get).toList());
                Object patt2971$temp = keyEntry.getKey();
                if (patt2971$temp instanceof CreativeTabRegistryImpl.TabKey.SupplierTabKey) {
                    CreativeTabRegistryImpl.TabKey.SupplierTabKey supplierTabKey = (CreativeTabRegistryImpl.TabKey.SupplierTabKey) patt2971$temp;
                    if (Objects.equals(CreativeModeTabRegistry.getName(event.getTab()), supplierTabKey.supplier().getId())) {
                        for (ItemStack stack : (List) stacks.get()) {
                            event.getEntries().put(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                    }
                } else if (keyEntry.getKey() instanceof CreativeTabRegistryImpl.TabKey.DirectTabKey directTabKey && event.getTab().equals(directTabKey.tab())) {
                    for (ItemStack stack : (List) stacks.get()) {
                        event.getEntries().put(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
                }
            }
        });
    }

    private interface TabKey {

        public static record DirectTabKey(CreativeModeTab tab) implements CreativeTabRegistryImpl.TabKey {

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else {
                    return o instanceof CreativeTabRegistryImpl.TabKey.DirectTabKey that ? this.tab == that.tab : false;
                }
            }

            public int hashCode() {
                return System.identityHashCode(this.tab);
            }
        }

        public static record SupplierTabKey(DeferredSupplier<CreativeModeTab> supplier) implements CreativeTabRegistryImpl.TabKey {

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else {
                    return o instanceof CreativeTabRegistryImpl.TabKey.SupplierTabKey that ? Objects.equals(this.supplier.getId(), that.supplier.getId()) : false;
                }
            }

            public int hashCode() {
                return Objects.hash(new Object[] { this.supplier.getId() });
            }
        }
    }
}