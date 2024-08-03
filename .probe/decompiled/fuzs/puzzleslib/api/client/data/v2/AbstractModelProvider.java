package fuzs.puzzleslib.api.client.data.v2;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.puzzlesaccessapi.api.client.data.v2.BlockModelBuilder;
import fuzs.puzzlesaccessapi.api.client.data.v2.ItemModelBuilder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class AbstractModelProvider implements DataProvider {

    public static final String BLOCK_PATH = "block";

    public static final String ITEM_PATH = "item";

    private final String modId;

    private final PackOutput.PathProvider blockStatePathProvider;

    private final PackOutput.PathProvider modelPathProvider;

    public AbstractModelProvider(DataProviderContext context) {
        this(context.getModId(), context.getPackOutput());
    }

    public AbstractModelProvider(String modId, PackOutput packOutput) {
        this.modId = modId;
        this.blockStatePathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    public abstract void addBlockModels(BlockModelBuilder var1);

    public abstract void addItemModels(ItemModelBuilder var1);

    protected boolean throwForMissingBlocks() {
        return true;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<Block, BlockStateGenerator> generators = Maps.newHashMap();
        Consumer<BlockStateGenerator> blockStateOutput = generator -> {
            Block block = generator.getBlock();
            BlockStateGenerator blockstategenerator = (BlockStateGenerator) generators.put(block, generator);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate block state definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();
        Set<Item> skippedAutoModels = Sets.newHashSet();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (resourceLocation, supplier) -> {
            if (models.put(resourceLocation, supplier) != null) {
                throw new IllegalStateException("Duplicate model definition for " + resourceLocation);
            }
        };
        this.addBlockModels(new BlockModelBuilder(blockStateOutput, modelOutput, skippedAutoModels::add));
        this.addItemModels(new ItemModelBuilder(modelOutput));
        List<Block> missingBlocks;
        if (this.throwForMissingBlocks()) {
            missingBlocks = BuiltInRegistries.BLOCK.m_6579_().stream().filter(entry -> ((ResourceKey) entry.getKey()).location().getNamespace().equals(this.modId) && !generators.containsKey(entry.getValue())).map(Entry::getValue).toList();
        } else {
            missingBlocks = Collections.emptyList();
        }
        if (!missingBlocks.isEmpty()) {
            throw new IllegalStateException("Missing block state definitions for " + missingBlocks);
        } else {
            BuiltInRegistries.BLOCK.m_6579_().forEach(entry -> {
                Item item = (Item) Item.BY_BLOCK.get(entry.getValue());
                if (item != null) {
                    if (!((ResourceKey) entry.getKey()).location().getNamespace().equals(this.modId) || skippedAutoModels.contains(item)) {
                        return;
                    }
                    ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(item);
                    if (!models.containsKey(resourcelocation)) {
                        models.put(resourcelocation, new DelegatedModel(ModelLocationUtils.getModelLocation((Block) entry.getValue())));
                    }
                }
            });
            return CompletableFuture.allOf(saveCollection(output, generators, block -> this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location())), saveCollection(output, models, this.modelPathProvider::m_245731_));
        }
    }

    @Override
    public final String getName() {
        return "Model Definitions";
    }

    private static <T> CompletableFuture<?> saveCollection(CachedOutput output, Map<T, ? extends Supplier<JsonElement>> map, Function<T, Path> pathExtractor) {
        return CompletableFuture.allOf((CompletableFuture[]) map.entrySet().stream().map(entry -> {
            Path path = (Path) pathExtractor.apply(entry.getKey());
            JsonElement jsonElement = (JsonElement) ((Supplier) entry.getValue()).get();
            return DataProvider.saveStable(output, jsonElement, path);
        }).toArray(CompletableFuture[]::new));
    }

    public static ResourceLocation getModelLocation(Block block) {
        return decorateBlockModelLocation(getLocation(block));
    }

    public static ResourceLocation decorateBlockModelLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPrefix("block/");
    }

    public static ResourceLocation getLocation(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String getName(Block block) {
        return getLocation(block).getPath();
    }

    public static ResourceLocation getModelLocation(Item item) {
        return decorateItemModelLocation(getLocation(item));
    }

    public static ResourceLocation decorateItemModelLocation(ResourceLocation resourceLocation) {
        return resourceLocation.withPrefix("item/");
    }

    public static ResourceLocation getLocation(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static String getName(Item item) {
        return getLocation(item).getPath();
    }

    public static ResourceLocation stripUntil(ResourceLocation resourceLocation, String s) {
        String path = resourceLocation.getPath();
        if (path.contains(s)) {
            path = path.substring(path.lastIndexOf(s) + 1);
            return new ResourceLocation(resourceLocation.getNamespace(), path);
        } else {
            return resourceLocation;
        }
    }

    public static ModelTemplate.JsonFactory overrides(ModelTemplate modelTemplate, AbstractModelProvider.ItemOverride.Factory... factories) {
        return (resourceLocation, map) -> {
            JsonObject jsonObject = modelTemplate.createBaseTemplate(resourceLocation, map);
            JsonArray jsonArray = new JsonArray();
            for (AbstractModelProvider.ItemOverride.Factory factory : factories) {
                jsonArray.add(((AbstractModelProvider.ItemOverride) factory.apply(resourceLocation)).toJson());
            }
            jsonObject.add("overrides", jsonArray);
            return jsonObject;
        };
    }

    public static record ItemOverride(ResourceLocation model, Map<ResourceLocation, Float> predicates) {

        public static AbstractModelProvider.ItemOverride of(ResourceLocation model, ResourceLocation p1, float f1) {
            return new AbstractModelProvider.ItemOverride(model, Map.of(p1, f1));
        }

        public static AbstractModelProvider.ItemOverride of(ResourceLocation model, ResourceLocation p1, float f1, ResourceLocation p2, float f2) {
            return new AbstractModelProvider.ItemOverride(model, Map.of(p1, f1, p2, f2));
        }

        public static AbstractModelProvider.ItemOverride of(ResourceLocation model, ResourceLocation p1, float f1, ResourceLocation p2, float f2, ResourceLocation p3, float f3) {
            return new AbstractModelProvider.ItemOverride(model, Map.of(p1, f1, p2, f2, p3, f3));
        }

        JsonElement toJson() {
            JsonObject jsonObject = new JsonObject();
            JsonObject predicates = new JsonObject();
            for (Entry<ResourceLocation, Float> entry : this.predicates.entrySet()) {
                predicates.addProperty(((ResourceLocation) entry.getKey()).toString(), (Number) entry.getValue());
            }
            jsonObject.add("predicate", predicates);
            jsonObject.addProperty("model", this.model.toString());
            return jsonObject;
        }

        public interface Factory extends Function<ResourceLocation, AbstractModelProvider.ItemOverride> {
        }
    }
}