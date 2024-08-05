package mezz.jei.api.helpers;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public interface IColorHelper {

    List<Integer> getColors(TextureAtlasSprite var1, int var2, int var3);

    List<Integer> getColors(ItemStack var1, int var2);

    String getClosestColorName(int var1);
}