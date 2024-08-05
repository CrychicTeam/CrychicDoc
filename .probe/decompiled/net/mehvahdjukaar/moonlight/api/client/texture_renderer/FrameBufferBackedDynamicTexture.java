package net.mehvahdjukaar.moonlight.api.client.texture_renderer;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class FrameBufferBackedDynamicTexture extends AbstractTexture {

    protected final Consumer<FrameBufferBackedDynamicTexture> drawingFunction;

    private boolean initialized = false;

    private RenderTarget frameBuffer;

    private final int width;

    private final int height;

    private final ResourceLocation resourceLocation;

    @Nullable
    private NativeImage cpuImage;

    public FrameBufferBackedDynamicTexture(ResourceLocation resourceLocation, int width, int height, @Nullable Consumer<FrameBufferBackedDynamicTexture> textureDrawingFunction) {
        this.width = width;
        this.height = height;
        this.resourceLocation = resourceLocation;
        this.drawingFunction = textureDrawingFunction;
    }

    public FrameBufferBackedDynamicTexture(ResourceLocation resourceLocation, int size, @Nullable Consumer<FrameBufferBackedDynamicTexture> textureDrawingFunction) {
        this(resourceLocation, size, size, textureDrawingFunction);
    }

    @Internal
    public void initialize() {
        this.initialized = true;
        Minecraft.getInstance().getTextureManager().register(this.resourceLocation, this);
        this.redraw();
    }

    public void redraw() {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> {
                this.m_117966_();
                if (this.drawingFunction != null) {
                    this.drawingFunction.accept(this);
                }
            });
        } else {
            this.m_117966_();
            if (this.drawingFunction != null) {
                this.drawingFunction.accept(this);
            }
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void load(ResourceManager manager) {
    }

    public RenderTarget getFrameBuffer() {
        if (this.frameBuffer == null) {
            this.frameBuffer = new MainTarget(this.width, this.height);
            this.f_117950_ = this.frameBuffer.getColorTextureId();
        }
        return this.frameBuffer;
    }

    public void bindWrite() {
        this.getFrameBuffer().bindWrite(true);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ResourceLocation getTextureLocation() {
        return this.resourceLocation;
    }

    @Override
    public int getId() {
        return this.getFrameBuffer().getColorTextureId();
    }

    @Override
    public void releaseId() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::clearGlId0);
        } else {
            this.clearGlId0();
        }
    }

    private void clearGlId0() {
        if (this.frameBuffer != null) {
            this.frameBuffer.destroyBuffers();
            this.frameBuffer = null;
        }
        this.f_117950_ = -1;
    }

    @Override
    public void close() {
        this.releaseId();
        if (this.cpuImage != null) {
            this.cpuImage.close();
            this.cpuImage = null;
        }
    }

    public NativeImage getPixels() {
        if (this.cpuImage == null) {
            this.cpuImage = new NativeImage(this.width, this.height, false);
        }
        return this.cpuImage;
    }

    public void download() {
        this.m_117966_();
        this.getPixels().downloadTexture(0, false);
    }

    public void upload() {
        if (this.cpuImage != null) {
            this.m_117966_();
            this.cpuImage.upload(0, 0, 0, false);
            this.cpuImage.close();
            this.cpuImage = null;
        } else {
            Moonlight.LOGGER.warn("Trying to upload disposed texture {}", this.getId());
        }
    }

    public List<Path> saveTextureToFile(Path texturesDir) throws IOException {
        return this.saveTextureToFile(texturesDir, this.resourceLocation.getPath().replace("/", "_"));
    }

    public List<Path> saveTextureToFile(Path texturesDir, String name) throws IOException {
        this.m_117966_();
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        List<Path> textureFiles = new ArrayList();
        int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
        int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
        int size = width * height;
        if (size == 0) {
            return List.of();
        } else {
            BufferedImage bufferedimage = new BufferedImage(width, height, 2);
            Path output = texturesDir.resolve(name + ".png");
            IntBuffer buffer = BufferUtils.createIntBuffer(size);
            int[] data = new int[size];
            GL11.glGetTexImage(3553, 0, 32993, 33639, buffer);
            buffer.get(data);
            bufferedimage.setRGB(0, 0, width, height, data, 0, width);
            ImageIO.write(bufferedimage, "png", output.toFile());
            textureFiles.add(output);
            return textureFiles;
        }
    }
}