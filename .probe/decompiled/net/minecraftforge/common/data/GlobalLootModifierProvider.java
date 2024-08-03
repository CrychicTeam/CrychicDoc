package net.minecraftforge.common.data;

import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;

public abstract class GlobalLootModifierProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    private final String modid;

    private final Map<String, JsonElement> toSerialize = new HashMap();

    private boolean replace = false;

    public GlobalLootModifierProvider(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
    }

    protected void replacing() {
        this.replace = true;
    }

    protected abstract void start();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.start();
        Path forgePath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("forge").resolve("loot_modifiers").resolve("global_loot_modifiers.json");
        Path modifierFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve("loot_modifiers");
        List<ResourceLocation> entries = new ArrayList();
        Builder<CompletableFuture<?>> futuresBuilder = new Builder();
        this.toSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
            entries.add(new ResourceLocation(this.modid, name));
            Path modifierPath = modifierFolderPath.resolve(name + ".json");
            futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
        }));
        JsonObject forgeJson = new JsonObject();
        forgeJson.addProperty("replace", this.replace);
        forgeJson.add("entries", GSON.toJsonTree(entries.stream().map(ResourceLocation::toString).collect(Collectors.toList())));
        futuresBuilder.add(DataProvider.saveStable(cache, forgeJson, forgePath));
        return CompletableFuture.allOf((CompletableFuture[]) futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    public <T extends IGlobalLootModifier> void add(String modifier, T instance) {
        JsonElement json = (JsonElement) IGlobalLootModifier.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, s -> {
        });
        this.toSerialize.put(modifier, json);
    }

    @Override
    public String getName() {
        return "Global Loot Modifiers : " + this.modid;
    }
}