package snownee.jade.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IJadeProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.view.IServerExtensionProvider;

public class WailaCommonRegistration implements IWailaCommonRegistration {

    public static final WailaCommonRegistration INSTANCE = new WailaCommonRegistration();

    public final HierarchyLookup<IServerDataProvider<BlockAccessor>> blockDataProviders = new HierarchyLookup<>(BlockEntity.class);

    public final HierarchyLookup<IServerDataProvider<EntityAccessor>> entityDataProviders = new HierarchyLookup<>(Entity.class);

    public final PriorityStore<ResourceLocation, IJadeProvider> priorities = new PriorityStore<>(IJadeProvider::getDefaultPriority, IJadeProvider::getUid);

    public final HierarchyLookup<IServerExtensionProvider<Object, ItemStack>> itemStorageProviders;

    public final HierarchyLookup<IServerExtensionProvider<Object, CompoundTag>> fluidStorageProviders;

    public final HierarchyLookup<IServerExtensionProvider<Object, CompoundTag>> energyStorageProviders;

    public final HierarchyLookup<IServerExtensionProvider<Object, CompoundTag>> progressProviders;

    WailaCommonRegistration() {
        this.priorities.setSortingFunction((store, allKeys) -> {
            List<ResourceLocation> keys = (List<ResourceLocation>) allKeys.stream().filter($ -> !$.getPath().contains(".")).sorted(Comparator.comparingInt(store::byKey)).collect(Collectors.toCollection(ArrayList::new));
            allKeys.stream().filter($ -> $.getPath().contains(".")).forEach($ -> {
                ResourceLocation parent = new ResourceLocation($.getNamespace(), $.getPath().substring(0, $.getPath().indexOf(46)));
                int index = keys.indexOf(parent);
                keys.add(index + 1, $);
            });
            return keys;
        });
        this.priorities.configurable("jade/sort-order", ResourceLocation.CODEC);
        this.itemStorageProviders = new HierarchyLookup<>(Object.class, true);
        this.fluidStorageProviders = new HierarchyLookup<>(Object.class, true);
        this.energyStorageProviders = new HierarchyLookup<>(Object.class, true);
        this.progressProviders = new HierarchyLookup<>(Object.class, true);
    }

    @Override
    public void registerBlockDataProvider(IServerDataProvider<BlockAccessor> dataProvider, Class<? extends BlockEntity> block) {
        this.blockDataProviders.register(block, dataProvider);
    }

    @Override
    public void registerEntityDataProvider(IServerDataProvider<EntityAccessor> dataProvider, Class<? extends Entity> entity) {
        this.entityDataProviders.register(entity, dataProvider);
    }

    public List<IServerDataProvider<BlockAccessor>> getBlockNBTProviders(BlockEntity block) {
        return this.blockDataProviders.get(block);
    }

    public List<IServerDataProvider<EntityAccessor>> getEntityNBTProviders(Entity entity) {
        return this.entityDataProviders.get(entity);
    }

    public void loadComplete() {
        this.blockDataProviders.loadComplete(this.priorities);
        this.entityDataProviders.loadComplete(this.priorities);
        this.itemStorageProviders.loadComplete(this.priorities);
        this.fluidStorageProviders.loadComplete(this.priorities);
        this.energyStorageProviders.loadComplete(this.priorities);
        this.progressProviders.loadComplete(this.priorities);
    }

    @Override
    public <T> void registerItemStorage(IServerExtensionProvider<T, ItemStack> provider, Class<? extends T> clazz) {
        this.itemStorageProviders.register(clazz, provider);
    }

    @Override
    public <T> void registerFluidStorage(IServerExtensionProvider<T, CompoundTag> provider, Class<? extends T> clazz) {
        this.fluidStorageProviders.register(clazz, provider);
    }

    @Override
    public <T> void registerEnergyStorage(IServerExtensionProvider<T, CompoundTag> provider, Class<? extends T> clazz) {
        this.energyStorageProviders.register(clazz, provider);
    }

    @Override
    public <T> void registerProgress(IServerExtensionProvider<T, CompoundTag> provider, Class<? extends T> clazz) {
        this.progressProviders.register(clazz, provider);
    }
}