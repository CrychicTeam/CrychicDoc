package com.prunoideae.powerfuljs;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CapabilityService {

    public static final CapabilityService INSTANCE = new CapabilityService();

    protected final Map<ItemBuilder, List<CapabilityBuilder<ItemStack, ?, ?>>> itemBuilders = new HashMap();

    protected final Map<Item, List<CapabilityBuilder<ItemStack, ?, ?>>> items = new HashMap();

    protected final Map<BlockEntityType<?>, List<CapabilityBuilder<BlockEntity, ?, ?>>> blockEntities = new HashMap();

    protected final Map<EntityType<?>, List<CapabilityBuilder<Entity, ?, ?>>> entities = new HashMap();

    protected final List<Pair<BlockEntityInfo, CapabilityBuilder<BlockEntity, ?, ?>>> blockEntityInfo = new ArrayList();

    public void addLazyBECapability(BlockEntityInfo info, CapabilityBuilder<BlockEntity, ?, ?> capabilityBuilder) {
        this.blockEntityInfo.add(Pair.of(info, capabilityBuilder));
    }

    public void resolveLazyBECapabilities() {
        if (!this.blockEntityInfo.isEmpty()) {
            for (Pair<BlockEntityInfo, CapabilityBuilder<BlockEntity, ?, ?>> pair : this.blockEntityInfo) {
                this.addBECapability(((BlockEntityInfo) pair.getFirst()).entityType, (CapabilityBuilder<BlockEntity, ?, ?>) pair.getSecond());
            }
            this.blockEntityInfo.clear();
        }
    }

    public void addBuilderCapability(ItemBuilder builder, CapabilityBuilder<ItemStack, ?, ?> capabilityBuilder) {
        ((List) this.itemBuilders.computeIfAbsent(builder, b -> new ArrayList())).add(capabilityBuilder);
    }

    public void addItemCapability(Item item, CapabilityBuilder<ItemStack, ?, ?> capabilityBuilder) {
        ((List) this.items.computeIfAbsent(item, i -> new ArrayList())).add(capabilityBuilder);
    }

    public void addBECapability(BlockEntityType<?> type, CapabilityBuilder<BlockEntity, ?, ?> capabilityBuilder) {
        ((List) this.blockEntities.computeIfAbsent(type, b -> new ArrayList())).add(capabilityBuilder);
    }

    public void addEntityCapability(EntityType<?> type, CapabilityBuilder<Entity, ?, ?> capabilityBuilder) {
        ((List) this.entities.computeIfAbsent(type, e -> new ArrayList())).add(capabilityBuilder);
    }

    public void loadBuilders() {
        for (Entry<ItemBuilder, List<CapabilityBuilder<ItemStack, ?, ?>>> entry : this.itemBuilders.entrySet()) {
            ItemBuilder builder = (ItemBuilder) entry.getKey();
            List<CapabilityBuilder<ItemStack, ?, ?>> capabilityBuilder = (List<CapabilityBuilder<ItemStack, ?, ?>>) entry.getValue();
            ((List) this.items.computeIfAbsent(builder.get(), i -> new ArrayList())).addAll(capabilityBuilder);
        }
    }

    public Optional<List<CapabilityBuilder<ItemStack, ?, ?>>> getCapabilitiesFor(ItemStack itemStack) {
        return Optional.ofNullable((List) this.items.get(itemStack.getItem()));
    }

    public Optional<List<CapabilityBuilder<BlockEntity, ?, ?>>> getCapabilitiesFor(BlockEntity blockEntity) {
        INSTANCE.resolveLazyBECapabilities();
        return Optional.ofNullable((List) this.blockEntities.get(blockEntity.getType()));
    }

    public Optional<List<CapabilityBuilder<Entity, ?, ?>>> getCapabilitiesFor(Entity entity) {
        return Optional.ofNullable((List) this.entities.get(entity.getType()));
    }
}