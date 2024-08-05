package dev.xkmc.l2library.base;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.EnchantmentBuilder;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.builders.EnchantmentBuilder.EnchantmentFactory;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2serial.serialization.custom_handler.RLClassHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;

public class L2Registrate extends AbstractRegistrate<L2Registrate> {

    public L2Registrate(String modid) {
        super(modid);
        this.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public <T extends NamedEntry<T>, P extends T> L2Registrate.GenericBuilder<T, P> generic(L2Registrate.RegistryInstance<T> cls, String id, NonNullSupplier<P> sup) {
        return (L2Registrate.GenericBuilder<T, P>) this.entry(id, cb -> new L2Registrate.GenericBuilder<>(this, id, cb, cls.key(), sup));
    }

    public <T extends Recipe<?>> RegistryEntry<RecipeType<T>> recipe(String id) {
        return this.simple(id, ForgeRegistries.Keys.RECIPE_TYPES, () -> new RecipeType<T>() {
        });
    }

    @Deprecated
    public <T extends Enchantment> EnchantmentBuilder<T, L2Registrate> enchantment(String name, EnchantmentCategory type, EnchantmentFactory<T> factory) {
        return super.enchantment(name, type, factory);
    }

    public <T extends Enchantment> EnchantmentBuilder<T, L2Registrate> enchantment(String name, EnchantmentCategory type, EnchantmentFactory<T> factory, String desc) {
        this.addRawLang("enchantment." + this.getModid() + "." + name + ".desc", desc);
        return super.enchantment(name, type, factory);
    }

    public <T extends MobEffect> NoConfigBuilder<MobEffect, T, L2Registrate> effect(String name, NonNullSupplier<T> sup, String desc) {
        this.addRawLang("effect." + this.getModid() + "." + name + ".description", desc);
        return (NoConfigBuilder<MobEffect, T, L2Registrate>) this.entry(name, cb -> new NoConfigBuilder(this, this, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup));
    }

    public <E extends NamedEntry<E>> L2Registrate.RegistryInstance<E> newRegistry(String id, Class<?> cls, Consumer<RegistryBuilder<E>> cons) {
        ResourceKey<Registry<E>> key = this.makeRegistry(id, () -> {
            RegistryBuilder<E> ans = new RegistryBuilder<>();
            ans.onCreate((r, s) -> new RLClassHandler(cls, () -> r));
            cons.accept(ans);
            return ans;
        });
        return new L2Registrate.RegistryInstance<>(Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(key)), key);
    }

    public <E extends NamedEntry<E>> L2Registrate.RegistryInstance<E> newRegistry(String id, Class<?> cls) {
        return this.newRegistry(id, cls, e -> {
        });
    }

    public synchronized RegistryEntry<CreativeModeTab> buildModCreativeTab(String name, String def, Consumer<CreativeModeTab.Builder> config) {
        ResourceLocation id = new ResourceLocation(this.getModid(), name);
        this.defaultCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, id));
        return this.buildCreativeTabImpl(name, this.addLang("itemGroup", id, def), config);
    }

    public synchronized RegistryEntry<CreativeModeTab> buildL2CreativeTab(String name, String def, Consumer<CreativeModeTab.Builder> config) {
        ResourceLocation id = new ResourceLocation("l2library", name);
        this.defaultCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, id));
        L2Registrate.TabSorter sorter = new L2Registrate.TabSorter(this.getModid() + ":" + name, id);
        return L2Library.REGISTRATE.buildCreativeTabImpl(name, this.addLang("itemGroup", id, def), b -> {
            config.accept(b);
            sorter.sort(b);
        });
    }

    private synchronized RegistryEntry<CreativeModeTab> buildCreativeTabImpl(String name, Component comp, Consumer<CreativeModeTab.Builder> config) {
        return this.generic((L2Registrate) this.self(), name, Registries.CREATIVE_MODE_TAB, () -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder().title(comp).withTabsBefore(new ResourceKey[] { CreativeModeTabs.SPAWN_EGGS });
            config.accept(builder);
            return builder.build();
        }).register();
    }

    public static class GenericBuilder<T extends NamedEntry<T>, P extends T> extends AbstractBuilder<T, P, L2Registrate, L2Registrate.GenericBuilder<T, P>> {

        private final NonNullSupplier<P> sup;

        GenericBuilder(L2Registrate parent, String name, BuilderCallback callback, ResourceKey<Registry<T>> registryType, NonNullSupplier<P> sup) {
            super(parent, parent, name, callback, registryType);
            this.sup = sup;
        }

        @NonnullType
        @NotNull
        protected P createEntry() {
            return (P) ((NamedEntry) this.sup.get());
        }

        public L2Registrate.GenericBuilder<T, P> defaultLang() {
            return (L2Registrate.GenericBuilder<T, P>) this.lang(NamedEntry::getDescriptionId, RegistrateLangProvider.toEnglishName(this.getName()));
        }
    }

    public static record RegistryInstance<E extends NamedEntry<E>>(Supplier<IForgeRegistry<E>> supplier, ResourceKey<Registry<E>> key) implements Supplier<IForgeRegistry<E>> {

        public IForgeRegistry<E> get() {
            return (IForgeRegistry<E>) this.supplier().get();
        }
    }

    private static class TabSorter {

        private static final TreeMap<String, L2Registrate.TabSorter> MAP = new TreeMap();

        private static final HashSet<ResourceLocation> SET = new HashSet();

        private final ResourceLocation id;

        private TabSorter(String str, ResourceLocation id) {
            MAP.put(str, this);
            SET.add(id);
            this.id = id;
        }

        public void sort(CreativeModeTab.Builder b) {
            ArrayList<L2Registrate.TabSorter> list = new ArrayList(MAP.values());
            boolean after = false;
            ResourceLocation before = null;
            for (L2Registrate.TabSorter e : list) {
                if (e == this) {
                    after = true;
                    if (before != null) {
                        b.withTabsBefore(new ResourceLocation[] { before });
                    }
                } else {
                    if (after) {
                        b.withTabsAfter(new ResourceLocation[] { e.id });
                        return;
                    }
                    before = e.id;
                }
            }
            for (Entry<ResourceKey<CreativeModeTab>, CreativeModeTab> ex : BuiltInRegistries.CREATIVE_MODE_TAB.entrySet()) {
                ResourceLocation id = ((ResourceKey) ex.getKey()).location();
                if (!known(id) && !known((CreativeModeTab) ex.getValue())) {
                    b.withTabsAfter(new ResourceLocation[] { id });
                }
            }
        }

        private static boolean known(ResourceLocation id) {
            return id.getNamespace().equals("minecraft") ? true : SET.contains(id);
        }

        private static boolean known(CreativeModeTab tab) {
            for (ResourceLocation other : tab.tabsAfter) {
                if (known(other)) {
                    return true;
                }
            }
            return false;
        }
    }
}