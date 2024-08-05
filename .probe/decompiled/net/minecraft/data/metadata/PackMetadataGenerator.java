package net.minecraft.data.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.DetectedVersion;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.flag.FeatureFlagSet;

public class PackMetadataGenerator implements DataProvider {

    private final PackOutput output;

    private final Map<String, Supplier<JsonElement>> elements = new HashMap();

    public PackMetadataGenerator(PackOutput packOutput0) {
        this.output = packOutput0;
    }

    public <T> PackMetadataGenerator add(MetadataSectionType<T> metadataSectionTypeT0, T t1) {
        this.elements.put(metadataSectionTypeT0.m_7991_(), (Supplier) () -> metadataSectionTypeT0.toJson(t1));
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        JsonObject $$1 = new JsonObject();
        this.elements.forEach((p_249290_, p_251317_) -> $$1.add(p_249290_, (JsonElement) p_251317_.get()));
        return DataProvider.saveStable(cachedOutput0, $$1, this.output.getOutputFolder().resolve("pack.mcmeta"));
    }

    @Override
    public final String getName() {
        return "Pack Metadata";
    }

    public static PackMetadataGenerator forFeaturePack(PackOutput packOutput0, Component component1) {
        return new PackMetadataGenerator(packOutput0).add(PackMetadataSection.TYPE, new PackMetadataSection(component1, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA)));
    }

    public static PackMetadataGenerator forFeaturePack(PackOutput packOutput0, Component component1, FeatureFlagSet featureFlagSet2) {
        return forFeaturePack(packOutput0, component1).add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(featureFlagSet2));
    }
}