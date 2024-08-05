package me.jellysquid.mods.sodium.client.render.chunk.compile;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ChunkBuildContext {

    public final ChunkBuildBuffers buffers;

    public final BlockRenderCache cache;

    private final ObjectOpenHashSet<TextureAtlasSprite> additionalCapturedSprites;

    private boolean captureAdditionalSprites;

    public ChunkBuildContext(ClientLevel world, ChunkVertexType vertexType) {
        this.buffers = new ChunkBuildBuffers(vertexType);
        this.cache = new BlockRenderCache(Minecraft.getInstance(), world);
        this.additionalCapturedSprites = new ObjectOpenHashSet();
    }

    public void cleanup() {
        this.buffers.destroy();
        this.cache.cleanup();
        this.additionalCapturedSprites.clear();
        this.captureAdditionalSprites = false;
    }

    public void setCaptureAdditionalSprites(boolean flag) {
        this.captureAdditionalSprites = flag;
        if (!flag) {
            this.additionalCapturedSprites.clear();
        }
    }

    public Iterable<TextureAtlasSprite> getAdditionalCapturedSprites() {
        return (Iterable<TextureAtlasSprite>) (this.additionalCapturedSprites.isEmpty() ? Collections.emptySet() : this.additionalCapturedSprites);
    }

    public void captureAdditionalSprite(TextureAtlasSprite sprite) {
        if (this.captureAdditionalSprites) {
            this.additionalCapturedSprites.add(sprite);
        }
    }
}