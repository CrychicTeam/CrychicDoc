package fuzs.puzzleslib.api.data.v2;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.apache.commons.lang3.StringUtils;

public final class AbstractLootProvider {

    public static LootTableProvider createProvider(PackOutput packOutput, LootTableSubProvider provider, LootContextParamSet paramSet) {
        return new LootTableProvider(packOutput, Set.of(), List.of(new LootTableProvider.SubProviderEntry(() -> provider, paramSet)));
    }

    public abstract static class Blocks extends BlockLootSubProvider implements DataProvider {

        private final LootTableProvider provider;

        protected final String modId;

        public Blocks(DataProviderContext context) {
            this(context.getModId(), context.getPackOutput());
        }

        public Blocks(String modId, PackOutput packOutput) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
            this.provider = AbstractLootProvider.createProvider(packOutput, this, LootContextParamSets.BLOCK);
            this.modId = modId;
        }

        @Override
        public final CompletableFuture<?> run(CachedOutput output) {
            return this.provider.run(output);
        }

        @Override
        public String getName() {
            return "Block Loot Tables";
        }

        @Override
        public final void generate() {
            this.addLootTables();
        }

        public abstract void addLootTables();

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            this.generate();
            Set<ResourceLocation> set = Sets.newHashSet();
            for (Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.m_6579_()) {
                ResourceKey<Block> resourceKey = (ResourceKey<Block>) entry.getKey();
                Block block = (Block) entry.getValue();
                if (resourceKey.location().getNamespace().equals(this.modId) && block.m_245993_(this.f_243739_)) {
                    ResourceLocation resourceLocation = block.m_60589_();
                    if (resourceLocation != BuiltInLootTables.EMPTY && resourceLocation.getNamespace().equals(this.modId) && set.add(resourceLocation)) {
                        LootTable.Builder builder = (LootTable.Builder) this.f_244441_.remove(resourceLocation);
                        if (builder == null) {
                            throw new IllegalStateException("Missing loot table '%s' for '%s'".formatted(resourceLocation, resourceKey.location()));
                        }
                        consumer.accept(resourceLocation, builder);
                    }
                }
            }
            if (!this.f_244441_.isEmpty()) {
                throw new IllegalStateException("Created block loot tables for non-blocks: " + this.f_244441_.keySet());
            }
        }

        protected void dropNothing(Block block) {
            this.m_247577_(block, m_246386_());
        }
    }

    public abstract static class EntityTypes extends EntityLootSubProvider implements DataProvider {

        private final LootTableProvider provider;

        protected final String modId;

        protected final Map<EntityType<?>, Map<ResourceLocation, LootTable.Builder>> map = Maps.newHashMap();

        public EntityTypes(DataProviderContext context) {
            this(context.getModId(), context.getPackOutput());
        }

        public EntityTypes(String modId, PackOutput packOutput) {
            super(FeatureFlags.REGISTRY.allFlags());
            this.provider = AbstractLootProvider.createProvider(packOutput, this, LootContextParamSets.ENTITY);
            this.modId = modId;
        }

        @Override
        public final CompletableFuture<?> run(CachedOutput output) {
            return this.provider.run(output);
        }

        @Override
        public String getName() {
            return "Entity Type Loot Tables";
        }

        @Override
        public final void generate() {
            this.addLootTables();
        }

        public abstract void addLootTables();

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            this.generate();
            Set<ResourceLocation> set = Sets.newHashSet();
            for (Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.m_6579_()) {
                ResourceKey<EntityType<?>> resourceKey = (ResourceKey<EntityType<?>>) entry.getKey();
                EntityType<?> entityType = (EntityType<?>) entry.getValue();
                if (resourceKey.location().getNamespace().equals(this.modId) && entityType.m_245993_(FeatureFlags.REGISTRY.allFlags())) {
                    Map<ResourceLocation, LootTable.Builder> map = (Map<ResourceLocation, LootTable.Builder>) this.map.remove(entityType);
                    if (this.canHaveLootTable(entityType)) {
                        ResourceLocation resourceLocation = entityType.getDefaultLootTable();
                        if (!resourceLocation.equals(BuiltInLootTables.EMPTY) && (map == null || !map.containsKey(resourceLocation))) {
                            throw new IllegalStateException(String.format(Locale.ROOT, "Missing loot table '%s' for '%s'", resourceLocation, resourceKey.location()));
                        }
                        if (map != null) {
                            map.forEach((resourceLocationx, builder) -> {
                                if (!set.add(resourceLocationx)) {
                                    throw new IllegalStateException(String.format(Locale.ROOT, "Duplicate loot table '%s' for '%s'", resourceLocationx, resourceKey.location()));
                                } else {
                                    consumer.accept(resourceLocationx, builder);
                                }
                            });
                        }
                    } else if (map != null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Weird loot table(s) '%s' for '%s', not a LivingEntity so should not have loot", map.keySet().stream().map(ResourceLocation::toString).collect(Collectors.joining(",")), resourceKey.location()));
                    }
                }
            }
            if (!this.map.isEmpty()) {
                throw new IllegalStateException("Created loot tables for entities not supported by data pack: " + this.map.keySet());
            }
        }

        protected boolean canHaveLootTable(EntityType<?> entityType) {
            return entityType.getCategory() != MobCategory.MISC;
        }
    }

    public abstract static class Simple implements LootTableSubProvider, DataProvider {

        private final LootTableProvider provider;

        protected final Map<ResourceLocation, LootTable.Builder> values = Maps.newHashMap();

        public Simple(LootContextParamSet paramSet, DataProviderContext context) {
            this(paramSet, context.getPackOutput());
        }

        public Simple(LootContextParamSet paramSet, PackOutput packOutput) {
            this.provider = AbstractLootProvider.createProvider(packOutput, this, paramSet);
        }

        @Override
        public final CompletableFuture<?> run(CachedOutput output) {
            return this.provider.run(output);
        }

        @Override
        public String getName() {
            return String.join(" ", StringUtils.splitByCharacterTypeCamelCase(this.getClass().getSimpleName()));
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> exporter) {
            this.addLootTables();
            this.values.forEach(exporter);
        }

        protected void add(ResourceLocation table, LootTable.Builder builder) {
            this.values.put(table, builder);
        }

        public abstract void addLootTables();
    }
}