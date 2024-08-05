package mezz.jei.library.color;

import java.util.List;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.library.config.ColorNameConfig;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class ColorHelper implements IColorHelper {

    private final ColorGetter colorGetter = new ColorGetter();

    private final ColorNameConfig colorNameConfig;

    public ColorHelper(ColorNameConfig colorNameConfig) {
        this.colorNameConfig = colorNameConfig;
    }

    @Override
    public List<Integer> getColors(TextureAtlasSprite textureAtlasSprite, int renderColor, int colorCount) {
        return this.colorGetter.getColors(textureAtlasSprite, renderColor, colorCount);
    }

    @Override
    public List<Integer> getColors(ItemStack itemStack, int colorCount) {
        return this.colorGetter.getColors(itemStack, colorCount);
    }

    @Override
    public String getClosestColorName(int color) {
        return this.colorNameConfig.getClosestColorName(color);
    }
}