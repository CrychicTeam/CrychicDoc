package net.minecraft.client.resources.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class AtlasSet implements AutoCloseable {

    private final Map<ResourceLocation, AtlasSet.AtlasEntry> atlases;

    public AtlasSet(Map<ResourceLocation, ResourceLocation> mapResourceLocationResourceLocation0, TextureManager textureManager1) {
        this.atlases = (Map<ResourceLocation, AtlasSet.AtlasEntry>) mapResourceLocationResourceLocation0.entrySet().stream().collect(Collectors.toMap(Entry::getKey, p_261403_ -> {
            TextureAtlas $$2 = new TextureAtlas((ResourceLocation) p_261403_.getKey());
            textureManager1.register((ResourceLocation) p_261403_.getKey(), $$2);
            return new AtlasSet.AtlasEntry($$2, (ResourceLocation) p_261403_.getValue());
        }));
    }

    public TextureAtlas getAtlas(ResourceLocation resourceLocation0) {
        return ((AtlasSet.AtlasEntry) this.atlases.get(resourceLocation0)).atlas();
    }

    public void close() {
        this.atlases.values().forEach(AtlasSet.AtlasEntry::close);
        this.atlases.clear();
    }

    public Map<ResourceLocation, CompletableFuture<AtlasSet.StitchResult>> scheduleLoad(ResourceManager resourceManager0, int int1, Executor executor2) {
        return (Map<ResourceLocation, CompletableFuture<AtlasSet.StitchResult>>) this.atlases.entrySet().stream().collect(Collectors.toMap(Entry::getKey, p_261401_ -> {
            AtlasSet.AtlasEntry $$4 = (AtlasSet.AtlasEntry) p_261401_.getValue();
            return SpriteLoader.create($$4.atlas).loadAndStitch(resourceManager0, $$4.atlasInfoLocation, int1, executor2).thenApply(p_250418_ -> new AtlasSet.StitchResult($$4.atlas, p_250418_));
        }));
    }

    static record AtlasEntry(TextureAtlas f_244361_, ResourceLocation f_260723_) implements AutoCloseable {

        private final TextureAtlas atlas;

        private final ResourceLocation atlasInfoLocation;

        AtlasEntry(TextureAtlas f_244361_, ResourceLocation f_260723_) {
            this.atlas = f_244361_;
            this.atlasInfoLocation = f_260723_;
        }

        public void close() {
            this.atlas.clearTextureData();
        }
    }

    public static class StitchResult {

        private final TextureAtlas atlas;

        private final SpriteLoader.Preparations preparations;

        public StitchResult(TextureAtlas textureAtlas0, SpriteLoader.Preparations spriteLoaderPreparations1) {
            this.atlas = textureAtlas0;
            this.preparations = spriteLoaderPreparations1;
        }

        @Nullable
        public TextureAtlasSprite getSprite(ResourceLocation resourceLocation0) {
            return (TextureAtlasSprite) this.preparations.regions().get(resourceLocation0);
        }

        public TextureAtlasSprite missing() {
            return this.preparations.missing();
        }

        public CompletableFuture<Void> readyForUpload() {
            return this.preparations.readyForUpload();
        }

        public void upload() {
            this.atlas.upload(this.preparations);
        }
    }
}