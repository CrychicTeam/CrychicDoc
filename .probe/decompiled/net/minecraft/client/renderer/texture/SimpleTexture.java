package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

public class SimpleTexture extends AbstractTexture {

    static final Logger LOGGER = LogUtils.getLogger();

    protected final ResourceLocation location;

    public SimpleTexture(ResourceLocation resourceLocation0) {
        this.location = resourceLocation0;
    }

    @Override
    public void load(ResourceManager resourceManager0) throws IOException {
        SimpleTexture.TextureImage $$1 = this.getTextureImage(resourceManager0);
        $$1.throwIfError();
        TextureMetadataSection $$2 = $$1.getTextureMetadata();
        boolean $$3;
        boolean $$4;
        if ($$2 != null) {
            $$3 = $$2.isBlur();
            $$4 = $$2.isClamp();
        } else {
            $$3 = false;
            $$4 = false;
        }
        NativeImage $$7 = $$1.getImage();
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> this.doLoad($$7, $$3, $$4));
        } else {
            this.doLoad($$7, $$3, $$4);
        }
    }

    private void doLoad(NativeImage nativeImage0, boolean boolean1, boolean boolean2) {
        TextureUtil.prepareImage(this.m_117963_(), 0, nativeImage0.getWidth(), nativeImage0.getHeight());
        nativeImage0.upload(0, 0, 0, 0, 0, nativeImage0.getWidth(), nativeImage0.getHeight(), boolean1, boolean2, false, true);
    }

    protected SimpleTexture.TextureImage getTextureImage(ResourceManager resourceManager0) {
        return SimpleTexture.TextureImage.load(resourceManager0, this.location);
    }

    protected static class TextureImage implements Closeable {

        @Nullable
        private final TextureMetadataSection metadata;

        @Nullable
        private final NativeImage image;

        @Nullable
        private final IOException exception;

        public TextureImage(IOException iOException0) {
            this.exception = iOException0;
            this.metadata = null;
            this.image = null;
        }

        public TextureImage(@Nullable TextureMetadataSection textureMetadataSection0, NativeImage nativeImage1) {
            this.exception = null;
            this.metadata = textureMetadataSection0;
            this.image = nativeImage1;
        }

        public static SimpleTexture.TextureImage load(ResourceManager resourceManager0, ResourceLocation resourceLocation1) {
            try {
                Resource $$2 = resourceManager0.m_215593_(resourceLocation1);
                InputStream $$3 = $$2.open();
                NativeImage $$4;
                try {
                    $$4 = NativeImage.read($$3);
                } catch (Throwable var9) {
                    if ($$3 != null) {
                        try {
                            $$3.close();
                        } catch (Throwable var7) {
                            var9.addSuppressed(var7);
                        }
                    }
                    throw var9;
                }
                if ($$3 != null) {
                    $$3.close();
                }
                TextureMetadataSection $$6 = null;
                try {
                    $$6 = (TextureMetadataSection) $$2.metadata().getSection(TextureMetadataSection.SERIALIZER).orElse(null);
                } catch (RuntimeException var8) {
                    SimpleTexture.LOGGER.warn("Failed reading metadata of: {}", resourceLocation1, var8);
                }
                return new SimpleTexture.TextureImage($$6, $$4);
            } catch (IOException var10) {
                return new SimpleTexture.TextureImage(var10);
            }
        }

        @Nullable
        public TextureMetadataSection getTextureMetadata() {
            return this.metadata;
        }

        public NativeImage getImage() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            } else {
                return this.image;
            }
        }

        public void close() {
            if (this.image != null) {
                this.image.close();
            }
        }

        public void throwIfError() throws IOException {
            if (this.exception != null) {
                throw this.exception;
            }
        }
    }
}