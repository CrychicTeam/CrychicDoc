package software.bernie.geckolib.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.loading.FMLPaths;

public abstract class GeoAbstractTexture extends AbstractTexture {

    protected static void generateTexture(ResourceLocation texturePath, Consumer<TextureManager> textureManagerConsumer) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            throw new IllegalThreadStateException("Texture loading called outside of the render thread! This should DEFINITELY not be happening.");
        } else {
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            if (!(textureManager.getTexture(texturePath, MissingTextureAtlasSprite.getTexture()) instanceof GeoAbstractTexture)) {
                textureManagerConsumer.accept(textureManager);
            }
        }
    }

    @Override
    public final void load(ResourceManager resourceManager) throws IOException {
        RenderCall renderCall = this.loadTexture(resourceManager, Minecraft.getInstance());
        if (renderCall != null) {
            if (!RenderSystem.isOnRenderThreadOrInit()) {
                RenderSystem.recordRenderCall(renderCall);
            } else {
                renderCall.execute();
            }
        }
    }

    protected void printDebugImageToDisk(ResourceLocation id, NativeImage newImage) {
        try {
            File file = new File(FMLPaths.GAMEDIR.get().toFile(), "GeoTexture Debug Printouts");
            if (!file.exists()) {
                file.mkdirs();
            } else if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
            file = new File(file, id.getPath().replace('/', '.'));
            if (!file.exists()) {
                file.createNewFile();
            }
            newImage.writeToFile(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    @Nullable
    protected abstract RenderCall loadTexture(ResourceManager var1, Minecraft var2) throws IOException;

    public static void uploadSimple(int texture, NativeImage image, boolean blur, boolean clamp) {
        TextureUtil.prepareImage(texture, 0, image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), blur, clamp, false, true);
    }

    public static ResourceLocation appendToPath(ResourceLocation location, String suffix) {
        String path = location.getPath();
        int i = path.lastIndexOf(46);
        return new ResourceLocation(location.getNamespace(), path.substring(0, i) + suffix + path.substring(i));
    }
}