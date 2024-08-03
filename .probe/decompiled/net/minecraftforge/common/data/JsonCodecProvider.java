package net.minecraftforge.common.data;

import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.slf4j.Logger;

public class JsonCodecProvider<T> implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final PackOutput output;

    protected final ExistingFileHelper existingFileHelper;

    protected final String modid;

    protected final DynamicOps<JsonElement> dynamicOps;

    protected final PackType packType;

    protected final String directory;

    protected final Codec<T> codec;

    protected final Map<ResourceLocation, T> entries;

    protected Map<ResourceLocation, ICondition[]> conditions = Collections.emptyMap();

    public JsonCodecProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid, DynamicOps<JsonElement> dynamicOps, PackType packType, String directory, Codec<T> codec, Map<ResourceLocation, T> entries) {
        ExistingFileHelper.ResourceType resourceType = new ExistingFileHelper.ResourceType(packType, ".json", directory);
        for (ResourceLocation id : entries.keySet()) {
            existingFileHelper.trackGenerated(id, resourceType);
        }
        this.output = output;
        this.existingFileHelper = existingFileHelper;
        this.modid = modid;
        this.dynamicOps = dynamicOps;
        this.packType = packType;
        this.directory = directory;
        this.codec = codec;
        this.entries = entries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path outputFolder = this.output.getOutputFolder(this.packType == PackType.CLIENT_RESOURCES ? PackOutput.Target.RESOURCE_PACK : PackOutput.Target.DATA_PACK);
        Builder<CompletableFuture<?>> futuresBuilder = new Builder();
        this.gather(LamdbaExceptionUtils.rethrowBiConsumer((id, value) -> {
            Path path = outputFolder.resolve(id.getNamespace()).resolve(this.directory).resolve(id.getPath() + ".json");
            JsonElement encoded = (JsonElement) this.codec.encodeStart(this.dynamicOps, value).getOrThrow(false, msg -> LOGGER.error("Failed to encode {}: {}", path, msg));
            ICondition[] conditions = (ICondition[]) this.conditions.get(id);
            if (conditions != null && conditions.length > 0) {
                if (encoded instanceof JsonObject obj) {
                    obj.add("forge:conditions", CraftingHelper.serialize(conditions));
                } else {
                    LOGGER.error("Attempted to apply conditions to a type that is not a JsonObject! - Path: {}", path);
                }
            }
            futuresBuilder.add(DataProvider.saveStable(cache, encoded, path));
        }));
        return CompletableFuture.allOf((CompletableFuture[]) futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    protected void gather(BiConsumer<ResourceLocation, T> consumer) {
        this.entries.forEach(consumer);
    }

    @Override
    public String getName() {
        return String.format("%s generator for %s", this.directory, this.modid);
    }

    public JsonCodecProvider<T> setConditions(Map<ResourceLocation, ICondition[]> conditions) {
        this.conditions = conditions;
        return this;
    }
}