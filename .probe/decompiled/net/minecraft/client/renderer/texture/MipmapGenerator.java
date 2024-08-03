package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;

public class MipmapGenerator {

    private static final int ALPHA_CUTOUT_CUTOFF = 96;

    private static final float[] POW22 = Util.make(new float[256], p_118058_ -> {
        for (int $$1 = 0; $$1 < p_118058_.length; $$1++) {
            p_118058_[$$1] = (float) Math.pow((double) ((float) $$1 / 255.0F), 2.2);
        }
    });

    private MipmapGenerator() {
    }

    public static NativeImage[] generateMipLevels(NativeImage[] nativeImage0, int int1) {
        if (int1 + 1 <= nativeImage0.length) {
            return nativeImage0;
        } else {
            NativeImage[] $$2 = new NativeImage[int1 + 1];
            $$2[0] = nativeImage0[0];
            boolean $$3 = hasTransparentPixel($$2[0]);
            for (int $$4 = 1; $$4 <= int1; $$4++) {
                if ($$4 < nativeImage0.length) {
                    $$2[$$4] = nativeImage0[$$4];
                } else {
                    NativeImage $$5 = $$2[$$4 - 1];
                    NativeImage $$6 = new NativeImage($$5.getWidth() >> 1, $$5.getHeight() >> 1, false);
                    int $$7 = $$6.getWidth();
                    int $$8 = $$6.getHeight();
                    for (int $$9 = 0; $$9 < $$7; $$9++) {
                        for (int $$10 = 0; $$10 < $$8; $$10++) {
                            $$6.setPixelRGBA($$9, $$10, alphaBlend($$5.getPixelRGBA($$9 * 2 + 0, $$10 * 2 + 0), $$5.getPixelRGBA($$9 * 2 + 1, $$10 * 2 + 0), $$5.getPixelRGBA($$9 * 2 + 0, $$10 * 2 + 1), $$5.getPixelRGBA($$9 * 2 + 1, $$10 * 2 + 1), $$3));
                        }
                    }
                    $$2[$$4] = $$6;
                }
            }
            return $$2;
        }
    }

    private static boolean hasTransparentPixel(NativeImage nativeImage0) {
        for (int $$1 = 0; $$1 < nativeImage0.getWidth(); $$1++) {
            for (int $$2 = 0; $$2 < nativeImage0.getHeight(); $$2++) {
                if (nativeImage0.getPixelRGBA($$1, $$2) >> 24 == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int alphaBlend(int int0, int int1, int int2, int int3, boolean boolean4) {
        if (boolean4) {
            float $$5 = 0.0F;
            float $$6 = 0.0F;
            float $$7 = 0.0F;
            float $$8 = 0.0F;
            if (int0 >> 24 != 0) {
                $$5 += getPow22(int0 >> 24);
                $$6 += getPow22(int0 >> 16);
                $$7 += getPow22(int0 >> 8);
                $$8 += getPow22(int0 >> 0);
            }
            if (int1 >> 24 != 0) {
                $$5 += getPow22(int1 >> 24);
                $$6 += getPow22(int1 >> 16);
                $$7 += getPow22(int1 >> 8);
                $$8 += getPow22(int1 >> 0);
            }
            if (int2 >> 24 != 0) {
                $$5 += getPow22(int2 >> 24);
                $$6 += getPow22(int2 >> 16);
                $$7 += getPow22(int2 >> 8);
                $$8 += getPow22(int2 >> 0);
            }
            if (int3 >> 24 != 0) {
                $$5 += getPow22(int3 >> 24);
                $$6 += getPow22(int3 >> 16);
                $$7 += getPow22(int3 >> 8);
                $$8 += getPow22(int3 >> 0);
            }
            $$5 /= 4.0F;
            $$6 /= 4.0F;
            $$7 /= 4.0F;
            $$8 /= 4.0F;
            int $$9 = (int) (Math.pow((double) $$5, 0.45454545454545453) * 255.0);
            int $$10 = (int) (Math.pow((double) $$6, 0.45454545454545453) * 255.0);
            int $$11 = (int) (Math.pow((double) $$7, 0.45454545454545453) * 255.0);
            int $$12 = (int) (Math.pow((double) $$8, 0.45454545454545453) * 255.0);
            if ($$9 < 96) {
                $$9 = 0;
            }
            return $$9 << 24 | $$10 << 16 | $$11 << 8 | $$12;
        } else {
            int $$13 = gammaBlend(int0, int1, int2, int3, 24);
            int $$14 = gammaBlend(int0, int1, int2, int3, 16);
            int $$15 = gammaBlend(int0, int1, int2, int3, 8);
            int $$16 = gammaBlend(int0, int1, int2, int3, 0);
            return $$13 << 24 | $$14 << 16 | $$15 << 8 | $$16;
        }
    }

    private static int gammaBlend(int int0, int int1, int int2, int int3, int int4) {
        float $$5 = getPow22(int0 >> int4);
        float $$6 = getPow22(int1 >> int4);
        float $$7 = getPow22(int2 >> int4);
        float $$8 = getPow22(int3 >> int4);
        float $$9 = (float) ((double) ((float) Math.pow((double) ($$5 + $$6 + $$7 + $$8) * 0.25, 0.45454545454545453)));
        return (int) ((double) $$9 * 255.0);
    }

    private static float getPow22(int int0) {
        return POW22[int0 & 0xFF];
    }
}