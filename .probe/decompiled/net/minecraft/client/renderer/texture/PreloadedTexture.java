package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class PreloadedTexture extends SimpleTexture {

    @Nullable
    private CompletableFuture<SimpleTexture.TextureImage> future;

    public PreloadedTexture(ResourceManager resourceManager0, ResourceLocation resourceLocation1, Executor executor2) {
        super(resourceLocation1);
        this.future = CompletableFuture.supplyAsync(() -> SimpleTexture.TextureImage.load(resourceManager0, resourceLocation1), executor2);
    }

    @Override
    protected SimpleTexture.TextureImage getTextureImage(ResourceManager resourceManager0) {
        if (this.future != null) {
            SimpleTexture.TextureImage $$1 = (SimpleTexture.TextureImage) this.future.join();
            this.future = null;
            return $$1;
        } else {
            return SimpleTexture.TextureImage.load(resourceManager0, this.f_118129_);
        }
    }

    public CompletableFuture<Void> getFuture() {
        return this.future == null ? CompletableFuture.completedFuture(null) : this.future.thenApply(p_118110_ -> null);
    }

    @Override
    public void reset(TextureManager textureManager0, ResourceManager resourceManager1, ResourceLocation resourceLocation2, Executor executor3) {
        this.future = CompletableFuture.supplyAsync(() -> SimpleTexture.TextureImage.load(resourceManager1, this.f_118129_), Util.backgroundExecutor());
        this.future.thenRunAsync(() -> textureManager0.register(this.f_118129_, this), executor(executor3));
    }

    private static Executor executor(Executor executor0) {
        return p_118124_ -> executor0.execute(() -> RenderSystem.recordRenderCall(p_118124_::run));
    }
}