package noppes.npcs.shared.client.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import noppes.npcs.shared.common.util.LogWriter;

public class TextureCache extends SimpleTexture {

    private final ResourceLocation original;

    public TextureCache(ResourceLocation location, ResourceLocation original) {
        super(location);
        this.original = original;
    }

    @Override
    public void load(ResourceManager p_195413_1_) throws IOException {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        Resource r = (Resource) manager.m_213713_(this.original).orElse(null);
        if (r != null) {
            try {
                BufferedImage bufferedimage = ImageIO.read(r.open());
                int i = bufferedimage.getWidth();
                int j = bufferedimage.getHeight();
                BufferedImage bufferedImage = new BufferedImage(i * 4, j * 2, 1);
                Graphics g = bufferedImage.getGraphics();
                g.drawImage(bufferedimage, 0, 0, null);
                g.drawImage(bufferedimage, i, 0, null);
                g.drawImage(bufferedimage, i * 2, 0, null);
                g.drawImage(bufferedimage, i * 3, 0, null);
                g.drawImage(bufferedimage, 0, i, null);
                g.drawImage(bufferedimage, i, j, null);
                g.drawImage(bufferedimage, i * 2, j, null);
                g.drawImage(bufferedimage, i * 3, j, null);
                Minecraft.getInstance().m_18691_(() -> CTextureUtil.uploadTextureImage(super.m_117963_(), bufferedImage));
            } catch (Exception var9) {
                LogWriter.error("Failed caching texture: " + this.f_118129_, var9);
            }
        }
    }
}