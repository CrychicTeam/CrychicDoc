package journeymap.client.cartography.color;

import com.mojang.blaze3d.platform.NativeImage;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.texture.TextureCache;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class ColoredSprite {

    private static Logger logger = Journeymap.getLogger();

    private final Integer color;

    private final TextureAtlasSprite sprite;

    public ColoredSprite(TextureAtlasSprite sprite, @Nullable Integer color) {
        this.sprite = sprite;
        this.color = null;
    }

    public ColoredSprite(BakedQuad quad) {
        this.sprite = quad.getSprite();
        this.color = null;
    }

    public String getIconName() {
        return this.sprite.contents().name().getPath();
    }

    @Nullable
    public Integer getColor() {
        return this.color;
    }

    public boolean hasColor() {
        return this.color != null;
    }

    @Nullable
    public NativeImage getColoredImage() {
        try {
            ResourceLocation resourceLocation = new ResourceLocation(this.getIconName());
            if (resourceLocation.equals(new ResourceLocation("missingno"))) {
                return null;
            } else {
                NativeImage image = this.sprite.contents().byMipLevel[0];
                if (image == null || image.getWidth() == 0) {
                    image = this.getImageResource(this.sprite);
                }
                return image != null && image.getWidth() != 0 ? image : null;
            }
        } catch (Throwable var3) {
            if (logger.isDebugEnabled()) {
                logger.error("ColoredSprite: Error getting image for " + this.getIconName() + ": " + LogFormatter.toString(var3));
            }
            return null;
        }
    }

    private NativeImage getImageResource(TextureAtlasSprite tas) {
        try {
            ResourceLocation iconNameLoc = new ResourceLocation(tas.contents().name().getPath());
            ResourceLocation fileLoc = new ResourceLocation(iconNameLoc.getNamespace(), "textures/" + iconNameLoc.getPath() + ".png");
            return TextureCache.resolveImage(fileLoc);
        } catch (Throwable var4) {
            logger.error(String.format("ColoredSprite: Unable to use texture file for %s: %s", tas.contents().name().getPath(), var4.getMessage()));
            return null;
        }
    }
}