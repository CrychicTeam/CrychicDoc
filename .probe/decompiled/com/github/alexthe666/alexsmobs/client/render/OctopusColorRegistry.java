package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.Color;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OctopusColorRegistry {

    public static final BlockState FALLBACK_BLOCK = Blocks.SAND.defaultBlockState();

    public static Object2IntMap<String> TEXTURES_TO_COLOR = new Object2IntOpenHashMap();

    public static int getBlockColor(BlockState stack) {
        String blockName = stack.toString();
        if (TEXTURES_TO_COLOR.containsKey(blockName)) {
            return TEXTURES_TO_COLOR.getInt(blockName);
        } else {
            int colorizer = -1;
            try {
                colorizer = Minecraft.getInstance().getBlockColors().getColor(stack, null, null, 0);
            } catch (Exception var7) {
                AlexsMobs.LOGGER.warn("Another mod did not use block colorizers correctly.");
            }
            int color = 16777215;
            if (colorizer == -1) {
                BufferedImage texture = null;
                try {
                    Color texColour = getAverageColour(getTextureAtlas(stack));
                    color = texColour.getRGB();
                } catch (NullPointerException var6) {
                    var6.printStackTrace();
                }
            } else {
                color = colorizer;
            }
            TEXTURES_TO_COLOR.put(blockName, color);
            return color;
        }
    }

    private static Color getAverageColour(TextureAtlasSprite image) {
        float red = 0.0F;
        float green = 0.0F;
        float blue = 0.0F;
        float count = 0.0F;
        int uMax = image.contents().width();
        int vMax = image.contents().height();
        for (float i = 0.0F; i < (float) uMax; i++) {
            for (float j = 0.0F; j < (float) vMax; j++) {
                int alpha = image.getPixelRGBA(0, (int) i, (int) j) >> 24 & 0xFF;
                if (alpha != 0) {
                    red += (float) (image.getPixelRGBA(0, (int) i, (int) j) >> 0 & 0xFF);
                    green += (float) (image.getPixelRGBA(0, (int) i, (int) j) >> 8 & 0xFF);
                    blue += (float) (image.getPixelRGBA(0, (int) i, (int) j) >> 16 & 0xFF);
                    count++;
                }
            }
        }
        return new Color((int) (red / count), (int) (green / count), (int) (blue / count));
    }

    private static TextureAtlasSprite getTextureAtlas(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state).getParticleIcon();
    }
}