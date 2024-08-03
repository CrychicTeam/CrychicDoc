package mezz.jei.library.color;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mezz.jei.common.platform.IPlatformRenderHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public final class ColorGetter {

    private static final Logger LOGGER = LogManager.getLogger();

    public List<Integer> getColors(ItemStack itemStack, int colorCount) {
        try {
            return this.unsafeGetColors(itemStack, colorCount);
        } catch (LinkageError | RuntimeException var5) {
            String itemStackInfo = ErrorUtil.getItemStackInfo(itemStack);
            LOGGER.warn("Failed to get color name for {}", itemStackInfo, var5);
            return Collections.emptyList();
        }
    }

    private List<Integer> unsafeGetColors(ItemStack itemStack, int colorCount) {
        Item item = itemStack.getItem();
        if (itemStack.isEmpty()) {
            return Collections.emptyList();
        } else if (item instanceof BlockItem itemBlock) {
            Block block = itemBlock.getBlock();
            return block == null ? Collections.emptyList() : this.getBlockColors(block, colorCount);
        } else {
            return this.getItemColors(itemStack, colorCount);
        }
    }

    private List<Integer> getItemColors(ItemStack itemStack, int colorCount) {
        IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
        ItemColors itemColors = renderHelper.getItemColors();
        int renderColor = itemColors.getColor(itemStack, 0);
        TextureAtlasSprite textureAtlasSprite = getTextureAtlasSprite(itemStack);
        return textureAtlasSprite == null ? Collections.emptyList() : this.getColors(textureAtlasSprite, renderColor, colorCount);
    }

    private List<Integer> getBlockColors(Block block, int colorCount) {
        BlockState blockState = block.defaultBlockState();
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        int renderColor = blockColors.getColor(blockState, null, null, 0);
        TextureAtlasSprite textureAtlasSprite = getTextureAtlasSprite(blockState);
        return textureAtlasSprite == null ? Collections.emptyList() : this.getColors(textureAtlasSprite, renderColor, colorCount);
    }

    public List<Integer> getColors(TextureAtlasSprite textureAtlasSprite, int renderColor, int colorCount) {
        return colorCount <= 0 ? Collections.emptyList() : (List) getNativeImage(textureAtlasSprite).map(bufferedImage -> {
            List<Integer> colors = new ArrayList(colorCount);
            int[][] palette = ColorThief.getPalette(bufferedImage, colorCount, 2, false);
            for (int[] colorInt : palette) {
                int red = (int) ((float) (colorInt[0] - 1) * (float) (renderColor >> 16 & 0xFF) / 255.0F);
                int green = (int) ((float) (colorInt[1] - 1) * (float) (renderColor >> 8 & 0xFF) / 255.0F);
                int blue = (int) ((float) (colorInt[2] - 1) * (float) (renderColor & 0xFF) / 255.0F);
                red = Mth.clamp(red, 0, 255);
                green = Mth.clamp(green, 0, 255);
                blue = Mth.clamp(blue, 0, 255);
                int color = 0xFF000000 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
                colors.add(color);
            }
            return colors;
        }).orElseGet(Collections::emptyList);
    }

    private static Optional<NativeImage> getNativeImage(TextureAtlasSprite textureAtlasSprite) {
        SpriteContents contents = textureAtlasSprite.contents();
        int iconWidth = contents.width();
        int iconHeight = contents.height();
        if (iconWidth > 0 && iconHeight > 0) {
            IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
            return renderHelper.getMainImage(textureAtlasSprite);
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    private static TextureAtlasSprite getTextureAtlasSprite(BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockRenderDispatcher blockRendererDispatcher = minecraft.getBlockRenderer();
        BlockModelShaper blockModelShapes = blockRendererDispatcher.getBlockModelShaper();
        BakedModel blockModel = blockModelShapes.getBlockModel(blockState);
        IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
        TextureAtlasSprite textureAtlasSprite = renderHelper.getParticleIcon(blockModel);
        return textureAtlasSprite.atlasLocation().equals(MissingTextureAtlasSprite.getLocation()) ? null : textureAtlasSprite;
    }

    @Nullable
    private static TextureAtlasSprite getTextureAtlasSprite(ItemStack itemStack) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemModelShaper itemModelMesher = itemRenderer.getItemModelShaper();
        BakedModel itemModel = itemModelMesher.getItemModel(itemStack);
        IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
        TextureAtlasSprite particleTexture = renderHelper.getParticleIcon(itemModel);
        return particleTexture.atlasLocation().equals(MissingTextureAtlasSprite.getLocation()) ? null : particleTexture;
    }
}