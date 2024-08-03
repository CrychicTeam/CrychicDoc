package dev.latvian.mods.kubejs.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

public class StencilTexture {

    public int width;

    public int height;

    public int[] pixels;

    public byte[] mcmeta;

    public StencilTexture(BufferedImage img, byte[] mcmeta) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new int[this.width * this.height];
        img.getRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
        this.mcmeta = mcmeta;
    }

    public byte[] create(JsonObject colors) {
        Int2IntArrayMap colorMap = new Int2IntArrayMap(colors.size());
        for (Entry<String, JsonElement> entry : colors.entrySet()) {
            String k = (String) entry.getKey();
            String v = ((JsonElement) entry.getValue()).getAsString();
            int col = Integer.parseUnsignedInt(v.startsWith("#") ? v.substring(1) : v, 16);
            if ((col & 0xFF000000) == 0) {
                col |= -16777216;
            }
            colorMap.put(Integer.parseUnsignedInt(k.startsWith("#") ? k.substring(1) : k, 16) & 16777215, col);
        }
        int[] result = new int[this.pixels.length];
        for (int i = 0; i < this.pixels.length; i++) {
            result[i] = (this.pixels[i] & 0xFF000000) == 0 ? 0 : colorMap.getOrDefault(this.pixels[i] & 16777215, this.pixels[i]);
        }
        BufferedImage img = new BufferedImage(this.width, this.height, 2);
        img.setRGB(0, 0, this.width, this.height, result, 0, this.width);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", out);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
        return out.toByteArray();
    }
}