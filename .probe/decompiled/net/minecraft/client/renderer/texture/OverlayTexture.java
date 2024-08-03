package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;

public class OverlayTexture implements AutoCloseable {

    private static final int SIZE = 16;

    public static final int NO_WHITE_U = 0;

    public static final int RED_OVERLAY_V = 3;

    public static final int WHITE_OVERLAY_V = 10;

    public static final int NO_OVERLAY = pack(0, 10);

    private final DynamicTexture texture = new DynamicTexture(16, 16, false);

    public OverlayTexture() {
        NativeImage $$0 = this.texture.getPixels();
        for (int $$1 = 0; $$1 < 16; $$1++) {
            for (int $$2 = 0; $$2 < 16; $$2++) {
                if ($$1 < 8) {
                    $$0.setPixelRGBA($$2, $$1, -1308622593);
                } else {
                    int $$3 = (int) ((1.0F - (float) $$2 / 15.0F * 0.75F) * 255.0F);
                    $$0.setPixelRGBA($$2, $$1, $$3 << 24 | 16777215);
                }
            }
        }
        RenderSystem.activeTexture(33985);
        this.texture.m_117966_();
        $$0.upload(0, 0, 0, 0, 0, $$0.getWidth(), $$0.getHeight(), false, true, false, false);
        RenderSystem.activeTexture(33984);
    }

    public void close() {
        this.texture.close();
    }

    public void setupOverlayColor() {
        RenderSystem.setupOverlayColor(this.texture::m_117963_, 16);
    }

    public static int u(float float0) {
        return (int) (float0 * 15.0F);
    }

    public static int v(boolean boolean0) {
        return boolean0 ? 3 : 10;
    }

    public static int pack(int int0, int int1) {
        return int0 | int1 << 16;
    }

    public static int pack(float float0, boolean boolean1) {
        return pack(u(float0), v(boolean1));
    }

    public void teardownOverlayColor() {
        RenderSystem.teardownOverlayColor();
    }
}