package net.minecraft.client.resources;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class LegacyStuffWrapper {

    @Deprecated
    public static int[] getPixels(ResourceManager resourceManager0, ResourceLocation resourceLocation1) throws IOException {
        InputStream $$2 = resourceManager0.m_215595_(resourceLocation1);
        int[] var4;
        try (NativeImage $$3 = NativeImage.read($$2)) {
            var4 = $$3.makePixelArray();
        } catch (Throwable var9) {
            if ($$2 != null) {
                try {
                    $$2.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }
            }
            throw var9;
        }
        if ($$2 != null) {
            $$2.close();
        }
        return var4;
    }
}