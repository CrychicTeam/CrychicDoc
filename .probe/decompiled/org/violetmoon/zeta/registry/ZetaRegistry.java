package org.violetmoon.zeta.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Lifecycle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.item.ZetaBlockItem;
import org.violetmoon.zeta.util.RegisterDynamicUtil;

public abstract class ZetaRegistry {

    protected final Zeta z;

    private final Multimap<ResourceLocation, Supplier<Object>> defers = ArrayListMultimap.create();

    protected final Map<Object, ResourceLocation> internalNames = new IdentityHashMap();

    private final Map<Block, String> blocksToColorProviderName = new HashMap();

    private final Map<Item, String> itemsToColorProviderName = new HashMap();

    private final Map<ResourceKey<Registry<?>>, List<ZetaRegistry.DynamicEntry<?>>> dynamicDefers = new HashMap();

    public ZetaRegistry(Zeta z) {
        this.z = z;
    }

    public <T> ResourceLocation getRegistryName(T obj, Registry<T> registry) {
        ResourceLocation internal = (ResourceLocation) this.internalNames.get(obj);
        return internal == null ? registry.getKey(obj) : internal;
    }

    public ResourceLocation newResourceLocation(String in) {
        return in.indexOf(58) == -1 ? new ResourceLocation(this.z.modid, in) : new ResourceLocation(in);
    }

    public <T> void register(T obj, ResourceLocation id, ResourceKey<Registry<T>> registry) {
        if (obj == null) {
            throw new IllegalArgumentException("Can't register null object.");
        } else {
            if (obj instanceof Block block && obj instanceof IZetaBlockColorProvider provider && provider.getBlockColorProviderName() != null) {
                this.blocksToColorProviderName.put(block, provider.getBlockColorProviderName());
            }
            if (obj instanceof Item item && obj instanceof IZetaItemColorProvider provider && provider.getItemColorProviderName() != null) {
                this.itemsToColorProviderName.put(item, provider.getItemColorProviderName());
            }
            this.internalNames.put(obj, id);
            this.defers.put(registry.location(), (Supplier) () -> obj);
        }
    }

    public <T> void register(T obj, String resloc, ResourceKey<Registry<T>> registry) {
        this.register(obj, this.newResourceLocation(resloc), registry);
    }

    public void registerItem(Item item, ResourceLocation id) {
        this.register(item, id, Registries.ITEM);
    }

    public void registerItem(Item item, String resloc) {
        this.register(item, this.newResourceLocation(resloc), Registries.ITEM);
    }

    public void registerBlock(Block block, ResourceLocation id, boolean hasBlockItem) {
        this.register(block, id, Registries.BLOCK);
        if (hasBlockItem) {
            this.defers.put(Registries.ITEM.location(), (Supplier) () -> this.createItemBlock(block));
        }
    }

    public void registerBlock(Block block, String resloc, boolean hasBlockItem) {
        this.registerBlock(block, this.newResourceLocation(resloc), hasBlockItem);
    }

    public void registerBlock(Block block, ResourceLocation id) {
        this.registerBlock(block, id, true);
    }

    public void registerBlock(Block block, String resloc) {
        this.registerBlock(block, resloc, true);
    }

    private Item createItemBlock(Block block) {
        Item.Properties props = new Item.Properties();
        ResourceLocation registryName = (ResourceLocation) this.internalNames.get(block);
        if (block instanceof IZetaItemPropertiesFiller filler) {
            filler.fillItemProperties(props);
        }
        BlockItem blockitem;
        if (block instanceof IZetaBlockItemProvider) {
            blockitem = ((IZetaBlockItemProvider) block).provideItemBlock(block, props);
        } else {
            blockitem = new ZetaBlockItem(block, props);
        }
        if (block instanceof IZetaItemColorProvider prov && prov.getItemColorProviderName() != null) {
            this.itemsToColorProviderName.put(blockitem, prov.getItemColorProviderName());
        }
        this.internalNames.put(blockitem, registryName);
        return blockitem;
    }

    public Collection<Supplier<Object>> getDefers(ResourceLocation registryId) {
        return this.defers.get(registryId);
    }

    public void clearDeferCache(ResourceLocation resourceLocation) {
        this.defers.removeAll(resourceLocation);
    }

    public void finalizeBlockColors(BiConsumer<Block, String> consumer) {
        this.blocksToColorProviderName.forEach(consumer);
        this.blocksToColorProviderName.clear();
    }

    public void finalizeItemColors(BiConsumer<Item, String> consumer) {
        this.itemsToColorProviderName.forEach(consumer);
        this.itemsToColorProviderName.clear();
    }

    private <T> ResourceKey<Registry<?>> erase(ResourceKey<? extends Registry<T>> weeeejava) {
        return (ResourceKey<Registry<?>>) weeeejava;
    }

    public <T> LateBoundHolder<T> registerDynamicF(Function<RegistryOps.RegistryInfoLookup, T> objCreator, ResourceKey<T> id, ResourceKey<? extends Registry<T>> registry) {
        RegisterDynamicUtil.signup(this.z);
        LateBoundHolder<T> hell = new LateBoundHolder<>(id);
        ((List) this.dynamicDefers.computeIfAbsent(this.erase(registry), __ -> new ArrayList())).add(new ZetaRegistry.DynamicEntry<>(id, objCreator, hell));
        return hell;
    }

    public <T> LateBoundHolder<T> registerDynamicF(Function<RegistryOps.RegistryInfoLookup, T> objCreator, ResourceLocation id, ResourceKey<? extends Registry<T>> registry) {
        return this.registerDynamicF(objCreator, ResourceKey.create(registry, id), registry);
    }

    public <T> LateBoundHolder<T> registerDynamicF(Function<RegistryOps.RegistryInfoLookup, T> objCreator, String regname, ResourceKey<? extends Registry<T>> registry) {
        return this.registerDynamicF(objCreator, this.newResourceLocation(regname), registry);
    }

    @Deprecated(forRemoval = true)
    public <T> Holder.Direct<T> registerDynamic(T obj, ResourceKey<T> id, ResourceKey<? extends Registry<T>> registry) {
        RegisterDynamicUtil.signup(this.z);
        ((List) this.dynamicDefers.computeIfAbsent(this.erase(registry), __ -> new ArrayList())).add(new ZetaRegistry.DynamicEntry<>(id, __ -> obj, null));
        return new Holder.Direct<>(obj);
    }

    @Deprecated(forRemoval = true)
    public <T> Holder.Direct<T> registerDynamic(T obj, ResourceLocation id, ResourceKey<? extends Registry<T>> registry) {
        return this.registerDynamic(obj, ResourceKey.create(registry, id), registry);
    }

    public <T> Holder.Direct<T> registerDynamic(T obj, String regname, ResourceKey<? extends Registry<T>> registry) {
        return this.registerDynamic(obj, this.newResourceLocation(regname), registry);
    }

    public <T> void performDynamicRegistration(RegistryOps.RegistryInfoLookup lookup, ResourceKey<? extends Registry<?>> registryKey, WritableRegistry<T> writable) {
        List<ZetaRegistry.DynamicEntry<?>> entries = (List<ZetaRegistry.DynamicEntry<?>>) this.dynamicDefers.get(registryKey);
        if (entries != null && !entries.isEmpty()) {
            this.z.log.info("Dynamically registering {} thing{} into {}", entries.size(), entries.size() > 1 ? "s" : "", registryKey.location());
            List<ZetaRegistry.DynamicEntry<T>> typePun = (List<ZetaRegistry.DynamicEntry<T>>) entries;
            typePun.forEach(entry -> {
                T thing = (T) entry.creator.apply(lookup);
                writable.register(entry.id, thing, Lifecycle.stable());
                if (entry.lateBound != null) {
                    entry.lateBound.bind(thing, writable);
                }
            });
        }
    }

    private static record DynamicEntry<T>(ResourceKey<T> id, Function<RegistryOps.RegistryInfoLookup, T> creator, @Nullable LateBoundHolder<T> lateBound) {
    }
}