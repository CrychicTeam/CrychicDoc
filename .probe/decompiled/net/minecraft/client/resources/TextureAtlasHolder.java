package net.minecraft.client.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public abstract class TextureAtlasHolder implements PreparableReloadListener, AutoCloseable {

    private final TextureAtlas textureAtlas;

    private final ResourceLocation atlasInfoLocation;

    public TextureAtlasHolder(TextureManager textureManager0, ResourceLocation resourceLocation1, ResourceLocation resourceLocation2) {
        this.atlasInfoLocation = resourceLocation2;
        this.textureAtlas = new TextureAtlas(resourceLocation1);
        textureManager0.register(this.textureAtlas.location(), this.textureAtlas);
    }

    protected TextureAtlasSprite getSprite(ResourceLocation resourceLocation0) {
        return this.textureAtlas.getSprite(resourceLocation0);
    }

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        return SpriteLoader.create(this.textureAtlas).loadAndStitch(resourceManager1, this.atlasInfoLocation, 0, executor4).thenCompose(SpriteLoader.Preparations::m_246429_).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_249246_ -> this.apply(p_249246_, profilerFiller3), executor5);
    }

    private void apply(SpriteLoader.Preparations spriteLoaderPreparations0, ProfilerFiller profilerFiller1) {
        profilerFiller1.startTick();
        profilerFiller1.push("upload");
        this.textureAtlas.upload(spriteLoaderPreparations0);
        profilerFiller1.pop();
        profilerFiller1.endTick();
    }

    public void close() {
        this.textureAtlas.clearTextureData();
    }
}