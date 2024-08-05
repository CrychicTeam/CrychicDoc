package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public abstract class AbstractTexture implements AutoCloseable {

    public static final int NOT_ASSIGNED = -1;

    protected int id = -1;

    protected boolean blur;

    protected boolean mipmap;

    public void setFilter(boolean boolean0, boolean boolean1) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.blur = boolean0;
        this.mipmap = boolean1;
        int $$2;
        int $$3;
        if (boolean0) {
            $$2 = boolean1 ? 9987 : 9729;
            $$3 = 9729;
        } else {
            $$2 = boolean1 ? 9986 : 9728;
            $$3 = 9728;
        }
        this.bind();
        GlStateManager._texParameter(3553, 10241, $$2);
        GlStateManager._texParameter(3553, 10240, $$3);
    }

    public int getId() {
        RenderSystem.assertOnRenderThreadOrInit();
        if (this.id == -1) {
            this.id = TextureUtil.generateTextureId();
        }
        return this.id;
    }

    public void releaseId() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                if (this.id != -1) {
                    TextureUtil.releaseTextureId(this.id);
                    this.id = -1;
                }
            });
        } else if (this.id != -1) {
            TextureUtil.releaseTextureId(this.id);
            this.id = -1;
        }
    }

    public abstract void load(ResourceManager var1) throws IOException;

    public void bind() {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> GlStateManager._bindTexture(this.getId()));
        } else {
            GlStateManager._bindTexture(this.getId());
        }
    }

    public void reset(TextureManager textureManager0, ResourceManager resourceManager1, ResourceLocation resourceLocation2, Executor executor3) {
        textureManager0.register(resourceLocation2, this);
    }

    public void close() {
    }
}