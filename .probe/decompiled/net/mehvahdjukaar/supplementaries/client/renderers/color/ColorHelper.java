package net.mehvahdjukaar.supplementaries.client.renderers.color;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HSLColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ColorHelper {

    private static float[][] soapColors;

    public static int pack(float[] rgb) {
        return FastColor.ARGB32.color(255, (int) (rgb[0] * 255.0F), (int) (rgb[1] * 255.0F), (int) (rgb[2] * 255.0F));
    }

    public static float normalizeHSLSaturation(float saturation, float lightness) {
        float c = 1.0F - Math.abs(2.0F * lightness - 1.0F);
        return Math.min(saturation, c);
    }

    public static int getRainbowColorPost(float division) {
        float scale = 3600.0F / division;
        float h = (float) ((int) (System.currentTimeMillis() % (long) ((int) scale))) / scale;
        return new RGBColor(16755200).asHCL().withHue(h).asRGB().toInt();
    }

    public static int getRandomBrightColor(RandomSource random) {
        float h = random.nextFloat();
        HSLColor hsl = prettyfyColor(new HSLColor(h, 0.62F + random.nextFloat() * 0.3F, 0.43F + random.nextFloat() * 0.15F, 1.0F));
        return hsl.asRGB().toInt();
    }

    public static int getRainbowColor(float division) {
        float scale = 3600.0F / division;
        float h = (float) ((int) (System.currentTimeMillis() % (long) ((int) scale))) / scale;
        HSLColor color = new HSLColor(h, 0.6F, 0.5F, 1.0F);
        return color.asRGB().toInt();
    }

    public static float[] getBubbleColor(float phase) {
        int n = soapColors.length;
        int ind = (int) Math.floor((double) ((float) n * phase));
        float delta = (float) n * phase % 1.0F;
        float[] start = soapColors[ind];
        float[] end = soapColors[(ind + 1) % n];
        float red = Mth.lerp(delta, start[0], end[0]);
        float green = Mth.lerp(delta, start[1], end[1]);
        float blue = Mth.lerp(delta, start[2], end[2]);
        return new float[] { red, green, blue };
    }

    public static HSLColor prettyfyColor(HSLColor hsl) {
        float h = hsl.hue();
        float s = hsl.saturation();
        float l = hsl.lightness();
        s = normalizeHSLSaturation(s, l);
        float minLightness = 0.47F;
        l = Math.max(l, minLightness);
        float j = 1.0F - l;
        float ratio = 0.35F;
        if (s < j) {
            s = ratio * j + (1.0F - ratio) * s;
        }
        float scaling = 0.15F;
        float angle = 90.0F;
        float n = (float) ((double) scaling * Math.exp((double) (-angle) * Math.pow((double) (h - 0.6666F), 2.0)));
        s -= n;
        return new HSLColor(h, s, l, 1.0F);
    }

    public static void refreshBubbleColors(ResourceManager manager) {
        List<Integer> c = SpriteUtils.parsePaletteStrip(manager, ResType.TEXTURES.getPath(ModTextures.BUBBLE_BLOCK_COLORS_TEXTURE), 6);
        float[][] temp = new float[c.size()][];
        for (int i = 0; i < c.size(); i++) {
            int j = (Integer) c.get(i);
            temp[i] = new float[] { (float) FastColor.ABGR32.red(j) / 255.0F, (float) FastColor.ABGR32.green(j) / 255.0F, (float) FastColor.ABGR32.blue(j) / 255.0F };
        }
        soapColors = temp;
    }
}