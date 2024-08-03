package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.client.ClientProperties;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.util.Lazy;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import net.minecraft.server.packs.resources.IoSupplier;

public interface WindowKJS {

    default List<IoSupplier<InputStream>> kjs$loadIcons(List<IoSupplier<InputStream>> original) throws IOException {
        if (Files.exists(KubeJSPaths.PACKICON, new LinkOption[0])) {
            InputStream in = Files.newInputStream(KubeJSPaths.PACKICON);
            List var4;
            try {
                BufferedImage img = ImageIO.read(in);
                var4 = List.of(new GeneratedData(KubeJS.id("icon_16x.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 16)), true), new GeneratedData(KubeJS.id("icon_24x.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 24)), true), new GeneratedData(KubeJS.id("icon_32x.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 32)), true), new GeneratedData(KubeJS.id("icon_48x.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 48)), true), new GeneratedData(KubeJS.id("icon_128.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 128)), true), new GeneratedData(KubeJS.id("icon_256x.png"), Lazy.of(new WindowKJS.KJSScaledIconProvider(img, 256)), true));
            } catch (Throwable var6) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (in != null) {
                in.close();
            }
            return var4;
        } else {
            return original;
        }
    }

    public static record KJSScaledIconProvider(BufferedImage original, int target) implements Supplier<byte[]> {

        public byte[] get() {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (this.original.getWidth() == this.target && this.original.getHeight() == this.target) {
                    ImageIO.write(this.original, "png", out);
                } else {
                    BufferedImage img = new BufferedImage(this.target, this.target, 2);
                    Graphics2D g = img.createGraphics();
                    if (ClientProperties.get().blurScaledPackIcon) {
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    } else {
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    }
                    g.drawImage(this.original, 0, 0, this.target, this.target, null);
                    g.dispose();
                    ImageIO.write(img, "png", out);
                }
                return out.toByteArray();
            } catch (Exception var4) {
                throw new IllegalStateException(this.original.toString(), var4);
            }
        }
    }
}